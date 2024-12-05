import sys
import functools
import re
import collections

def mult(line):
    return [int(x)*int(y) for x,y in re.findall(r"mul\((\d+)\,(\d+)\)", line)]

def multiply(filename):
    with open(filename) as f:
        return functools.reduce(lambda x,y: x+sum(y), [mult(line) for line in f], 0)


if __name__ == "__main__":
    print(multiply(sys.argv[1]))
