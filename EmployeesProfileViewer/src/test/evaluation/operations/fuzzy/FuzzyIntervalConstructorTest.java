package test.evaluation.operations.fuzzy;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import rs.ac.ns.ftn.tmd.datatypes.FuzzyInterval;
import rs.ac.ns.ftn.tmd.datatypes.FuzzySet;

public class FuzzyIntervalConstructorTest {

	private FuzzyInterval fint1;
	
	@Before 
	public void setUp(){
		fint1 = new FuzzyInterval(3,5,2,1,FuzzySet.FUNCTION_LINEAR,
				FuzzySet.FUNCTION_LINEAR,100);
	}
	
	@Test
	public void constructionFuzzyInterval1(){
		assertTrue(fint1.getMembershipValue(0)==0);
		assertTrue(fint1.getMembershipValue(1)==0);
		assertTrue(fint1.getMembershipValue(1.5)==0.25);
		assertTrue(fint1.getMembershipValue(2)==0.5);
		assertTrue(fint1.getMembershipValue(2.5)==0.75);
		assertTrue(fint1.getMembershipValue(3)==1);
		assertTrue(fint1.getMembershipValue(4)==1);
		assertTrue(fint1.getMembershipValue(5)==1);
		assertTrue(fint1.getMembershipValue(5.25)==0.75);
		assertTrue(fint1.getMembershipValue(5.5)==0.5);
		assertTrue(fint1.getMembershipValue(5.75)==0.25);
		assertTrue(fint1.getMembershipValue(6)==0);
		assertTrue(fint1.getMembershipValue(7)==0);
	}
}
