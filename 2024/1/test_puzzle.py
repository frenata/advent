import puzzle

def test_distance():
    actual = puzzle.distance("1/example.txt")
    assert actual == 11
