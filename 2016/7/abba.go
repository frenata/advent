package main

import (
	"bufio"
	"fmt"
	"os"
)

func supportsTLS(ip string) bool {
	hypernet := false
	hyperabba := false
	ipabba := false

	for i, v := range ip {
		//fmt.Println(string(v), hypernet, ipabba)
		if v == '[' {
			hypernet = true
		} else if v == ']' && hypernet {
			hypernet = false
		} else {
			if hypernet && !hyperabba {
				hyperabba = abba(ip[i:])
			} else if !hypernet && !ipabba {
				//fmt.Println(ip[i:])
				ipabba = abba(ip[i:])
			}
		}
	}
	//fmt.Println(ipabba, hyperabba)
	return ipabba && !hyperabba
}
func abba(str string) bool {
	return len(str) >= 4 &&
		str[0] == str[3] &&
		str[1] == str[2] &&
		str[0] != str[2]
}

func supportsSSL(ip string) bool {
	hypernet := false
	aba := make([]string, 0)
	bab := make([]string, 0)

	for i, v := range ip {
		if v == '[' {
			hypernet = true
		} else if v == ']' && hypernet {
			hypernet = false
		}

		if isaba(ip[i:]) {
			if hypernet {
				bab = append(bab, ip[i:i+3])
			} else {
				aba = append(aba, ip[i:i+3])
			}
		}
	}
	fmt.Println(aba, bab)
	for _, a := range aba {
		for _, b := range bab {
			if a[0] == b[1] && b[0] == a[1] {
				return true
			}
		}
	}
	return false
}

func isaba(str string) bool {
	return len(str) >= 3 &&
		str[0] == str[2] &&
		str[0] != str[1]
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

	countTLS := 0
	countSSL := 0
	for {
		line, err := readline(r)
		if err != nil {
			break
		}
		if supportsTLS(line) {
			countTLS++
		}
		if supportsSSL(line) {
			countSSL++
		}
	}
	fmt.Println(countTLS, countSSL)
}
