(ns factorio-graph.core
  (:require [factorio-graph.parser :as p]
            [rhizome.viz :refer [view-graph]])
  (:gen-class))

(defn parse-recursive 
  [directory]
  (eduction
    (filter #(.isFile %)) 
    (mapcat #(p/parse-transform (slurp %))) 
    (file-seq (clojure.java.io/file directory))))

(defn factorio-dir
  [& dirs]
  (let [root-dir (System/getenv "FACTORIO")]
    (assert (not (nil? root-dir)) "Set the environment variable FACTORIO to the root of your installation")
    (apply str root-dir dirs)))

(def ^:const DATA "/data/base")
(def ^:const TECHNOLOGY "/prototypes/technology")

(defn transpose
  [g]
  (reduce 
    (fn [m [k v]]
      (reduce (fn [m c] 
                (update m c (fnil #(conj % k) []))) 
              m v))
    (into {} (map (fn [[k v]] [k []])) g) 
    g))

(defn technology-graph
  []
  (->> (parse-recursive (factorio-dir DATA TECHNOLOGY))
       (filter #(= (:type %) "technology"))  
       (map (fn [{:keys [name prerequisites]}] [name prerequisites]))
       (into {})
       transpose))

(defn view-technology-graph
  [graph]
  (view-graph 
    (keys graph) graph
    :node->descriptor (fn [n] {:label n})))

(defn -main
  [& args]
  (view-technology-graph (technology-graph)))
