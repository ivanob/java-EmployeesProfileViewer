package test.evaluation.operations.scalar;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import rs.ac.ns.ftn.tmd.datatypes.CrispSet;
import rs.ac.ns.ftn.tmd.datatypes.FuzzySet;
import rs.ac.ns.ftn.tmd.datatypes.ScalarNumber;

public class ScalarDistancesTest {

	private ScalarNumber scalar1, scalar2, s3;
	private CrispSet cs1;
	
	@Before 
	public void setUp(){
		scalar1 = new ScalarNumber(0);
		scalar2 = new ScalarNumber(3);
		s3 = new ScalarNumber(5);
		cs1 = new CrispSet(2,10);
	}
	
	/* Este test evalua la operacion de distancia entre
	 * 2 objetos Scalar */
	@Test
	public void evalScalarDistanceWithScalar1(){
		FuzzySet res = (CrispSet)scalar1.distance(scalar2);
		assertTrue(res instanceof ScalarNumber);
		ScalarNumber resScalar = (ScalarNumber)res;
		//Puntos limite		
		assertTrue(resScalar.getModalValue()==3);
		assertTrue(resScalar.getMembershipValue(2)==0);
		assertTrue(resScalar.getMembershipValue(4)==0);
	}
	
	/* Este test evalua la operacion de distancia entre
	 * 2 objetos Scalar */
	@Test
	public void evalScalarDistanceWithScalar2(){
		FuzzySet res = s3.distance(scalar2);
		assertTrue(res instanceof ScalarNumber);
		ScalarNumber resScalar = (ScalarNumber)res;
		//Puntos limite		
		assertTrue(resScalar.getModalValue()==2);
		assertTrue(resScalar.getMembershipValue(3)==0);
		assertTrue(resScalar.getMembershipValue(1)==0);
	}
	
	/* Este test evalua la operacion de distancia entre
	 * 2 objetos CrispSet */
	@Test
	public void evalScalarDistanceWithCrisp1(){
		FuzzySet res = (CrispSet)scalar2.distance(cs1);
		assertTrue(res instanceof CrispSet);
		CrispSet resCrisp = (CrispSet)res;
		//Puntos limite		
		assertTrue(resCrisp.getLeftBoundary()==0);
		assertTrue(resCrisp.getRightBoundary()==7);
		assertTrue(resCrisp.getMembershipValue(0)==1);
		//Valores extremos
		assertTrue(resCrisp.getMembershipValue(8)==0);
		assertTrue(resCrisp.getMembershipValue(9)==0);
		assertTrue(resCrisp.getMembershipValue(10)==0);
	}
}
