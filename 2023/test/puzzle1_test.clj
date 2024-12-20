(ns puzzle1-test
  (:require 
    [advent.1.puzzle :as sut]
    [clojure.test :refer :all]
    ))

(deftest test-sum
  (is (= 142 (sut/sum-valid "data/1/example.txt"))))

(deftest test-sum-natural
  (is (= 281 (sut/sum-valid "data/1/example2.txt"))))
