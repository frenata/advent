(ns advent.5.puzzle
  (:require 
    [clojure.java.io :as io]
    [clojure.string :as str]
    [clojure.set :as set]
    [advent.util :as util]))



(defn parse-ranges [rs] 
  (->> rs
       (#(str/split % #"\n"))
       (map #(str/split % #"-"))
       (map (partial map Long/parseLong))
       (sort (fn [[s1 t1] [s2 t2]] (> s2 s1)))))

(defn parse-available [as] 
  (->> as
       (#(str/split % #"\n"))
       (map Long/parseLong)))

(defn parse [input]
  (->> input
       (#(str/split % #"\n\n"))
       ((fn [[ranges available]] 
          [(parse-ranges ranges)
           (parse-available available)]))))

(defn attach-range [ranges start stop]
  (loop [range (first ranges)
         ranges (rest ranges)
         skipped []]
    (let [[r-start r-stop] range]
      (cond 
        ;; ignore new range
        (and (<= r-start start r-stop)
             (<= r-start stop r-stop)) 
        (concat (conj skipped range) ranges)

        ;; ignore existing range
        (and (<= start r-start)
             (>= stop r-stop)) 
        (concat (conj skipped [start stop]) ranges)

        ;; extend the range's stop
        (and (<= r-start start r-stop)
             (not (<= r-start stop r-stop))) 
        (concat (conj skipped [r-start stop]) ranges)

        ;; extend the range's start
        (and (<= r-start stop r-stop)
             (not (<= r-start start r-stop))) 
        (concat (conj skipped [start r-stop]) ranges)

        (empty? ranges) 
        (conj skipped range [start stop])

        :else (recur (first ranges) (rest ranges) (conj skipped range))))))

(defn find-fresh 
  [[ranges ingredients]]
  (let [fresh-ranges 
        (loop [acc [(first ranges)]
               ranges (rest ranges)]
          (let [[start stop] (first ranges)] 
            (if (nil? start)
              [acc ingredients]
              (recur
                (attach-range acc start stop)
                (rest ranges)))))]

    [(->> (first fresh-ranges)
          (sort-by first)
          ((fn [ranges]
             (loop [
                    acc []
                    current (first ranges)
                    next  (first (rest ranges))
                    ranges (rest (rest ranges)) ]
               (if (nil? next)
                 (conj acc current)
                 (if (< (first next) (last current))
                   (recur acc [(first current) (last next)] (first ranges) (rest ranges))
                   (recur (conj acc current) next (first ranges) (rest ranges)))))))) 
     (second fresh-ranges)]))


;; NOTE: this is still way too slow / memory intensive
#_(let [n 10000000000000
        chunk-size 1000000]
    (->> (range 0 n chunk-size)
         (pmap (fn [start]
                 (count (filter (every-pred odd? even?) 
                                (range start (min n (+ start chunk-size)))))))
         (reduce +)))

(defn check-available [available? [ranges ingredients]]
  (if available?
    (let [fresh (mapcat (fn [[start stop]]
                          (range start (inc stop))) ranges)]

      (count (set/intersection (into #{} fresh) (into #{} ingredients))))
    (let [ranges (sort-by first ranges)
          fresh (map (fn [[start stop]] 
                       (- (inc stop) start))
                     ranges)]

      (reduce + fresh))))

(defn -main 
  ([filename]
   (-main filename false))
  ([filename available?]
   (with-open [rdr (io/reader filename)]
     (->> rdr
          slurp
          util/start-timer!
          parse
          find-fresh
          ((partial check-available available?))
          (util/spy-timer! "output:")))))

#_(
   ;; first attempt -- too low
   192898532743555
   ;; second attempt (combines ranges) -- obviously still too low
   189536154197740
   ;; third attempt (fixing a bug with skipping combinging ranges?)
   ;; too high
   344306344403174
   ;; subtle bug? sorting by beginning of range instead of width of range solved it
   344306344403172
   )
