(set! *warn-on-reflection* true)
;;(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns sicpplus.test.cartesian
  
  {:doc "Unit tests for sicpplus.cartesian"
   :author "palisades dot lakes at gmail dot com"
   :version "2021-04-23"}
  
  (:require [clojure.test :as test]
            [sicpplus.cartesian :as cartesian]))
;; mvn clojure:test -Dtest=sicpplus.test.cartesian
;;----------------------------------------------------------------
(test/deftest test-compose
  (test/testing 
    "2 function composition"
    (let [fg (cartesian/compose 
               (fn [x] [:foo x])
               (fn [x] [:bar x]))]
      (test/is (= [:foo [:bar :z]] 
                  (fg :z))))))

;;(run-tests)
;;----------------------------------------------------------------
