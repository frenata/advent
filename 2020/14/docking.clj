(ns advent.docking
  (:require [clojure.string :as str]))

(def input "14/input.dat")

(defn para->block [[mask & mems :as block]]
  (let [mems 
  (->> mems
       (map #(rest (re-find #"(\d+)\] = (\d+)" %)))
       (map #(vector (Integer/parseInt (first %)) (Integer/parseInt (second %)))))]
    {:mask mask :mems mems}))

(defn blocks [input]
        (->> input
             slurp
             (#(str/split % #"mask = "))
             (filter #(> (count %) 0))
             (map str/split-lines)
             (map para->block)))

(defn process-block [prog]
  (let [bits (->> (:mask prog)
                  str/reverse
                  (map-indexed (fn [i x] (case x \0 #(bit-clear % i) \1 #(bit-set % i) nil)))
                  (filter some?))]
    (->> (:mems prog)
         (mapcat (fn [[addr val]] [addr (reduce (fn [acc f] (f acc)) val bits)]))
         (apply hash-map))))

(defn process-block-addrs [prog]
  (let [bits (->> (:mask prog)
                  str/reverse
                  (map-indexed (fn [i x] (case x \X #(vector (bit-set % i) (bit-clear % i)) \1 #(vector (bit-set % i)) nil)))
                  (filter some?))]
    (->> (:mems prog)
         (map (fn [[addr val]] [(reduce (fn [acc f] (mapcat f acc)) [addr] bits) val]))
         (mapcat (fn [[addrs val]]
                (mapcat (fn [addr] [addr val]) addrs)))
         (apply hash-map))))

(def star1 (->> input blocks (map process-block) (apply merge) vals (reduce +)))
(def star2 (->> input blocks (map process-block-addrs) (apply merge) vals (reduce +)))

(println star1 star2)
