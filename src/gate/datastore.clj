;; gate.DataStore
(ns gate.datastore
  (:use
     [gate.document]
     [clojure.contrib.io :only (file-str as-url)])
  (:import 
    (gate Factory)
    (gate.creole.annic Constants)
    (gate.creole.annic.lucene LuceneSearcher LuceneIndexer)
    (org.apache.commons.io FileUtils)
    (gate.persist LuceneDataStoreImpl)))

(defn create-lucene-ds
  "Create Lucene DataStore from the files in directory"
  [src-dir doc-extensions ds-dir]
  (let 
    [store (LuceneDataStoreImpl.)
     files (FileUtils/listFiles (file-str src-dir) (into-array doc-extensions) true)]
    (doto store
      (.setStorageDir (file-str ds-dir)) 
      (.create)
      (.open))
    (doall
      (map 
        #(.sync store (.adopt store (document-from-file %) nil))
        files))
    store))

(defn create-searchable-datastore
  "As described http://gate.ac.uk/releases/gate-6.0-build3764-ALL/doc/tao/splitch9.html#chap:annic"
  [ds-location index-location]
  (let [ds (Factory/createDataStore "gate.persist.LuceneDataStoreImpl" ds-location)
        indexer (LuceneIndexer. (as-url index-location))
        sets-to-include ["<null>"]
        params {Constants/INDEX_LOCATION_URL (as-url index-location)
                Constants/BASE_TOKEN_ANNOTATION_TYPE "Token"
                Constants/CREATE_TOKENS_AUTOMATICALLY true
                Constants/INDEX_UNIT_ANNOTATION_TYPE "Sentence"
                Constants/ANNOTATION_SETS_NAMES_TO_INCLUDE sets-to-include
                Constants/ANNOTATION_SETS_NAMES_TO_EXCLUDE ["Split" "SpaceToken"]
                Constants/FEATURES_TO_INCLUDE []
                Constants/FEATURES_TO_EXCLUDE []}]
    (doto ds
      (.setIndexer indexer params)
      (.setSearcher (LuceneSearcher.)))
    ds))

(defn load-lucene-ds [src-dir] 
  (let [store (LuceneDataStoreImpl.)]
    (.setStorageDir store (file-str src-dir))
    store))
