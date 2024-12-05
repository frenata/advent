import sys
import functools
import re


def extract(line, use_conditions):
    commands = []
    for x, y, enable, disable in re.findall(
        r"mul\((\d+)\,(\d+)\)|(do)\(\)|(don)'t\(\)", line
    ):
        if x and y:
            commands.append((int(x), int(y)))
        elif enable and use_conditions:
            commands.append(True)
        elif disable and use_conditions:
            commands.append(False)
    return commands


def multiply(filename, use_conditions=False):
    with open(filename) as f:
        commands = functools.reduce(
            lambda x, y: x + y, [extract(line, use_conditions) for line in f], []
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
    print(multiply(sys.argv[1], True))
