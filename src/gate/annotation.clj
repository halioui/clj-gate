(ns gate.annotation
  (:require [gate.document :as gdoc])
  (:import (gate Annotation AnnotationSet Document Utils)))


(defn to-map
  "Convert Annotation to CLJ map"
  [doc ann]
  {:text (gdoc/text doc ann)
   :type (.getType ann)
   :features (into {} (.getFeatures ann))})

(defn sort-anns [anns]
  (into [] (sort-by #(.. % getStartNode getOffset) anns)))

(defn annotation-types
  "GATE Javadoc. Get a set of java.lang.String objects representing all the annotation types present in this annotation set."
  ([document] (.. document getAnnotations getAllTypes))
  ([document as-name] (-> document (.getAnnotations as-name) .getAllTypes)))


(defn annotations
  ([document] (.getAnnotations document))
  ([document a-type] (.getAnnotations document a-type))
  ([document a-type a-name] (.get (.getAnnotations document a-type) a-name)))

(defn contained-annotations
  "Wrapper around
  https://gate.ac.uk/releases/latest/doc/javadoc/gate/Utils.html#getContainedAnnotations (gate.AnnotationSet, gate.AnnotationSet, java.lang.String))"
  [source-annotation-set containing-annotation-set ann-type]
  (gate.Utils/getContainedAnnotations
    source-annotation-set
    containing-annotation-set
    ann-type))

(defn sorted-offsets [document]
  "Return set of all offsets in the document. Good for sequential traversal of the document"
  (apply sorted-set
         (mapcat
           #(vec [(.. % getStartNode getOffset) (.. % getEndNode getOffset)])
           (annotations document))))

(defn has-type [a-set t-name] (some #{t-name} (.getAllTypes a-set)))

(defn feature [annot f-name]
  "Get named feature form the annotation's feature map"
  (.get (.getFeatures annot) f-name))

(defn annotation-set-features
  "Return named features of annotations in annotation set"
  [a-set f-name]
  (filter #(not (nil? %)) (map #(feature % f-name) a-set)))
