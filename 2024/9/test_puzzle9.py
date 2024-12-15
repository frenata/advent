import puzzle9 as sut


def test_checksum():
    actual = sut.checksum("9/example.txt")
    assert actual == 1928
