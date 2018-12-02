(ns checksum
  (:require [clojure.java.io :as io]
            [clojure.pprint :as pp]))

(defn read-lines [f]
  (with-open [r (io/reader f)]
    (into [] (line-seq r))))

(def ids (read-lines "input.txt"))

(defn checksum [ids]
  (let [freqs (comp set vals frequencies)
        id-freqs (map freqs ids)]
    (*
     (count (filter #(contains? % 2) id-freqs))
     (count (filter #(contains? % 3) id-freqs)))))

(def star1 (println "Star 1: " (checksum ids)))

(defn distance [x y]
  (when (apply = (map count [x y]))
    (apply + (map #(if (= % %2) 0 1) x y))))

(defn find-ids [ids]
  (->> (for [xid ids]
            (->> ids
                 (map (fn [yid] {:d (distance xid yid)
                                :x xid
                                :y yid}))
                 (filter #(= 1 (:d %)))))
          (filter seq)
          ffirst))

(defn common [ids]
  (transduce (comp (filter (partial apply =))
                (map first))
             str
             (map vector (:x ids) (:y ids))))

(def star2 (println "Star 2: " (-> ids find-ids common)))
