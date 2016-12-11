package main

import (
	"fmt"
	"testing"
)

func TestNewFactory(t *testing.T) {
	f := NewFactory(4)
	t.Log(f)
	if !f.Equals(NewFactory(4)) {
		t.Fatal("2 blank factories are unequal")
	}
	if f.failure() {
		t.Fatal("blank factory reports radiation")
	}
}

func TestFilledFactory(t *testing.T) {
	f := sampleFactory()

	if !f.Equals(sampleFactory()) {
		t.Log(f)
		t.Fatal("2 sample factories are unequal")
	}

	if f.failure() {
		t.Fatal("sample factory reports radiation")
	}
}

func TestClone(t *testing.T) {
	f := sampleFactory()

	clone := f.clone(0)
	if !f.Equals(clone) {
		t.Log(f, clone)
		t.Fatal("cloned factory is not equal")
	}

	clone = f.clone(1)
	if f.Equals(clone) {
		t.Log(f, clone)
		t.Fatal("cloned (but moved elevator) factory is equal")
	}

}

func TestExample(t *testing.T) {
	f := sampleFactory()
	t.Log(f)
	t.Log(f.PossibleMoves())
	t.Log(f.PossibleMoves()[0].PossibleMoves())

	//search(f)
	root := node{f, nil, nil}
	fmt.Println(root)
	root.insert()
	fmt.Println(root.children)

	search(f)
}

func sampleFactory() factory {
	f := NewFactory(4)
	f.floors[0] = floor{[]tool{chip{"hydrogen"}, chip{"lithium"}}}
	f.floors[1] = floor{[]tool{generator{"hydrogen"}}}
	f.floors[2] = floor{[]tool{generator{"lithium"}}}

	return f
}
