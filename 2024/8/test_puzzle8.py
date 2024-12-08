import puzzle7 as sut
import operator


def test_calibration():
    actual = sut.calibrate("7/example.txt", [operator.add, operator.mul])
    assert actual == 3749


def test_calibration_with_concat():
    actual = sut.calibrate("7/example.txt", [operator.add, operator.mul, sut.concat])
    assert actual == 11387


def test_concat():
    actual = sut.concat(5, 16)
    assert actual == 516
