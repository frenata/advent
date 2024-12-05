import sys
import collections
import functools
import heapq


def split(xs, line):
    left, right = xs
    l, r = line.split()
    return (left + [int(l)], right + [int(r)])


def distance(filename):
    with open(filename) as f:
        left, right = functools.reduce(split, f, ([], []))

    heapq.heapify(left)
    heapq.heapify(right)

    total = 0
    while left:
        total += abs(heapq.heappop(left) - heapq.heappop(right))

    return total


def similarity(filename):
    with open(filename) as f:
        left, right = functools.reduce(split, f, ([], []))

    right = collections.Counter(right)
    return functools.reduce(lambda total, x: total + x * right.get(x, 0), left, 0)


if __name__ == "__main__":
    print(distance(sys.argv[1]))
    print(similarity(sys.argv[1]))
