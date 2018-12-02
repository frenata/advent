(ns calibrate
  (:require [clojure.string :as str]
            [clojure.pprint :as pp]
            [clojure.java.io :as io]))

(defn read-lines [f]
  (with-open [r (io/reader f)]
    (into [] (line-seq r))))

(def freqs (->> "input.txt"
                read-lines
                (map #(Integer/parseInt %))))

(defn star1 []
  (println "Star 1: "
           (reduce + freqs)))

(star1)

(def changes (atom []))

(defn star2 []
  (let [store (atom {})
        add-and-store (fn [acc n]
                        (let [freq (+ acc n)]
                          (swap! changes conj acc)
                          (swap! store
                                 update-in [acc] (fnil inc 0))
                          (when (< 1 (get @store acc))
                            (throw (ex-info (str acc) {:store store
                                                       :repeat acc})))
                          freq))]
    (try (reduce add-and-store (cycle freqs))
         (catch Exception e
           (println "Star 2: " (:repeat (ex-data e)))))))

(star2)
