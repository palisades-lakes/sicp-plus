package sicpplus.java.numbers;

import static sicpplus.java.numbers.Numbers.hiWord;
import static sicpplus.java.numbers.Numbers.unsigned;

import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;

import org.apache.commons.rng.UniformRandomProvider;

import sicpplus.java.algebra.OneSetOneOperation;
import sicpplus.java.algebra.Set;
import sicpplus.java.prng.Generator;
import sicpplus.java.prng.GeneratorBase;
import sicpplus.java.prng.Generators;

/** /** A proof-of-concept implementation of unbounded natural
 * numbers. Only implementing a commutative monoid 
 * (ie just addition) for now.
 * 
 * This is in contrast to {@link Natural} and
 * {@link java.math.BigInteger}, which both have bounded ranges,
 * limited, for one thing, by the fact that bits are addressable
 * by <code>int</code>.
 *  
 * @author palisades dot lakes at gmail dot com
 * @version 2021-05-31
 */

@SuppressWarnings("unchecked")
public final class UnboundedNatural 
implements Comparable<UnboundedNatural> {

  //--------------------------------------------------------------
  // an unbounded immutable sequence of ints
  //--------------------------------------------------------------
  // TODO: Any value to implementing Iterator<Integer> or
  // PrimitiveIterator.OfInt?
  //
  // TODO: What about zero-length sequences? 
  // Is there a better choice than just null?
  //
  // TODO: could do without this class; have the U.N. be the
  // sequence. Then next/rest would be shifting down 32 bits?

  private static record Words (int word, Words next) { }

  /** Will return <code>null</code> if <code>s</code> is 
   * <code>null</code>.
   */
  private static final Words reverse (final Words s) {
    if (null == s) { return null; }
    Words out = new Words(s.word,null);
    Words in = s.next;
    while (null != in) {
      out = new Words(in.word,out);
      in = in.next; }
    return out; }

  //--------------------------------------------------------------
  // fields
  //--------------------------------------------------------------
  /** Least significant word first. Never modified.
   */

  private final Words words;

  /** Singleton. */
  public static final UnboundedNatural ZERO = 
    new UnboundedNatural(null); 

  /** Singleton. */
  public static final UnboundedNatural ONE = 
    new UnboundedNatural(new Words(1,null)); 

  //--------------------------------------------------------------
  // monoid operation
  //--------------------------------------------------------------

  public final UnboundedNatural add (final UnboundedNatural u) {
    Words tt = words;
    Words uu = u.words;
    Words vv = null;
    long sum = 0L;
    for (;(null!=tt)&&(null!=uu);
      tt = tt.next,uu = uu.next) {
      sum += unsigned(tt.word) + unsigned(uu.word);
      vv = new Words((int) sum,vv); 
      sum = hiWord(sum); }
    if (null==tt) {
      for (;null!=uu;uu=uu.next) {
        sum += unsigned(uu.word);
        vv = new Words((int) sum,vv); 
        sum = hiWord(sum); } }
    else {
      for (;null!=tt;tt=tt.next) {
        sum += unsigned(tt.word);
        vv = new Words((int) sum,vv); 
        sum = hiWord(sum); } }
    if (0L!=sum) { vv = new Words(1,vv); } 
    return new UnboundedNatural(reverse(vv)); }

  //--------------------------------------------------------------
  // Comparable
  //--------------------------------------------------------------

  @Override
  public final int compareTo (final UnboundedNatural u) {
    Words tt = words;
    Words uu = u.words;
    int result = 0;
    for (;(null!=tt)&&(null!=uu); tt=tt.next,uu=uu.next) {
      final int c = Integer.compareUnsigned(tt.word,uu.word);
      if (c<0) { result = -1; }
      else if (c>0) { result = 1; } } 
    if (null==tt) {
      for (;null!=uu;uu=uu.next) {
        if (0!=uu.word) { return -1; } } }
    else {
      for (;null!=tt;tt=tt.next) { 
        if (0!=tt.word) { return 1; } } }
    return result; }

  //--------------------------------------------------------------
  // Object methods
  //--------------------------------------------------------------

  @Override
  public final int hashCode () { 
    final int prime = 31;
    int c = 1;
    Words tt = words;
    while (null != tt) { 
      c = (int) ((prime * c) + unsigned(tt.word)); }
    return c; }

  @Override
  public final boolean equals (final Object x) {
    if (x==this) { return true; }
    if (!(x instanceof UnboundedNatural)) { return false; }
    final UnboundedNatural u = (UnboundedNatural) x;
    Words tt = words;
    Words uu = u.words;
    while ((null!=tt)&&(null!=uu)) {
      final int ti = tt.word;
      final int ui = uu.word;
      if (ti!=ui) { return false; } }
    if (null==tt) {
      while (null!=uu) {
        if (0!=uu.word) { return false; }
        uu = uu.next; } }
    else {
      while (null!=tt) {
        if (0!=tt.word) { return false; }
        tt = tt.next; } }
    return true; }

  //--------------------------------------------------------------
  // "random" instances for testing, etc.
  //--------------------------------------------------------------

  /** Generate an {@link UnboundedNatural} with <code>n</code>
   * random <code>int</code> words.
   */

  private static final Generator 
  randomBitsGenerator (final long n,
                       final UniformRandomProvider urp) {
    final Generator ig = Generators.intGenerator(urp);
    return new GeneratorBase ("UnboundedNaturalRandomBits:" + n) {
      @Override
      public final Object next () {
        Words w = null;
        for (long i=0;i<n;i++) { w = new Words(ig.nextInt(),w); }
        return new UnboundedNatural(w); } }; }

  /** Intended primarily for testing. 
   * For now, just random bits in roughly twice 
   * the number of words supported by {@link Natural}.
   */

  public static final Generator
  generator (final UniformRandomProvider urp)  {
    return randomBitsGenerator (2L+Integer.MAX_VALUE,urp); }

  //--------------------------------------------------------------
  // construction
  //-------------------------------------------------------------
  /** UNSAFE: doesn't copy <code>words</code> or check 
   * <code>loInt</code> or <code>hiInt</code.
   */

  private UnboundedNatural (final Words w) { words = w; }

  public static final UnboundedNatural valueOf (final Natural u) {
    final int n = u.hiInt();
    Words r = null;
    for (int i=0;i<n;i++) { r = new Words(u.word(i),r); }
    return new UnboundedNatural(reverse(r)); }

  /** Generate large-ish values for testing. 
   * Corresponds to shifting <code>u1</code> up past the highest
   * non-zero word in <code>u0</code> and adding.
   * @param u0 low words
   * @param u1 high words
   */
  public static final UnboundedNatural concatenate (final Natural u0,
                                                    final Natural u1) {
    Words w = null;
    final int n0 = u0.hiInt();
    for (int i=0;i<n0;i++) { w = new Words(u0.word(i),w); }
    final int n1 = u1.hiInt();
    for (int i=0;i<n1;i++) { w = new Words(u1.word(i),w); }
    return new UnboundedNatural(reverse(w)); }

  //--------------------------------------------------------------
  // mathematical structures using UnboundedNatural
  //--------------------------------------------------------------
  /** Contains all instances of {@link UnboundedNatural}. 
   * Could be extended to include all non-negative integer
   * values, but not necessary for proof of concept.
   */

  public static final Set SET = new Set() {
    @Override
    @SuppressWarnings("unused")
    public boolean contains (final Object element) {
      return element instanceof UnboundedNatural; } 
    @Override
    public final Supplier generator (final Map options) {
      final UniformRandomProvider urp = Set.urp(options);
      final Generator g = UnboundedNatural.generator(urp);
      return
        new Supplier () {
        @Override
        public final Object get () { return g.next(); } }; }
  };

  public static final BinaryOperator<UnboundedNatural> adder () {
    return new BinaryOperator<> () {
      @Override
      public final String toString () { return "BF.add()"; }
      @Override
      public final UnboundedNatural 
      apply (final UnboundedNatural q0,
             final UnboundedNatural q1) {
        return q0.add(q1); } }; }

  public static final OneSetOneOperation MONOID = 
    OneSetOneOperation.commutativeMonoid(adder(),SET,ZERO); 


  //--------------------------------------------------------------
}
//--------------------------------------------------------------
