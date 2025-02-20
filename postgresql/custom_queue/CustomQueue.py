import json

from peewee import IntegerField, CharField, DateTimeField, CompositeKey, BigIntegerField
from pendulum import datetime, now, date

from peewee import *
from playhouse.postgres_ext import JSONField

NOTIFICATION_QUEUE_TABLE_NAME = 'custom_queue'
PARTITION_FORMAT = "YYYY_MM_DD"

pg_db = PostgresqlDatabase('test', user='postgres', password='postgres', host='localhost', port=5432)

class BaseModel(Model):
    class Meta:
        database = pg_db


class CustomQueue(BaseModel):
    """ Notification Queue DB Model """

    id: int = BigIntegerField()
    status: int = IntegerField(default=0, null=False)
    try_count: int = IntegerField(default=0, null=False)
    max_tries: int = IntegerField(default=3, null=False)
    key: str = CharField(max_length=200)
    payload: int = JSONField(default='{}')

    created_at: datetime = DateTimeField(default=now())
    updated_at: datetime = DateTimeField(default=now())
    priority: int = IntegerField(default=0, null=False)

    class Meta:
        table_name = NOTIFICATION_QUEUE_TABLE_NAME
        primary_key = CompositeKey('id', 'created_at')
        indexes = (
            (('checksum', 'created_at'), True),
        )
        table_settings = 'PARTITION BY RANGE (created_at)'

    @classmethod
    def enqueue(cls, key: str, message: dict):
        cls.insert(key=key, payload=json.dumps(message)).execute()

    @classmethod
    def dequeue(cls, offset: int, limit: int):
        return cls.select().where(cls.status == 0).offset(offset).limit(limit).for_update("FOR UPDATE SKIP LOCKED")

    @classmethod
    def update_success_done(cls, id):
        return cls.update(
            try_account=cls.try_count + 1,
            status=1,
            updated_at=now()
        ).where(cls.id == id).execute()

    @classmethod
    def update_failure_done(cls, id):
        where = cls.update(
            try_account=cls.try_count + 1,
            status=-1,
            updated_at=now()
        ).where(cls.id == id)
        return where.execute()

if __name__ == "__main__":
    now = datetime(year=2024, month=5, day=23, hour=13, minute=13)
    CustomQueue.enqueue(
        "UMSV10001:12",
        {"message": "hello world", "user_id": 12},
    )

    chunk_size = 100
    offset = 0
    limit = chunk_size
    dequeue = CustomQueue.dequeue(offset, limit)

    print(dequeue)
    for message in dequeue:
        print(message)

if __name__ == "__main__":
    CustomQueue.create_partition_if_not_exists(date(year=2024, month=5, day=23))
