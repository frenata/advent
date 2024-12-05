import sys
import collections


def is_safe(nums, tolerance):
    # print("test", nums, tolerance)
    last = nums[0]
    dirs = []
    if tolerance and is_safe(nums[1:], 0):
        return True

    for i, n in enumerate(nums[1:]):
        diff = abs(last - n)
        if diff < 1 or diff > 3:
            return is_safe(nums[: i + 1] + nums[i + 2 :], 0) if tolerance else False

        dir = 1 if n > last else (-1 if n < last else 0)
        dirs.append(dir)
        last = n

    dirs = collections.Counter(dirs)
    # print(dirs)
    asc = dirs.get(1, 0)
    dsc = dirs.get(-1, 0)

    if not tolerance:
        return asc == 0 or dsc == 0
    else:
        return not (asc > 1 and dsc > 1)


def safe(filename, failure_tolerance=0):
    with open(filename) as f:
        safe_count = collections.Counter(
            [is_safe([int(x) for x in line.split()], failure_tolerance) for line in f]
        )
        print(safe_count)
        return safe_count.get(True, 0)


if __name__ == "__main__":
    print(safe(sys.argv[1]))
    print(safe(sys.argv[1], 1))
