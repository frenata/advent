(ns advent.seating
  (:require [clojure.string :as str]))

(def input "11/test.dat")

(defn- text->hash-map 
  "Reads a collection of lines into a hash-map from x,y coords to the value in the block."
  ([lines] (text->hash-map identity lines))
  ([f lines]
   (->> lines 
        (map-indexed (fn [y row]
                       (->> row
                            (map-indexed (fn [x cell] [[x y] (f cell)]))
                            (apply concat))))
        (apply concat)
        (apply hash-map))))

(defn count-adjacent [seats [x y :as pos]]
  (let [adj [(seats [(dec x) (dec y)])
             (seats [(dec x) y])
             (seats [(dec x) (inc y)])
             (seats [x (dec y)])
             ;; (seats x y)
             (seats [x (inc y)])
             (seats [(inc x) (dec y)])
             (seats [(inc x) y])
             (seats [(inc x) (inc y)])]]
    (apply + (filter some? adj))))

(defn- print-seats [seats]
  (doseq [y (range 9)]
    (doseq [x (range 10)]
      (print (case (seats [x y]) 0 "L" 1 "#" ".")))
    (print \newline)))

(defn seating-round [seats]
  (reduce (fn [acc [pos type :as seat]]
            (let [adj (count-adjacent seats pos)]
              (conj acc
                    (case type
                      nil seat
                      1 [pos (if (>= adj 4) 0 1)]
                      0 [pos (if (=  adj 0) 1 0)]
                      ))))
          {} seats))

(defn chaotic-iteration [opts seats]
  (loop [prev {}
         curr seats
         round 0]
    (when (:print opts)
      (println "Round " round)
      (print-seats curr))
    (if (= prev curr) [curr (dec round)]
        (recur curr (seating-round curr) (inc round)))))

(def star1
  (->> input
       slurp
       str/split-lines
       (text->hash-map #(case % \. nil \L 0 1))
       (chaotic-iteration {:print false})
       first
       (filter #(= 1 (second %)))
       count))

(println star1)

#_(assert (= star1 2470))
