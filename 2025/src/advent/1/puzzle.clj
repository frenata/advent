(ns advent.1.puzzle
  (:require 
    [clojure.java.io :as io]
    [advent.util :as util]))

(defn parse [line]
  (->> line
       (re-matches #"(L|R)(\d+)")
       rest
       ((fn [[dir dist]] 
          (let [ mult (if (= dir "L") -1 1)
                dist (Integer/parseInt dist)]
            (* mult dist))))))

(defn tick-exact [[pos zeros] rotate] 
  (let [new-pos (mod (+ pos rotate) 100)
        new-zeros (if (= 0 new-pos) (inc zeros) zeros)]
    [new-pos new-zeros]))

(defn tick-pass [[pos zeros] rotate] 
  (let [raw-pos (+ pos rotate)
        passes (cond 
                 (= 0 raw-pos) 1
                 (> raw-pos 0) (Math/abs (quot raw-pos 100))
                 (< raw-pos 0) (+ 
                                 (if (= pos 0) 0 1)
                                 (Math/abs (quot raw-pos 100)))) ]
    #_(println [pos zeros rotate raw-pos passes])
    [(mod raw-pos 100) (+ zeros passes)]))

(defn -main 
  ([filename]
   (-main filename tick-pass))
  ([filename func]
   (with-open [rdr (io/reader filename)]
     (->> rdr
          line-seq
          (mapv parse)
          (reduce func [50 0])
          second
          (util/spy>> "password:")))))
