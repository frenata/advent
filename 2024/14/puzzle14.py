import sys
import operator
import functools
import collections

def _parse(filename):
    robots = collections.defaultdict(list)
    with open(filename) as f:
        for line in f:
            pos, vel = line.split(" ")
            x, y = pos[2:].split(",")
            dx, dy = vel[2:-1].split(",")
            robots[(int(x), int(y))].append((int(dx), int(dy)))
    return robots

def _move(pos, vel, limits):
    modx, mody = limits
    return ((pos[0]+vel[0]) % modx, (pos[1]+vel[1]) %mody)

def iterate(old_robots, limits):
    robots = collections.defaultdict(list)
    for pos, vels in old_robots.items():
        for vel in vels:
            robots[_move(pos, vel, limits)].append(vel)
    return robots

def simulate(robots, iterations, limits):
    for _ in range(iterations):
        robots = iterate(robots, limits)
    return robots

def quadrants(robots, limits):
    midx, midy = limits[0]//2, limits[1]//2
    quads = {(0,0): 0, (0,1): 0, (1,0):0, (1,1):0}
    for pos,vels in robots.items():
        x,y = pos
        if x < midx and y < midy:
            quads[(0,0)] += len(vels)
        elif x < midx and y > midy:
            quads[(0,1)] += len(vels)
        elif x > midx and y < midy:
            quads[(1,0)] += len(vels)
        elif x > midx and y > midy:
            quads[(1,1)] += len(vels)

    return quads

def safety(filename, iterations, limits):
    robots = simulate(_parse(filename), iterations, limits)
    quads = quadrants(robots, limits)
    return functools.reduce(operator.mul, quads.values(), 1)

def pprint(robots, limits):
    for x in range(limits[0]):
        for y in range(limits[1]):
            print(len(robots[(x,y)]), end='')
        print("\n")

def is_tree(robots):
    xs = collections.defaultdict(int)
    ys = collections.defaultdict(int)
    for x,y in robots.keys():
        xs[x] += 1
        ys[y] += 1
    if max(xs.values()) == 35 and max(ys.values()) == 32:
        return True

def find_tree(filename, limits):
    robots = _parse(filename)
    iterations = 0
    while not is_tree(robots):
        robots = simulate(robots, 1, limits)
        iterations += 1
    return iterations

if __name__ == "__main__":
    print(safety(sys.argv[1], 100, (101,103)))
    print(find_tree(sys.argv[1], (101,103)))
