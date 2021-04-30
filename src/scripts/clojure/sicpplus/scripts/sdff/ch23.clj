;; clj src/scripts/clojure/sicpplus/scripts/sdff/ch23.clj
(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns sicpplus.scripts.sdff.ch23
  
  {:doc "examples from SDFF ch 2.3"
   :author "palisades dot lakes at gmail dot com"
   :version "2021-04-29"}
  
  #_(:refer-clojure :exclude [iterate])
  (:require [clojure.java.io :as io]
            [clojure.string :as s]
            [sicpplus.commons.core :as scc]
            [sicpplus.cartesian :as cartesian])
  (:import 
    [java.lang Math]
    [clojure.lang IFn]))

;;----------------------------------------------------------------
;; Could possibly use clojure.math.numeric-tower 
;; for some functions,
;; to maybe get exact rational computation, 
;; but it doesn't have <code>atan</code>,
;; so we would be falling back to something else anyway
;; Last time I checked, a few years ago, clojure.lang.Ratio
;; had lots of problems, so I'm avoinding that as well.
;; TOobegin, use <code>java.lang.Math</code>
;; and <code>doubnle</double>


(def gas-constant "J/(K*mol)" (double 8.3144621))

(defn gas-law-volume ^double [^double pressure 
                              ^double temperature 
                              ^double amount]
  (/ (* gas-constant amount temperature)
     pressure))

;;(def pi (* 4 (Math/atan2 1 1))) == Math/PI
(def one-third (/ 1.0 3.0))
;; (def three-over-4pi (/ 3.0 (* 4 pi))) == three-over-4PI
(def three-over-4PI (/ 3 (* 4 Math/PI)))

(defn sphere-radius ^double [^double volume]
  (Math/pow (* three-over-4PI volume) one-third)) 
  
;;----------------------------------------------------------------

(scc/echo
  (class gas-constant)
  Math/PI
;  pi
;  (- Math/PI pi)
;  three-over-4pi
  three-over-4PI
;  (- three-over-4PI three-over-4pi)
  (sphere-radius (/ (* 4 Math/PI) 3))
  )

