import puzzle4 as sut


def test_search():
    actual = sut.search("4/example.txt", sut.check_x)
    assert actual == 18


def test_search_a():
    actual = sut.search("4/example.txt", sut.check_a)
    assert actual == 9
