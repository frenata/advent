(ns advent.1.puzzle
  (:require [clojure.java.io :as io]))

(defn parse [line]
  (->> line
       seq
       (filter Character/isDigit)
       ((juxt first last))
       (clojure.string/join)
       (Integer/parseInt)))

(defn sum-valid [filename]
  (with-open [rdr (io/reader filename)]
    (->> rdr
         line-seq
         (mapv parse)
         (reduce +))))

(defn -main [filename]
  (-> filename
      sum-valid
      println))
