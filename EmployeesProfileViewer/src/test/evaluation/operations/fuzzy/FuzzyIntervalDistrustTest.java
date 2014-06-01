package test.evaluation.operations.fuzzy;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import rs.ac.ns.ftn.tmd.datatypes.CrispSet;
import rs.ac.ns.ftn.tmd.datatypes.FuzzyInterval;
import rs.ac.ns.ftn.tmd.datatypes.FuzzySet;
import controller.competences.Competence;
import controller.evaluations.Evaluation;
import controller.evaluations.Source;

public class FuzzyIntervalDistrustTest {

	private FuzzyInterval fuzzy1, fuzzy2, fuzzy3;
	private Source source1, source2;
	private Evaluation eval1, eval2;
	private Competence comp1;
	
	@Before 
	public void setUp(){
		fuzzy1 = new FuzzyInterval(3,4,1,1,FuzzySet.FUNCTION_EXPONENTIAL,
				FuzzySet.FUNCTION_EXPONENTIAL, 100);
		fuzzy2 = new FuzzyInterval(1,5,1,1,FuzzySet.FUNCTION_EXPONENTIAL,
				FuzzySet.FUNCTION_EXPONENTIAL, 100);
		source1 = new Source("source1", 0.5f, 0.5f);
		source2 = new Source("source1", 0f, 0f);
		comp1 = new Competence("comp1", 0);
	}
	
	@Test
	public void evalCrispDistrust1(){
		eval1 = new Evaluation(comp1, source1, fuzzy1);
		FuzzySet res = eval1.getMark();
		assertTrue(res instanceof FuzzyInterval);
		FuzzyInterval resFuzzy = (FuzzyInterval)res;
		//Valores limite
		assertTrue(resFuzzy.getLeftBoundary()==1);
		assertTrue(resFuzzy.getRightBoundary()==7.5);
		assertTrue(resFuzzy.getModalValue()==4.25);
		//Valores intermedios
		//Base superior
		assertTrue(resFuzzy.getMembershipValue(1.5)==1);
		assertTrue(resFuzzy.getMembershipValue(2)==1);
		assertTrue(resFuzzy.getMembershipValue(3)==1);
		assertTrue(resFuzzy.getMembershipValue(4)==1);
		assertTrue(resFuzzy.getMembershipValue(5)==1);
		assertTrue(resFuzzy.getMembershipValue(6)==1);
		assertTrue(resFuzzy.getMembershipValue(7)==1);
		//Extremos
		assertTrue(resFuzzy.getModalLeft()==1.5);
		assertTrue(resFuzzy.getModalRight()==7);
	}
	
	@Test
	public void evalCrispDistrust2(){
		eval1 = new Evaluation(comp1, source1, fuzzy2);
		FuzzySet res = eval1.getMark();
		assertTrue(res instanceof FuzzyInterval);
		FuzzyInterval resFuzzy = (FuzzyInterval)res;
		//Valores limite
		assertTrue(resFuzzy.getLeftBoundary()==0);
		assertTrue(resFuzzy.getRightBoundary()==8);
		assertTrue(resFuzzy.getModalValue()==4);
		//Valores intermedios
		//Base superior
		assertTrue(resFuzzy.getMembershipValue(0.5)==1);
		assertTrue(resFuzzy.getMembershipValue(1)==1);
		assertTrue(resFuzzy.getMembershipValue(3)==1);
		assertTrue(resFuzzy.getMembershipValue(4)==1);
		assertTrue(resFuzzy.getMembershipValue(5)==1);
		assertTrue(resFuzzy.getMembershipValue(6)==1);
		assertTrue(resFuzzy.getMembershipValue(7)==1);
		assertTrue(resFuzzy.getMembershipValue(7.5)==1);
		//Extremos
		assertTrue(resFuzzy.getModalLeft()==0.5);
		assertTrue(resFuzzy.getModalRight()==7.5);
	}
	
	@Test
	public void evalCrispDistrust3(){
		eval1 = new Evaluation(comp1, source2, fuzzy1);
		FuzzySet res = eval1.getMark();
		assertTrue(res instanceof FuzzyInterval);
		FuzzyInterval resFuzzy = (FuzzyInterval)res;
		//Valores limite
		assertTrue(resFuzzy.getLeftBoundary()==2);
		assertTrue(resFuzzy.getRightBoundary()==5);
		assertTrue(resFuzzy.getModalValue()==3.5);
		//Valores intermedios
		//Base superior
		assertTrue(resFuzzy.getMembershipValue(3)==1);
		assertTrue(resFuzzy.getMembershipValue(3.1)==1);
		assertTrue(resFuzzy.getMembershipValue(3.5)==1);
		assertTrue(resFuzzy.getMembershipValue(3.9)==1);
		assertTrue(resFuzzy.getMembershipValue(4)==1);
		//Extremos
		assertTrue(resFuzzy.getModalLeft()==3);
		assertTrue(resFuzzy.getModalRight()==4);
	}
}
