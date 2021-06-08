package sicpplus.java.scripts.numbers;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableMap;

import sicpplus.java.algebra.Set;
import sicpplus.java.algebra.Structure;
import sicpplus.java.numbers.BoundedNatural;
import sicpplus.java.numbers.UnboundedNatural;
import sicpplus.java.prng.Generator;
import sicpplus.java.prng.Generators;
import sicpplus.java.prng.PRNG;
import sicpplus.java.test.algebra.SetTests;

//----------------------------------------------------------------
/** Profiling {@link UnboundedNatural}.
 * <p>
 * <pre>
 * j src/scripts/java/sicpplus/java/scripts/numbers/Unbounded.java
 * jy src/scripts/java/sicpplus/java/scripts/numbers/Unbounded.java
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2021-06-07
 */

@SuppressWarnings("unchecked")
public final class Unbounded {

  /** passes in a 56g JVM:
   * <table>
   *  <tr> <th>nwords</th>     <th>sec</th> </tr>
   *  <tr> <td>1L+     MAX_WORDS</td>  <td>  9</td> </tr>
   *  <tr> <td>1L+( 2L*MAX_WORDS)</td> <td> 28</td> </tr>
   *  <tr> <td>1L+( 4L*MAX_WORDS)</td> <td> 70</td> </tr>
   *  <tr> <td>1L+( 8L*MAX_WORDS)</td> <td>150</td> </tr>
   *  <tr> <td>1L+(16L*MAX_WORDS)</td> <td>1207 (OOM)</td> </tr>
   * </table>
   */

  private static final void noOverflow () {
    final long t0 = System.nanoTime();
    try {
      final long n = 1L+(BoundedNatural.MAX_WORDS<<4);
      System.out.println("n=" + n);
      System.out.flush();
      final Generator g = 
        UnboundedNatural.randomBitsGenerator(
          n,PRNG.well44497b("seeds/Well44497b-2019-01-05.txt"));

      final UnboundedNatural u0 = 
        (UnboundedNatural) g.next();
      //    final UnboundedNatural u1 = 
      //      (UnboundedNatural) g.next();
      // no overflow from add 
      final UnboundedNatural v = u0.add(u0); 
      final int c0 = u0.compareTo(v);
      assert (c0 < 0) :
        "\nadd u0 doesn't increase value\n" 
        + "compareTo -> " + c0;
      //    final int c1 = u0.compareTo(v); 
      //    assert (c1 < 0) :
      //      "\nadd u1 doesn't increase value\n" 
      //          + "compareTo -> " + c1;
    }
    finally {
      System.out.printf("Total seconds: %4.3f\n",
        Double.valueOf((System.nanoTime()-t0)*1.0e-9)); } }

  /** passes in a 56g JVM:
   * <table>
   *  <tr> <th>nwords</th>     <th>sec</th> </tr>
   *  <tr> <td>1L+     MAX_WORDS</td>  <td> 176</td> </tr>
   *  <tr> <td>1L+( 2L*MAX_WORDS)</td> <td> 590</td> </tr>
   *  <tr> <td>1L+( 4L*MAX_WORDS)</td> <td>1439</td> </tr>
   *  <tr> <td>1L+( 8L*MAX_WORDS)</td> <td>1752 OOM</td> </tr>
   * </table>
   */

  private static final void monoid () {

    final Structure monoid = UnboundedNatural.MONOID;
    final Supplier g = new Supplier () {
      final Generator rbg = 
        UnboundedNatural.randomBitsGenerator(
          1L+(BoundedNatural.MAX_WORDS<<3),
          PRNG.well44497b("seeds/Well44497b-2019-01-05.txt"));
      @Override
      public final Object get () { return rbg.next(); } };
      final Map generators = 
        ImmutableMap.of(UnboundedNatural.SET,g);

      final int trys = 2;
      final long t0 = System.nanoTime();
      try {
        SetTests.tests(monoid,trys,g);
        for(final Predicate law : monoid.laws()) {
          for (int i=0; i<trys; i++) {
            final boolean result = law.test(generators);
            assert result: 
              monoid.toString() + " : " + law.toString(); } } }
      finally {
        System.out.printf("Total seconds: %4.3f\n",
          Double.valueOf((System.nanoTime()-t0)*1.0e-9)); } }

  public static final void main (final String[] args) {
    noOverflow();
    monoid();
  }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
