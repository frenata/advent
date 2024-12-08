import sys
import operator
import functools
import collections


def oob(pos, limits):
    x, y = pos
    xl, yl = limits
    return x < 0 or y < 0 or x > xl or y > yl

def _make_pair(x, y):
    x0, x1 = x
    y0, y1 = y
    if x0 < y0 or (x0 == y0 and x1 < y1):
        return (x,y)
    else:
        return (y,x)

def _parse(filename):
    nodes = collections.defaultdict(set)

    with open(filename) as f:
        for i, line in enumerate(f):
            for j, letter in enumerate(line[:-1]):
                if letter != ".":
                    nodes[letter].add((i,j))
    return nodes, (i,j)

def _pairs(nodes):
    pairs = collections.defaultdict(set)
    for node in nodes:
        pairs[node] = {_make_pair(x,y) for x in nodes[node] for y in nodes[node] if x != y}

    return functools.reduce(lambda xs, x: xs.union(x), pairs.values(), set())

def _make_anti_pair(pair):
    p0, p1 = pair
    x0, x1 = p0
    y0, y1 = p1

    diff0, diff1 = (x0-y0, x1-y1)
    return ((x0+diff0, x1+diff1), (y0-diff0, y1-diff1))


def count_antinodes(filename):
    nodes, limits = _parse(filename)
    pairs = _pairs(nodes)
    anti_pairs = [anti for pair in pairs if (anti := _make_anti_pair(pair))]
    anti_pairs = {anti for pairs in anti_pairs for anti in pairs if not oob(anti, limits)}
    return len(anti_pairs)

if __name__ == "__main__":
    print(count_antinodes(sys.argv[1]))
