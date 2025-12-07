(ns advent.7.puzzle
  (:require 
    [clojure.java.io :as io]
    [clojure.string :as str]
    [advent.util :as util]))

(def beam (atom #{}))
(def splits (atom 0))

(defn parse-line [row line]
  (->> line
       (map-indexed (fn [col pos] [[row col] pos]))
       (filter (fn [[pos val]] 
                 (if (= val \S) (reset! beam #{pos})) 
                 (= val \^)))
       (into {})))

(defn parse [lines]
  (->> lines
       (map-indexed parse-line)
       (apply merge)))

(defn traverse! [manifold]
  #_(println beam)
  (let [move! 
        #(->> % 
              (mapcat 
                (fn [[x y]] 
                  (let [next [(inc x) y]]
                    #_(println manifold next)
                    (if (contains? manifold next)
                      (do (swap! splits inc)
                          [[(inc x) (inc y)] [(inc x) (dec y)]])
                      [next]))))
              (into #{})
              )]
    (swap! beam move!)
    manifold))

(defn -main 
  [filename]
  (with-open [rdr (io/reader filename)]
    (let [manifold (->> rdr
                        line-seq
                        util/start-timer!
                        parse)]
      (doall
        (repeatedly 150 (partial traverse! manifold)))
      (util/spy-timer! "output" (deref splits)))))
