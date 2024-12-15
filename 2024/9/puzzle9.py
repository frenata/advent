import sys
import functools

def _print(disk):
    for block in disk:
        print(block if block is not None else ".", end="")
    print("")

def _parse(filename):
    disk = []
    with open(filename) as f:
        for line in f:
            inp = [int(x) for x in line[:-1]]

    i = 0
    while i < len(inp)//2:
        disk.extend([i]*inp[i*2])
        disk.extend([None]*inp[i*2+1])
        i += 1

    disk.extend([i]*inp[i*2])
    #_print(disk)
    return disk

def decompress(disk):
    start = 0
    end = len(disk)-1
    while start < end:
        while disk[start] is not None and start < end:
            start += 1
        if start == end:
            return disk
        disk[start] = disk[end]
        disk[end] = None
        end -= 1
        #_print(disk)
    return disk

def checksum(filename):
    disk = decompress(_parse(filename))
    return functools.reduce(lambda acc, x: acc + x[0]*x[1] if x[1] is not None else acc, enumerate(disk), 0)

if __name__ == "__main__":
    print(checksum(sys.argv[1]))
