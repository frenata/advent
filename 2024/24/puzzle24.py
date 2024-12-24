import sys
import collections
import re

Gate = collections.namedtuple("Gate", ["gate", "inputs", "out"])

WIRE = r"(\w+): ([01])"
GATE = r"(\w+) (AND|OR|XOR) (\w+) -> (\w+)"

def _parse(filename):
    wires = collections.defaultdict(int)
    gates = []
    collect_wires = True
    with open(filename) as f:
        for line in f:
            if line == "\n":
                collect_wires = False
                continue
            if collect_wires:
                wire, value =  re.match(WIRE, line).groups()
                wires[wire] = int(value)
            else:
                in1, gate, in2, out = re.match(GATE, line).groups()
                gates.append(Gate(gate=gate, inputs=(in1, in2), out=out))

    return wires, gates


def resolve(wires, gates):
    new_gates = []
    for gate in gates:
        ins = [wires[inp] if inp in wires else None for inp in gate.inputs]
        if gate.gate == "AND":
            if None in ins:
                new_gates.append(gate)
                continue
            else:
                # resolve the gate
                wires[gate.out] = ins[0] & ins[1]
        elif gate.gate == "OR":
            if True not in ins and False not in ins:
                new_gates.append(gate)
                continue
            else:
                wires[gate.out] = int(any(ins))
        elif gate.gate == "XOR":
            if None in ins:
                new_gates.append(gate)
                continue
            else:
                wires[gate.out] = ins[0] ^ ins[1]
        else:
            raise ValueError("unexpected gate type")


    return wires, new_gates


def binary(wires):
    out = 0
    zs = [(int(wire[1:]), value) for wire,value in wires.items() if wire.startswith("z")]
    zs.sort(key=lambda x: x[0])

    breakpoint()
    for i, v in enumerate([z[1] for z in zs]):
        out += v << i
    return out
    breakpoint()
    pass


def simulate(filename):
    wires, gates = _parse(filename)
    while gates:
        print("simulation step")
        wires, gates = resolve(wires,gates)
    breakpoint()
    return binary(wires)
    pass


if __name__ == "__main__":
    print(simulate(sys.argv[1]))
