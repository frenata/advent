import sys


class slist(list):
    def get(self, index, default=None):
        if index < 0:
            return default
        try:
            return self[index]
        except IndexError:
            return default


def check_a(grid, x, y):
    if grid[x][y] != "A":
        return 0

    if set(
        grid.get(x + 1, slist()).get(y + 1, "")
        + grid.get(x - 1, slist()).get(y - 1, "")
    ) == {"M", "S"} and set(
        grid.get(x + 1, slist()).get(y - 1, "")
        + grid.get(x - 1, slist()).get(y + 1, "")
    ) == {"M", "S"}:
        return 1
    else:
        return 0


def check_x(grid, x, y):
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


def search(filename, func):
    with open(filename) as f:
        grid = slist([slist(line[:-1]) for line in f])

    total = 0
    for i, line in enumerate(grid):
        for j, letter in enumerate(line):
            total += func(grid, i, j)
    return total


if __name__ == "__main__":
    print(search(sys.argv[1], check_x))
    print(search(sys.argv[1], check_a))
