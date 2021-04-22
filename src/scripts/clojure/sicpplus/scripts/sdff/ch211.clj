;; clj src/scripts/clojure/sicpplus/scripts/sdff/ch211.clj
(set! *warn-on-reflection* true)
;;(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns sicpplus.scripts.sdff.ch211
  
  {:doc "examples from SDFF ch 2.1.1"
   :author "palisades dot lakes at gmail dot com"
   :version "2021-04-21"}
  
  (:refer-clojure :exclude [iterate])
  (:require [clojure.java.io :as io]
            [clojure.string :as s]
            [sicpplus.commons.core :as scc])
  (:import 
    [java.lang Math]
    [clojure.lang IFn]
    ))

;; TODO: print macro that echos the expression being evaluated
;; think I  have that somewhere...

;;----------------------------------------------------------------
;; check associativity of clojhure <code>comp</code>?

(let [add1 (fn [x] (+ 1 x))
      mul2 (fn [x] (* 2 x))
      sqrt (fn [x] (Math/sqrt x))]
  (scc/echo 
    (comp (comp sqrt mul2) add1)
    (comp sqrt (comp mul2 add1))
    (comp sqrt mul2 add1)))  
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
;; Note: potential for off-by-1 error between direct (scheme)
;; version and use of <code>clojure.core/iterate</code>

;; translation from scheme

(defn iterate [^long n f]
  (if (zero? n)
    identity
    (compose f (iterate (dec n) f))))

;; clojure.core.iterate returns an infinite lazy sequence
;; might be worth a performance test, comparing to a more direct
;; implementation

(defn iterate0 [^long n f]
  (fn iterated [x] 
    (last (take (inc n) (clojure.core/iterate f x)))))

;;----------------------------------------------------------------
(defn square [^long x] (* x x))

(scc/echo 
  ((iterate 3 square) 5)
  ((iterate0 3 square) 5))
;;----------------------------------------------------------------
;; p 26
(defn parallel-combine [h f g]
  (fn the-combination [& args]
    (h (apply f args) (apply g args))))

;; why different arg names in
;; <code>[x y z]</code> vs <code>[u v w]?
;; did using the same names confuse students?

(scc/echo 
  ((parallel-combine vector
                     (fn [x y z] ['foo x y z])
                     (fn [u v w] ['bar u v w]))
    'a 'b 'c))
;;----------------------------------------------------------------
;; Arity p 27, Multiple values p 30
;;
;; This example demonstrates how functions fail to be first class
;; in clojure:
;; (1) No built-in way to get at the arity; instead resort
;; to Java reflection depending on undocumented internal 
;; interfaces.
;; (2) No straightforward way to create a function with arity
;; calculated at runtime.
;; Original Scheme code seems like a hack too, with "metadata"
;; stored in a global, presuambly unsynchronized, hashtable.
;; Also depends on functions objects either being unique,
;; or with expected notion of equality. This isn't true of
;; clojure (at least it wasn't in the past). It was possible
;; load the same function definition multiple times, and
;; resulting instances were not <code>.equal</code>.
;; Adding "metadata" at runetime, either to the function object,
;; or to a separate hashtable, won't necessarily be the same
;; for all instances.
;;
;; As written, the Arity section is an example of what not to do.
;; Bug prone, relying on obscure functionality only in a 
;; particular dialect of Scheme, needlessly restrictive.
;; Conceptually just wrong, functions don't have a particular
;; arity.
;;
;; A better approach acknowledges function domains and codomains
;; (I'll make them explicit later). 
;; The existence of varying arities is in effect just syntactic
;; sugar over the sequences that are the elements of the domains.
;; The functionality here can then be implemented using
;; <code>compose</code> and <code>parallel-compose</code>
;; to combine arbitrary functions and transformations between
;; domains;

(defn spread-combine 
  
  "A better approach to the examples in SDFF 2.1.1 Arity:
    <dl>
    <dt>h</dt><dd>arbitrary function</dd>
    <dt>fg2h</dt><dd>
    maps from 
    <code>(cartesian-product (codomain f) (codomain g))</code>
    to <code>(codomain h)</code?.</dd>
    <dt>f</dt><dd>arbitrary function
    <dt>h2f</dt><dd>maps from <code>(domain h)</code> to
    <code>(domain f)</code>
    <dt>g</dt><dd>arbitrary function
    <dt>h2g</dt><dd>maps from <code>(domain h)</code> to
    <code>(domain g)</code>
    </dl>
    The body of this function is very simple, evidence 
    against actually defining it."
  [h fg2h f h2f g h2g]
  (parallel-combine 
    (compose h fg2h) (compose f h2f) (compose g h2g)))

;;----------------------------------------------------------------

(scc/echo
  ((spread-combine 
     reverse vector
     (fn [xf] (vec xf)) (fn [xh] (take 2 xh))
     (fn [xg] (vec xg)) (fn [xh] (take-last 3 xh)))
    (list :a :b :c :d :e)))

