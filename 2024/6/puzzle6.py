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
        print(f"Start: {start}, Obstacles: {obstacles}, Limits: {(i, j)}")
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

    def will_intersect(_pos, _dir):
        local_visited = collections.defaultdict(set)
        while not oob(_pos) and _dir not in local_visited[_pos]:
            if _dir in visited[_pos] or _dir in local_visited[_pos]:
                print(f"Loop detected at {_pos} with direction {_dir}")
                return True
            local_visited[_pos].add(_dir)
            print(f"Checking position {_pos} with direction {_dir}")
            _pos, _dir = advance(_pos, _dir, obstacles)

        # Check if the robot revisits any position with the same direction
        for pos, dirs in visited.items():
            if _dir in dirs and pos == _pos:
                print(f"Loop detected at {_pos} with direction {_dir}")
                return True

        return False

    pos = start
    direction = (-1, 0)

    while not oob(pos):
        print(f"Current Position: {pos}, Direction: {direction}")
        if pivot(direction) in visited[pos] or will_intersect(pos, pivot(direction)):
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
