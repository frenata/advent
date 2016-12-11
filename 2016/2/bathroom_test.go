package main

import "testing"

func TestExample(t *testing.T) {
	instructions := "ULL\nRRDDD\nLURDL\nUUUUD"
	expected := "1985"
	actual := Keypad().GetCode(instructions)
	if actual != expected {
		t.Fatalf("\n%s did not result in %s, instead %s", instructions, expected, actual)
	}
}

func TestFunky(t *testing.T) {
	instructions := "ULL\nRRDDD\nLURDL\nUUUUD"
	expected := "5DB3"
	actual := FunkyKeypad().GetCode(instructions)
	if actual != expected {
		t.Fatalf("\n%s did not result in %s, instead %s", instructions, expected, actual)
	}
}
