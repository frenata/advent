import puzzle3 as sut


def test_multiply():
    actual = sut.multiply("3/example.txt")
    assert actual == 161


def test_conditional_multiply():
    actual = sut.multiply("3/example.txt", True)
    assert actual == 48
