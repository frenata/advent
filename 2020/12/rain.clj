(ns advent.rain
  (:require [clojure.string :as str]))

(def input "12/input.dat")

(def directions ["N" "E" "S" "W"])

(defn parse-instructions [input]
  (->> input slurp str/split-lines
     (map (comp rest #(re-find #"(\w)(\d+)" %)))
     (map (fn [[order n]] [order (Integer/parseInt n)]))))

(defn rotate [[x y :as wp] dist]
    (reduce (fn [[x y] v] [y (- x)]) wp (range (mod dist 4))))

(defn move-dir [[x y :as pos] direction distance]
  (case direction
    "N" [x (+ y distance)]
    "E" [(+ x distance) y]
    "S" [x (- y distance)]
    "W" [(- x distance) y]))

(defn move-wp [pos [x' y' :as wp] times]
    (reduce (fn [[x y :as acc] v] [(+ x x') (+ y y')]) pos (range times)))

(defn wrong-navigate [direction actions]
  (loop [direction direction
         pos [0 0]
         [[order n] & actions] actions]
    (if (nil? order)
      [pos direction]
      (case order
        "R" (recur (nth directions (mod (+ (.indexOf directions direction) (/ n 90)) 4)) pos actions)
        "L" (recur (nth directions (mod (- (.indexOf directions direction) (/ n 90)) 4)) pos actions)
        "F" (recur direction (move-dir pos direction n) actions)
        (recur direction (move-dir pos order n) actions)))))

(defn waypoint-navigate [waypoint actions]
  (loop [[x y :as waypoint] waypoint
         pos [0 0]
         [[order n] & actions] actions]
    (if (nil? order)
      [pos waypoint]
      (case order
        "R" (recur (rotate waypoint (/ n 90)) pos actions)
        "L" (recur (rotate waypoint (/ n -90)) pos actions)
        "F" (recur waypoint (move-wp pos waypoint n) actions)
        "N" (recur [x (+ y n)] pos actions)
        "E" (recur [(+ x n) y] pos actions)
        "W" (recur [(- x n) y] pos actions)
        "S" (recur [x (- y n)] pos actions)))))

(defn manhattan [[x y]] (+ (Math/abs x) (Math/abs y)))

(def star1 (->> input parse-instructions (wrong-navigate "E") first manhattan)) 
(def star2 (->> input parse-instructions (waypoint-navigate [10 1]) first manhattan))

(println star1 star2)

(assert (= star1 364))
(assert (= star2 39518))
