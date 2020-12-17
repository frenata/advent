(ns advent.ticket
  (:require [clojure.string :as str]
            [clojure.set :as st]
            ))

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
  (let [[rules ticket nearby] (str/split (slurp input) #"\n\n")]
    {:rules (parse-rules rules)
     :ticket (first (parse-ticket ticket))
     :nearby (parse-ticket nearby)}))

(defn find-invalid [aggr-fn map-fn {rules :rules nearby :nearby}]
  (let [ticket-vals (apply concat nearby)
        ranges (apply concat (vals rules))
        find-in-ranges (fn [ranges tv]
                         (reduce (fn [_ [r-min r-max]] (when (<= r-min tv r-max) (reduced true))) false ranges))]
    (->> nearby 
         (map #(map (partial find-in-ranges ranges) %))
         (map #(map vector %1 %2) nearby)
         (aggr-fn map-fn))))


(defn solve-constraints [rules tickets]
  (let [groups (apply map vector tickets)
        constrain-group (fn [group]
                          (reduce (fn [acc [type [[r-min-1 r-max-1] [r-min-2 r-max-2]] :as rule]]
                                    (let [constrained (every? (fn [tv] (or (<= r-min-1 tv r-max-1) (<= r-min-2 tv r-max-2))) group)]
                                      (if constrained (conj acc type) acc)))
                                  [] rules))
        sorted-constrained (sort-by (comp count second) (map-indexed (fn [i g] [i (constrain-group g)]) groups))]
    (loop [[[n options :as c] & constrained] sorted-constrained
           allocated {}]
      (if (nil? c)
        allocated
        (recur constrained (assoc allocated (first (st/difference (into #{} options) (into #{} (keys allocated)))) n))))))

(def parsed-input (parse-input input))

(def star1 (->> parsed-input
                (find-invalid map (partial filter (fn [x] (nil? (second x)))))
                (apply concat)
                (map first)
                (apply +)))

(def star2 (->> parsed-input
                (find-invalid filter (fn [tvs] (every? (comp some? second) tvs)))
                (map #(map first %))
                (#(conj % (:ticket parsed-input)))
                (solve-constraints (:rules parsed-input))
                (filter #(str/starts-with? (key %) "departure"))
                (map second)
                (map #(nth (:ticket parsed-input) %) )
                (apply *)))

(println star1 star2)

(assert (= star1 23036))
(assert (= star2 1909224687553))
