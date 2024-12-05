import sys
import collections
import functools
import heapq


def is_safe(line, tolerance):
    nums = [int(x) for x in line.split()]
    dir = 1 if nums[1] > nums[0] else -1
    last = nums[0]
    for n in nums[1:]:
        if dir == 1 and n <= last:
            return False
        elif dir == -1 and n >= last:
            return False
        elif abs(n - last) > 3:
            return False
        last = n

    return True


def safe(filename, failure_tolerance=0):
    with open(filename) as f:
        return collections.Counter([is_safe(line, failure_tolerance) for line in f]).get(True)


if __name__ == "__main__":
    print(safe(sys.argv[1]))
