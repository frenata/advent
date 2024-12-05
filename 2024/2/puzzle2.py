import sys
import collections


def is_safe(nums, tolerance):
    last = nums[0]
    dirs = []

    failure = False

    # First look for diff failures and collect directions
    for i, n in enumerate(nums[1:]):
        diff = abs(last - n)
        if diff < 1 or diff > 3:
            failure = True
            break

        dir = 1 if n > last else (-1 if n < last else 0)
        dirs.append(dir)
        last = n

    dirs = collections.Counter(dirs)
    asc = dirs.get(1, 0)
    dsc = dirs.get(-1, 0)

    if not failure:  # there still might be direction failure!
        if asc == 0 or dsc == 0:
            return True
        else:
            failure = True

    if not tolerance and failure:
        return False

    # the level isn't safe, so try all possible removals
    # annoyingly 'inefficient', but I couldn't figure out how to
    # properly detect possible direction failure greedily
    for i, _ in enumerate(nums):
        if is_safe(nums[:i] + nums[i + 1 :], 0):
            return True
    return False


def safe(filename, failure_tolerance=0):
    with open(filename) as f:
        safe_count = collections.Counter(
            [is_safe([int(x) for x in line.split()], failure_tolerance) for line in f]
        )
        return safe_count.get(True, 0)


if __name__ == "__main__":
    print(safe(sys.argv[1]))
    print(safe(sys.argv[1], 1))
