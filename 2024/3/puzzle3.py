import sys
import functools
import re
import collections


def mult(line):
    return [int(x) * int(y) for x, y in re.findall(r"mul\((\d+)\,(\d+)\)", line)]


def multiply(filename):
    with open(filename) as f:
        return functools.reduce(lambda x, y: x + sum(y), [mult(line) for line in f], 0)


def extract(line):
    commands = []
    for x, y, enable, disable in re.findall(
        r"mul\((\d+)\,(\d+)\)|(do)\(\)|(don)'t\(\)", line
    ):
        if x and y:
            commands.append((int(x), int(y)))
        elif enable:
            commands.append(True)
        elif disable:
            commands.append(False)
    return commands


def conditional_multiply(filename):
    with open(filename) as f:
        commands = functools.reduce(
            lambda x, y: x + y, [extract(line) for line in f], []
        )

        enabled = True
        total = 0
        for command in commands:
            if command is True:
                enabled = True
            elif command is False:
                enabled = False
            elif not enabled:
                pass
            else:
                total += command[0] * command[1]

    return total


if __name__ == "__main__":
    print(multiply(sys.argv[1]))
    print(conditional_multiply(sys.argv[1]))
