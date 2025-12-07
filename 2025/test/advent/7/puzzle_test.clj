(ns advent.7.puzzle-test
  (:require 
    [advent.7.puzzle :as sut]
    [clojure.test :refer :all]))

(deftest test-star1
  (is (= 21 (sut/-main "data/7/example.txt"))))
