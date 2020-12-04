(ns advent.toboggan
  (:require [clojure.string :as str]
            [clojure.pprint :as pp]
            [clojure.java.io :as io]))

(def input "3/input.dat")

(defn read-lines
  "Open a file and read all the lines"
  [f]
  (with-open [r (io/reader f)]
    (into [] (line-seq r))))

(defn text->hash-map 
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

(defn count-trees [[tree-map height width] [right down]]
    (loop [[x y] [0 0]
           hits  0]
      (if (>= y height)
        hits
        (recur [(mod (+ x right) width) (+ y down)]
               (+ hits (tree-map [x y]))))))

(def tree-map
  (->> input
       read-lines
       ((juxt (partial text->hash-map #(if (= \# %) 1 0))
              count
              (comp count first)))))

(def star1 (count-trees tree-map [3 1]))

(def star2 (apply * (map (partial count-trees tree-map) [[1 1] [3 1] [5 1] [7 1] [5 2]])))

#_(println star1 star2)

(assert (= star1 178))
(assert (= star2 3492520200))
