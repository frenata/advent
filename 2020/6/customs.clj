(ns advent.customs
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.set :as set]
            ))

(def input "6/input.dat")

(defn read-paras [input]
  (->> input
       slurp
       (#(str/split % #"\n\n"))))

(defn group-any-answers [input]
  (->> input
       read-paras
       (map #(remove (partial = \newline) %))
       (map distinct)))

(defn group-all-answers [input]
  (->> input
       read-paras
       (map #(str/split % #"\n"))
       (map #(map set %))
       (map #(apply set/intersection %))))

(def star1 (apply + (map count (group-any-answers input))))
(def star2 (apply + (map count (group-all-answers input))))

(println star1 star2)
