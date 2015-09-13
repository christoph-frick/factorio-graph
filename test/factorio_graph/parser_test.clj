(ns factorio-graph.parser-test
  (:require [clojure.test :refer :all]
            [factorio-graph.parser :refer :all]))

(defn- wrap-de [s] (str "data:extend(" s ")"))

(deftest parse-and-transform
  (testing "random bits and bobs"
    (is (= 
          {:a "a"}  
          (parse-transform (wrap-de "{a=\"a\"}"))))
    (is (=
         {:a 1, :b 2, :c 3}
         (parse-transform (wrap-de "{a=1, b=2, c=3}"))))
    (is (= 
          {:a [1 2 3]}  
          (parse-transform (wrap-de "{a={1,2,3}}")))))
  (testing "number"
    (is (= 
          [1 2 3]  
          (parse-transform (wrap-de "{1,2,3}")))))
    (is (= 
          [-1]  
          (parse-transform (wrap-de "{-1}")))) 
  (testing "boolean"
    (is (= 
          [true]
          (parse-transform (wrap-de "{true}"))))
    (is (= 
          [false]
          (parse-transform (wrap-de "{false}"))))))
