(ns gate.document
  (:require [clojure.java.io :as io]
            [clojure.string :as s]
            [net.cgrand.enlive-html :as html])
  (:import
     (gate Annotation AnnotationSet Document Utils Factory)
     (gate.corpora DocumentImpl)
     (gate.util InvalidOffsetException)))

(defn text
  ([document annotation] (if annotation (Utils/cleanStringFor document annotation) ""))
  ([document] (Utils/cleanStringFor document (Utils/start document) (Utils/end document))))

(defn- clean[t] (-> (s/replace t #"\s+" " ") s/trim s/trim-newline))

(defn clean-text
  ([document annotation] (clean  (text document annotation)))
  ([document] (clean (text document (Utils/start document) (Utils/end document)))))

(defmulti build-doc class)

(defmethod build-doc String [f] (Factory/newDocument f))

(defmethod build-doc java.io.File [f]
  (doto (Factory/newDocument (slurp f))
    (.setSourceUrl (-> f .toURI .toURL))))

(defmethod build-doc java.net.URL [f]
  (doto (Factory/newDocument (slurp f))
    (.setSourceUrl f)))
