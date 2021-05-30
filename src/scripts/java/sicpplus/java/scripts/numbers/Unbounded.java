package sicpplus.java.scripts.numbers;

import sicpplus.java.numbers.Natural;
import sicpplus.java.numbers.UnboundedNatural;

//----------------------------------------------------------------
/** Test bounded ranges for various number implementations.
 * <p>
 * <pre>
 * j src/scripts/java/sicpplus/java/scripts/numbers/Unbounded.java
 * jy src/scripts/java/sicpplus/java/scripts/numbers/Unbounded.java
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2021-05-29
 */

public final class Unbounded {

  private static final void addition () {
    final long t0 = System.nanoTime();
    try {
      final Natural max = Natural.maxValue();
      UnboundedNatural u = 
        UnboundedNatural.concatenate(max,max);
      for (int i=0;i<32;i++) { 
        final UnboundedNatural u1 = u.add(u); 
        assert (u.compareTo(u1) < 0); 
        u = u1; } }
    finally {
      System.out.printf("Total seconds: %4.3f\n",
        Double.valueOf((System.nanoTime()-t0)*1.0e-9)); } }

  public static final void main (final String[] args) {
    addition();
  }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
