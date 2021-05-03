;; clj src/scripts/clojure/sicpplus/scripts/sdff/ch232.clj
(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns sicpplus.scripts.sdff.ch232
  
  {:doc "examples from SDFF ch 2.3.2"
   :author "palisades dot lakes at gmail dot com"
   :version "2021-05-02"}
  
  (:refer-clojure :exclude [*])
  (:require [clojure.java.io :as io]
            [clojure.string :as s]
            [sicpplus.commons.core :as scc]
            [sicpplus.cartesian :as cartesian])
  (:import 
    [java.lang Math]
    [clojure.lang IFn IFn$DD Keyword]))

;;----------------------------------------------------------------

(defn gas-law-volume ^double [^double pressure 
                              ^double temperature 
                              ^double amount]
  (let [gas-constant (double 8.3144621)] ;; J/(K*mol) 
    (/ (clojure.core/* gas-constant amount temperature)
       pressure)))

;;(def pi (* 4 (Math/atan2 1 1))) == Math/PI
;; (def three-over-4pi (/ 3.0 (* 4 pi))) == three-over-4PI

(let [one-third (/ 1.0 3.0)
      three-over-4PI (/ 3 (clojure.core/* 4 Math/PI))]
  (defn sphere-radius ^double [^double volume]
    (Math/pow (clojure.core/* three-over-4PI volume) one-third)) )

;;----------------------------------------------------------------

(defn make-converter ^IFn [^IFn$DD f ^IFn$DD f-inverse]
  (fn 
    ;; zero arity call returns inverse converter
    (^IFn []  (make-converter f-inverse f))
    (^double [^double x] (f x))))

;;----------------------------------------------------------------
;; group operations
;;----------------------------------------------------------------

(defn invert ^IFn [^IFn u] (u))

;; order of args reverse of comp!

(defn * ^IFn [^IFn u0 ^IFn u1]
  (make-converter (comp u1 u0)
                  (comp (invert u0) (invert u1))))

(defn divide ^IFn [^IFn u0 ^IFn u1] (* u0 (invert u1)))

;; tail recursive version?
(defn pow ^IFn [^IFn u ^long n]
  (cond (zero? n) u
        (neg? n) (invert (pow u (- n)))
        :else (* u (pow u (dec n)))))

;;----------------------------------------------------------------
;; A two key map in-unit, out-unit -> converter
;; TODO: do we still need the converter to hold its inverse?

(def ^:private converters (atom {}))

(defn register [^Keyword from 
                ^Keyword to 
                ^IFn$DD converter]
  ;; TODO: should we check for existing converter
  (swap! converters assoc-in [from to] converter)
  (swap! converters assoc-in [to from] (invert converter)))

(defn converter ^IFn [^Keyword from
                      ^Keyword to]
  (let [c (get-in @converters [from to])]
    (assert (not (nil? c)))
    c))

;----------------------------------------------------------------

(register 
  :fahrenheit :celsius 
  (make-converter 
    (fn ^double [^double f] (/ (clojure.core/* 5 (- f 32)) 9))
    (fn ^double [^double c] (+ (/ (clojure.core/* 9 c) 5) 32))))

(register 
  :celsius :kelvin
  (let [zero-celsius 273.15] ; kelvin degrees
    (make-converter 
      (fn ^double [^double c] (+ c zero-celsius))
      (fn ^double [^double k] (- k zero-celsius)))))

;;----------------------------------------------------------------

(register
  :pound :newton
  (make-converter 
    (fn ^double [^double pounds] (/ pounds 0.22480894387))
    (fn ^double [^double newtons] (clojure.core/* newtons 0.22480894387))))

(register
  :inch :meter
  (make-converter 
    (fn ^double [^double inches] (/ inches 39.3700787))
    (fn ^double [^double meters] (clojure.core/* meters 39.3700787))))

(register 
  :psi :nsm
  (make-converter 
    (comp (converter :pound :newton)
          (converter :meter :inch)
          (converter :meter :inch))
    (comp (converter :inch :meter) 
          (converter :inch :meter) 
          (converter :newton :pound))))

(register
  :fahrenheit :kelvin 
  (* (converter :fahrenheit :celsius) 
     (converter :celsius :kelvin)))

;;----------------------------------------------------------------

(scc/echo @converters)

;;----------------------------------------------------------------

;(defn specializer ^IFn [^IFn procedure
;                             implicit-output-unit
;                             & implicit-input-units]
;  (fn specializer ^IFn [specific-output-unit
;                        & specific-input-units]
;    (let [output-converter (make-converter implicit-output-unit
;                                           specific-output-unit)
;          input-converters (mapv make-converter 
;                                 specific-input-units
;                                 implicit-input-units)]
;      (fn specialized-procedure ^IFn [& arguments]
;        (assert (= (count input-converters) (count arguments)))
;        (output-converter
;          (apply 
;            procedure
;            (map (fn [converter argument] (converter argument))
;                 input-converters
;                 arguments)))))))
