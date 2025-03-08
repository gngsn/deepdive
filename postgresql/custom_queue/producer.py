import random
from uuid import uuid4

# import keyboard

from BaseQueue import Producer
from CustomQueue import CustomQueue

class Generator(Producer):
    def get(self):
        pass
    # def _get(self):
        # item_id = random.randint(0, 10_000)
        # user_id = 'sunny'
        # key = "{}:{}:{}".format(uuid4(), item_id, user_id)
        # queue_name = "medium-demo"
        # message = {"id": uuid4(), "user": f"sun{item_id}", "device": "iOSmini3","channel": ["Tab", "Push"]}

        # CustomQueue.enqueue(queue_name, key, message)


if __name__ == '__main__':
    # Generator().get()
    print("asd")