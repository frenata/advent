import puzzle9 as sut


def test_checksum():
    actual = sut.checksum("9/example.txt", sut.compress)
    assert actual == 1928

def test_checksum_with_defrag():
    actual = sut.checksum("9/example.txt", sut.defrag)
    assert actual == 2858
