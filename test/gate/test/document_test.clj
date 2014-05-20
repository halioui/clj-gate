(ns gate.test.document-test
  (:require [gate.document :refer :all]
            [midje.sweet :refer :all]
            [gate.controller :refer [gate-init]]
            [clojure.java.io :as io]))

(def txt (slurp "test/data/short.txt"))

(gate-init)

(facts "about document building"
  (fact "build doc from string"
        (text (build-doc txt)) => txt
        (text (build-doc (io/as-file "test/data/short.txt"))) => txt
        (text (build-doc (io/as-url "file:./test/data/short.txt"))) => txt))
