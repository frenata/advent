package main

import "testing"

func TestValidTriangle(t *testing.T) {
	input := "5 10 25"

	if Triangle(input).isValid() {
		t.Fatalf("%s should be invalid, not valid", input)
	}
}
