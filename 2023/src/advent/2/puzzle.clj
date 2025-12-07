(ns advent.2.puzzle
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as str])
  (:require [advent.util :as util]))

(defn possible? [bag games]
  (let [red (:red bag)
        blue (:blue bag)
        green (:green bag)
        ]
    (do
      #_(println red)
      #_(println blue)
      #_(println green)
      games
      ))
  )

(defn parse-draw [draw]
  (let [cubes (str/split draw #",")]
    (->> cubes 
      (map #(-> %
          str/trim
          (str/split #" ")
          reverse
          #_(map (fn [[fst snd]] [(keyword fst) snd]))
          #_((fn [c] (into {} c)))
          ))
      #_(map #(into {} %))
      flatten
      (apply hash-map)
      )

  ))

(defn parse [line]
  (let [[game games] (str/split line #":")
        id (drop 5 game)
        draws (str/split games #";")
        ]
    (do
      (println id)
      (->> draws
           (map parse-draw)
           (util/spy>> "draw")
      ))))




(defn parse-games [filename]
  (with-open [rdr (io/reader filename)]
    (->> rdr
         line-seq
         (mapv parse)
         )))

(defn -main [filename]
  (->> filename
      parse-games
      (map (partial possible? {:red 12 :green 13 :blue 14}))
      println))
