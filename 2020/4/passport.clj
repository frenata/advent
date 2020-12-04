(ns advent.passport
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(defn int! [x] (try (Integer/parseInt x) (catch Exception e -1)))

(def required-fields
  {"byr" (fn [x] (<= 1920 (int! x) 2002))
   "iyr" (fn [x] (<= 2010 (int! x) 2020))
   "eyr" (fn [x] (<= 2020 (int! x) 2030))
   "hgt" (fn [x] (let [[n measure] (rest (re-find #"(\d+)(in|cm)" x))]
                   (case measure
                     "cm" (<= 150 (int! n) 193)
                     "in" (<= 59 (int! n) 76)
                     false)))
   "hcl" (fn [x] (re-find #"#[0-9a-f]{6}" x))
   "ecl" (fn [x] (#{"amb" "blu" "brn" "gry" "grn" "hzl" "oth"} x))
   "pid" (fn [x] (re-find #"^\d{9}$" x))})

(def input "4/input.dat")

(defn read-lines
  "Open a file and read all the lines"
  [f]
  (with-open [r (io/reader f)]
    (into [] (line-seq r))))

(defn make-paragraphs [lines]
  (loop [lines lines
         paragraphs []]
    (let [[head tail] (split-with #(not= "" %) lines)]
      (if (and (seq tail) )
        (recur (rest tail) (conj paragraphs (str/join " " head)))
        (conj paragraphs (str/join " " head))))))

(defn str->passport [inp]
  (apply hash-map (mapcat #(str/split % #":") (str/split inp #" "))))

(defn check-keys [opts passport]
  (reduce (fn [acc [k f]]
            (and acc
                 (passport k)
                 (or (:ignore-rules opts) (f (passport k)))))
          true
          required-fields))

(defn find-valid-passports [inp policy]
  (->> inp
       read-lines
       make-paragraphs
       (map str->passport)
       (filter (partial check-keys policy))))


(def star1 (count (find-valid-passports input {:ignore-rules true})))
(def star2 (count (find-valid-passports input {})))

(println star1 star2)

(assert (= star1 230))
(assert (= star2 156))
