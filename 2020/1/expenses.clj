(ns advent.expenses
  (:require [clojure.string :as str]
            [clojure.pprint :as pp]
            [clojure.java.io :as io]))

(defn read-lines [f]
  (with-open [r (io/reader f)]
    (into [] (line-seq r))))


(def nums (->> "1/input.dat"
                read-lines
                (map #(Integer/parseInt %))))

(defn cartesian-product [colls]
  (if (empty? colls)
    '(())
    (for [more (cartesian-product (rest colls))
          tupl (first colls)]
      (cons tupl more))))

(defn sol [nums n]
  (->> (repeat n nums)
       (cartesian-product)
       (map (juxt #(apply + %) identity))
       (filter #(= 2020 (first %)))
       first
       second
       (apply *)))

(def star1 (sol nums 2))
(def star2 (sol nums 3))

(print "\n" star1 star2)
