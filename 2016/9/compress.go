package main

import (
	"bufio"
	"fmt"
	"io"
	"os"
	"regexp"
	"strconv"
	"strings"
)

// regex to match a compression signature
const compressionSignature = "\\((\\d+)x(\\d+)\\)"

// decompresses the string, ignoring recursive decompression
func decompress(input string) string {
	output := input
	if before, middle, after, times, ok := splitCompression(input); ok {
		output = before +
			repeat(middle, times) +
			decompress(after)
	}
	return output
}

// recursively decompresses the string
func deepDecompress(input string) string {
	output := input
	if before, middle, after, times, ok := splitCompression(input); ok {
		output = before +
			repeat(deepDecompress(middle), times) +
			deepDecompress(after)
	}
	return output
}

// counts the length of what the recursively decompressed string *would* be
func deepCount(input string) int {
	count := len(input)
	if before, middle, after, times, ok := splitCompression(input); ok {
		count = deepCount(before) +
			deepCount(middle)*times +
			deepCount(after)
	}
	return count
}

// splits tho string up into:
// before -- before the decompression signature
// middle -- what the decompression signature says should be repeated
// after -- after what needs to be repeated
// times -- how many times the middle should be repeated
// additionally returns true if a compression is found, false if not
func splitCompression(input string) (string, string, string, int, bool) {
	matches, ok := findCompression(input)
	if !ok {
		return "", "", "", 0, false
	}

	// regex match says these values are numbers, so discard the error
	length, _ := strconv.Atoi(input[matches[2]:matches[3]])
	times, _ := strconv.Atoi(input[matches[4]:matches[5]])

	before := input[:matches[0]]
	after := input[matches[1]+length:]
	middle := input[matches[1] : matches[1]+length]

	return before, middle, after, times, true
}

// checks far a compression signature and returns the match indexes if found
func findCompression(input string) ([]int, bool) {
	decomp := regexp.MustCompile(compressionSignature)
	matches := decomp.FindStringSubmatchIndex(input)

	if len(matches) > 0 {
		return matches, true
	} else {
		return matches, false
	}
}

// returns input repeated n times
func repeat(input string, n int) string {
	output := ""
	for i := 0; i < n; i++ {
		output += input
	}
	return output
}

func main() {
	fr := openFileReader("input.txt")

	count := 0
	rcount := 0
	for line, err := readline(fr); err != io.EOF; line, err = readline(fr) {
		count += len(decompress(strings.TrimSpace(line)))
		// runs out of memory:
		// rcount += len(deepDecompress(strings.TrimSpace(line)))
		rcount += deepCount(strings.TrimSpace(line))
	}

	fmt.Println(count, rcount)
}

func readline(r *bufio.Reader) (string, error) {
	line, err := r.ReadString('\n')
	if err != nil {
		return "", err
	}
	return line, err
}

func openFileReader(name string) *bufio.Reader {
	f, err := os.Open(name)
	if err != nil {
		fmt.Println(err)
	}
	return bufio.NewReader(f)
}
