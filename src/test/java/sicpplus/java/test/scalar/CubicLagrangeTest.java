package sicpplus.java.test.scalar;

import static sicpplus.java.test.scalar.Common.affines;
import static sicpplus.java.test.scalar.Common.constants;
import static sicpplus.java.test.scalar.Common.cubics;
import static sicpplus.java.test.scalar.Common.cubicKnots;
import static sicpplus.java.test.scalar.Common.cubicTestPts;
import static sicpplus.java.test.scalar.Common.exact;
import static sicpplus.java.test.scalar.Common.expand;
import static sicpplus.java.test.scalar.Common.general;
import static sicpplus.java.test.scalar.Common.quadratics;
import static sicpplus.java.test.scalar.Common.otherFns;

import java.util.List;
import java.util.function.BiFunction;

import org.junit.jupiter.api.Test;

import com.google.common.collect.Iterables;

import sicpplus.java.functions.Domain;
import sicpplus.java.functions.Function;
import sicpplus.java.functions.scalar.CubicLagrange;

//----------------------------------------------------------------
/** Test monomial form cubics. 
 * <p>
 * <pre>
 * mvn -q -Dtest=sicpplus/java/test/scalar/CubicLagrangeTest test > CubicLagrangeTest.txt
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2018-10-09
 */

public final class CubicLagrangeTest {

  @SuppressWarnings({ "static-method" })
  @Test
  public final void exactTests () {
    final Domain support = expand(cubicTestPts);
    final List<BiFunction> factories = 
      List.of(CubicLagrange::interpolate);
    final Iterable<Function> functions = Iterables.concat(
      cubics,   
      quadratics, affines, constants);
    for (final BiFunction factory : factories) {
      for (final Function f : functions) {
        //System.out.println();
//        System.out.println(f);
        for (final double[][] kn : cubicKnots) {
          if (CubicLagrange.validKnots(kn)) {
//            System.out.println(
//              Arrays.toString(kn[0]) + 
//              ", " + 
//              Arrays.toString(kn[1]));
            exact(f,factory,kn,cubicTestPts,support,
              5.0e6,4.0e10,7.0e10); } } } } }

  @SuppressWarnings({ "static-method" })
  @Test
  public final void generalTests () {
    final Domain support = expand(cubicTestPts);
    final List<BiFunction> factories = 
      List.of(CubicLagrange::interpolate);
    final Iterable<Function> functions = Iterables.concat(otherFns);
    for (final BiFunction factory : factories) {
      for (final Function f : functions) {
        for (final double[][] kn : cubicKnots) {
          if (CubicLagrange.validKnots(kn)) {
            general(f,factory,kn,support,
              1.0e0,1.0e0,1.0e0); } } } } }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------


