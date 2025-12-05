(ns advent.4.puzzle-test
  (:require 
    [advent.4.puzzle :as sut]
    [clojure.test :refer :all]))

(deftest test-star1
    (is (= 13 (sut/-star1-main "data/4/example.txt" 10))))

(deftest test-star2
    (is (= 43 (sut/-main "data/4/example.txt" 10))))
