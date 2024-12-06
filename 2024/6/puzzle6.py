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
                    start = (i,j)
                elif symbol == "#":
                    obstacles.add((i,j))
        return start, obstacles, (i,j)

def pivot(dir):
    return {(-1,0): (0,1), (0,1): (1,0), (1,0): (0,-1), (0,-1): (-1,0)}[dir]

def advance(pos, dir, obstacles):
    forward = pos[0] + dir[0], pos[1] + dir[1]
    if forward not in obstacles:
        return forward, dir
    else:
        print("hit obstacle!", pos, dir)
        dir = pivot(dir)
        return (pos[0] + dir[0], pos[1] + dir[1]), dir


def simulate(start, obstacles, limits):
    new_obs = 0
    visited = collections.defaultdict(set)

    def oob(pos):
        #print("pos", pos)
        x,y = pos
        xl, yl = limits
        return x < 0 or y < 0 or x > xl or y > yl

    pos = start
    direction = (-1, 0)
    
    while not oob(pos):
        if pivot(direction) in visited[pos]:
            new_obs += 1

        visited[pos].add(direction)
        pos, direction = advance(pos, direction, obstacles)

    return visited, new_obs


if __name__ == "__main__":
    start, obstacles, limits = parse_map(sys.argv[1])
    print(start, obstacles, limits)
    visited, new_obstacles = simulate(start, obstacles, limits)
    print(len(visited))
    print(new_obstacles)
