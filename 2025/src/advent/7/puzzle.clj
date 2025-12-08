(ns advent.7.puzzle
  (:require 
    [clojure.java.io :as io]
    [clojure.string :as str]
    [advent.util :as util]))

(def beam (atom #{}))
(def timelines (atom []))
(def splits (atom 0))

(defn parse-line [row line]
  (->> line
       (map-indexed (fn [col pos] [[row col] pos]))
       (filter (fn [[pos val]] 
                 (if (= val \S) 
                   (do 
                     (reset! beam #{pos})
                     (reset! timelines [pos]))) 
                 (= val \^)))
       (into {})))

(defn parse [lines]
  (->> lines
       (map-indexed parse-line)
       (apply merge)))

(defn move! [manifold count? beam]
  (let [new-vals 
        (->> beam
             (mapcat 
               (fn [[x y]] 
                 (let [next [(inc x) y]]
                   (if (contains? manifold next)
                     (do (if count? (swap! splits inc))
                         [[(inc x) (inc y)] [(inc x) (dec y)]])
                     [next])))))]
    (if (set? beam)
      (into #{} new-vals)
      (into [] new-vals))))


(defn traverse! [manifold]
  (println "traverse!")
  (swap! beam      (partial move! manifold true))
  (swap! timelines (partial move! manifold false)))

(defn -main 
  [filename]
  (with-open [rdr (io/reader filename)]
    (let [manifold (->> rdr
                        line-seq
                        util/start-timer!
                        parse)]
      (doall
        (repeatedly 150 (partial traverse! manifold)))
      (util/spy-timer! "splits/timelines: " [(deref splits) (count (deref timelines))]))))
