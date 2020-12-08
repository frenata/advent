(ns advent.haversacks
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.set :as set]
            ))

(def input "7/input.dat")

(defn read-lines
  "Open a file and read all the lines"
  [f]
  (with-open [r (io/reader f)]
    (into [] (line-seq r))))

(def bag-regex #"(\d+)? ?(?!no|o)(\w+ \w+)(?= bags?)")

(defn ->bag-graph [input]
  (->> input
       read-lines
       (map (partial re-seq bag-regex))
       (map (fn [xs] (map rest xs)))
       (mapcat (juxt (comp (partial vector "1") second first) rest))
       (apply hash-map)))

(defn- add-to-graph-inv [graph [held holding]]
  (reduce (fn [acc h]
            (if (acc h)
              (assoc acc h (conj (acc h) holding))
              (assoc acc h [holding])))
          graph held))

(defn ->bag-inverse-graph [input]
  (->> input
       read-lines
       (map (partial re-seq bag-regex))
       (map (fn [xs] (map #(nth % 2) xs)))
       (map (juxt rest first))
       (reduce add-to-graph-inv {})))

(defn find-descendants-inv-graph [graph key]
  (loop [desc #{}
         keys [key]]
    (if (empty? keys)
      desc
      (recur (set/union desc (set (graph (first keys))))
             (concat (rest keys) (graph (first keys)))))))

(defn find-descendants-graph [graph key]
  (loop [desc []
         [[n key] & keys] [key]]
      (if (nil? key)
        desc
        (recur (concat desc (map (fn [[m k]] [(* (Integer/parseInt n) (Integer/parseInt m)) k]) (graph ["1" key])))
               (concat keys (map (fn [[m k]] [(str (* (Integer/parseInt n) (Integer/parseInt m))) k]) (graph ["1" key])))
               ))))

(def star1 (count (find-descendants-inv-graph (->bag-inverse-graph input) "shiny gold")))
(def star2 (apply + (map first (find-descendants-graph (->bag-graph input) ["1" "shiny gold"]))))

(println star1 star2)

(assert (= 259 star1))
(assert (= 45018 star2))
