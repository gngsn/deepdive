def chunks(ls: iter, size: int):
    assert size > 0
    for i in range(0, len(ls), size):
        yield ls[i:i + size]



if __name__ == '__main__':
    [print(i) for i in chunks(ls=range(1000), size=10)]