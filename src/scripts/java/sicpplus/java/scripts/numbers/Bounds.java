package sicpplus.java.scripts.numbers;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

import sicpplus.java.numbers.Natural;

//----------------------------------------------------------------
/** Test bounded ranges for various number implementations.
 * <p>
 * <pre>
 * j src/scripts/java/sicpplus/java/scripts/numbers/Bounds.java
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2021-05-24
 */

public final class Bounds {

  public static final void main (final String[] args) {
    
    BigInteger n = 
      BigInteger.valueOf(1)
      .shiftLeft(Integer.MAX_VALUE-63)
      .subtract(BigInteger.TEN);
    for (long i=0L;i<=Long.MAX_VALUE;i++) {
      System.out.print(
        i
        + ": " +
        Integer.toUnsignedString(n.bitLength(),16)); 
      final long t = System.nanoTime();
      n = n.multiply(n); 
      System.out.printf(" [%4.3f]\n",
        Double.valueOf((System.nanoTime()-t)*1.0e-9));}
    
  }

//  public static final void main (final String[] args) {
//    
//    Natural n = Natural.valueOf(Long.MAX_VALUE);
//    for (long i=0L;i<=Long.MAX_VALUE;i++) {
//      System.out.print(
//        i
//        + ": " +
//        Integer.toUnsignedString(n.hiInt(),16)); 
//      final long t = System.nanoTime();
//      n = n.square(); 
//      System.out.printf(" [%8.2f]\n",
//        Double.valueOf((System.nanoTime()-t)*1.0e-9));}
//    
//  }


  //--------------------------------------------------------------
}
//--------------------------------------------------------------
