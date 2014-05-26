(ns gate.document-test
  (:require [gate.document :refer :all]
            [midje.sweet :refer :all]
            [gate.controller :refer [gate-init]]
            [clojure.java.io :refer :all]))

(def txt (slurp "test/data/short.txt"))

(gate-init)

(facts "about document building"
  (fact "build doc from string" (text (build-doc txt)) => txt)
  (fact "build doc from file"   (text (build-doc (as-file "test/data/short.txt"))) => txt)
  (fact "build doc from url"    (text (build-doc (as-url "file:./test/data/short.txt"))) => txt))


(facts "about parsing non-text docs"
  (fact "get original markups from html"
        (-> "test/data/short.html"
          as-file
          build-doc
          (.getAnnotations "Original markups")
          count)
        => 6))
