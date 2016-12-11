package main

import "testing"

func TestSample(t *testing.T) {
	sample := []string{"eedadn",
		"drvtee",
		"eandsr",
		"raavrd",
		"atevrs",
		"tsrnev",
		"sdttsa",
		"rasrtv",
		"nssdts",
		"ntnada",
		"svetve",
		"tesnvt",
		"vntsnd",
		"vrdear",
		"dvrsen",
		"enarar"}

	if actual := code(sample); actual != "easter" {
		t.Fatalf("fail: %s", actual)
	}
}
