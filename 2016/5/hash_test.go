package main

import (
	"fmt"
	"testing"
)

func TestNiceHash(t *testing.T) {
	h := hash("abc", 3231929)

	fmt.Println(h)
	if h[5] != '1' {
		t.Fatal("fail")
	}
}

func TestPassword(t *testing.T) {
	p := password("abc")

	t.Log(p)
	if p != "18f47a30" {
		t.Fatal("password fail")
	}
}
func TestFancyPassword(t *testing.T) {
	p := fancyPassword("abc")

	t.Log(p)
	if p != "05ace8e3" {
		t.Fatal("password fail")
	}
}
