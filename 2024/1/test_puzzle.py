import puzzle


def test_distance():
    actual = puzzle.distance("1/example.txt")
    assert actual == 11


def test_similarity():
    actual = puzzle.similarity("1/example.txt")
    assert actual == 31
