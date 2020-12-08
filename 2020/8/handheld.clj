(ns advent.handheld
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def input "8/input.dat")
(def regex-opcode #"(\w{3}) ([-|\+]\d+)")

(defn ->program
  "Parse a file into a program structure."
  [input]
  (->> input
       slurp
       str/split-lines
       (map-indexed
        (fn [idx line]
          (let [[_ opcode num] (re-find regex-opcode line)]
            [idx [opcode (Integer/parseInt num)]])))
       (apply concat)
       (apply hash-map)))


(defn execute-line
  "Execute a single line's change on the program state."
  [[opcode num] [pc acc]]
  (case opcode
    "nop" [(inc pc)   acc]
    "jmp" [(+ pc num) acc]
    "acc" [(inc pc)   (+ acc num)]))

(defn execute-program
  "Returns the program counter and accumulator when the program halts or repeats."
  [program]
  (loop [[pc acc] [0 0] 
         visited #{}]
    (cond
      (or (contains? visited pc) (nil? (program pc))) [pc acc]
      :else (recur (execute-line (program pc) [pc acc]) (conj visited pc)))))

(defn permutate-program
  "Given a program produce all possible opcode permutations."
  [program]
  (letfn [(inject-change [line-number]
            (let [[opcode num] (program line-number)]
              (case opcode
                "nop" (assoc program line-number ["jmp" num])
                "jmp" (assoc program line-number ["nop" num])
                program)))]
    (map inject-change (range (count program)))))

(def star1 (second (execute-program (->program input))))
(def star2 (second (apply max-key first (map execute-program (permutate-program (->program input))))))

(println star1 star2)

(assert (= star1 1528))
(assert (= star2 640))
