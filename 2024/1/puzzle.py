import sys
import functools
import heapq

def split(xs, line):
    left, right = xs
    l, r = line.split()
    return (left + [int(l)], right + [int(r)])

def distance(filename):
    with open(filename) as f:
        left, right = functools.reduce(split, f, ([],[]))

    heapq.heapify(left)
    heapq.heapify(right)

    total = 0
    while left:
        total += abs(heapq.heappop(left) - heapq.heappop(right))

    return total


if __name__ == "__main__":
    print(distance(sys.argv[1]))
