package sicpplus.java.test.scalar;

import static sicpplus.java.test.scalar.Common.affines;
import static sicpplus.java.test.scalar.Common.constantKnots;
import static sicpplus.java.test.scalar.Common.constantTestPts;
import static sicpplus.java.test.scalar.Common.constants;
import static sicpplus.java.test.scalar.Common.cubics;
import static sicpplus.java.test.scalar.Common.exact;
import static sicpplus.java.test.scalar.Common.expand;
import static sicpplus.java.test.scalar.Common.general;
import static sicpplus.java.test.scalar.Common.otherFns;
import static sicpplus.java.test.scalar.Common.quadratics;

import java.util.List;
import java.util.function.BiFunction;

import org.junit.jupiter.api.Test;

import com.google.common.collect.Iterables;

import sicpplus.java.functions.Domain;
import sicpplus.java.functions.Function;
import sicpplus.java.functions.scalar.ConstantFunctional;

//----------------------------------------------------------------
/** Test constant 'interpolants'. 
 * <p>
 * <pre>
 * mvn -q -Dtest=sicpplus/java/test/scalar/ConstantFunctionTest test > ConstantFunctionTest.txt
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2018-10-09
 */

public final class ConstantFunctionalTest {

  @SuppressWarnings({ "static-method" })
  @Test
  public final void exactTests () {
    final Domain support = expand(constantTestPts);
    final List<BiFunction> factories = 
      List.of(ConstantFunctional::interpolate);
    final Iterable<Function> functions = Iterables.concat(
       constants);
    for (final BiFunction factory : factories) {
      for (final Function f : functions) {
        //System.out.println();
        //System.out.println(f);
        for (final double[][] kn : constantKnots) {
          //System.out.println(Arrays.toString(kn));
          exact(f,factory,kn,constantTestPts,support,
            1.0e0,1.0e0,1.0e0); } } } }

  @SuppressWarnings({ "static-method" })
  @Test
  public final void generalTests () {
    final Domain support = expand(constantTestPts);
    final List<BiFunction> factories = 
      List.of(ConstantFunctional::interpolate);
    final Iterable<Function> functions = Iterables.concat(
      cubics,   
      quadratics, affines, otherFns);
    for (final BiFunction factory : factories) {
      for (final Function f : functions) {
        for (final double[][] kn : constantKnots) {
          general(f,factory,kn,support,1.0e0,1.0e0,3.0e0); } } } }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
