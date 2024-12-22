import functools
import sys
import igraph as ig
from dijkstar import Graph, find_path
import collections

import re


def vpaths_to_epath_dirs(gr, vpaths):
    epaths = []
    for path in vpaths:
        epath = "" 
        for i, v in enumerate(path[:-1]):
            pos = gr.vs[v]
            target_i = path[i+1]
            for edge in pos.out_edges():
                if edge.target == target_i:
                    epath += edge["dir"]
        epaths.append(epath)
    return epaths


key_adjs = [
    # +---+---+---+
    # | 7 | 8 | 9 |
    # +---+---+---+
    # | 4 | 5 | 6 |
    # +---+---+---+
    # | 1 | 2 | 3 |
    # +---+---+---+
    #     | 0 | A |
    #     +---+---+
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

dir_adjs = [
    #     +---+---+
    #     | ^ | A |
    # +---+---+---+
    # | < | v | > |
    # +---+---+---+
            ["^", "A", ">"], ["^","v","v"],
            ["A", "^", "<"], ["A",">","v"],
            ["<", "v", ">"],
            ["v", "<", "<"], ["v",">",">"], ["v", "^", "^"],
            [">", "A", "^"], [">","v","<"],

]

directions = {
    "A": {"^": "<", ">": "v", "v": "<v", "<": "v<<", "A": ""},
    "<": {"^": ">^", ">": ">>", "v": "v", "<": "", "A": ">>^"},
    ">": {"^": "<^", ">": "", "v": "<", "<": "<<", "A": "^"},
    "^": {"^": "", ">": "v>", "v": "v", "<": "v<", "A": ">"},
    "v": {"^": "^", ">": ">", "v": "", "<": "<", "A": "^>"},
}


def _mk_graph(nodes, adjs):

    graph = ig.Graph()
    graph.to_directed()

    graph.add_vertices(list(nodes))
    for s,t,dir in adjs:
        graph.add_edge(s,t, dir=dir)

    keypad = collections.defaultdict(dict)
    for node in nodes:
        keypad[node] = collections.defaultdict(list)

    for node in nodes:
        for node2 in nodes:
            if node != node2:
                paths = graph.get_all_shortest_paths(node, node2)
                commands = vpaths_to_epath_dirs(graph, paths)
                keypad[node][node2] = commands

    return keypad

keypad = _mk_graph("A0123456789", key_adjs)
directions = _mk_graph("A<^v>", dir_adjs)

import itertools

def expand(steps):
    commands = [""]
    min_length = None

    for step in steps:
        new_commands = []
        for command in commands:
            if len(step) > 0:
                for option in step:
                    new_command = command + option + "A"
                    if min_length is None or len(new_command) <= min_length:
                        new_commands.append(new_command)
                        if min_length is None or len(new_command) < min_length:
                            min_length = len(new_command)
            else:
                new_command = command + "A"
                if min_length is None or len(new_command) <= min_length:
                    new_commands.append(new_command)
                    if min_length is None or len(new_command) < min_length:
                        min_length = len(new_command)
        commands = new_commands

    return commands


def first(target, pos="A"):
    keys = []
    for key in target:
        keys.append(keypad[pos][key])
        pos = key

    return expand(keys)

def second(targets, pos="A"):
    out = []
    for target in targets:
        keys = []
        for key in target:
            try:
                keys.append(directions[pos][key])
            except Exception as e:
                breakpoint()
                pass
            pos = key
        out.append(expand(keys))

    options = [item for sublist in out for item in sublist]
    min_ = len(min(options, key=len))
    # breakpoint()
    return [option for option in options if len(option) == min_]

def press_code(target, iters):
    fst = first(target)
    return functools.reduce(lambda xs, x: second(xs), [None]*(iters-1), fst)

    return second(second(first(target)))

def complexity(target, iters):
    prefix = int(re.match(r"\d+", target)[0]) 
    controls = press_code(target, iters)
    # breakpoint()
    control = min(controls, key=min)
    print(prefix,len(control))
    return prefix * len(control)


def _parse(filename):
    codes = []
    with open(filename) as f:
        for line in f:
            codes.append(line[:-1])
    return codes


if __name__ == "__main__":
    codes = _parse(sys.argv[1])
    print(list(complexity(code, 3) for code in codes))
    print(sum(complexity(code, 3) for code in codes))

    #print(list(complexity(code, 25) for code in codes))
    #print(sum(complexity(code, 25) for code in codes))
