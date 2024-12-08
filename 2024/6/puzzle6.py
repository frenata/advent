import sys
import functools
import collections


def parse_map(filename):
    with open(filename) as f:
        start = None
        obstacles = set()
        for i, line in enumerate(f):
            for j, symbol in enumerate(line[:-1]):
                if symbol == "^":
                    start = (i, j)
                elif symbol == "#":
                    obstacles.add((i, j))
        return start, obstacles, (i, j)


def pivot(dir):
    return {(-1, 0): (0, 1), (0, 1): (1, 0), (1, 0): (0, -1), (0, -1): (-1, 0)}[dir]


def advance(pos, dir, obstacles):
    forward = pos[0] + dir[0], pos[1] + dir[1]
    if forward not in obstacles:
        return forward, dir
    else:
        # print("hit obstacle!", pos, dir)
        dir = pivot(dir)
        return (pos[0] + dir[0], pos[1] + dir[1]), dir


def simulate(start, obstacles, limits):
    new_obs = set()
    visited = collections.defaultdict(set)

    def oob(pos):
        x, y = pos
        xl, yl = limits
        return x < 0 or y < 0 or x > xl or y > yl

    def will_intersect(_pos, _dir, _obs):
        local_visited = collections.defaultdict(set)
        _obstacles = obstacles.union(set(_obs))
        while not (oob(_pos) or _dir in local_visited[_pos] or _dir in visited[_pos]): # and _dir not in visited[_pos]:
            local_visited[_pos].add(_dir)
            #print(f"Checking position {_pos} with direction {_dir}")
            _pos, _dir = advance(_pos, _dir, _obstacles)

        if oob(_pos):
            return False
        else:
            return True, f"YYY projected loop @ {_pos} with direction {_dir}"

        raise ValueError("should not occur")

    pos = start
    direction = (-1, 0)

    while not oob(pos):
        #print(f"Current Position: {pos}, Direction: {direction}")
        next = (pos[0] + direction[0], pos[1] + direction[1])
        if next in new_obs:
            pass
            #elif pivot(direction) in visited[pos]:
            #print(f"ZZZ pivoting loops @ {pos} with direction {direction} obs @ {next}")
            #new_obs.add(next)
        elif out := will_intersect(pos, pivot(direction), next):
            _, msg = out
            print(msg + f" obs @ {next}")
            new_obs.add(next)

        visited[pos].add(direction)
        pos, direction = advance(pos, direction, obstacles)

    return {k: v for k, v in visited.items() if v}, new_obs


if __name__ == "__main__":
    start, obstacles, limits = parse_map(sys.argv[1])
    # print(start, obstacles, limits)
    visited, new_obstacles = simulate(start, obstacles, limits)
    print(len(visited))
    print(len(new_obstacles))
    #print(new_obstacles)
