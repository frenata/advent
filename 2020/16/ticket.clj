(ns advent.ticket
  (:require [clojure.string :as str]))

(def input "16/input.dat")
(def rule-regex #"([\w| ]+): (\d+)-(\d+) or (\d+)-(\d+)")
(defn int! [s] (Integer/parseInt s))

(defn- parse-rules [rules]
  (letfn [(parse-rule [line]
            (let [[type min1 max1 min2 max2] (rest (re-find rule-regex line))]
              [type [[(int! min1) (int! max1)] [(int! min2) (int! max2)]]]))]
    (apply hash-map (mapcat parse-rule (str/split-lines rules)))))

(defn- parse-ticket [tickets]
  (map (fn [line] (map int! (str/split line #","))) (rest (str/split-lines tickets))))


(defn parse-input [input]
  (let [[rules ticket nearby](str/split (slurp input) #"\n\n")]
    {:rules (parse-rules rules)
     :ticket (first (parse-ticket ticket))
     :nearby (parse-ticket nearby)}))

(defn find-invalid [{rules :rules nearby :nearby}]
  (let [ticket-vals (apply concat nearby)
        ranges (apply concat (vals rules))
        find-in-ranges (fn [ranges tv]
                         (reduce (fn [_ [r-min r-max]] (when (<= r-min tv r-max) (reduced true))) false ranges))]
    (->> ticket-vals
         (map (partial find-in-ranges ranges))
         (map vector ticket-vals)
         (remove #(second %))
         )))

(def star1 (->> input parse-input find-invalid (map first) (apply +)))

(println star1)

(assert (= star1 23036))
