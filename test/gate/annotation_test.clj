(ns gate.annotation-test
  (:require [midje.sweet :refer :all]
            [gate.controller :refer [gate-init]]
            [gate.document :refer :all]
            [gate.annotation :refer :all]))

(gate-init)

(def document (build-doc (slurp "test/data/annotated.xml")))

(def dtxt (partial text document))

(defn anns [aname] (-> document (annotations nil aname)))

(facts "about retrieving annotations"
  (fact "get sorted annotations of a given type"
    (map dtxt (sort-anns (anns "Organization")))
      => ["EU" "Front" "UK Independence Party" "EU" "EU" "European Parliament"])
  (fact "contained annotations"
    (dtxt
      (contained-annotations (.getAnnotations document)
                             (first (sort-anns (anns "Sentence")))
                             "Organization"))
      => "EU"))

