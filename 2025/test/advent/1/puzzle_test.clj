(ns advent.1.puzzle-test
  (:require 
    [advent.1.puzzle :as sut]
    [clojure.java.io :as io]
    [clojure.test :refer :all]))

(deftest test-star1
    (is (= 3 (sut/-main "data/1/example.txt" sut/tick-exact))))

(deftest test-star2
    (is (= 6 (sut/-main "data/1/example.txt" sut/tick-pass))))

(deftest test-star2-multiple-passes
    (is (= 12 (sut/-main "data/1/example2.txt" sut/tick-pass))))

(deftest test-star2-bug
    (is (= 2 (sut/-main "data/1/example3.txt" sut/tick-pass))))
