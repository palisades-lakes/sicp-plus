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
;; check associativity of clojure <code>comp</code>?

(let [add1 (fn [x] (+ 1 x))
      mul2 (fn [x] (* 2 x))
      sqrt (fn [x] (Math/sqrt x))
      c21 (comp (comp sqrt mul2) add1)
      c12 (comp sqrt (comp mul2 add1))
      c3 (comp sqrt mul2 add1)]
  (scc/echo 
    c21
    c12
    c3
    (= c21 c12 c3)))  
;;----------------------------------------------------------------
;; simplest translation of example p 24

(defn compose [f g]
  (fn the-composition [x] (f (g x))))

;;----------------------------------------------------------------
;; more metadata

(defn compose1 
  "Returns a named function which evaluates the composition
   of <code>f</code> and <code>g</code>."
  {:added "2021-04-20"}
  ^IFn [^IFn f ^IFn g]
  (fn the-composition [x] (f (g x))))

;;----------------------------------------------------------------
;; example p 24
;; clojure.core

;(let [fgc (comp (fn [x] (list :foo x))
;                (fn [x] (list :bar x)))
;      fg0 (compose (fn [x] (list :foo x))
;                   (fn [x] (list :bar x)))
;      fg1 (compose1 (fn [x] (list :foo x))
;                    (fn [x] (list :bar x)))]
;  
;  (scc/echo
;    (fgc :z)
;    (fg0 :z)
;    (fg1 :z)))
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

;(scc/echo 
;  ((iterate 3 square) 5)
;  ((iterate0 3 square) 5))
;;----------------------------------------------------------------
;; TODO: cartesian namespace

(defn cartesian-tuple 
  "Collect the <code>elements</code> into a tuple in the 
   appropriate cartesian product space.
   Here just using <code>vector</code> for all tuples,
   supporting only the set of all possible Java objects 
   as the element domains." 
  [& elements] 
  (vec elements))

;; TODO: memoize this?

(defn cartesian-projection 
  "Return the projection that selects the <code>i<code>th
   element of a cartesian tuple."
  [i]
  (fn [tuple] (get tuple i)))

(def cartesian-project-0 (cartesian-projection 0))
(def cartesian-project-1 (cartesian-projection 1))

(defn cartesian-diagonal 
  "Take 2 functions and return a function that maps the 
   cartesian product of the domains to the cartesian product
   of the codomains by applying each function to its corresponding
   domain element in the input 
   and making a tuple of the 2 results."
  [f g]
  (fn [x] 
    [(f (cartesian-project-0 x)) 
     (g (cartesian-project-1 x))]))

(defn cartesian-split
  "Take 2 functions with the same domain 
   and return a function that maps that
   to the cartesian product of the codomains 
   by applying both function to the input 
   and making a tuple of the 2 results."
  [f g]
  (fn [x] [(f x) (g x)]))

;;----------------------------------------------------------------
;; p 26
;; why different arg names in
;; <code>[x y z]</code> vs <code>[u v w]?
;; did using the same names confuse students?

(defn cartesian-combine [h f g]
  (compose h (cartesian-split f g)))

(scc/echo 
  ((cartesian-combine reverse
                      (fn [x] [:foo x])
                      (fn [x] [:bar x]))
    [:a :b :c])
  )

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

(defn diagonal-combine 
  "Like <code>spread-combine</code>,
   but assumes <code>(domain h)</code> is the cartesian product
   of the codomains of <code>f</code> and <code>g</code>,
   and the domain of the returned function is the cartesian 
   product of the domains of <code>f</code> and <code>g</code>."
  [h f g]
  (compose h (cartesian-diagonal f g)))

;;----------------------------------------------------------------

(scc/echo
  ((diagonal-combine reverse reverse reverse)
    [[:a :b] [:c :d :e]])
  )

