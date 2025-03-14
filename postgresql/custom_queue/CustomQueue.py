import json
import pprint
import random
from uuid import uuid4

import keyboard
from peewee import IntegerField, CharField, DateField, DateTimeField, CompositeKey, BigIntegerField
from pendulum import datetime, now, date

from peewee import *
from playhouse.postgres_ext import JSONField

from BaseQueue import Producer, BaseQueue
from async_sender import PRODUCER_NAME

NOTIFICATION_QUEUE_TABLE_NAME = 'custom_queue'
PARTITION_FORMAT = "YYYY_MM_DD"

database_ = PostgresqlDatabase('postgres', user='postgres', password='postgres', host='localhost', port=5332)


class BaseModel(Model):
    class Meta:
        database = database_

MAX_RETRY_COUNT = 3


class CustomQueue(BaseModel):
    """ Notification Queue DB Model """

    name: str = CharField(max_length=30)
    key: str = CharField(max_length=256)
    status: int = IntegerField(default=0, null=False)
    payload: json = JSONField(default='{}')

    try_count: int = IntegerField(default=0, null=False)

    enqueue_dt: date = DateField(default=now())
    created_at: datetime = DateTimeField(default=now())
    updated_at: datetime = DateTimeField(default=now())

    class Meta:
        table_name = NOTIFICATION_QUEUE_TABLE_NAME
        primary_key = CompositeKey('name', 'key', 'enqueue_dt')
        indexes = (
            (('status', 'created_at'), True),
        )
        table_settings = 'PARTITION BY RANGE (enqueue_dt)'

    @classmethod
    def enqueue(cls, name: str, key: str, message: dict):
        cls.insert(name=name, key=key, payload=json.dumps(message)).execute()

    @classmethod
    def dequeue(cls, name: str, limit: int):
        return cls.select().where(
            cls.name == name,
            cls.status == 0,
            cls.try_count <= MAX_RETRY_COUNT
        ).limit(limit).for_update("FOR UPDATE SKIP LOCKED")

    @classmethod
    def update_success(cls, name: str, key: str):
        return cls.update(
            try_count=cls.try_count + 1,
            status=1,
            updated_at=now()
        ).where(cls.name == name, cls.key == key).execute()

    @classmethod
    def update_failure(cls, name: str, key: str):
        where = cls.update(
            try_count=cls.try_count + 1,
            status=-1,
            updated_at=now()
        ).where(cls.name == name, cls.key == key)
        return where.execute()


def execute(sql):
    return database_.execute_sql(sql)


def is_exist_partition(table_name, partition_name):
    cursor = execute(f"""
            SELECT
                parent.relname      AS parent,
                child.relname       AS child
            FROM pg_inherits
                JOIN pg_class parent            ON pg_inherits.inhparent = parent.oid
                JOIN pg_class child             ON pg_inherits.inhrelid   = child.oid
            WHERE parent.relname='{table_name}' and child.relname='{partition_name}';
        """)

    return cursor.rowcount != 0


def create_partition_if_not_exists(now_date=now().date()):
    def get_partition_name():
        formatted_date = now_date.format(PARTITION_FORMAT)

        return f"{NOTIFICATION_QUEUE_TABLE_NAME}_{format(formatted_date)}"

    table_name = NOTIFICATION_QUEUE_TABLE_NAME
    partition_name = get_partition_name()

    if is_exist_partition(table_name, partition_name):
        return

    execute(f"""
            CREATE TABLE {partition_name}
            PARTITION OF {table_name}
            FOR VALUES FROM ('{now_date}') TO ('{now_date.add(days=1)}')""")



if __name__ == "__main__":
    database_.create_tables([CustomQueue])
    create_partition_if_not_exists()

    try:
        chunk_size = 100
        dequeue = CustomQueue.dequeue(PRODUCER_NAME, chunk_size)

        for message in dequeue.dicts():
            pprint.pprint(message)

        CustomQueue.update_success(dequeue.name, dequeue.key)
    except:
        CustomQueue.update_failure(dequeue.name, dequeue.key)

