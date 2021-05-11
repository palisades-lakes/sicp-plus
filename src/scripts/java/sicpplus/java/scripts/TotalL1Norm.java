package sicpplus.java.scripts;

import sicpplus.java.accumulators.Accumulator;
import sicpplus.java.prng.Generator;
import sicpplus.java.prng.Generators;

/** Benchmark l1 norm.
 *
 * <pre>
 * jy --source 12 src/scripts/java/xfp/java/scripts/TotalL1Norm.java
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-10-09
 * 7
 */
@SuppressWarnings("unchecked")
public final class TotalL1Norm {

  public static final void main (final String[] args) {
    final int dim = (2*1024*1024);
    final int trys = 8 * 1024;
    //final Generator g = Generators.make("finite",dim);
    final Generator g = Generators.make("uniform",dim);
    final Accumulator a = 
      sicpplus.java.accumulators.RationalFloatAccumulator.make();
//    sicpplus.java.accumulators.BigFloatAccumulator.make();
    assert a.isExact();
    for (int i=0;i<trys;i++) {
      final double[] x = (double[]) g.next();
      final double z = a.clear().addAbsAll(x).doubleValue();
      assert Double.isFinite(z); } }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
