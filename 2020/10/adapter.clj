(ns advent.adapter
  (:require [clojure.string :as str]))

(def input "10/input.dat")

(defn find-diffs [adapters]
  (loop [joltage 0
         diffs []
         [adapter & adapters] adapters]
    (if (not (nil? adapter))
      (recur adapter (conj diffs (- adapter joltage)) adapters)
      diffs)))

(defn find-runs [adapters]
  (loop [runs []
         current-run [0]
         [adapter & adapters] adapters]
    (cond
      (nil? adapter) (if (> (count current-run) 2) (conj runs current-run) runs)

      (= adapter (inc (last current-run)))
      (recur runs (conj current-run adapter) adapters)

      (not= adapter (inc (last current-run)))
      (recur (if (> (count current-run) 2) (conj runs current-run) runs) [adapter] adapters))))

(defn sort-joltages [input] (->> input slurp str/split-lines (map #(Integer/parseInt %)) sort))

(def star1 (->> input sort-joltages find-diffs (concat [3]) frequencies ((juxt #(get % 1) #(get % 3))) (apply *)))
(def star2 (->> input sort-joltages find-runs (map (comp {3 2, 4 4, 5 7} count)) (apply *)))

(println star1 star2)

(assert (= star1 2310))
(assert (= star2 64793042714624))
