(ns gate.annotation-test
  (:import [gate Factory Utils])
  (:use [gate.document] :reload-all)
  (:use [gate.annotation] :reload-all)
  (:use [clojure.test]))

(gate.controller/gate-init)
(def document (gdoc/build-doc (slurp "test/data/annotated.xml")))

(facts "about retrieving annotations"
  (fact "get sorted annotations of a given type"
    (annotations document nil "Organization") => nil))
    
