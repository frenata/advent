import sys
from dijkstar import Graph, find_path
import collections

import re

def _mk_graph():
# +---+---+---+
# | 7 | 8 | 9 |
# +---+---+---+
# | 4 | 5 | 6 |
# +---+---+---+
# | 1 | 2 | 3 |
# +---+---+---+
#     | 0 | A |
#     +---+---+


    adjs = [
        ["A", "0", "<"], ["A", "3", "^"],
        ["0", "A", ">"], ["0", "2", "^"],

        ["1", "2", ">"], ["1", "4", "^"],
        ["2", "1", "<"], ["2", "0", "v"], ["2", "3", ">"], ["2", "5", "^"],
        ["3", "2", "<"], ["3", "A", "v"], ["3", "6", "^"],

        ["4", "1", "v"], ["4", "5", ">"], ["4", "7", "^"],
        ["5", "4", "<"], ["5", "2", "v"], ["5", "6", ">"], ["5", "8", "^"],
        ["6", "5", "<"], ["6", "3", "v"], ["6", "9", "^"],

        ["7", "4", "v"], ["7", "8", ">"], 
        ["8", "7", "<"], ["8", "5", "v"], ["8", "9", ">"],
        ["9", "8", "<"], ["9", "6", "v"], 
    ]

    def _no_cost(a,b,c,d):
        return 0

    graph = Graph()
    for adj in adjs:
        graph.add_edge(*adj)

    nodes = "A0123456789"

    keypad = collections.defaultdict(dict)
    for node in nodes:
        for node2 in nodes:
            if node != node2:
                keypad[node][node2] = "".join(find_path(graph, node, node2, cost_func=_no_cost).edges)

    return keypad

keypad = _mk_graph()

directions = {
    "A": {"^": "<", ">": "v", "v": "<v", "<": "v<<", "A": ""},
    "<": {"^": ">^", ">": ">>", "v": "v", "<": "", "A": ">>^"},
    ">": {"^": "<^", ">": "", "v": "<", "<": "<<", "A": "^"},
    "^": {"^": "", ">": "v>", "v": "v", "<": "v<", "A": ">"},
    "v": {"^": "^", ">": ">", "v": "", "<": "<", "A": "^>"},
}

def first(target, pos="A"):
    keys = []
    for key in target:
        keys.append(keypad[pos][key])
        pos = key

    return "A".join(keys)+"A"

def second(target, pos="A"):
    keys = []
    for key in target:
        try:
            keys.append(directions[pos][key])
        except:
            breakpoint()
            pass
        pos = key

    return "A".join(keys)+"A"

def press_code(target):
    return second(second(first(target)))

def complexity(target):
    prefix = int(re.match("\d+", target)[0]) 
    control =press_code(target)
    print(prefix,control)
    return prefix * len(control)


def _parse(filename):
    codes = []
    with open(filename) as f:
        for line in f:
            codes.append(line[:-1])
    return codes


if __name__ == "__main__":
    codes = _parse(sys.argv[1])
    print(list(complexity(code) for code in codes))
