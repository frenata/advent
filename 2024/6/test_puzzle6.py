import puzzle6 as sut


def test_order():
    actual = len(sut.simulate(*sut.parse_map("6/example.txt")))
    assert actual == 41
