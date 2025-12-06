(ns advent.5.puzzle-test
  (:require 
    [advent.5.puzzle :as sut]
    [clojure.test :refer :all]))

(deftest test-star1
    (is (= 3 (sut/-main "data/5/example.txt"))))
