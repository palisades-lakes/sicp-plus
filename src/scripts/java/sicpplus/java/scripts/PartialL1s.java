package sicpplus.java.scripts;

import sicpplus.java.accumulators.Accumulator;
import sicpplus.java.accumulators.BigFloatAccumulator;
import sicpplus.java.prng.Generator;
import sicpplus.java.prng.Generators;

/** Benchmark partial L1 norms
 *
 * <pre>
 * jy --source 12 src/scripts/java/xfp/java/scripts/PartialL1s.java
 * </pre>
 * @author palisades dot lakes at gmail dot com
 * @version 2019-09-09
 */
@SuppressWarnings("unchecked")
public final class PartialL1s {

  public static final void main (final String[] args) {
    final int dim = (1*1024*1024) - 1;
    final int trys = 8 * 1024;
    //final Generator g = Generators.make("exponential",dim);
    //final Generator g = Generators.make("finite",dim);
    final Generator g = Generators.make("gaussian",dim);
    //final Generator g = Generators.make("laplace",dim);
    //final Generator g = Generators.make("uniform",dim);
    final Accumulator a = BigFloatAccumulator.make();
    assert a.isExact();
    for (int i=0;i<trys;i++) {
      final double[] x = (double[]) g.next();
      final double[] z = a.partialL1s(x); 
      assert ! Double.isNaN(z[dim-1]);} }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
