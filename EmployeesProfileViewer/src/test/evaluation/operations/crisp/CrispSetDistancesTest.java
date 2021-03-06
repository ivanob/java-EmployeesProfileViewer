package test.evaluation.operations.crisp;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import rs.ac.ns.ftn.tmd.datatypes.CrispSet;
import rs.ac.ns.ftn.tmd.datatypes.DecomposedFuzzyNumber;
import rs.ac.ns.ftn.tmd.datatypes.FuzzyInterval;
import rs.ac.ns.ftn.tmd.datatypes.FuzzySet;

public class CrispSetDistancesTest {
	private CrispSet cs1, cs2, cs3, cs4, cs5, cs6, cs7, cs8;
	private DecomposedFuzzyNumber dfn1, dfn2, dfn3, dfn4, dfn5;
	private FuzzyInterval fi1;
	
	@Before 
	public void setUp(){
		cs1 = new CrispSet(1,3);
		cs2 = new CrispSet(6,7);
		cs3 = new CrispSet(3,5);
		cs4 = new CrispSet(2,4);
		cs5 = new CrispSet(3,6);
		cs6 = new CrispSet(0,2);
		cs7 = new CrispSet(0,10);
		cs8 = new CrispSet(9,10);
		dfn1 = new DecomposedFuzzyNumber(6,1,1,FuzzySet.FUNCTION_LINEAR, FuzzySet.FUNCTION_LINEAR, 100);
		dfn2 = new DecomposedFuzzyNumber(4,1,1,FuzzySet.FUNCTION_LINEAR, FuzzySet.FUNCTION_LINEAR, 100);
		dfn3 = new DecomposedFuzzyNumber(3,1,1,FuzzySet.FUNCTION_LINEAR, FuzzySet.FUNCTION_LINEAR, 100);
		dfn4 = new DecomposedFuzzyNumber(4,2,2,FuzzySet.FUNCTION_LINEAR, FuzzySet.FUNCTION_LINEAR, 100);
		dfn5 = new DecomposedFuzzyNumber(3,2,1,FuzzySet.FUNCTION_LINEAR, FuzzySet.FUNCTION_LINEAR, 100);
		fi1 = new FuzzyInterval(7,8,1,1,FuzzySet.FUNCTION_LINEAR,FuzzySet.FUNCTION_LINEAR,100);
	}
	
	/* Este test evalua la operacion de distancia entre
	 * 2 objetos CrispSet */
	@Test
	public void evalCrispDistanceWithCrisp1(){
		FuzzySet res = (CrispSet)cs1.distance(cs2);
		assertTrue(res instanceof CrispSet);
		CrispSet resCrisp = (CrispSet)res;
		//Puntos limite		
		assertTrue(resCrisp.getLeftBoundary()==3);
		assertTrue(resCrisp.getRightBoundary()==6);
		//Valores extremos
		assertTrue(resCrisp.getMembershipValue(0)==0);
		assertTrue(resCrisp.getMembershipValue(3)==1);
		assertTrue(resCrisp.getMembershipValue(4)==1);
		assertTrue(resCrisp.getMembershipValue(4.5)==1);
		assertTrue(resCrisp.getMembershipValue(5)==1);
		assertTrue(resCrisp.getMembershipValue(6)==1);
		assertTrue(resCrisp.getMembershipValue(6.01)==0);
	}
	
	@Test
	public void evalCrispDistanceWithFuzzyNumber1(){
		FuzzySet res = cs1.distance(dfn1);
		assertTrue(res instanceof FuzzyInterval);
		FuzzyInterval fuzzyRes = (FuzzyInterval)res;
		//Puntos limite		
		assertTrue(fuzzyRes.getMembershipValue(3)==1);
		assertTrue(fuzzyRes.getMembershipValue(4)==1);
		assertTrue(fuzzyRes.getMembershipValue(5)==1);
		//Valores extremos
		assertTrue(fuzzyRes.getMembershipValue(2)==0);
		assertTrue(fuzzyRes.getMembershipValue(6)==0);
		assertTrue(fuzzyRes.getMembershipValue(2.5)==0.5);
		assertTrue(fuzzyRes.getMembershipValue(5.5)==0.5);
		//Valores exteriores
		assertTrue(fuzzyRes.getMembershipValue(0)==0);
		assertTrue(fuzzyRes.getMembershipValue(7)==0);
		assertTrue(fuzzyRes.getMembershipValue(-1)==0);
		assertTrue(fuzzyRes.getMembershipValue(10)==0);
	}

	@Test
	public void evalCrispDistanceWithFuzzyInt1(){
		FuzzySet res = cs3.distance(fi1);
		assertTrue(res instanceof FuzzyInterval);
		FuzzyInterval fuzzyRes = (FuzzyInterval)res;
		//Puntos limite		
		assertTrue(fuzzyRes.getMembershipValue(2)==1);
		assertTrue(fuzzyRes.getMembershipValue(5)==1);
		assertTrue(fuzzyRes.getMembershipValue(1)==0);
		assertTrue(fuzzyRes.getMembershipValue(6)==0);
		//Valores extremos
		assertTrue(fuzzyRes.getMembershipValue(1.25)==0.25);
		assertTrue(fuzzyRes.getMembershipValue(1.5)==0.5);
		assertTrue(fuzzyRes.getMembershipValue(1.75)==0.75);
		assertTrue(fuzzyRes.getMembershipValue(5.25)==0.75);
		assertTrue(fuzzyRes.getMembershipValue(5.5)==0.5);
		assertTrue(fuzzyRes.getMembershipValue(5.75)==0.25);
		//Valores exteriores
		assertTrue(fuzzyRes.getMembershipValue(0)==0);
		assertTrue(fuzzyRes.getMembershipValue(7)==0);
		assertTrue(fuzzyRes.getMembershipValue(10)==0);
		assertTrue(fuzzyRes.getMembershipValue(-1)==0);
	}
	
	/* Test with overlap between crisp sets */
	@Test
	public void evalCrispDistanceWithCrispOverlaped1(){
		FuzzySet res = (CrispSet)cs4.distance(cs5);
		assertTrue(res instanceof CrispSet);
		CrispSet resCrisp = (CrispSet)res;
		//Puntos limite		
		assertTrue(resCrisp.getLeftBoundary()==0);
		assertTrue(resCrisp.getRightBoundary()==4);
		//Valores extremos
		assertTrue(resCrisp.getMembershipValue(0)==1);
		assertTrue(resCrisp.getMembershipValue(1)==1);
		assertTrue(resCrisp.getMembershipValue(2)==1);
		assertTrue(resCrisp.getMembershipValue(3)==1);
		assertTrue(resCrisp.getMembershipValue(4)==1);
		assertTrue(resCrisp.getMembershipValue(5)==0);
		assertTrue(resCrisp.getMembershipValue(6)==0);
		assertTrue(resCrisp.getMembershipValue(-1)==0);
	}
	
	/* Test with overlap between crisp set and fuzzy number */
	@Test
	public void evalCrispDistanceWithFuzzyNumberOverlap1(){
		FuzzySet res = cs4.distance(dfn2);
		assertTrue(res instanceof FuzzyInterval);
		FuzzyInterval fuzzyRes = (FuzzyInterval)res;
		//Puntos limite
		assertTrue(fuzzyRes.getMembershipValue(0)==1);
		assertTrue(fuzzyRes.getMembershipValue(1)==1);
		assertTrue(fuzzyRes.getMembershipValue(2)==1);
		//Valores extremos
		assertTrue(fuzzyRes.getMembershipValue(3)==0);
		//Valores exteriores
		assertTrue(fuzzyRes.getMembershipValue(3.1)==0);
		assertTrue(fuzzyRes.getMembershipValue(-1)==0);
	}
	
	/* Test with overlap between crisp set and fuzzy number */
	@Test
	public void evalCrispDistanceWithFuzzyNumberOverlap2(){
		FuzzySet res = cs4.distance(dfn3);
		assertTrue(res instanceof FuzzyInterval);
		FuzzyInterval fuzzyRes = (FuzzyInterval)res;
		//Puntos limite
		assertTrue(fuzzyRes.getMembershipValue(0)==1);
		assertTrue(fuzzyRes.getMembershipValue(1)==1);
		assertTrue(fuzzyRes.getMembershipValue(2)==0);
		//Valores exteriores
		assertTrue(fuzzyRes.getMembershipValue(3)==0);
		assertTrue(fuzzyRes.getMembershipValue(-1)==0);
	}
	
	/* Test with overlap between crisp set and fuzzy number */
	@Test
	public void evalCrispDistanceWithFuzzyNumberOverlap3(){
		FuzzySet res = cs1.distance(dfn4);
		assertTrue(res instanceof FuzzyInterval);
		FuzzyInterval fuzzyRes = (FuzzyInterval)res;
		//Puntos limite
		assertTrue(fuzzyRes.getMembershipValue(1)==1);
		assertTrue(fuzzyRes.getMembershipValue(3)==1);
		assertTrue(fuzzyRes.getMembershipValue(5)==0);
		//assertTrue(fuzzyRes.getMembershipValue(5)==0);
		//Valores exteriores
		assertTrue(fuzzyRes.getMembershipValue(6)==0);
		assertTrue(fuzzyRes.getMembershipValue(-1)==0);
	}
	
	/* Test with overlap between crisp set and fuzzy number */
	@Test
	public void evalCrispDistanceWithFuzzyNumberOverlap4(){
		FuzzySet res = cs6.distance(dfn5);
		assertTrue(res instanceof FuzzyInterval);
		FuzzyInterval fuzzyRes = (FuzzyInterval)res;
		//Puntos limite
		assertTrue(fuzzyRes.getMembershipValue(1)==1);
		assertTrue(fuzzyRes.getMembershipValue(2)==1);
		assertTrue(fuzzyRes.getMembershipValue(3)==1);
		//Valores exteriores
		assertTrue(fuzzyRes.getMembershipValue(0)==0.5);
		assertTrue(fuzzyRes.getMembershipValue(3.5)==0.5);
		assertTrue(fuzzyRes.getMembershipValue(4)==0);
	}
	
	/* Test with overlap between crisp set and fuzzy number */
	@Test
	public void evalCrispDistanceWithFuzzyNumberOverlap5(){
		FuzzySet res = cs7.distance(cs8);
		assertTrue(res instanceof CrispSet);
		CrispSet fuzzyRes = (CrispSet)res;
		//Puntos limite
		assertTrue(fuzzyRes.getMembershipValue(0)==1);
		assertTrue(fuzzyRes.getMembershipValue(5)==1);
		assertTrue(fuzzyRes.getMembershipValue(10)==1);
		//Valores exteriores
		assertTrue(fuzzyRes.getMembershipValue(-1)==0);
		assertTrue(fuzzyRes.getMembershipValue(11)==0);
		assertTrue(fuzzyRes.getMembershipValue(-5)==0);
	}
}
