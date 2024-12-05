import puzzle2 as sut

def test_safe_normal():
    actual = sut.safe("2/example.txt")
    assert actual == 2
