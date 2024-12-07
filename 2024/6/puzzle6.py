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
        while not oob(_pos) and _dir not in local_visited[_pos] and _dir not in visited[_pos]:
            local_visited[_pos].add(_dir)
            #print(f"Checking position {_pos} with direction {_dir}")
            _pos, _dir = advance(_pos, _dir, obstacles.union(set(_obs)))

        if oob(_pos):
            return False
        else:
            print(f"YYY Loop detected at {_pos} with direction {_dir}")
            return True
        #return not oob(_pos)

    pos = start
    direction = (-1, 0)

    while not oob(pos):
        #print(f"Current Position: {pos}, Direction: {direction}")
        if pivot(direction) in visited[pos] or will_intersect(pos, pivot(direction), (pos[0] + direction[0], pos[1] + direction[1])):
            new_obs.add((pos[0] + direction[0], pos[1] + direction[1]))

        visited[pos].add(direction)
        pos, direction = advance(pos, direction, obstacles)

    return {k: v for k, v in visited.items() if v}, new_obs


if __name__ == "__main__":
    start, obstacles, limits = parse_map(sys.argv[1])
    # print(start, obstacles, limits)
    visited, new_obstacles = simulate(start, obstacles, limits)
    print(len(visited))
    print(len(new_obstacles))
