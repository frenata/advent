package main

import (
	"bufio"
	"fmt"
	"os"
	"regexp"
	"strconv"
	"strings"
)

type triangle struct {
	a, b, c int
}

func Triangle(spec string) triangle {
	sides := strings.Split(spec, " ")
	a, _ := strconv.Atoi(sides[0])
	b, _ := strconv.Atoi(sides[1])
	c, _ := strconv.Atoi(sides[2])

	return triangle{a, b, c}
}

func ThreeTriangles(str string) [3]triangle {
	var sides [9]int
	nums := strings.Split(str, " ")
	for i, s := range nums {
		fmt.Println(i)
		sides[i], _ = strconv.Atoi(s)
	}

	return [3]triangle{{sides[0], sides[3], sides[6]}, {sides[1], sides[4], sides[7]}, {sides[2], sides[5], sides[8]}}
}

func (t triangle) isValid() bool {
	return t.a+t.b > t.c && t.a+t.c > t.b && t.b+t.c > t.a
}

func main() {
	f, err := os.Open("input.dat")
	if err != nil {
		fmt.Println(err)
	}

	r := bufio.NewReader(f)
	count := 0
	for {
		l1, err := readline(r)
		l2, err := readline(r)
		l3, err := readline(r)
		if err != nil {
			break
		}
		lines := fmt.Sprintf("%s %s %s", l1, l2, l3)
		fmt.Println(lines)
		triangles := ThreeTriangles(lines)
		for _, t := range triangles {
			if t.isValid() {
				count++
			}
		}
	}
	fmt.Println(count)
}

func readline(r *bufio.Reader) (string, error) {
	line, err := r.ReadString('\n')
	if err != nil {
		return "", err
	}
	re := regexp.MustCompile("\\s+")
	line = re.ReplaceAllString(line, " ")
	return strings.TrimSpace(line), nil
}
