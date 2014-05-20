(ns gate.test.annotation
  (:import [gate Factory Utils])
  (:use [gate.document] :reload-all)
  (:use [gate.annotation] :reload-all)
  (:use [clojure.test]))
(comment
(deftest test-annotations-at-offset
  (let [d (build-document "title" "content AAA")
        doc-annotations (annotations d)]
    (.add doc-annotations 1 (long 0) (long 10) "n-gram" (Factory/newFeatureMap))
    (.add doc-annotations 2 (long 0) (long 6) "test" (Factory/newFeatureMap))
    (.add doc-annotations 3 (long 8) (long 10) "test" (Factory/newFeatureMap))
    (let [ngram-a1 (.get (annotations d 0) 1)
          ngram-a2 (.getCovering (annotations d) "" (long 8) (long 8))]
      (is (= "n-gram" (.getType ngram-a1)))
      (is (= #{"n-gram", "test"} (.getAllTypes ngram-a2))))))
  )
