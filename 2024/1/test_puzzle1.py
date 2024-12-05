import puzzle1 as sut


def test_distance():
    actual = sut.distance("1/example.txt")
    assert actual == 11


def test_similarity():
    actual = sut.similarity("1/example.txt")
    assert actual == 31
