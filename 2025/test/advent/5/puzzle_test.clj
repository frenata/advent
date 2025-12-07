(ns advent.5.puzzle-test
  (:require 
    [advent.5.puzzle :as sut]
    [clojure.test :refer :all]))

(deftest test-star1
    (is (= 3 (sut/-main "data/5/example.txt" true))))

(deftest test-star2
    (is (= 14 (sut/-main "data/5/example.txt"))))
