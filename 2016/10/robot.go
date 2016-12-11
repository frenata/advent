package main

import (
	"bufio"
	"fmt"
	"io"
	"os"
	"regexp"
	"strconv"
)

type bins struct {
	n int
	m map[int][]int
}

func Bins() bins { return bins{0, make(map[int][]int)} }

func (b *bins) add(target int, chip chan int) {
	b.n++
	go func() {
		in := <-chip
		b.m[target] = append(b.m[target], in)
		b.n--
	}()
}

type robot struct {
	id            int
	chips         []int
	in, low, high chan int
}

func (r robot) String() string {
	return fmt.Sprintf("R%d: %v ", r.id, r.chips)
}

func Robot(id int) *robot {
	r := robot{}
	r.id = id
	r.chips = make([]int, 0)
	r.in = make(chan int, 2)
	r.low = make(chan int, 1)
	r.high = make(chan int, 1)
	go r.collect()
	return &r
}

func (r *robot) In(from chan int) {
	go func() {
		n := <-from
		r.in <- n
	}()
}

func (r *robot) collect() {
	fmt.Println("collecting")
	for i := 0; i < 2; i++ {
		n := <-r.in
		fmt.Printf("robot %d collects %d\n", r.id, n)
		r.chips = append(r.chips, n)
	}

	if r.chips[0] < r.chips[1] {
		r.low <- r.chips[0]
		r.high <- r.chips[1]
	}
	fmt.Println(r.chips)
	r.high <- r.chips[0]
	r.low <- r.chips[1]
}

func Input(n int) chan int {
	out := make(chan int, 1)
	out <- n
	return out
}

type robots struct {
	r map[int]*robot
}

func Robots() robots {
	return robots{make(map[int]*robot)}
}

func (r *robots) In(target int, ch chan int) {
	if _, ok := r.r[target]; !ok {
		r.r[target] = Robot(target)
	}
	r.r[target].In(ch)
}

func (r *robots) Low(target int) chan int {
	if _, ok := r.r[target]; !ok {
		r.r[target] = Robot(target)
	}
	return r.r[target].low
}
func (r *robots) High(target int) chan int {
	if _, ok := r.r[target]; !ok {
		r.r[target] = Robot(target)
	}
	return r.r[target].high
}

func (r robots) String() string {
	s := ""
	for _, v := range r.r {
		s += v.String()
	}
	return s
}

type factory struct {
	b *bins
	r *robots
}

func Factory() factory {
	b := Bins()
	r := Robots()
	return factory{&b, &r}
}

func (f *factory) execute(instruction string) {
	r := f.r
	b := f.b
	input := regexp.MustCompile("value (\\d+) goes to bot (\\d+)")
	output := regexp.MustCompile("bot (\\d+) gives low to (bot|output) (\\d+) and high to (bot|output) (\\d+)")

	fmt.Println(instruction)
	var matches []string
	if matches = input.FindStringSubmatch(instruction); len(matches) > 0 {
		fmt.Println("input match")
		chip, _ := strconv.Atoi(matches[1])
		robot, _ := strconv.Atoi(matches[2])
		r.In(robot, Input(chip))
	} else if matches = output.FindStringSubmatch(instruction); len(matches) > 0 {
		fmt.Println("output match")
		giver, _ := strconv.Atoi(matches[1])
		targetlow, _ := strconv.Atoi(matches[3])
		targethigh, _ := strconv.Atoi(matches[5])
		if matches[2] == "bot" {
			fmt.Println("give to bot", targetlow)
			r.In(targetlow, r.Low(giver))
		} else {
			fmt.Println("give to output", targetlow)
			b.add(targetlow, r.Low(giver))
		}
		if matches[4] == "bot" {
			fmt.Println("give to bot", targethigh)
			r.In(targethigh, r.High(giver))
		} else {
			fmt.Println("give to output", targethigh)
			b.add(targethigh, r.High(giver))
		}
	} else {
		panic("bad instruction")
	}
}

func (f factory) done() bool {
	return f.b.n == 0
}

func (f factory) String() string {
	return fmt.Sprintf("bins:\n%v\nrobots:\n%v", f.b, f.r)
}

func main() {
	fr := openFileReader("input.txt")

	f := Factory()
	for line, err := readline(fr); err != io.EOF; line, err = readline(fr) {
		f.execute(line)
	}

	for !f.done() {
	}

	fmt.Println(f)
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
