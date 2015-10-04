(defproject factorio-graph "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [instaparse "1.4.1"]
                 [rhizome "0.2.5"]]
  :main ^:skip-aot factorio-graph.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
