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
       (map (fn [[start stop]] 
              (fn [ingredient] 
                (let [fresh (and (>= ingredient start) (<= ingredient stop))]
                fresh))))))

(defn parse-available [as] 
  (->> as
       (#(str/split % #"\n"))
       (map Long/parseLong)))

(defn parse [input]
  (->> input
       (#(str/split % #"\n\n"))
       ((fn [[ranges available]] 
          [(parse-ranges ranges)
           (parse-available available)]))))

(defn find-fresh 
  ([[ranges available]]
   (filter (apply some-fn ranges) available))
  ([ingredients [ranges _]]
   (filter (apply some-fn ranges) (range ingredients))
  ))


(defn -main 
  ([filename ingredients]

   (with-open [rdr (io/reader filename)]
     (->> rdr
          slurp
          util/start-timer!
          parse
          #_(util/spy>> "parsed")
          ((partial find-fresh ingredients))
          #_(util/spy>> "fresh")
          count

          (util/spy-timer! "output:"))))
  ([filename]
   (with-open [rdr (io/reader filename)]
     (->> rdr
          slurp
          util/start-timer!
          parse
          #_(util/spy>> "parsed")
          ((partial find-fresh 1000000000000000))
          #_(util/spy>> "fresh")
          count
          (util/spy-timer! "output:")))))
