import puzzle7 as sut
import operator


def test_calibration():
    actual = sut.calibrate("7/example.txt", [operator.add, operator.mul])
    assert actual == 3749
