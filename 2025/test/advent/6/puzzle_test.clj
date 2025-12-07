(ns advent.6.puzzle-test
  (:require 
    [advent.6.puzzle :as sut]
    [clojure.test :refer :all]))

(deftest test-star1
  (is (= 4277556 (sut/-main "data/6/example.txt" sut/parse-star1))))

(deftest test-star2
  (is (= 3263827 (sut/-main "data/6/example.txt" sut/parse-star2))))
