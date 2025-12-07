(ns advent.6.puzzle
  (:require 
    [clojure.java.io :as io]
    [clojure.string :as str]
    [advent.util :as util]))

(defn parse-token [token]
  (cond
    (= token "*") *
    (= token "+") +
    :else (Integer/parseInt token)))

(defn parse-line [row line]
  (->> line
       str/trim
       (#(str/split % #" +"))
       (map-indexed (fn [col pos] [col [(parse-token pos)]]))))

(defn parse [lines]
  (->> lines
       (map-indexed parse-line)
       (map (partial into {}))
       (apply merge-with into)))

(defn calculate [problem]
  (let [op (last problem)
        ns (drop-last problem)]
    #_(println op ns)
    (apply op ns)
  ))

(defn -main 
  [filename]
   (with-open [rdr (io/reader filename)]
     (->> rdr
          line-seq
          util/start-timer!
          parse
          #_(util/spy>> "parsed")
          vals
          (map calculate)
          (reduce +)
          (util/spy-timer! "output:"))))
