;; Set of utility functions to extract content from Gate documents
;;
(ns gate.document
  (:require [clojure.java.io :as io]
            [clojure.string :as s]
            [net.cgrand.enlive-html :as html])
  (:import 
     (gate Annotation AnnotationSet Document Utils)
     (gate.corpora DocumentImpl)
     (gate.util InvalidOffsetException)))

(defn text
  ([document annotation] (if annotation (Utils/stringFor document annotation) ""))
  ([document] (Utils/stringFor document (Utils/start document) (Utils/end document))))

(defn- clean[t] (-> (s/replace t #"\s+" " ") s/trim s/trim-newline))

(defn clean-text 
  ([document annotation] (clean  (text document annotation)))
  ([document] (clean (text document (Utils/start document) (Utils/end document)))))

(defn build-document
  "Create GATE document"
  [title url]
  (let [document (DocumentImpl.)]
    (doto document
      (.setMarkupAware true) ;otherwise content will not be parsed (pdf, html, ...)
      (.setName title)
      (.setSourceUrl (io/as-url url))
      (.init))
    document))

(defn doc-from-string [content]
  (let [document (DocumentImpl.)]
    (doto document
      (.setMarkupAware false) ;otherwise content will not be parsed (pdf, html, ...)
      (.setStringContent content)
      (.init))
    document))


(defn document-from-file
  "Convert file contents to Gate document, file name becomes name of the document"
  [f]
  (build-document (.getName f) f))
