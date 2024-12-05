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


def order(filename):
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

        #print(rules, pages)

    rules = graph(rules)
    # print(rules)
    # print([is_ordered(rules, ps) for ps in pages])
    ordered_pages = [page for page in pages if is_ordered(rules, page)]
    return functools.reduce(lambda xs, y: xs + int(y[len(y) // 2]), ordered_pages, 0)


if __name__ == "__main__":
    print(order(sys.argv[1]))
