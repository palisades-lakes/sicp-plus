;; clj src/scripts/clojure/sicpplus/scripts/sdff/ch2.clj
(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns sicpplus.scripts.sdff.ch2
  
  {:doc "examples from SDFF ch 2"
   :author "palisades dot lakes at gmail dot com"
   :version "2021-04-20"}
  
  (:refer-clojure :exclude [iterate])
  (:require [clojure.java.io :as io]
            [clojure.string :as s]
            [sicpplus.commons.core :as scc])
  (:import 
    [clojure.lang IFn]
    ))

;; TODO: print macro that echos the expression being evaluated
;; think I  have that somewhere...

;;----------------------------------------------------------------
;; simplest translation of example p 24

(defn compose [f g]
  (fn the-composition [& args]
    (f (apply g args))))

;;----------------------------------------------------------------
;; more metadata

(defn compose1 
  "Returns a named function which evaluates the composition
   of <code>f</code> and <code>g</code>."
  {:added "2021-04-20"}
  ^IFn [^IFn f ^IFn g]
  (fn the-composition [& args]
    (f (apply g args))))

;;----------------------------------------------------------------
;; example p 24
;; clojure.core

(let [fgc (comp (fn [x] (list 'foo x))
                (fn [x] (list 'bar x)))
      fg0 (compose (fn [x] (list 'foo x))
                   (fn [x] (list 'bar x)))
      fg1 (compose1 (fn [x] (list 'foo x))
                    (fn [x] (list 'bar x)))]
  
  (scc/echo
    (fgc 'z)
    (fg0 'z)
    (fg1 'z)))
;;----------------------------------------------------------------
;; example p 25: iterate
;; type hints to eliminate boxed math warnings, could just disable
;; Note: only <code>double</code>  and <code>long</code>
;; primitive hints are supported (why?)

;; translation from scheme

(defn iterate [^long n f]
  (if (zero? n)
    identity
    (compose f (iterate (dec n) f))))

; clojure.core.iterate returns an infinite lazy sequence
;; might be worth a performance test, comparing to a more direct
;; implementation

(defn iterate0 [^long n f]
  (fn iterated [x] 
    (last (take (inc n) (clojure.core/iterate f x)))))

;;----------------------------------------------------------------

(defn square [^long x] (* x x))

;;----------------------------------------------------------------
(scc/echo 
  ((iterate 3 square) 5)
  ((iterate0 3 square) 5))
;;----------------------------------------------------------------

