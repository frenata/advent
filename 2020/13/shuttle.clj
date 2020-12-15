(ns advent.shuttle
  (:require [clojure.string :as str]))

(def input "13/input.dat")

(defn parse-notes [input]
  (->> input slurp str/split-lines ((fn [[ts buses]] [(Integer/parseInt ts) (map #(Integer/parseInt %) (re-seq #"\d+" buses))]))))

(defn until-greater [b a]
  "Adds a to itself until it is greater than b."
  (loop [curr a]
    (if (>= curr b) curr (recur (+ a curr)))))

(defn next-bus [ts buses]
  (apply min-key first (map (juxt (partial until-greater ts) identity) buses)))

(def star1 (let [[start buses :as notes] (parse-notes input)
                 [ts bus] (next-bus start buses)]
             (* bus (- ts start))))

(println star1)

(defn apply-constraints [ts constraints]
  (reduce
   (fn [acc [diff n]] (and acc (= (+ ts diff) n)))  true constraints))

(defn solve-equation [[[start-diff start] & constraints]]
  (println start-diff start constraints)

  (reduce (fn [_ proposed]
            (if
                (= 0 (+ (rem (+ proposed 60) 383) (rem (+ proposed 70) 41) (rem (+ proposed 23) 37) (rem proposed 29) (rem (+ proposed 52) 23) (rem (+ proposed 48) 19) (rem (+ proposed 77) 17) (rem (+ proposed 47) 13)))
                #_(= 0 (+ (rem proposed 1789) (rem (+ proposed 1) 37) (rem (+ proposed 2) 47)))
                #_(= 0 (+ (rem proposed 17) (rem (+ proposed 2) 13)))
              (reduced proposed)
              )
            ;; (println proposed)
            #_(let [constrained (map (fn [[r n]] (= 0 (mod (+ r (rem proposed n)) n))) constraints)]
                ;; (println constrained constraints)
                (if (reduce #(and %1 %2) constrained)
                  (reduced proposed)
                  )
                )
            )
          0
          (range 379862000000 #_(- start start-diff) #_1068722 #_1068782 1000000000000000 start))
                                                                      ;; 100000000000000
                                                                                  ;; 379862000000

  #_(loop [ts start]
      (if (apply-constraints ts constraints) ts
          (recur (+ ts start)))))

(defn parse-contest [input]
  (->> input slurp str/split-lines second
       (#(str/split % #","))
       (map-indexed (fn [i x] (when (not= "x" x) [i (Integer/parseInt x)])))
       (filter some?)
       #_(#(do (println %)%))))

(def star2 (->> input parse-contest (sort-by second >)  solve-equation))

(println star2)
