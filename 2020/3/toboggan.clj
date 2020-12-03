(ns advent.toboggan
  (:require [clojure.string :as str]
            [clojure.pprint :as pp]
            [clojure.java.io :as io]))

(defn read-lines [f]
  (with-open [r (io/reader f)]
    (into [] (line-seq r))))

(def input
  (->> "3/input.dat"
       read-lines))

(def slope-map (->> input 
     (map-indexed (fn [i row]
                    (->> row
                         (map-indexed (fn [j col] [[j i] (if (= \# col) 1 0)]))
                         (apply concat))))
     (apply concat)
     (apply hash-map)))

(defn count-trees [slope [right down]]
  (let [bottom (second (apply max-key second (keys slope)))
        wrap   (inc (first (apply max-key first (keys slope))))]
    (loop [[x y] [0 0]
           hits  0]
      (if (> y bottom)
        hits
        (recur [(mod (+ x right) wrap) (+ y down)]
               (+ hits (slope [x y])))))))

(def star1 (count-trees slope-map [3 1]))
(def star2 (apply * (map (partial count-trees slope-map) [[1 1] [3 1] [5 1] [7 1] [5 2]])))

#_(println star1 star2)

(assert (= star1 178))
(assert (= star2 3492520200))
