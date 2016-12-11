package main

import "testing"

func TestSample(t *testing.T) {
	sample(t, "ADVENT", "ADVENT")
	sample(t, "A(1x5)BC", "ABBBBBC")
	sample(t, "(3x3)XYZ", "XYZXYZXYZ")
	sample(t, "A(2x2)BCD(2x2)EFG", "ABCBCDEFEFG")
	sample(t, "(6x1)(1x3)A", "(1x3)A")
	sample(t, "X(8x2)(3x3)ABCY", "X(3x3)ABC(3x3)ABCY")
	deepsample(t, "X(8x2)(3x3)ABCY", "XABCABCABCABCABCABCY")
}

func sample(t *testing.T, input, expected string) {
	actual := decompress(input)
	if actual != expected {
		t.Fatalf("%s does not decompress to %s; instead: %s", input, expected, actual)
	}
}
func deepsample(t *testing.T, input, expected string) {
	actual := deepDecompress(input)
	if actual != expected {
		t.Fatalf("%s does not decompress to %s; instead: %s", input, expected, actual)
	}
}

func TestBig(t *testing.T) {
	if len(deepDecompress("(27x12)(20x12)(13x14)(7x10)(1x12)A")) != 241920 {
		t.Fatal("big recursive decempress failed")
	}
}

func TestCount(t *testing.T) {
	if deepCount("(27x12)(20x12)(13x14)(7x10)(1x12)A") != 241920 {
		t.Fatal("big recursive decempress failed")
	}
	if deepCount("(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN") != 445 {
		t.Fatal("big recursive decempress failed")
	}
}
