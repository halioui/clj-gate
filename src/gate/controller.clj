(ns gate.controller
  (:use environ.core
        gate.document
        clojure.java.io)
  (:import
     (gate Gate)
     (gate.corpora DocumentImpl)
     (gate.util.persistence PersistenceManager)
     (org.apache.commons.io FileUtils)))

(defn gate-init
  "Initialize gate, if no gate home is given then try to load it from gate.properties file"
  []
  (if (not (Gate/isInitialised))
    (do
      (Gate/setGateHome (as-file (env :gate-home)))
      (Gate/init))))

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

(defn corpus-init [] (create-resource "gate.corpora.CorpusImpl"))

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
