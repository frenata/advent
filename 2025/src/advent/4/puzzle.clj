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
  (let [-find (fn 
                [[[x y] _]]
                {[(inc x) (inc y)] 1
                 [(inc x) (dec y)] 1
                 [(inc x)      y ] 1
                 [(dec x) (inc y)] 1
                 [(dec x) (dec y)] 1
                 [(dec x)      y ] 1
                 [x       (inc y)] 1
                 [x       (dec y)] 1
                 [x            y ] 0})

        nearby (apply merge-with + (map -find rolls))]
    (filter (fn [[pos _]] (contains? rolls pos)) nearby)))

(defn remove-adjacent [size rolls]
  (loop [rolls   rolls
         removed 0]
    (let [removeable (into {} (filter (fn [[_ n]] (< n 4)) (find-adjacent size rolls)))
          n          (count removeable)]
      (if (= 0 n)
        removed
        (recur
          (into {} (remove (fn [[r _]] (contains? removeable r)) rolls))
          (+ removed n))))))

(defn -star1-main 
  ([filename]
   (-star1-main filename 140))
  ([filename size]
   (with-open [rdr (io/reader filename)]
     (->> rdr
          line-seq
          parse
          (find-adjacent size)
          (filter (fn [[pos n]] (< n 4)))
          count
          (util/spy>> "output:")))))

(defn -main 
  ([filename]
   (-main filename 140))
  ([filename size]
   (with-open [rdr (io/reader filename)]
     (->> rdr
          line-seq
          util/start-timer!
          parse
          (remove-adjacent size)
          (util/spy-timer! "output:")))))
