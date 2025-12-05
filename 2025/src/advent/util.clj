(ns advent.util
 (:require [stopwatch.core :as stopwatch]) )


(defn spy> [val msg] (prn msg val) val)
(defn spy>> [msg val] (spy> val msg))

(def timer (atom nil))

(defn start-timer! [val]
  (reset! timer (stopwatch/start))
  val) 

(defn spy-timer! [msg val]
  (println "elapsed:" (/ ((deref timer)) 1000000.0) "ms" )
  (spy> val msg)
  val)
