(ns factorio-graph.core
  (:require [factorio-graph.parser :as p]
            [loom.graph :as g]
            [loom.io :as gio])
  (:gen-class))

(defn -main
  [& args]
  (let [root (System/getenv "FACTORIO")]
    (assert (not (nil? root)) "Set the environment variable FACTORIO to the root of your installation")
    (let [technology-dir (str root "/data/base/prototypes/technology/")
          parsed (eduction
                   (filter #(.isFile %)) 
                   (mapcat #(p/parse-transform (slurp %))) 
                   (file-seq (clojure.java.io/file root)))
          technology (into {}
                           (comp
                             (filter #(= (:type %) "technology"))  
                             (map (juxt :name :prerequisites)))
                           parsed)
          graph (-> technology g/digraph g/transpose)]
      (gio/view graph :node {:shape "none"}))))
