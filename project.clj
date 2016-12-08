(defproject clj-gate "0.5.3"
  :description "GATE embedded wrappers"
  :dependencies[[enlive "1.1.6"]
                [environ "0.5.0"]
                [commons-io "1.4"]
                [commons-lang "2.3"]
                [uk.ac.gate/gate-core "8.2"]
                [org.apache.tika/tika-parsers "1.14"]
                [org.clojure/clojure "1.8.0"]]
  :profiles {:uberjar {:aot :all}
             :dev {:dependencies [[midje "1.8.3"]]}})
