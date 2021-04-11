package sicpplus.java.test.scalar;


import static sicpplus.java.test.scalar.Common.affines;
import static sicpplus.java.test.scalar.Common.constants;
import static sicpplus.java.test.scalar.Common.cubics;
import static sicpplus.java.test.scalar.Common.exact;
import static sicpplus.java.test.scalar.Common.expand;
import static sicpplus.java.test.scalar.Common.general;
import static sicpplus.java.test.scalar.Common.otherFns;
import static sicpplus.java.test.scalar.Common.quadraticKnots;
import static sicpplus.java.test.scalar.Common.quadraticTestPts;
import static sicpplus.java.test.scalar.Common.quadratics;

import java.util.List;
import java.util.function.BiFunction;

import org.junit.jupiter.api.Test;

import com.google.common.collect.Iterables;

import sicpplus.java.functions.Domain;
import sicpplus.java.functions.Function;
import sicpplus.java.functions.scalar.QuadraticLagrange;

//----------------------------------------------------------------
/** Test Lagrange form parabolas. 
 * <p>
 * <pre>
 * mvn -q -Dtest=sicpplus/java/test/scalar/QuadraticLagrangeTest test > QuadraticLagrangeTest.txt
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2018-10-09
 */

public final class QuadraticLagrangeTest {

  @SuppressWarnings({ "static-method" })
  @Test
  public final void exactTests () {
    final Domain support = expand(quadraticTestPts);
    final List<BiFunction> factories = 
      List.of(QuadraticLagrange::interpolate);
    final Iterable<Function> functions = Iterables.concat(
        
      quadratics, affines, constants);
    for (final BiFunction factory : factories) {
      for (final Function f : functions) {
        //System.out.println();
        //System.out.println(f);
        for (final double[][] kn : quadraticKnots) {
          if (QuadraticLagrange.validKnots(kn)) {
            //System.out.println(Arrays.toString(kn));
            exact(f,factory,kn,quadraticTestPts,support,
              6.0e5,2.0e8,8.0e7); } } } } }

  @SuppressWarnings({ "static-method" })
  @Test
  public final void generalTests () {
    final Domain support = expand(quadraticTestPts);
    final List<BiFunction> factories = 
      List.of(QuadraticLagrange::interpolate);
    final Iterable<Function> functions = Iterables.concat(
      cubics, otherFns);
    for (final BiFunction factory : factories) {
      for (final Function f : functions) {
        for (final double[][] kn : quadraticKnots) {
          if (QuadraticLagrange.validKnots(kn)) {
            general(f,factory,kn,support,
              2.0e0,1.0e0,2.0e6); } } } } }

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
