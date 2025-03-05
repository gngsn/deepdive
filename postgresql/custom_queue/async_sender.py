import asyncio
import logging
from abc import ABC, abstractmethod
from datetime import time
from urllib.request import Request

import aiohttp

from CustomQueue import CustomQueue

PRODUCER_NAME = "custom_producer"

class Sender(ABC):
    """ Notification Sender """
    name: bool
    active: bool

    def send(self, requests: list[Request]):
        logging.info(f"{self.name} turns {'on' if self.active else 'off'}")

        if not self.active:
            return

        [self._send(request) for request in requests]

    @abstractmethod
    def _send(self, request: Request):
        pass


def run(senders: list, chunk: int = 1000, async_worker_cnt=100):
    while True:
        s1 = time.time()
        dequeue = list(CustomQueue.dequeue(PRODUCER_NAME, chunk))

        if len(dequeue) == 0:
            break

        for chunked in chunks(dequeue, async_worker_cnt):
            s2 = time.time()
            notifications = list(chunked)

            for sender in senders:
                asyncio.run(process(notifications, sender))

        logging.info({"title": "sender_process", "duration": f"{time.time() - s1}s", "count": len(dequeue)})


async def process(notifications, sender):
    async with aiohttp.ClientSession() as session:
        sub_loop = asyncio.get_running_loop()
        tasks = [asyncio.create_task(send(session, sender, n, sub_loop)) for n in notifications]
        result = await asyncio.gather(*tasks, return_exceptions=True)
        return result


async def send(session, sender, noti, loop):
    try:
        await sender.send(session, noti.payload)
    except Exception as e:
        logging.error(f"Sender('{sender.name}') 메세지 발송 실패", exc_info=e)

        if sender.to_update_status:
            return await loop.run_in_executor(
                None,
                CustomQueue.update_failure, PRODUCER_NAME, noti.key, noti.enqueue_dt
            )

    if sender.to_update_status:
        return await loop.run_in_executor(
            None,
            CustomQueue.update_success, PRODUCER_NAME, noti.key, noti.enqueue_dt
        )


class ConsoleSender:
    name = 'Custom Sender'
    timeout: float

    active: bool
    to_update_status: bool

    def __init__(self, timeout_ms=1000, env="DEV") -> None:
        ...

    async def send(self, session, payload):
        data = {
            "title": f"[{self.env}] :bell: NEW MESSAGE",
            "payload": payload
        }

        async with async_timeout.timeout(self.timeout):
            # async with session.request(
            #         method='post',
            #         url=f"{self.client.host}/send-message/{self.client.event_code}",
            #         headers=header,
            #         data=json.dumps(data),
            # ) as resp:
            #     if "application/json" in resp.content_type:
            #         return await resp.json()
            #     else:
            #         return await resp.text()

