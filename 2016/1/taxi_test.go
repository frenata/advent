package main

import "testing"

func TestExamples(t *testing.T) {
	Distance("R2, L3", 5, t)
	Distance("R2, R2, R2", 2, t)
	Distance("R5, L5, R5, R3", 12, t)
	VisitTwice("R8, R4, R4, R8", 4, t)
}

func Distance(input string, expected int, t *testing.T) {
	actual := routeDistance(input)
	if actual != expected {
		t.Fatalf("%s should evaluate to %d, instead %d", input, expected, actual)
	}
}
func VisitTwice(input string, expected int, t *testing.T) {
	actual := visitTwice(input).toOrigin()
	if actual != expected {
		t.Fatalf("%s should evaluate to %d, instead %d", input, expected, actual)
	}
}
