(ns advent.recitation
  (:require [clojure.string :as str]))

(def input "15/test.dat")

(defn recite [starting-numbers n]
  ;; (println "START")
  (loop [record (transient (apply hash-map (apply concat (map-indexed (fn [i x] [x (transient [(inc i)])]) starting-numbers))))
         round (inc (count starting-numbers))
         prev (last starting-numbers)
         ]
    ;; (println record round prev)
    (let [prev-record (record prev)
          ;; diff-record (- (nth prev-record 0) (nth prev-record 1))
          ]
      ;; (println prev-record )
      ;; (println round)
    (cond
      (> round n) prev

      (= 1 (count prev-record))
      (recur (assoc! record 0 (assoc! (record 0) 1 (nth (record 0) 0) 0 round) #_(take 2 (conj! (record 0) round))) (inc round) 0)

      (= 2 (count prev-record))
      (let [diff (- (nth prev-record 0) (nth prev-record 1))]
        ;; (println "Prev: " prev "DIff: " diff (nth prev-record 0) (nth prev-record 1))
        (recur
                       (if (record diff)
         (assoc! record diff
                       (assoc! (record diff) 1 (nth (record diff) 0) 0 round )
                       )
                       (assoc! record diff (transient [round]))
                       #_(take 2 (conj! (record diff-record) round))) (inc round) diff)
      ))
)
    )
  )

(recite [0 3 6] 2020)

;; (def out (recite [0 3 6] 5))

;; (def outp (persistent! out))

;; (map (fn [[k v]] [k (persistent! v)])  outp)

#_(recite [0 1 4 13 15 12 16] 2020)
(recite [0 3 6] 30000000)
#_(time (println (recite [0 1 4 13 15 12 16] 30000000)))
