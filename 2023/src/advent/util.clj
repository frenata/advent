(ns advent.util)

(defn spy> [val msg] (prn msg val) val)
(defn spy>> [msg val] (spy> val msg))
