(ns advent.4.puzzle
  (:require 
    [clojure.java.io :as io]
    [clojure.string :as str]
    [advent.util :as util]))

(defn parse [line]
  line)


(defn -main 
  [filename]
  (with-open [rdr (io/reader filename)]
    (->> rdr
         line-seq
         ((constantly 0))
         (util/spy>> "output:"))))
