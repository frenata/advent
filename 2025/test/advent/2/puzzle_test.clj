(ns advent.2.puzzle-test
  (:require 
    [advent.2.puzzle :as sut]
    [clojure.test :refer :all]))

(deftest test-star1
    (is (= 1227775554 (sut/-main "data/2/example.txt" sut/twice?))))

(deftest test-star2
    (is (= 4174379265 (sut/-main "data/2/example.txt" sut/at-least-twice?))))

(deftest at-least-twice?
  (let [inputs [3 33 333 450 450450 909090909090909090 909090909]]
    (->> inputs
         (filter sut/at-least-twice?)
         (= [3 33 333 450450 909090909090909090]))))
