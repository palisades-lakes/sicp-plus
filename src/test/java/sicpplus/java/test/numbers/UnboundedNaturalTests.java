package sicpplus.java.test.numbers;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableMap;

import sicpplus.java.algebra.Set;
import sicpplus.java.algebra.Structure;
import sicpplus.java.numbers.Natural;
import sicpplus.java.numbers.UnboundedNatural;
import sicpplus.java.prng.PRNG;
import sicpplus.java.test.algebra.SetTests;

//----------------------------------------------------------------
/** <pre>
 * mvn -Dtest=sicpplus/java/test/numbers/UnboundedNaturalTests test > UNT.txt
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2021-05-31
 */

@SuppressWarnings("unchecked")
public final class UnboundedNaturalTests {

  @SuppressWarnings({ "static-method" })
  @Test
  public final void noOverflow () {
    final UnboundedNatural u = 
      UnboundedNatural.valueOf(Natural.maxValue());
    // no overflow from add 
    final UnboundedNatural v = u.add(u); 
    final int cmp = u.compareTo(v);
    Assertions.assertTrue(
      (cmp < 0),
      () -> { 
        return "\nadd one doesn't increase value\n" 
          + "compareTo -> " + cmp; }); }

  @SuppressWarnings({ "static-method" })
  @Test
  public final void monoid () {

    final Structure s = UnboundedNatural.MONOID;
    final int n = 2;
    SetTests.tests(s,n);
    final Map<Set,Supplier> generators =
      s.generators(
        ImmutableMap.of(
          Set.URP,
          PRNG.well44497b("seeds/Well44497b-2019-01-09.txt")));
    for(final Predicate law : s.laws()) {
      for (int i=0; i<n; i++) {
        final boolean result = law.test(generators);
        assertTrue(result,
          s.toString() + " : " + law.toString()); } } }


  //--------------------------------------------------------------
}
//--------------------------------------------------------------
