(ns advent.encoding
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def input "9/input.dat")

(defn ->numbers [input]
  (->> input slurp str/split-lines (map #(Long/parseLong %))))

(defn combine [xs]
  (->> xs
       (reduce (fn [acc x] (concat acc (map (partial vector x) xs))) [])
       (filter (fn [[x y]] (not= x y)))))

(defn is-valid-sum? [preamble n]
  (let [combos (combine preamble)]
    (seq (filter (fn [c] (= n (apply + c))) combos))))

(defn find-invalid [nums n]
  (loop [preamble (take n nums)
         [n & ns] (drop n nums)]
    (cond
      (nil? ns) n
      (is-valid-sum? preamble n) (recur (concat (rest preamble) [n]) ns)
      :else n)))

(defn find-sequence-sum [nums target]
  (loop [[min max] [0 1]]
    (let [ss (subvec nums min (inc max))
          sum (apply + ss)]
      (cond (= sum target) ss
            (> sum target) (recur [(inc min) max])
            (< sum target) (recur [min (inc max)])))))

(def star1 (find-invalid (->numbers input) 25))
(def star2 (apply + ((juxt first last) (sort (find-sequence-sum (vec (->numbers input)) star1)))))

(println star1 star2)

(assert (= star1 400480901))
(assert (= star2 67587168))
