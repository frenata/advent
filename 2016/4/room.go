package main

import (
	"regexp"
	"sort"
	"strconv"
	"strings"
)

type room struct {
	code, name, sector, checksum string
}

func SumRealSector(rooms []string) (sum int) {
	for _, s := range rooms {
		r := Room(s)
		if r.isReal() {
			sum += r.Sector()
		}
	}
	return sum
}

func Room(input string) room {
	re := regexp.MustCompile("([A-Za-z\\-]+)(\\d+)\\[([A-Za-z]+)\\]")
	split := re.FindStringSubmatch(input)
	name := strings.TrimSuffix(split[1], "-")
	sector := split[2]
	checksum := split[3] //strings.TrimSuffix(split[1], "]")

	return room{input, name, sector, checksum}
}

func (r room) isReal() bool {
	//fmt.Println(r.checksum, r.Checksum())
	return r.checksum == r.Checksum()
}

func (r room) Sector() int {
	s, _ := strconv.Atoi(r.sector)
	return s
}

func (r room) Checksum() string {
	count := make(map[rune]int)
	name := strings.Replace(r.name, "-", "", -1)
	for _, v := range name {
		count[v]++
	}

	pl := make(pairlist, len(count))
	i := 0
	for k, v := range count {
		pl[i] = pair{k, v}
		i++
	}
	sort.Sort(pl)

	cs := ""

	for i := 0; i < 5; i++ {
		cs += string(pl[i].key)
	}

	return cs
}

func (r room) Decrypt() string {
	real := ""
	shifts := r.Sector() % 26
	for _, v := range r.name {
		if v == '-' {
			real += " "
			continue
		}
		n := int(v)
		n += shifts
		if n > 122 {
			remainder := n - 122
			n = 96 + remainder
		}
		real += string(n)
	}

	return real
}

func (r room) String() string {
	return r.name + " " + r.sector + " " + r.checksum
}

type pair struct {
	key   rune
	value int
}
type pairlist []pair

func (p pairlist) Len() int      { return len(p) }
func (p pairlist) Swap(i, j int) { p[i], p[j] = p[j], p[i] }
func (p pairlist) Less(i, j int) bool {
	if p[i].value == p[j].value {
		return p[i].key < p[j].key
	} else {
		return p[i].value > p[j].value
	}
}
