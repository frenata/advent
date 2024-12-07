import sys
import operator
import functools
import collections


def _parse(filename):
    equations = {}
    with open(filename) as f:
        for line in f:
            total, nums = line.split(":")
            nums = [int(n) for n in nums.split()]
            equations[int(total)] = nums

    return equations


def _is_valid(equation):
    total, nums = equation
    if len(nums) == 1:
        return total == nums[0]

    for op in [operator.mul, operator.add]:
        if _is_valid((total, [op(nums[0], nums[1])] + nums[2:])):
            return True

    return False


def calibrate(filename):
    equations = _parse(filename)
    # return _is_valid(*equations.items()[0])
    valid = list(filter(_is_valid, equations.items()))
    # print(list(valid))
    return functools.reduce(lambda total, eq: total + eq[0], valid, 0)


if __name__ == "__main__":
    print(calibrate(sys.argv[1]))