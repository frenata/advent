(ns advent.passwords
  (:require [clojure.string :as str]
            [clojure.pprint :as pp]
            [clojure.java.io :as io]))

(defn read-lines [f]
  (with-open [r (io/reader f)]
    (into [] (line-seq r))))

(defn parser [line]
  (rest (re-find #"(\d+)\-(\d+)\W(\w)\:\W(\w+)" line)))

(def passwords
  (->> "2/input.dat"
       read-lines
       #_(take 10)
       (map parser)))

(defn find-valids [passwords policy]
  (->> passwords
       (map (partial apply policy))
       (filter true?)
       count))

(defn valid-password-range? [low high letter password]
  (let [low (Integer/parseInt low)
        high (Integer/parseInt high)
        letter (first letter)
        freqs (frequencies password)]
    (<= low (or (freqs letter) 0) high)))

(defn- xor [a b]
  (or (and a (not b))
      (and (not a) b)))

(defn valid-password-position? [low high letter password]
  (let [low (dec (Integer/parseInt low))
        high (dec (Integer/parseInt high))
        letter (first letter)]

    (xor (= letter (nth password low))
         (= letter (nth password high)))))

(def star1 (find-valids passwords valid-password-range?))
(def star2 (find-valids passwords valid-password-position?))

(println star1 star2)
