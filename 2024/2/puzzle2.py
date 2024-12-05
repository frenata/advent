import sys
import collections
import functools
import heapq


def is_safe(nums, tolerance):
    dir = 1 if nums[1] > nums[0] else -1
    dir2 = 1 if nums[2] > nums[1] else -1
    without_first = False
    if dir != dir2 and tolerance:
        without_first = is_safe(nums[1:], 0)

    last = nums[0]
    for i, n in enumerate(nums[1:]):
        #print(nums, dir, last, n, i)
        if dir == 1 and n <= last:
            #print(nums[:i+1], nums[i+2:])
            return without_first or (is_safe(nums[:i+1]+nums[i+2:], 0) if tolerance else False)
        elif dir == -1 and n >= last:
            #print(nums[:i+1], nums[i+2:])
            return without_first or (is_safe(nums[:i+1]+nums[i+2:], 0) if tolerance else False)
        elif abs(n - last) > 3:
            #print(nums[:i+1], nums[i+2:])
            return without_first or (is_safe(nums[:i+1]+nums[i+2:], 0) if tolerance else False)
        last = n

    return True


def safe(filename, failure_tolerance=0):
    with open(filename) as f:
        safe_count = collections.Counter(
            [is_safe([int(x) for x in line.split()], failure_tolerance) for line in f]
        )
        #print(safe_count)
        return safe_count.get(True, 0)


if __name__ == "__main__":
    print(safe(sys.argv[1]))
    print(safe(sys.argv[1], 1))
