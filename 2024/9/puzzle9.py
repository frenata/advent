import sys
import functools

def _print(disk):
    for block in disk:
        print(block if block is not None else ".", end="")
    print("")

def _parse(filename):
    disk = []
    holes = []
    with open(filename) as f:
        for line in f:
            inp = [int(x) for x in line[:-1]]

    i,pos = 0, 0
    while i < len(inp)//2:
        #block_len = inp[i*2]
        disk.extend([i]*(block_len := inp[i*2]))
        pos += block_len
        disk.extend([None]*(hole_len := inp[i*2+1]))
        holes.append((pos, hole_len))
        pos += hole_len
        i += 1

    disk.extend([i]*inp[i*2])
    #_print(disk)
    return disk, holes

def compress(disk, _):
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

def defrag(disk, holes):
    end = len(disk) - 1
    block = disk[end]
    moved = set()
    #_print(disk)

    while block != 0:
        while block is None or block in moved:
            end -= 1
            if end < 0:
                return disk
            block = disk[end]
        #print(block, end)
        block_len = 1
        while disk[end-1] == block:
            end -= 1
            block_len += 1
        #print(block, end, block_len)
        moved.add(block)
        #print(holes)
        for i,hole in enumerate(holes):
            #print(i,hole)
            if hole[1] >= block_len:
                for j in range(block_len):
                    disk[hole[0]+j] = block
                    disk[end+j] = None
                holes[i] = (hole[0]+block_len, hole[1]-block_len)
                #_print(disk)
                break
        end -= 1
        block = disk[end]
    return disk

def checksum(filename, compression_func):
    disk = compression_func(*_parse(filename))
    return functools.reduce(lambda acc, x: acc + x[0]*x[1] if x[1] is not None else acc, enumerate(disk), 0)

if __name__ == "__main__":
    print(checksum(sys.argv[1], compress))
    print(checksum(sys.argv[1], defrag))
