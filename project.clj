(defproject clj-gate "0.5.1"
  :description "FIXME: write"
  :dependencies[[enlive "1.1.1"]
                [commons-io "1.4"]
                [commons-lang "2.3"]
                [com.thoughtworks.xstream/xstream "1.4.2"]
                [uk.ac.gate/gate-core "7.1"]
                [org.apache.tika/tika-parsers "1.2"]
                [nekohtml "1.9.6.2"]
                [org.clojure/clojure "1.6.0"]]
  :exclusions [de.l3s.boilerpipe/boilerpipe 
               org.apache.lucene/lucene-contrib])
