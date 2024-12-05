import puzzle2 as sut


def test_safe_normal():
    actual = sut.safe("2/example.txt")
    assert actual == 2


def test_safe_tolerance():
    actual = sut.safe("2/example.txt", 1)
    assert actual == 4


def test_safe_tolerance_with_direction_errors():
    actual = sut.safe("2/check.txt", 1)
    assert actual == 3
