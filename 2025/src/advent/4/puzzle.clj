(ns advent.4.puzzle
  (:require 
    [clojure.java.io :as io]
    [clojure.string :as str]
    [advent.util :as util]))

(defn parse-line [row line]
  (->> line
       (map-indexed (fn [col pos] [[row col] pos]))
       (filter (fn [[_ pos]] (= pos \@)))
       (into {})))

(defn parse [lines]
  (->> lines
       (map-indexed parse-line)
       (apply merge)))

(defn find-adjacent [size rolls]
  (let [-find 
        (fn 
          [[[x y] _]]
          (let [adjs 
                [
                 [(inc x) (inc y)]
                 [(inc x) (dec y)]
                 [(inc x)      y ]
                 [(dec x) (inc y)]
                 [(dec x) (dec y)]
                 [(dec x)      y ]
                 [x       (inc y)]
                 [x       (dec y)]
                 ]]
            adjs
            #_(filter (fn [[x y]] 
                      (not (or (> x size) (> y size)
                               (< x 0) (< y 0)))) adjs)
            ))

    nearby (mapcat -find rolls)
        ]
    ; (println rolls nearby)
    (filter (fn [pos] (contains? rolls pos)) nearby)))

(defn -main 
  ([filename]
   (-main filename 140))
  ([filename size]
  (with-open [rdr (io/reader filename)]
    (->> rdr
         line-seq
         parse
         ; (util/spy>> "rolls:")
         ; (map (partial find-adjacent size))
         (find-adjacent size)
         ; (util/spy>> "before concat")
         ; (apply concat)
         ; sort
         ; (util/spy>> "before freq")
         frequencies
         ; sort
         ; (util/spy>> "nears:")
         (filter (fn [[pos n]] (< n 4)))
         ; (util/spy>> "moveable:")
         count
         (util/spy>> "output:")))))
