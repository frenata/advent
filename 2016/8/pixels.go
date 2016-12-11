package main

import (
	"bufio"
	"fmt"
	"os"
	"regexp"
	"strconv"
)

type panel struct {
	x, y  int
	array [][]int
}

func NewPanel(x, y int) *panel {
	p := make([][]int, y)
	for i := range p {
		/*inner := make([]rune, x)
		for j := range inner {
			inner[j] = '.'
		}
		p[i] = inner*/
		p[i] = make([]int, x)
	}

	return &panel{x, y, p}
}

func (p *panel) on(x, y int) {
	if x >= p.x {
		x = x % p.x
	}
	if y >= p.y {
		y = y % p.y
	}
	//fmt.Printf("turn on %dx%d\n", x, y)
	p.array[y][x]++
}
func (p *panel) off(x, y int) {
	//fmt.Printf("turn off %dx%d\n", x, y)
	p.array[y][x]--
}
func (p *panel) isOn(x, y int) bool {
	return p.array[y][x] > 0
}
func (p *panel) copy() *panel {
	array := make([][]int, p.y)
	for i := range array {
		array[i] = make([]int, p.x)
		for j := range array[i] {
			array[i][j] = p.array[i][j]
		}
	}
	return &panel{p.x, p.y, array}
}

func (p *panel) normalize() {
	a := p.array
	for i := range a {
		for j := range a[i] {
			if a[i][j] > 0 {
				a[i][j] = 1
			} else {
				a[i][j] = 0
			}
		}
	}
}
func (p panel) count() int {
	n := 0
	for i := 0; i < p.x; i++ {
		for j := 0; j < p.y; j++ {
			if p.isOn(i, j) {
				n++
			}
		}
	}
	return n
}
func (p panel) String() string {
	str := ""
	for i := range p.array {
		for j := range p.array[i] {
			if p.array[i][j] > 0 {
				str += "#"
			} else {
				str += "."
			}
		}
		str += "\n"
	}
	return str
}

func (p *panel) parse(line string) {
	rectanglePattern := regexp.MustCompile("rect (\\d+)x(\\d+)")
	rotatePattern := regexp.MustCompile("rotate (?:row|column) (x|y)=(\\d+) by (\\d+)")

	//fmt.Println(line)
	var matches []string
	if matches = rectanglePattern.FindStringSubmatch(line); len(matches) > 0 {
		x, _ := strconv.Atoi(matches[1])
		y, _ := strconv.Atoi(matches[2])
		p.rectangle(x, y)
	} else if matches = rotatePattern.FindStringSubmatch(line); len(matches) > 0 {
		target, _ := strconv.Atoi(matches[2])
		distance, _ := strconv.Atoi(matches[3])
		p.rotate(matches[1], target, distance)
	}
}

func (p *panel) rectangle(x, y int) {
	//fmt.Printf("turn on rectangle %d by %d\n", x, y)
	for i := 0; i < x; i++ {
		for j := 0; j < y; j++ {
			p.on(i, j)
		}
	}
	p.normalize()
}

func (p *panel) rotate(dir string, target, distance int) {
	switch dir {
	case "y":
		p.rotateRight(target, distance)
	case "x":
		p.rotateDown(target, distance)
	}
}

func (p *panel) rotateRight(target, distance int) {
	//
	//fmt.Printf("shift row %d by %d right\n", target, distance)
	old := p.copy()
	for i := 0; i < p.x; i++ {
		if old.isOn(i, target) {
			p.off(i, target)
			p.on(i+distance, target)
		}
	}
	p.normalize()
}

func (p *panel) rotateDown(target, distance int) {
	//
	//fmt.Printf("shift column %d by %d down\n", target, distance)
	old := p.copy()
	for i := 0; i < p.y; i++ {
		if old.isOn(target, i) {
			p.off(target, i)
			p.on(target, i+distance)
		}
	}
	p.normalize()
}

func main() {
	p := NewPanel(7, 3)
	p.parse("rect 3x2")
	fmt.Println(p)
	p.parse("rotate column x=1 by 1")
	fmt.Println(p)
	p.parse("rotate row y=0 by 4")
	fmt.Println(p)
	p.parse("rotate row x=1 by 1")
	fmt.Println(p)
	fmt.Println(p.count())

	f, err := os.Open("input.txt")
	if err != nil {
		fmt.Println(err)
	}

	r := bufio.NewReader(f)

	p = NewPanel(50, 6)
	for {
		line, err := readline(r)
		if err != nil {
			break
		}
		p.parse(line)
	}
	fmt.Println(p)
	fmt.Println(p.count())
}

func readline(r *bufio.Reader) (string, error) {
	line, err := r.ReadString('\n')
	if err != nil {
		return "", err
	}
	return line, err
}
