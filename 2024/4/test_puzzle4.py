import puzzle4 as sut


def test_search():
    actual = sut.search("4/example.txt")
    assert actual == 18
