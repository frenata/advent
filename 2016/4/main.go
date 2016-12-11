package main

import (
	"bufio"
	"fmt"
	"os"
	"strings"
)

func main() {
	f, err := os.Open("input.txt")
	if err != nil {
		fmt.Println(err)
	}

	r := bufio.NewReader(f)

	sum := 0
	for {
		line, err := readline(r)
		if err != nil {
			break
		}
		r := Room(line)
		if r.isReal() {
			sum += r.Sector()
		}
		if strings.Contains(r.Decrypt(), "north") {
			fmt.Println(r)
		}
	}
	fmt.Println(sum)
}

func readline(r *bufio.Reader) (string, error) {
	line, err := r.ReadString('\n')
	if err != nil {
		return "", err
	}
	return line, err
}
