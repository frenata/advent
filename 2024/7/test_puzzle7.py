import puzzle7 as sut


def test_calibration():
    actual = sut.calibrate("7/example.txt")
    assert actual == 3749
