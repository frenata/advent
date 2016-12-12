package main

import "testing"

func TestSample(t *testing.T) {
	a := NewAssembler(sample())
	t.Log(a.assemble())

	if a.registers["a"] != 42 {
		t.Fatalf("a is not 42, instead %d\n", a.registers["a"])
	}
}

func sample() []string {
	return []string{
		"cpy 41 a",
		"inc a",
		"inc a",
		"dec a",
		"jnz a 2",
		"dec a"}
}
