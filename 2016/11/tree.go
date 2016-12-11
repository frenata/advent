package main

import "fmt"

type tool interface {
	Element() string
	Generator() bool
	String() string
}

type generator struct {
	element string
}

func (g generator) Element() string { return g.element }
func (g generator) Generator() bool { return true }
func (g generator) String() string  { return g.element + "-G" }

type chip struct {
	element string
}

func (c chip) Element() string { return c.element }
func (c chip) Generator() bool { return false }
func (c chip) String() string  { return c.element + "-C" }

type floor struct {
	tools []tool
}

func NewFloor() floor {
	return floor{make([]tool, 0)}
}

func (f *floor) Remove(t tool) bool {
	for i := range f.tools {
		if f.tools[i].String() == t.String() {
			//fmt.Printf("removed %s\n", t)
			f.tools = append(f.tools[:i], f.tools[i+1:]...)
			return true
		}
	}
	return false
}

func (f *floor) Add(t tool) {
	//fmt.Printf("added %s\n", t)
	f.tools = append(f.tools, t)
}

func (f floor) isChipPowered(c tool) bool {
	for _, t := range f.tools {
		if !c.Generator() && t.Generator() && t.Element() == c.Element() {
			return true
		}
	}
	return false
}

// returns true if radiation has ruined the product on this floor
func (f floor) failure() bool {
	if f.radiation() {
		for _, t := range f.tools {
			if !t.Generator() && !f.isChipPowered(t) {
				//fmt.Println(t)
				return true
			}
		}
	}
	return false
}

// returns true if the floor has a generator on it
func (f floor) radiation() bool {
	for _, t := range f.tools {
		if t.Generator() {
			return true
		}
	}
	return false
}

type factory struct {
	elevator int
	floors   []floor
}

// attempts to carry tools in the direction specified
// 1 means up
// -1 means down
// if legal: returns a new factory state and true
// otherwise: returns nil and false
func (f factory) Move(dir int, carried []tool) (factory, bool) {
	switch dir {
	case 1:
		if f.elevator+1 >= len(f.floors) {
			return factory{}, false
		}
	case -1:
		if f.elevator-1 < 0 {
			return factory{}, false
		}
	default:
		return factory{}, false
	}
	fnew := f.clone(dir)

	for _, t := range carried {
		fnew.floors[f.elevator].Remove(t)
		fnew.floors[fnew.elevator].Add(t)
	}

	return fnew, !fnew.failure()
}

// returns new factory that matches old but has moved elevator by dir
func (f factory) clone(dir int) factory {
	fnew := NewFactory(len(f.floors))
	fnew.elevator = f.elevator + dir

	for i := range f.floors {
		fnew.floors[i].tools = make([]tool, len(f.floors[i].tools))
		copy(fnew.floors[i].tools, f.floors[i].tools)
	}

	return fnew
}

func NewFactory(levels int) factory {
	fl := make([]floor, levels)

	for i := range fl {
		fl[i] = NewFloor()
	}
	return factory{0, fl}
}

// returns true if radiation has ruined the product
func (f factory) failure() bool {
	for _, fl := range f.floors {
		if fl.failure() {
			return true
		}
	}
	return false
}

func (a factory) Equals(b factory) bool {
	if a.elevator != b.elevator {
		return false
	}

	if len(a.floors) != len(b.floors) {
		return false
	}

	for f := range a.floors {
		for _, t1 := range a.floors[f].tools {
			match := false
			for _, t2 := range b.floors[f].tools {
				if t1 == t2 {
					match = true
				}
			}
			if match == false {
				return false
			}
		}
	}
	return true
}

func (f factory) String() string {
	str := ""
	for i := len(f.floors) - 1; i >= 0; i-- {
		str += fmt.Sprintf("%d: ", i+1)
		if i == f.elevator {
			str += "E "
		} else {
			str += "  "
		}
		for _, t := range f.floors[i].tools {
			str += t.String() + " "
		}
		str += "\n"
	}
	return str
}

func (f factory) current() floor {
	return f.floors[f.elevator]
}

func (f factory) PossibleMoves() []factory {
	possible := Factories()

	for i, v := range f.current().tools {
		possible.addIfValid(f.Move(-1, []tool{v}))
		possible.addIfValid(f.Move(1, []tool{v}))
		for _, v2 := range f.current().tools[i+1:] {
			possible.addIfValid(f.Move(-1, []tool{v, v2}))
			possible.addIfValid(f.Move(1, []tool{v, v2}))
		}
	}
	fmt.Printf("possible moves %d for \n%s\n", len(possible.f), f)
	return possible.f
}

func (f factory) Success() bool {
	for i := 0; i < len(f.floors)-1; i++ {
		if len(f.floors[i].tools) > 0 {
			return false
		}
	}
	return true
}

type factories struct{ f []factory }

func Factories() factories {
	return factories{make([]factory, 0)}
}

func (c *factories) addIfValid(f factory, b bool) {
	if b {
		c.f = append(c.f, f)
	}
}

type node struct {
	this     factory
	parent   *node
	children []node
}

func (n *node) insert() []node {
	n.children = make([]node, 0)

	for _, f := range n.this.PossibleMoves() {
		matches := false

		gpa := n.parent
		for gpa != nil && !matches {
			if gpa.this.Equals(f) {
				matches = true
			}
			gpa = gpa.parent
		}
		if !matches {
			n.children = append(n.children, node{f, n, nil})
		}
	}
	return n.children
}

func (n node) String() string {
	return n.this.String()
}

type nodelist []node

func (n nodelist) String() string {
	str := ""
	for _, v := range n {
		str += v.String() + "\n"
	}
	return str
}

func search(root factory) {
	n := node{root, nil, nil}

	count := 1
	children := n.insert()
	for {
		fmt.Printf("Searching... %d\n", count)
		for _, v := range children {
			if v.this.Success() {
				fmt.Println(v, count)
				return
			}
		}
		children = spread(children)
		fmt.Println(children)
		count++
	}
}

func spread(in []node) []node {
	out := make([]node, 0)

	for _, v := range in {
		out = append(out, v.insert()...)
	}
	return out
}

func main() {
	f := NewFactory(4)
	f.floors[0] = floor{[]tool{
		generator{"polonium"},
		generator{"thulium"},
		chip{"thulium"},
		generator{"promethium"},
		generator{"ruthenium"},
		chip{"ruthenium"},
		generator{"cobalt"},
		chip{"cobalt"},
	}}
	f.floors[1] = floor{[]tool{
		chip{"polonium"},
		chip{"promethium"},
	}}

	search(f)
}
