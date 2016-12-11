package main

import "testing"

func TestSample(t *testing.T) {
	TLS("abba[mnop]qrst", true, t)
	TLS("acba[mnop]abba", true, t)
	TLS("abcd[bddb]xyyx", false, t)
	TLS("aaaa[qwer]tyui", false, t)
	TLS("ioxxoj[asdfgh]zxcvbn", true, t)

}

func TLS(ip string, expected bool, t *testing.T) {
	actual := supportsTLS(ip)
	if actual != expected {
		t.Fatalf("%s fails, should be %v", ip, expected)
	}
}

func SSL(ip string, expected bool, t *testing.T) {
	actual := supportsSSL(ip)
	if actual != expected {
		t.Fatalf("%s fails, should be %v", ip, expected)
	}
}

func TestSSL(t *testing.T) {
	SSL("aba[bab]xyz", true, t)
	SSL("xyx[xyx]xyx", false, t)
	SSL("aaa[kek]eke", true, t)
	SSL("zazbz[bzb]cdb", true, t)
}
