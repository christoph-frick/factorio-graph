(ns factorio-graph.parser
  (:require [instaparse.core :as insta]))

(def parser
  (insta/parser 
    "
    S = <'data:extend'> <'('> (List | Object) <')'>
    <Type> = Object|List|String|Boolean|Number
    List = <'{'> Type* <'}'>
    Object = <'{'> Attribute+ <'}'>
    Attribute = Name <'='> Type
    Name = #'[\\w_][\\w\\d_]*'
    String = <'\"'> #'[^\"]*' <'\"'>
    Boolean = 'true' | 'false'
    Number = #'-?\\d+(\\.\\d+)?'
    "
    :auto-whitespace :comma
    )) 

(def transformation
  {:S identity
   :List vector
   :Object (fn [& args] (apply conj {} args))
   :Attribute vector
   :Name keyword
   :Number #(BigDecimal. %)
   :String str
   :Boolean #(case % "true" true false)
   })

(defn parse
  [string]
  (insta/parses parser string))

(defn transform
  [tree]
  (first (insta/transform transformation tree)))

(defn parse-transform
  [string]
  (-> string parse transform))
