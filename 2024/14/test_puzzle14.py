import puzzle14 as sut


def test_count_safe():
    actual = sut.safety("14/example.txt", 100, (7,11))
    assert actual == 12


def test_find_tree():
    actual = sut.find_tree("14/challenge.txt", (101,103))
    assert actual == 8270
