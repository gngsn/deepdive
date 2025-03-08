from abc import ABC
from typing import Generator, Any


class BaseQueue(ABC):
    def enqueue(self) -> None:
        pass

    def dequeue(self) -> Generator[Any, None, None]:
        pass


class Consumer(ABC):
    def accept(self) -> None:
        pass

class Producer(ABC):
    def get(self) -> None:
        pass

