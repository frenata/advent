package main

import (
	"bufio"
	"fmt"
	"os"
)

func code(noise []string) string {
	str := ""
	for i := 0; i < len(noise[0]); i++ {
		str = str + string(posFreq(noise, i))
	}

	return str
}

func posFreq(noise []string, i int) rune {
	freq := make(map[rune]int)
	for _, v := range noise {
		letter := rune(v[i])
		freq[letter]++
	}

	/* part 1
	max := 0
	var maxLetter rune
	for letter, n := range freq {
		if n > max {
			max = n
			maxLetter = letter
		}
	}
	return maxLetter*/
	var min int
	var minLetter rune
	for letter, n := range freq {
		if n < min || min == 0 {
			min = n
			minLetter = letter
		}
	}
	return minLetter
}

func readline(r *bufio.Reader) (string, error) {
	line, err := r.ReadString('\n')
	if err != nil {
		return "", err
	}
	return line, err
}

func main() {
	f, err := os.Open("input.txt")
	if err != nil {
		fmt.Println(err)
	}

	r := bufio.NewReader(f)

	noise := make([]string, 0)
	for {
		line, err := readline(r)
		if err != nil {
			break
		}
		noise = append(noise, line)
	}

	fmt.Println(code(noise))
}
