package main

import (
	"errors"
	"fmt"
	"strconv"
	"strings"
)

type assembler struct {
	instructions []string
	current      int
	registers    map[string]int
}

func NewAssembler(instructions []string) assembler {
	return assembler{instructions, 0, make(map[string]int)}
}

// returns an error if there is no further instruction to execute
func (a *assembler) next() error {
	if a.current >= len(a.instructions) {
		return errors.New("past the end of instruction list, assembly finished")
	}
	a.evaluate(a.instructions[a.current])
	return nil
}

func (a *assembler) evaluate(instruction string) {
	cmd, arg1, arg2 := split(instruction)

	switch cmd {
	case "inc":
		a.registers[arg1]++
	case "dec":
		a.registers[arg1]--
	case "cpy":
		a.registers[arg2] = a.value(arg1)
	case "jnz":
		if a.value(arg1) != 0 {
			a.current += a.value(arg2)
			fmt.Printf("%s(%d) is not zero, jumping %d\n", arg1, a.value(arg1), a.value(arg2))
			return // to avoid the counter inrcement below
		}
	}

	a.current++
}

// if arg is an int, returns itself, otherwise returns value stored in register list
func (a assembler) value(arg string) int {
	n, err := strconv.Atoi(arg)
	if err != nil {
		n = a.registers[arg]
	}
	return n
}

func split(instruction string) (string, string, string) {
	args := strings.Split(instruction, " ")
	if len(args) < 3 {
		args = append(args, "")
	}
	return args[0], args[1], args[2]
}

func (a *assembler) assemble() map[string]int {
	for a.next() == nil {
	}
	return a.registers
}

func main() {
	puzzle := []string{
		"cpy 1 a",
		"cpy 1 b",
		"cpy 26 d",
		"jnz c 2",
		"jnz 1 5",
		"cpy 7 c",
		"inc d",
		"dec c",
		"jnz c -2",
		"cpy a c",
		"inc a",
		"dec b",
		"jnz b -2",
		"cpy c b",
		"dec d",
		"jnz d -6",
		"cpy 16 c",
		"cpy 12 d",
		"inc a",
		"dec d",
		"jnz d -2",
		"dec c",
		"jnz c -5"}

	a := NewAssembler(puzzle)
	a.registers["c"] = 1 // part 2
	a.assemble()
	fmt.Println(a)
}
