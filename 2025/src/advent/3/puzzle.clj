(ns advent.3.puzzle
  (:require 
    [clojure.java.io :as io]
    [clojure.string :as str]
    [advent.util :as util]))

(defn parse [line]
  (->> line
       (#(str/split % #""))
       (map Integer/parseInt)))

(defn max-with-pos [ns]
  (reduce 
    (fn [[max-i max] [i n]]
      (if (> n max)
        [i n]
        [max-i max]))
    (map-indexed (fn [i n] [i n]) ns)))

(defn max-joltage 
  [batts ns]
  (loop [acc []
         batts batts
         safety (dec batts)
         ns ns]
    (let 
      [[max-pos max-batt] (max-with-pos (drop-last (max safety (dec batts)) ns))]
      (if (= (dec batts) 0)
        (->> (conj acc [max-pos max-batt])
             (map second)
             (apply str)
             Long/parseLong)
        (recur 
          (conj acc [max-pos max-batt])
          (dec batts)
          (min 0 (- safety max-pos))
          (drop (inc max-pos) ns))))))

(defn -main 
  ([filename]
   (-main filename 12))
  ([filename batteries]
   (with-open [rdr (io/reader filename)]
     (->> rdr
          line-seq
          (map parse)
          (map (partial max-joltage batteries))
          (reduce +)
          (util/spy>> "output:")))))
