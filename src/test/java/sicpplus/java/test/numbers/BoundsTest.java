package sicpplus.java.test.numbers;

import java.math.BigInteger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import sicpplus.java.Classes;
import sicpplus.java.numbers.Natural;

//----------------------------------------------------------------
/** Test bounded ranges for various number implementations.
 * <p>
 * <pre>
 * mvn -q -Dtest=sicpplus/java/test/numbers/BoundsTest test > BT.txt
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2021-05-26
 */

public final class BoundsTest {

  @SuppressWarnings({ "static-method" })
  @Test
  public final void overflowBigInteger () {
    final BigInteger n0 = BigInteger.ONE
      .shiftLeft(Integer.MAX_VALUE-2)
      .subtract(BigInteger.ONE);
    final BigInteger n1 =  BigInteger.ONE
      .shiftLeft(Integer.MAX_VALUE-1)
      .add(n0)
      .add(n0);
    Assertions.assertThrows(
      ArithmeticException.class,
      () -> {
        // overflow at 2nd add
        BigInteger n = n1;
        for (long i=1L;i<=Integer.MAX_VALUE;i++) {
          n = n.add(BigInteger.ONE); } },
      Classes.className(n0)); } 

  @SuppressWarnings({ "static-method" })
  @Test
  public final void overflowNatural () {
    final Natural one = Natural.valueOf(1);
    final Natural n0 = one
      .shiftUp(Natural.MAX_BITS-2)
      .subtract(1);
    final Natural n1 = one
      .shiftUp(Natural.MAX_BITS-1)
      .add(n0)
      .add(n0);
    Assertions.assertThrows(
      ArithmeticException.class,
      () -> {
        // overflow at 2nd add
        Natural n = n1;
        for (long i=1L;i<=3;i++) { n = n.add(1); } },
      Classes.className(n0)); } 

  //  @SuppressWarnings({ "static-method" })
  //  @Test
  //  public final void overflowNatural () {
  //    //Debug.DEBUG=false;
  //
  //    final long t0 = System.nanoTime();
  //    try {
  //      Natural n = 
  ////      Natural.valueOf(0);
  //      Natural.valueOf(1)
  //        .shiftUp(Natural.MAX_BITS-1);
  //      n = n.add(n.subtract(10));
  //      for (long i=1L;i<=Long.MAX_VALUE;i++) {
  //        System.out.print("Natural: " + i + ": " +
  //          Integer.toUnsignedString(n.hiBit(),16)); 
  //        final long t = System.nanoTime();
  //        final Natural n1 = n.add(1L);
  //        assert n1.compareTo(n) > 0 : 
  //          n1.compareTo(n) + "\n" +
  //        Integer.toUnsignedString(n1.hiBit(),16) + " " +
  //        Integer.toUnsignedString(Integer.MAX_VALUE,16);
  //        n = n1; 
  //        System.out.printf(" [%4.3f]\n",
  //          Double.valueOf((System.nanoTime()-t)*1.0e-9)); 
  //        } }
  //    finally {
  //      System.out.printf("Total seconds: %4.3f\n",
  //        Double.valueOf((System.nanoTime()-t0)*1.0e-9)); } 
  //    
  //    //Debug.DEBUG=false;
  //  }


  //--------------------------------------------------------------
}
//--------------------------------------------------------------
