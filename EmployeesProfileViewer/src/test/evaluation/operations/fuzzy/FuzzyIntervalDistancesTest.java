package test.evaluation.operations.fuzzy;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import rs.ac.ns.ftn.tmd.datatypes.CrispSet;
import rs.ac.ns.ftn.tmd.datatypes.DecomposedFuzzyNumber;
import rs.ac.ns.ftn.tmd.datatypes.FuzzyInterval;
import rs.ac.ns.ftn.tmd.datatypes.FuzzySet;
import rs.ac.ns.ftn.tmd.datatypes.ScalarNumber;

public class FuzzyIntervalDistancesTest{
	private FuzzyInterval fi1, fi2, fi3;
	private CrispSet cs1;
	private DecomposedFuzzyNumber dfn1;
	private ScalarNumber sc1, sc2;
	
	@Before 
	public void setUp(){
		fi1 = new FuzzyInterval(6,8,1,1,FuzzySet.FUNCTION_LINEAR, FuzzySet.FUNCTION_LINEAR,100);
		fi2 = new FuzzyInterval(2,3,1,1,FuzzySet.FUNCTION_LINEAR, FuzzySet.FUNCTION_LINEAR,100);
		fi3 = new FuzzyInterval(6,7,1,1,FuzzySet.FUNCTION_LINEAR, FuzzySet.FUNCTION_LINEAR,100);
		dfn1 = new DecomposedFuzzyNumber(3,1,1,FuzzySet.FUNCTION_LINEAR,FuzzySet.FUNCTION_LINEAR,100);
		cs1 = new CrispSet(1,4);
		sc1 = new ScalarNumber(2);
		sc2 = new ScalarNumber(6);
	}
	
	@Test
	public void evalFuzzyIntDistanceWithScalar1(){
		FuzzySet fuzzyres = fi1.distance(sc1);
		assertTrue(fuzzyres instanceof FuzzyInterval);
		FuzzyInterval fuzzIntRes = (FuzzyInterval)fuzzyres;
		//Valores picos
		assertTrue(fuzzIntRes.getMembershipValue(3)==0);
		assertTrue(fuzzIntRes.getMembershipValue(7)==0);
		assertTrue(fuzzIntRes.getMembershipValue(4)==1);
		assertTrue(fuzzIntRes.getMembershipValue(6)==1);
		//Valores intermedios
		assertTrue(fuzzIntRes.getMembershipValue(3.5)==0.5);
		assertTrue(fuzzIntRes.getMembershipValue(6.5)==0.5);
	}
	
	@Test
	public void evalFuzzyIntDistanceWithScalarOverlaped2(){
		FuzzySet fuzzyres = fi1.distance(sc2);
		assertTrue(fuzzyres instanceof FuzzyInterval);
		FuzzyInterval fuzzIntRes = (FuzzyInterval)fuzzyres;
		//Valores picos
		assertTrue(fuzzIntRes.getMembershipValue(0)==1);
		assertTrue(fuzzIntRes.getMembershipValue(2)==1);
		assertTrue(fuzzIntRes.getMembershipValue(3)==0);
		//Valores intermedios
	}
	
	@Test
	public void evalFuzzyIntDistanceWithInterval1(){
		FuzzySet fuzzyres = fi1.distance(cs1);
		assertTrue(fuzzyres instanceof FuzzyInterval);
		FuzzyInterval fuzzIntRes = (FuzzyInterval)fuzzyres;
		//Valores picos
		assertTrue(fuzzIntRes.getMembershipValue(1)==0);
		assertTrue(fuzzIntRes.getMembershipValue(8)==0);
		assertTrue(fuzzIntRes.getMembershipValue(2)==1);
		assertTrue(fuzzIntRes.getMembershipValue(7)==1);
		//Valores intermedios
		assertTrue(fuzzIntRes.getMembershipValue(1.25)==0.25);
		assertTrue(fuzzIntRes.getMembershipValue(1.5)==0.5);
		assertTrue(fuzzIntRes.getMembershipValue(1.75)==0.75);
		assertTrue(fuzzIntRes.getMembershipValue(7.25)==0.75);
		assertTrue(fuzzIntRes.getMembershipValue(7.5)==0.5);
		assertTrue(fuzzIntRes.getMembershipValue(7.75)==0.25);
		assertTrue(fuzzIntRes.getMembershipValue(2.05)==1);
		assertTrue(fuzzIntRes.getMembershipValue(4)==1);
		assertTrue(fuzzIntRes.getMembershipValue(5)==1);
		assertTrue(fuzzIntRes.getMembershipValue(6)==1);
		assertTrue(fuzzIntRes.getMembershipValue(6.5)==1);
		assertTrue(fuzzIntRes.getMembershipValue(7)==1);
	}
	
	@Test
	public void evalFuzzyIntDistanceWithCrisp1(){
		FuzzySet fuzzyres = fi2.distance(fi3);
		assertTrue(fuzzyres instanceof FuzzyInterval);
		FuzzyInterval fuzzIntRes = (FuzzyInterval)fuzzyres;
		//Valores picos
		assertTrue(fuzzIntRes.getMembershipValue(1)==0);
		assertTrue(fuzzIntRes.getMembershipValue(3)==1);
		assertTrue(fuzzIntRes.getMembershipValue(5)==1);
		assertTrue(fuzzIntRes.getMembershipValue(7)==0);
		//Valores intermedio
		assertTrue(fuzzIntRes.getMembershipValue(2)==0.5);
		assertTrue(fuzzIntRes.getMembershipValue(1.5)==0.25);
		assertTrue(fuzzIntRes.getMembershipValue(2.5)==0.75);
		assertTrue(fuzzIntRes.getMembershipValue(5.5)==0.75);
		assertTrue(fuzzIntRes.getMembershipValue(6)==0.5);
		assertTrue(fuzzIntRes.getMembershipValue(6.5)==0.25);
		
	}
	
	@Test
	public void evalFuzzyIntDistanceWithDecomposed1(){
		FuzzySet fuzzyres = fi3.distance(dfn1);
		assertTrue(fuzzyres instanceof FuzzyInterval);
		FuzzyInterval fuzzIntRes = (FuzzyInterval)fuzzyres;
		//Valores picos
		assertTrue(fuzzIntRes.getMembershipValue(3)==1);
		assertTrue(fuzzIntRes.getMembershipValue(4)==1);
		assertTrue(fuzzIntRes.getMembershipValue(1)==0);
		assertTrue(fuzzIntRes.getMembershipValue(6)==0);
		//Valores intermedio
		assertTrue(fuzzIntRes.getMembershipValue(2)==0.5);
		assertTrue(fuzzIntRes.getMembershipValue(5)==0.5);
		assertTrue(fuzzIntRes.getMembershipValue(1.5)==0.25);
		assertTrue(fuzzIntRes.getMembershipValue(2.5)==0.75);
		assertTrue(fuzzIntRes.getMembershipValue(4.5)==0.75);
		assertTrue(fuzzIntRes.getMembershipValue(5.5)==0.25);		
	}
}
