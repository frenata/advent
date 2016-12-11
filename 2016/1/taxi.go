package main

import (
	"fmt"
	"math"
	"strconv"
	"strings"
)

const (
	N int = iota
	E
	S
	W
)

type pos struct{ x, y, dir int }

func NewPos() pos {
	return pos{0, 0, N}
}

func (p pos) order(input string) pos {
	dest := p
	turn := input[0]
	far, _ := strconv.Atoi(string(input[1:]))

	switch turn {
	case 'R':
		dest.dir++
	case 'L':
		dest.dir--
	}

	if dest.dir > W {
		dest.dir = N
	} else if dest.dir < N {
		dest.dir = W
	}

	dest.move(far)

	fmt.Println(dest)
	return dest
}

func (p *pos) move(far int) {
	switch p.dir {
	case N:
		p.y += far
	case E:
		p.x += far
	case S:
		p.y -= far
	case W:
		p.x -= far
	}
}

func (p pos) toOrigin() int {
	return int(math.Abs(float64(p.x)) + math.Abs(float64(p.y)))
}

func (p pos) equals(p2 pos) bool {
	return p.x == p2.x && p.y == p2.y
}

func routeDistance(input string) int {
	p := NewPos()

	orders := strings.Split(input, ", ")
	for _, o := range orders {
		p = p.order(o)
	}

	return p.toOrigin()
}

func visitTwice(input string) pos {
	p := NewPos()
	path := make([]pos, 0)
	path = append(path, p)

	orders := strings.Split(input, ", ")
	for _, o := range orders {
		dest := p.order(o)
		final, ok, newpath := addPath(path, p, dest)
		path = newpath
		if ok {
			return final
		}
		p = dest
	}
	return pos{}
}

func addPath(path []pos, start, end pos) (pos, bool, []pos) {
	fmt.Println(path)
	start.dir = end.dir
	for p := start; p != end; {
		p.move(1)
		fmt.Println("one move")
		for _, already := range path {
			fmt.Printf("old: %v new: %v\n", already, p)
			if already.equals(p) {
				return p, true, path
				fmt.Println("found match")
			}
		}
		path = append(path, p)
	}
	return pos{}, false, path
}

func main() {
	fmt.Println(visitTwice("R3, R1, R4, L4, R3, R1, R1, L3, L5, L5, L3, R1, R4, L2, L1, R3, L3, R2, R1, R1, L5, L2, L1, R2, L4, R1, L2, L4, R2, R2, L2, L4, L3, R1, R4, R3, L1, R1, L5, R4, L2, R185, L2, R4, R49, L3, L4, R5, R1, R1, L1, L1, R2, L1, L4, R4, R5, R4, L3, L5, R1, R71, L1, R1, R186, L5, L2, R5, R4, R1, L5, L2, R3, R2, R5, R5, R4, R1, R4, R2, L1, R4, L1, L4, L5, L4, R4, R5, R1, L2, L4, L1, L5, L3, L5, R2, L5, R4, L4, R3, R3, R1, R4, L1, L2, R2, L1, R4, R2, R2, R5, R2, R5, L1, R1, L4, R5, R4, R2, R4, L5, R3, R2, R5, R3, L3, L5, L4, L3, L2, L2, R3, R2, L1, L1, L5, R1, L3, R3, R4, R5, L3, L5, R1, L3, L5, L5, L2, R1, L3, L1, L3, R4, L1, R3, L2, L2, R3, R3, R4, R4, R1, L4, R1, L5").toOrigin())
}
