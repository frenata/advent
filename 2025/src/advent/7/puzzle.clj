(ns advent.7.puzzle
  (:require 
    [clojure.java.io :as io]
    [clojure.string :as str]
    [advent.util :as util]))

(def beam (atom {}))
(def splits (atom 0))

(defn parse-line [row line]
  (->> line
       (map-indexed (fn [col pos] [[row col] pos]))
       (filter (fn [[pos val]] 
                 (if (= val \S) 
                   (reset! beam {pos 1})) 
                 (= val \^)))
       (into {})))

(defn parse [lines]
  (->> lines
       (map-indexed parse-line)
       (apply merge)))

(defn move! [manifold beam]
  (->> beam
       (mapcat 
         (fn [[[x y] n]] 
           (let [next [[(inc x) y] n] ]
             (if (contains? manifold (first next))
               (do (swap! splits inc)
                   [[[(inc x) (inc y)] n]  [[(inc x) (dec y)] n] ])
               [next]))))
       (reduce (fn [acc [k v]] 
                 (if (contains? acc k)
                   (update acc k (partial + v))
                   (assoc acc k v)))
               {})))


(defn traverse! [manifold]
  (swap! beam (partial move! manifold)))

(defn -main 
  [filename]
  (with-open [rdr (io/reader filename)]
    (let [manifold 
          (->> rdr
               line-seq
               util/start-timer!
               parse)]
      (doall
        (repeatedly 150 (partial traverse! manifold)))
      (util/spy-timer! "splits/timelines: " [(deref splits) (reduce + (vals (into {} (deref beam))))]))))
