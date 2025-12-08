(ns advent.7.puzzle-test
  (:require 
    [advent.7.puzzle :as sut]
    [clojure.test :refer :all]))

(deftest test-stars
  (let [[splits timelines] (sut/-main "data/7/example.txt")]
    (is (= splits 21))
    (is (= timelines 40))))
