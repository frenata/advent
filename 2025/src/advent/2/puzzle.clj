(ns advent.2.puzzle
  (:require 
    [clojure.java.io :as io]
    [clojure.string :as str]
    [advent.util :as util]))

(defn parse [rnge]
  (let [[start stop] (str/split rnge #"-")]
    (range (Long/parseLong (str/trim start)) (inc (Long/parseLong (str/trim stop))))))

(defn twice? [id]
  (let [id (str id)
        half (/ (count id) 2)]
    (= (take half id) (drop half id))))

(defn at-least-twice? [id]
  false)

(defn -main 
  ([filename]
   (-main filename twice?))
  ([filename invalid?]
   (with-open [rdr (io/reader filename)]
     (->> rdr
          slurp
          (#(str/split % #","))
          (mapv parse)
          (apply concat)
          (filter invalid?)
          (reduce +)
          (util/spy>> "output:")))))
