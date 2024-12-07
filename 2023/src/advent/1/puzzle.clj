(ns advent.1.puzzle
  (:require [clojure.java.io :as io])
  (:require [advent.util :as util]))

(defn str->num [s]
  (case s
    "one" "1"
    "two" "2"
    "three" "3"
    "four" "4"
    "five" "5"
    "six" "6"
    "seven" "7"
    "eight" "8"
    "nine" "9"
    s))

(defn parse [line]
  (->> line
       (re-seq #"(?=(\d|one|two|three|four|five|six|seven|eight|nine))")
       (map second)
       (map str->num)
       ((juxt first last))
       (clojure.string/join)
       (Integer/parseInt)))

(defn sum-valid [filename]
  (with-open [rdr (io/reader filename)]
    (->> rdr
         line-seq
         (mapv parse)
         #_(util/spy>> "before reduce")
         (reduce +))))

(defn -main [filename]
  (-> filename
      sum-valid
      println))
