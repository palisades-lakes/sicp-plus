package sicpplus.java.scripts.numbers;

import java.math.BigInteger;

import sicpplus.java.numbers.Natural;
import sicpplus.java.numbers.UnboundedNatural;

//----------------------------------------------------------------
/** Test bounded ranges for various number implementations.
 * <p>
 * <pre>
 * j src/scripts/java/sicpplus/java/scripts/numbers/Bounds.java
 * jy src/scripts/java/sicpplus/java/scripts/numbers/Bounds.java
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2021-05-27
 */

public final class Bounds {

  private static final void additionUnboundedNatural () {
    final long t0 = System.nanoTime();
    try {
      final Natural one = Natural.valueOf(1);
      final Natural n0 = one
        .shiftUp(Natural.MAX_BITS-2)
        .subtract(1);
      final Natural n1 = one
        .shiftUp(Natural.MAX_BITS-1)
        .add(n0)
        .add(n0);
    UnboundedNatural uone = UnboundedNatural.valueOf(one);
    UnboundedNatural u = UnboundedNatural.valueOf(n1);
    for (int i=1;i<=3;i++) { u = u.add(uone); } }
    finally {
      System.out.printf("Total seconds: %4.3f\n",
        Double.valueOf((System.nanoTime()-t0)*1.0e-9)); } }

  private static final void additionNatural () {
    final long t0 = System.nanoTime();
    try {
      final Natural one = Natural.valueOf(1);
      final Natural n0 = one
        .shiftUp(Natural.MAX_BITS-2)
        .subtract(1);
      final Natural n1 = one
        .shiftUp(Natural.MAX_BITS-1)
        .add(n0)
        .add(n0);
      Natural n = n1;
      for (long i=1L;i<=3;i++) { 
        System.out.print("Natural: " + i + ": " +
          Integer.toUnsignedString(n.hiBit(),16)); 
        final long t = System.nanoTime();
        final Natural nn = n.add(1);
        assert nn.compareTo(n) > 0 : 
          nn.compareTo(n) + "\n" +
          Integer.toUnsignedString(n1.hiBit(),16) + " " +
          Integer.toUnsignedString(Integer.MAX_VALUE,16);
        System.out.printf(" [%4.3f]\n",
          Double.valueOf((System.nanoTime()-t)*1.0e-9)); 
        n = nn;  
      } }
    finally {
      System.out.printf("Total seconds: %4.3f\n",
        Double.valueOf((System.nanoTime()-t0)*1.0e-9)); } }

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
    additionUnboundedNatural();
    additionNatural();
    additionBigInteger();
  }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
