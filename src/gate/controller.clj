;; Clojure Interface to GATE (General Architecture for Text Engineering)
(ns gate.controller
  (:use 
     [gate.document]
     [clojure.java.io])
  (:import
     (org.apache.log4j BasicConfigurator)
     (gate Gate)
     (gate.corpora DocumentImpl)
     (gate.util.persistence PersistenceManager)
     (org.apache.commons.io FileUtils)))

;http://stackoverflow.com/questions/7777882/loading-configuration-file-in-clojure-as-data-structure
(defn- read-properties
  [file-name]
  (with-open [^java.io.Reader reader (clojure.java.io/reader file-name)] 
    (let [props (java.util.Properties.)]
      (.load props reader)
      props)))


(defn gate-init
  "Initialize gate, if no gate home is given then try to load it from gate.properties file"
  ([gate-home gate-config]
    (if (nil? (Gate/getGateHome)) (Gate/setGateHome (as-file gate-home)))
    (Gate/setUserConfigFile (as-file gate-config))
    (Gate/init))
  ([] (if (not (Gate/isInitialised))
    (let 
      [props (read-properties "gate.properties")
       home (. props getProperty "gate.home")
       config (. props getProperty "gate.config")]
      (gate-init home config)))))

(defn- create-resource 
  "This should not be that difficult and not needed at all, but
  http://groups.google.com/group/clojure/browse_thread/thread/3cfa1779d01783a3/03c269357d9c2e6b#03c269357d9c2e6b"
  [clazz]
  (let [factory (Class/forName "gate.Factory")
        createResource (.getMethod factory "createResource" (into-array [(.getClass clazz)]))]
    (.invoke createResource factory (into-array [clazz]))))

(defn load-application
  "Load GATE application, from gapp file"
  [file-name] (PersistenceManager/loadObjectFromFile file-name))
  ;[file-name] (create-resource "gate.creole.SerialAnalyserController"))

(defn corpus-init [] (create-resource "gate.corpora.CorpusImpl"))

;;DEPRECATED due to memory issues
(defn load-corpus 
  "Load all documents into corpus and return it"
  [directory extensions]
  (let [corpus (corpus-init)
        files (FileUtils/listFiles (as-file directory) (into-array extensions) true)]
    (doall (map #(.add corpus (document-from-file %)) files))
    corpus))

;;DEPRECATED due to memory issues
(defn execute-pipline
  "Execute pipeline with document corpus"
  [pipline-file corpus]
  (doto (load-application (as-file pipline-file))
    (.setCorpus corpus)
    (.execute)))

(defn gapp-init 
  ([gapp-file corpus] (let [gapp (load-application (as-file gapp-file))]
                        (.setCorpus gapp corpus)
                        gapp))
  ([gapp-file] (gapp-init gapp-file (corpus-init))))

(defn delete-gate-resource [resource]
 (let [factory (Class/forName "gate.Factory")
       method (.getMethod factory "deleteResource" (into-array [gate.Resource]))]
   (.cleanup resource)
   (-> resource .getFeatures .clear)
   (.invoke method factory (into-array [resource]))))

(defn run-gapp
  [gapp document process]
  (.add (.getCorpus gapp) document)
  (.execute gapp)
  (let [results (process document)]
    (.clear (.getCorpus gapp))
    (delete-gate-resource document)
    results))
