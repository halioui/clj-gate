(ns tryit
  (:use :reload
     [gate.document]
     [gate.controller]
     [gate.annotation]
     [gate.output]))

(comment
(gate-init)
(let [corpus (load-corpus "/tmp/small" ["htm"])
      first-doc (.get corpus 0)]
  (execute-pipline
    "/tmp/standalone.gapp"
    corpus)
  (doall (map 
           #(println %) 
           (output-seq
             first-doc 
             [(fn [an] (annotation-set-features an "type"))
              (fn [an] (annotation-set-features an "value"))]))))
)

(defn doc-tos []
  (let [d (document-from-file "file:/etc/profile" )]
    (prn (text d))))
