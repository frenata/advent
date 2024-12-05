import sys
import functools
import collections


def graph(rules):
    g = collections.defaultdict(list)
    for first, last in rules:
        g[first].append(last)
    return g


def is_ordered(g, pages):
    for i, page in enumerate(pages):
        if i == len(pages) - 1:
            pass
        elif pages[i + 1] not in g[page]:
            return False
    return True


def extract(filename):
    with open(filename) as f:
        collect = "rules"
        rules = []
        pages = []
        for line in f:
            if line == "\n":
                collect = "pages"

            if collect == "rules":
                rules.append(line[:-1].split("|"))
            elif line != "\n":
                pages.append(line[:-1].split(","))

    return graph(rules), pages


def middles(pages):
    return functools.reduce(lambda xs, y: xs + int(y[len(y) // 2]), pages, 0)


def order(rules, pages):
    def is_before(x, y):
        if x == y:
            return 0
        elif y in rules[x]:
            return -1
        elif x in rules[y]:
            return 1

    return sorted(pages, key=functools.cmp_to_key(is_before))


if __name__ == "__main__":
    rules, pages = extract(sys.argv[1])
    print(middles([page for page in pages if is_ordered(rules, page)]))
    print(
        middles([order(rules, page) for page in pages if not is_ordered(rules, page)])
    )
