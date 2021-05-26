package sicpplus.java.scripts.numbers;

import java.math.BigInteger;

import sicpplus.java.numbers.Natural;

//----------------------------------------------------------------
/** Test bounded ranges for various number implementations.
 * <p>
 * <pre>
 * j src/scripts/java/sicpplus/java/scripts/numbers/Bounds.java
 * jy src/scripts/java/sicpplus/java/scripts/numbers/Bounds.java
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2021-05-25
 */

public final class Bounds {

  private static final void additionBigInteger () {
    final long t0 = System.nanoTime();
    try {
      BigInteger n = 
        BigInteger.valueOf(1)
        .shiftLeft(Integer.MAX_VALUE-1);
      n = n.add(n.subtract(BigInteger.TEN));
      for (long i=1L;i<=Long.MAX_VALUE;i++) {
        System.out.print("BigInteger: " + i + ": " +
          Integer.toUnsignedString(n.bitLength(),16)); 
        final long t = System.nanoTime();
        final BigInteger n1 = n.add(BigInteger.ONE); 
        assert n1.compareTo(n) > 0;
        n = n1;
        System.out.printf(" [%4.3f]\n",
          Double.valueOf((System.nanoTime()-t)*1.0e-9)); } }
    finally {
      System.out.printf("Total seconds: %4.3f\n",
        Double.valueOf((System.nanoTime()-t0)*1.0e-9)); } }

  private static final void additionNatural () {
    final long t0 = System.nanoTime();
    try {
      Natural n = 
      Natural.valueOf(0);
//      Natural.valueOf(1)
//        .shiftUp(Integer.MAX_VALUE-1);
//      n = n.add(n.subtract(10));
      for (long i=1L;i<=Long.MAX_VALUE;i++) {
//        System.out.print("Natural: " + i + ": " +
//          Integer.toUnsignedString(n.hiBit(),16)); 
        final long t = System.nanoTime();
        final Natural n1 = n.add(1L);
        assert n1.compareTo(n) > 0 : n1.compareTo(n) + " " +
        Integer.toUnsignedString(n1.hiBit(),16);
        n = n1; 
//        System.out.printf(" [%4.3f]\n",
//          Double.valueOf((System.nanoTime()-t)*1.0e-9)); 
        } }
    finally {
      System.out.printf("Total seconds: %4.3f\n",
        Double.valueOf((System.nanoTime()-t0)*1.0e-9)); } }

  private static final void multiplicationBigInteger () {

    final long t0 = System.nanoTime();
    try {
      BigInteger n = 
        BigInteger.valueOf(1)
        .shiftLeft(1+(Integer.MAX_VALUE>>1))
        .subtract(BigInteger.TEN);
      for (long i=0L;i<=Long.MAX_VALUE;i++) {
        System.out.print("BigInteger: " + i + ": " +
          Integer.toUnsignedString(n.bitLength(),16)); 
        final long t = System.nanoTime();
        n = n.multiply(n); 
        System.out.printf(" [%4.3f]\n",
          Double.valueOf((System.nanoTime()-t)*1.0e-9)); } }
    finally {
      System.out.printf("Total seconds: %4.3f\n",
        Double.valueOf((System.nanoTime()-t0)*1.0e-9)); } }

  private static final void multiplicationNatural() {

    final long t0 = System.nanoTime();
    try {
      Natural n = 
        Natural.valueOf(1,1+(Integer.MAX_VALUE>>1))
        .subtract(10);
      for (long i=0L;i<=Long.MAX_VALUE;i++) {
        System.out.print("Natural " +
          i
          + ": " +
          Integer.toUnsignedString(n.hiBit(),16)); 
        final long t = System.nanoTime();
        n = n.multiply(n); 
        System.out.printf(" [%4.3f]\n",
          Double.valueOf((System.nanoTime()-t)*1.0e-9)); } }
    finally {
      System.out.printf("Total seconds: %4.3f\n",
        Double.valueOf((System.nanoTime()-t0)*1.0e-9)); } }

  public static final void main (final String[] args) {
    additionNatural();
    additionBigInteger();
  }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
