package main

import "testing"

var examples []string = []string{"aaaaa-bbb-z-y-x-123[abxyz]", "a-b-c-d-e-f-g-h-987[abcde]", "not-a-real-room-404[oarel]", "totally-real-room-200[decoy]"}

func TestExample(t *testing.T) {
	isRealRoom(t, examples[0], true)
	isRealRoom(t, examples[1], true)
	isRealRoom(t, examples[2], true)
	isRealRoom(t, examples[3], false)
}

func TestSectorSum(t *testing.T) {
	sum := SumRealSector(examples)

	if sum != 1514 {
		t.Fatal("sum failed for test input")
	}
}

func TestDecrypt(t *testing.T) {
	real := Room("qzmt-zixmtkozy-ivhz-343[aaaaa]").Decrypt()
	if real != "very encrypted name" {
		t.Fatalf("decrypt fails: %s", real)
	}
}

func isRealRoom(t *testing.T, input string, expected bool) {
	r := Room(input)
	t.Log(r)
	actual := r.isReal()

	if actual != expected {
		t.Fatalf("%s's reality was %v, should be %v, computed checksum %s", input, actual, expected, r.Checksum())
	}
}
