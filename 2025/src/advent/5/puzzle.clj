(ns advent.5.puzzle
  (:require 
    [clojure.java.io :as io]
    [clojure.string :as str]
    [advent.util :as util]))

#_(defn parse [lines]
  (->> lines
       (map-indexed parse-line)
       (apply merge)))

(defn -main 
  [filename ]
   (with-open [rdr (io/reader filename)]
     (->> rdr
          line-seq
          util/start-timer!
          ((constantly 0))
          (util/spy-timer! "output:"))))
