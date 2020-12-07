(ns advent.boarding
  (:require [clojure.java.io :as io]))

(def input "5/input.dat")
(def ROWS 128)
(def COLUMNS 8)

(defn read-lines
  "Open a file and read all the lines"
  [f]
  (with-open [r (io/reader f)]
    (into [] (line-seq r))))

(defn search [max down up s]
  (loop [n 0
         [min max] [0 max]]
    (cond
      (= n    (count s)) min
      (= down (nth s n)) (recur (inc n) [min (/ (+ min max) 2)])
      (= up   (nth s n)) (recur (inc n) [(/ (+ min max) 2) max]))))

(defn str->boarding-pass [s]
  [(search ROWS \F \B (take 7 s))
   (search COLUMNS \L \R (drop 7 s))])

(defn boarding-pass->boarding-id [[row column]]
  (+ column (* 8 row)))

(defn find-all-ids [input]
  (->> input read-lines (map (comp boarding-pass->boarding-id str->boarding-pass))))

(defn find-missing [xs]
  (loop [prev (first xs)
         [x & xs] (rest xs)]
    (cond
      (nil? xs) nil ;; shouldn't ever occur given problem definition, but not writing a base case feels icky
      (not= (inc prev) x) (inc prev)
      :else (recur x xs))))

(def star1 (apply max (find-all-ids input)))
(def star2 (find-missing (sort (find-all-ids input))))

(println star1 star2)

(assert (= 915 star1))
(assert (= 699 star2))
