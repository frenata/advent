(ns advent.6.puzzle-test
  (:require 
    [advent.6.puzzle :as sut]
    [clojure.test :refer :all]))

(deftest test-star1
  (is (= 4277556 (sut/-main "data/6/example.txt"))))
