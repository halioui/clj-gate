(ns gate.annotation-test
  (:require [midje.sweet :refer :all]
            [gate.document :refer :all]
            [gate.annotation :refer :all]))

(gate.controller/gate-init)
(def document (build-doc (slurp "test/data/annotated.xml")))

(def dtxt (partial text document))

(facts "about retrieving annotations"
  (fact "get sorted annotations of a given type"
    (map dtxt (-> document (annotations nil "Organization") sort-anns))
      => ["EU" "Front" "UK Independence Party" "EU" "EU" "European Parliament"]))
    
