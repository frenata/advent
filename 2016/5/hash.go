package main

import (
	"crypto/md5"
	"fmt"
	"strconv"
)

func hash(door string, i int) string {
	str := door + strconv.Itoa(i)
	return fmt.Sprintf("%x", md5.Sum([]byte(str)))
}

func password(door string) string {
	pass := ""
	for i := 0; ; i++ {
		if len(pass) == 8 {
			break
		}
		h := hash(door, i)
		if v, ok := niceHash(h); ok {
			pass += string(v)
		}
	}
	return pass
}

func fancyPassword(door string) string {
	pass := make([]rune, 8)
	count := 0
	for i := 0; ; i++ {
		if count == 8 {
			fmt.Println(i)
			break
		}
		h := hash(door, i)
		if v, pos, ok := niceHashPos(h); ok {
			if pass[pos] == 0 {
				pass[pos] = v
				count++
			}
		}
	}
	return string(pass)
}

func niceHash(candidate string) (rune, bool) {
	for i := 0; i < 5; i++ {
		if candidate[i] != '0' {
			return ' ', false
		}
	}
	return rune(candidate[5]), true
}

func niceHashPos(candidate string) (rune, int, bool) {
	for i := 0; i < 5; i++ {
		if candidate[i] != '0' {
			return ' ', 0, false
		}
	}
	pos, err := strconv.Atoi(candidate[5:6])
	if err != nil || pos < 0 || pos > 7 {
		return ' ', 0, false
	}
	return rune(candidate[6]), pos, true
}

func main() {
	fmt.Println(password("uqwqemis"))
	fmt.Println(fancyPassword("uqwqemis"))
}
