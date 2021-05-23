package sicpplus.java.scripts.numbers;

/** Check maximum <code>int</code> array size.
 * <pre>
 * j src/scripts/java/sicpplus/java/scripts/numbers/MaxArrayLength.java
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2021-05-23
 */

public final class MaxArrayLength {

  public static final void main (final String[] args) {

    // openjdk version "16" 2021-03-16
    // OpenJDK Runtime Environment (build 16+36-2231)
    // OpenJDK 64-Bit Server VM (build 16+36-2231, mixed mode, sharing)
    // -Xms29g -Xmx29g -Xmn11g
    
    //final int n = Integer.MAX_VALUE-1; // OutOfMemoryError: Requested array size exceeds VM limit
    final int n = Integer.MAX_VALUE-2; // works:
    // 2147483644 -> -1
    // 2147483644 -> -1
    // 2147483644 -> ffffffff
    
    final int[] words = new int[n];
    words[n-1] = -1;
    System.out.println(n-1 + " -> " + words[n-1]);
    System.out.println(n-1 + " -> " + Integer.toString(words[n-1],16));
    System.out.println(n-1 + " -> " + Integer.toUnsignedString(words[n-1],16));
  }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
