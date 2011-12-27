(ns partner-test.main
  (:use [partner-test.core])
  (:require [clojure.main :as clj])
  (:gen-class))

(defn- usage []
  (println "Partner test.")
  (println "-------------")
  (println "To trigger a lot to a subcontractor execute (trigger {\"<<lotname>>\" \"<<subcontractor>>\"})")
  (println "For example: (trigger {\"A12345\" \"O_CARSEM1\"})"))

(defn -main [& args]
  (usage)
  (clj/repl :init (fn [] (use 'partner-test.core))))
