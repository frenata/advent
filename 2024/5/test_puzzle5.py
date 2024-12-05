import puzzle5 as sut


def test_order():
    actual = sut.order("5/example.txt")
    assert actual == 143
