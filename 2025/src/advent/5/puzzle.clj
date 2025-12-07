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
       (sort (fn [[s1 t1] [s2 t2]] (> (- t1 s1) (- t2 s2))))
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
        ;; ignore range
        (and (<= r-start start r-stop)
             (<= r-start stop r-stop)) 
        (do #_(println "ignore range") (concat skipped ranges))

        ;; extend the range's stop
        (and (<= r-start start r-stop)
             (not (<= r-start stop r-stop))) 
        (do #_(println "extend range stop") (concat (conj skipped [r-start stop]) ranges))

        ;; extend the range's start
        (and (<= r-start stop r-stop)
             (not (<= r-start start r-stop))) 
        (do #_(println "extend range start") (concat (conj skipped [start r-stop]) ranges))

        (empty? ranges) 
        (do #_(println "make new range") #_(println range) (concat (conj skipped [start stop]) [range]))

        :else (recur (first ranges) (rest ranges) (conj skipped range))

        )
      )

    ))

(defn find-fresh 
  [[ranges ingredients]]
  #_(println "find fresh" ranges)
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

    )
  )


;; NOTE: this is still way too slow / memory intensive
#_(let [n 10000000000000
      chunk-size 1000000]
  (->> (range 0 n chunk-size)
       (pmap (fn [start]
               (count (filter (every-pred odd? even?) 
                             (range start (min n (+ start chunk-size)))))))
       (reduce +)))

(defn check-available [available? [ranges ingredients]]
  (println "# of ranges to check" (count ranges))
  #_(println (take 5 ranges))
  (let [ranges (sort-by first ranges)
        fresh (map (fn [[start stop]] 
                     (println start stop) 
                     (- (inc stop) start)
                     #_(range start (inc stop))) 
                   ranges)]
    #_(println available? fresh ingredients)
    (if (not available?)
      (reduce + fresh)
      #_(into #{} (apply concat fresh))
      (set/intersection (into #{} fresh) (into #{} ingredients))
      )

  ))

(defn -main 
  ([filename]
   (-main filename false)
   )
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
  ;; too low
192898532743555)
