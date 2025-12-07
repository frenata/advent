(ns advent.6.puzzle
  (:require 
    [clojure.java.io :as io]
    [clojure.string :as str]
    [advent.util :as util]))

(defn parse-token [token]
  (cond
    (= token "*") *
    (= token "+") +
    :else (Integer/parseInt token)))

(defn parse-line-star1 [row line]
  (->> line
       str/trim
       (#(str/split % #" +"))
       (map-indexed (fn [col pos] [col [(parse-token pos)]]))))

(defn parse-star1 [lines]
  (->> lines
       (map-indexed parse-line-star1)
       (map (partial into {}))
       (apply merge-with into)
       vals))

(defn parse-problem [lines [skip problems] [op size]]
  (let [digits (->> (range size) 
                    reverse
                    (map (fn [n] (map (fn [line] (nth line (+ n skip))) lines) ))
                    (map (comp Integer/parseInt str/trim #(apply str %))))]
    [(+ skip (inc size)) (conj problems digits)]))

(defn parse-star2 [lines]
  (let [ops (->> lines
                 last
                 (re-seq #"(\*|\+)( +)")
                 (map (fn [[_ op line]]
                        [op (count line)]))
                 ((fn [ops] (concat (drop-last ops)
                                    [[(first (last ops)) (inc (second (last ops)))]]))))
        problems (last (reduce (partial parse-problem (drop-last lines)) [0 []] ops)) ]
    (map (fn [[op _] args] (concat args [(parse-token op)]) ) ops problems)))

(defn calculate [problem]
  (let [op (last problem)
        ns (drop-last problem)]
    (apply op ns)))

(defn -main 
  ([filename]
   (-main filename parse-star2))
  ([filename parser]
   (with-open [rdr (io/reader filename)]
     (->> rdr
          line-seq
          util/start-timer!
          parser
          (map calculate)
          (reduce +)
          (util/spy-timer! "output:")))))
