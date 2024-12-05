import distance

def test_distance():
    actual = distance.main("1/example.txt")
    assert actual == 11
