(ns advent.seating
  (:require [clojure.string :as str]))

(def input "11/input.dat")

(defn- text->hash-map 
  "Reads a collection of lines into a hash-map from x,y coords to the value in the block."
  ([lines] (text->hash-map identity lines))
  ([f lines]
   (->> lines 
        (map-indexed (fn [y row]
                       (->> row
                            (map-indexed (fn [x cell] [[x y] (f cell)]))
                            (apply concat))))
        (apply concat)
        (apply hash-map)
        (assoc {:rows (count lines) :cols (count (first lines))} :map))))

(defn count-adjacent [{seats :map} [x y :as pos]]
  (let [adj [(seats [(dec x) (dec y)])
             (seats [(dec x) y])
             (seats [(dec x) (inc y)])
             (seats [x (dec y)])
             ;; (seats x y)
             (seats [x (inc y)])
             (seats [(inc x) (dec y)])
             (seats [(inc x) y])
             (seats [(inc x) (inc y)])]]
    (apply + (filter some? adj))))

(defn first-seen [{seats :map} [x y :as pos] [x-func y-func]]
  (let [cmb-fn (cond
                 (= > x-func y-func) (fn [[x' y'] [x'' y'']] (= (- x'' x') (- y'' y')))
                 (= < x-func y-func) (fn [[x' y'] [x'' y'']] (= (- x'' x') (- y'' y')))
                 (and (= > x-func) (= < y-func)) (fn [[x' y'] [x'' y'']] (= (- x'' x') (- y' y'')))
                 (and (= < x-func) (= > y-func)) (fn [[x' y'] [x'' y'']] (= (- x' x'') (- y'' y')))
                 :else #(vector %1 %2))
        target (filter (fn [[[x' y'] v]] (and (x-func x' x) (y-func y' y) (cmb-fn [x y] [x' y']))) seats)
        ordered (into (sorted-map-by (fn [[x' y'] [x'' y'']] (and (x-func x'' x') (y-func y'' y')))) target)
        filtered (filter #(not= nil (second %)) ordered)
        ]
    (second (first filtered))
    )
  )

(defn count-sightline [{seats :map :as seat-map} [x y :as pos]]
  (let [
        adj [
             (first-seen seat-map pos [< <])
             (first-seen seat-map pos [< >])
             (first-seen seat-map pos [< =])
             (first-seen seat-map pos [= <])
             (first-seen seat-map pos [= >])
             (first-seen seat-map pos [> <])
             (first-seen seat-map pos [> >])
             (first-seen seat-map pos [> =])

             ]]

    ;; (println adj)
    (apply + (filter some? adj))))


(defn- print-seats [{seats :map :as ss}]
  (doseq [y (range 10)]
    (doseq [x (range 10)]
      (print (case (seats [x y]) 0 "L" 1 "#" ".")))
    (print \newline)))

(defn seating-round [{count-fn :count-fn threshold :threshold} seats]
  (assoc
   (dissoc seats :map)
   :map
   (reduce (fn [acc [pos type :as seat]]
             (let [adj (count-fn seats pos)]
               (conj acc
                     (case type
                       nil seat
                       1 [pos (if (>= adj threshold) 0 1)]
                       0 [pos (if (=  adj 0) 1 0)]
                       ))))
           {} (:map seats))))

(defn chaotic-iteration [opts seats]
  (loop [prev {}
         curr seats
         round 0]
    (when (:print opts)
      (println "Round " round)
      (print-seats curr))
    (if (= prev curr) [curr (dec round)]
        (recur curr (seating-round opts curr) (inc round)))))

(def star1
  (->> input
       slurp
       str/split-lines
       (text->hash-map #(case % \. nil \L 0 1))
       #_(#(do (println "FOO: " %) %))
       (chaotic-iteration {:print false :count-fn count-adjacent :threshold 4})
       first
       :map
       (filter #(= 1 (second %)))
       count))

(println star1)

(def star2
  (->> input
       slurp
       str/split-lines
       (text->hash-map #(case % \. nil \L 0 1))
       (chaotic-iteration {:print false :count-fn count-sightline :threshold 5})
       first
       :map
       (filter #(= 1 (second %)))
       count))


(println star1 star2)

#_(->> "11/test.dat"
     slurp
     str/split-lines
     (text->hash-map #(case % \. nil \L 0 1))
     (#(count-sightline % [3 0] #_[< >]))
     #_(#(first-seen % [3 4] [> =]))
     #_println
     )

#_(assert (= star1 2470))
#_(assert (= star2 26))
