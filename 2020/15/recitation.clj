(ns advent.recitation
  (:require [clojure.string :as str]))

(def input "15/test.dat")

(defn recite [starting-numbers n]
  (loop [record (apply hash-map (apply concat (map-indexed (fn [i x] [x (list (inc i))]) starting-numbers)))
         round (inc (count starting-numbers))
         prev (last starting-numbers)]
    (let [curr-record (record prev)
          diff-record (apply - curr-record)]
      (cond
        (> round n) prev

        (= 1 (count curr-record))
        (recur (assoc record 0 (take 2 (conj (record 0) round))) (inc round) 0)

        (= 2 (count curr-record))
        (recur (assoc record diff-record (take 2 (conj (record diff-record) round))) (inc round) diff-record)))))

(recite [0 3 6] 2020)
(println (recite [0 1 4 13 15 12 16] 2020))
(println (recite [0 1 4 13 15 12 16] 30000000))
