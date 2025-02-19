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
