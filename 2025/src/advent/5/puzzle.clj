(ns advent.5.puzzle
  (:require 
    [clojure.java.io :as io]
    [clojure.string :as str]
    [advent.util :as util]))



(defn parse-ranges [rs] 
  (->> rs
       (#(str/split % #"\n"))
       (map #(str/split % #"-"))
       (map (partial map Long/parseLong))
       (map (fn [[start stop]] (range start (inc stop))))))

(defn parse-available [as] 
  (->> as
       (#(str/split % #"\n"))
       (map Long/parseLong)))

(defn parse [input]
  (->> input
       (#(str/split % #"\n\n"))
       ((fn [[ranges available]] 
          [(parse-ranges ranges)
           (parse-available available)])
       )))

(defn find-fresh [[ranges available]]
  (let [fresh (->> ranges flatten (into #{}))]
    (filter #(contains? fresh %) available)
    )
  )


(defn -main 
  [filename ]
   (with-open [rdr (io/reader filename)]
     (->> rdr
          slurp
          util/start-timer!
          parse
          #_(util/spy>> "parsed")
          find-fresh
          #_(util/spy>> "fresh")

          count
          #_((constantly 0))
          (util/spy-timer! "output:"))))
