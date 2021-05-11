package sicpplus.java.test.polynomial;

import org.junit.jupiter.api.Test;

import sicpplus.java.polynomial.MonomialBigFloat;
import sicpplus.java.polynomial.MonomialDouble;
import sicpplus.java.polynomial.MonomialDoubleBF;
import sicpplus.java.polynomial.MonomialDoubleRF;
import sicpplus.java.polynomial.MonomialRationalFloat;
import sicpplus.java.test.Common;

//----------------------------------------------------------------
/** Test desired properties of monimial calculators.
 * <p>
 * <pre>
 * mvn -q -Dtest=xfp/java/test/polynomial/MonomialTest test > MT.txt
 * </pre>
 *
 * @author palisades dot lakes at gmail dot com
 * @version 2021-01-27
 */

public final class MonomialTest {

  @SuppressWarnings({ "static-method" })
  @Test
  public final void dbf () { 
    Common.monomial(MonomialDoubleBF.class); } 

  @SuppressWarnings({ "static-method" })
  @Test
  public final void d () { 
    Common.monomial(MonomialDouble.class); } 

  @SuppressWarnings({ "static-method" })
  @Test
  public final void drf () { 
    Common.monomial(MonomialDoubleRF.class); } 

  @SuppressWarnings({ "static-method" })
  @Test
  public final void rf () { 
    Common.monomial(MonomialRationalFloat.class); } 

  @SuppressWarnings({ "static-method" })
  @Test
  public final void bf () { 
    Common.monomial(MonomialBigFloat.class); } 

  @SuppressWarnings({ "static-method" })
  @Test
  public final void er () { 
    Common.monomial(MonomialERational.class); } 

  //--------------------------------------------------------------
}
//--------------------------------------------------------------
