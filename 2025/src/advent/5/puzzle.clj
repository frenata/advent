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
       (sort (fn [[s1 t1] [s2 t2]] (> s2 s1)))
       #_(map (fn [[start stop]] 
              (fn [ingredient] 
                (let [fresh (and (>= ingredient start) (<= ingredient stop))]
                #_(println start stop ingredient fresh)
                fresh))))))

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
  #_(println "attach range" [start stop] "to" ranges)
  (loop [range (first ranges)
         ranges (rest ranges)
         skipped []]
    (let [[r-start r-stop] range]
      #_(println "comp" start stop r-start r-stop)
      (cond 
        ;; ignore new range
        (and (<= r-start start r-stop)
             (<= r-start stop r-stop)) 
        (do (println "ignore new range" r-start r-stop start stop) (concat (conj skipped range) ranges))

        ;; ignore existing range
        (and (<= start r-start)
             (>= stop r-stop)) 
        (do (println "ignore existing range" r-start r-stop start stop) (concat (conj skipped [start stop]) ranges))

        ;; extend the range's stop
        (and (<= r-start start r-stop)
             (not (<= r-start stop r-stop))) 
        (do (println "extend range stop" r-start r-stop start stop) (concat (conj skipped [r-start stop]) ranges))

        ;; extend the range's start
        (and (<= r-start stop r-stop)
             (not (<= r-start start r-stop))) 
        (do (println "extend range start" r-start r-stop start stop) (concat (conj skipped [start r-stop]) ranges))

        (empty? ranges) 
        (do (println "make new range" r-start r-stop start stop) #_(println range) (conj skipped range [start stop]) )

        :else (do (println "recur") (recur (first ranges) (rest ranges) (conj skipped range)))

        )
      )

    ))

(defn find-fresh 
  [[ranges ingredients]]
  #_(println "find fresh" ranges)
  (let [fresh-ranges 
  (loop [acc [(first ranges)]
         ; [start stop] (first (rest ranges))
         ranges (rest ranges)
         ]
    (let [[start stop] (first ranges)] 
      #_(println "attach" start stop acc ranges)
      (if (nil? start)
        (do #_(println acc ranges) [acc ingredients])
        (recur
          (attach-range acc start stop)
          (rest ranges)
          )
        ))
    ;; NOTE: needs a second step that combines ranges further

    )]
    #_(println fresh-ranges)

    [(->> (first fresh-ranges)
         (sort-by first)
         ((fn [ranges]
            (loop [
                   acc []
                   current (first ranges)
                   next  (first (rest ranges))
                   ranges (rest (rest ranges))
                   ]
              #_(println "acc" acc)
              (if (nil? next)
                (conj acc current)
                (if (< (first next) (last current))
                  (recur acc [(first current) (last next)] (first ranges) (rest ranges))
                  (recur (conj acc current) next (first ranges) (rest ranges))

                  ))
              ))
          )
         ) (second fresh-ranges)]
    ))


;; NOTE: this is still way too slow / memory intensive
#_(let [n 10000000000000
      chunk-size 1000000]
  (->> (range 0 n chunk-size)
       (pmap (fn [start]
               (count (filter (every-pred odd? even?) 
                             (range start (min n (+ start chunk-size)))))))
       (reduce +)))

(defn check-available [available? [ranges ingredients]]
  (println "# of ranges to check" (count ranges) available?)
  #_(println (take 5 ranges))
  (if available?
    (let [fresh (mapcat (fn [[start stop]]
                       (range start (inc stop))) ranges)]

        (count (set/intersection (into #{} fresh) (into #{} ingredients)))
    )
  (let [ranges (sort-by first ranges)
        fresh (map (fn [[start stop]] 
                     (println start stop (- (inc stop) start)) 
                     (- (inc stop) start)
                     #_(range start (inc stop))) 
                   ranges)]

        (reduce + fresh)

  )))

(defn -main 
  ([filename]
   (-main filename false))
  ([filename available?]
   (with-open [rdr (io/reader filename)]
     (->> rdr
          slurp
          util/start-timer!
          parse
          #_(util/spy>> "parsed")
          find-fresh
          #_(util/spy>> "fresh")
          ((partial check-available available?))
          #_count
          (util/spy-timer! "output:")))))

#_(
;; first attempt -- too low
192898532743555
;; second attempt (combines ranges) -- obviously still too low
189536154197740
;; third attempt (fixing a bug with skipping combinging ranges?)
;; too high :(
344306344403174
;; subtle bug? sorting by beginning of range instead of width of range solved it
344306344403172
)
