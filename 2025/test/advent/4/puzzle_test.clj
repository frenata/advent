(ns advent.4.puzzle-test
  (:require 
    [advent.4.puzzle :as sut]
    [clojure.test :refer :all]))

(deftest test-star1
    (is (= 13 (sut/-main "data/4/example.txt" 10))))
