(ns advent.2.puzzle
  (:require 
    [clojure.java.io :as io]
    [clojure.string :as str]
    [advent.util :as util]))

(defn parse [rnge]
  (let [[start stop] (str/split rnge #"-")]
    [(Integer/parseInt (str/trim start)) (Integer/parseInt (str/trim stop))]
    )

  )

(defn -main 
  [filename]
   (with-open [rdr (io/reader filename)]
     (->> rdr
          slurp
          (#(str/split % #","))
          (mapv parse)
          (util/spy>> "output:")
          )))
