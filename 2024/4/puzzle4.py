import sys


class slist(list):
    def get(self, index, default=None):
        if index < 0:
            return default
        try:
            return self[index]
        except IndexError:
            return default


def check(grid, x, y):
    if grid[x][y] != "X":
        return 0
    matches = 0

    if (
        grid.get(x + 1, slist()).get(y) == "M"
        and grid.get(x + 2, slist()).get(y) == "A"
        and grid.get(x + 3, slist()).get(y) == "S"
    ):
        matches += 1

    if (
        grid.get(x - 1, slist()).get(y) == "M"
        and grid.get(x - 2, slist()).get(y) == "A"
        and grid.get(x - 3, slist()).get(y) == "S"
    ):
        matches += 1

    if (
        grid.get(x, slist()).get(y + 1) == "M"
        and grid.get(x, slist()).get(y + 2) == "A"
        and grid.get(x, slist()).get(y + 3) == "S"
    ):
        matches += 1

    if (
        grid.get(x, slist()).get(y - 1) == "M"
        and grid.get(x, slist()).get(y - 2) == "A"
        and grid.get(x, slist()).get(y - 3) == "S"
    ):
        matches += 1

    if (
        grid.get(x + 1, slist()).get(y + 1) == "M"
        and grid.get(x + 2, slist()).get(y + 2) == "A"
        and grid.get(x + 3, slist()).get(y + 3) == "S"
    ):
        matches += 1

    if (
        grid.get(x - 1, slist()).get(y + 1) == "M"
        and grid.get(x - 2, slist()).get(y + 2) == "A"
        and grid.get(x - 3, slist()).get(y + 3) == "S"
    ):
        matches += 1

    if (
        grid.get(x + 1, slist()).get(y - 1) == "M"
        and grid.get(x + 2, slist()).get(y - 2) == "A"
        and grid.get(x + 3, slist()).get(y - 3) == "S"
    ):
        matches += 1

    if (
        grid.get(x - 1, slist()).get(y - 1) == "M"
        and grid.get(x - 2, slist()).get(y - 2) == "A"
        and grid.get(x - 3, slist()).get(y - 3) == "S"
    ):
        matches += 1

    return matches


def search(filename):
    with open(filename) as f:
        grid = slist([slist(line[:-1]) for line in f])

    total = 0
    for i, line in enumerate(grid):
        for j, letter in enumerate(line):
            total += check(grid, i, j)
    return total


if __name__ == "__main__":
    print(search(sys.argv[1]))
