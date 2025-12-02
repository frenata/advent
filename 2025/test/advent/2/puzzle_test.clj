(ns advent.2.puzzle-test
  (:require 
    [advent.2.puzzle :as sut]
    [clojure.java.io :as io]
    [clojure.test :refer :all]))

(deftest test-star1
    (is (= 1227775554 (sut/-main "data/2/example.txt" sut/twice?))))

#_(deftest test-star2
    (is (= 4174379265 (sut/-main "data/2/example.txt" sut/at-least-twice?))))

