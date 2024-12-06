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

def advance(pos, dir, obstacles):
    forward = pos[0] + dir[0], pos[1] + dir[1]
    if forward not in obstacles:
        return forward, dir
    else:
        print("hit obstacle!", pos, dir)
        dir = {(-1,0): (0,1), (0,1): (1,0), (1,0): (0,-1), (0,-1): (-1,0)}[dir]
        return (pos[0] + dir[0], pos[1] + dir[1]), dir

def simulate(start, obstacles, limits):
    visited = collections.defaultdict(int)

    def oob(pos):
        #print("pos", pos)
        x,y = pos
        xl, yl = limits
        return x < 0 or y < 0 or x > xl or y > yl

    pos = start
    direction = (-1, 0)
    
    while not oob(pos):
        visited[pos] += 1
        pos, direction = advance(pos, direction, obstacles)

    return visited


if __name__ == "__main__":
    start, obstacles, limits = parse_map(sys.argv[1])
    print(start, obstacles, limits)
    print(len(simulate(start, obstacles, limits)))
