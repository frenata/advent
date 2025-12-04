(ns advent.3.puzzle-test
  (:require 
    [advent.3.puzzle :as sut]
    [clojure.test :refer :all]))

(deftest test-star1
    (is (= 357 (sut/-main "data/3/example.txt" 2))))

(deftest test-star2
    (is (= 3121910778619 (sut/-main "data/3/example.txt" 12))))

(deftest test-max-joltage
  (is (= 78 (sut/max-joltage 2 '(2 3 4 2 3 4 2 3 4 2 3 4 2 7 8))))
  (is (= 92 (sut/max-joltage 2 '(8 1 8 1 8 1 9 1 1 1 1 2 1 1 1)))))

(deftest test-max-with-pos
  (is (= [3 9] (sut/max-with-pos [8 2 4 9 1 3 9]))))

(deftest test-max-joltage-12
  (is (= 478 (sut/max-joltage 3 '(2 3 4 2 3 4 2 3 4 2 3 4 2 7 8))))
  (is (= 92  (sut/max-joltage 2 '(8 1 8 1 8 1 9 1 1 1 1 2 1 1 1))))
  (is (= 78 (sut/max-joltage 2 '(2 3 4 2 3 4 2 3 4 2 3 4 2 7 8)))))
