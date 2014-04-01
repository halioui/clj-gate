;;GATE annotatated document output to Lucene index, CRFSuite, etc
(ns gate.output
  (:use [gate.annotation]))


(defn crfsuite-format
  [coll]
  (if (not= " " (first coll)) ;CRF prints no spaces
    (do
      (doall 
        (map #(print (format "%s\t" %)) coll))
      (println)
      (if (some #{"Split"} coll) (println)))))

(defn output-seq
  [document ftr]
  (map
    (fn [offset]
      (let 
        [annotations-at-offset (annotations document offset)]
        (map #(% annotations-at-offset) ftr)))
    (sorted-offsets document)))
