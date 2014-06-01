package test.evaluation.operations.fuzzy;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import rs.ac.ns.ftn.tmd.datatypes.DecomposedFuzzyNumber;
import rs.ac.ns.ftn.tmd.datatypes.FuzzySet;
import controller.competences.Competence;
import controller.evaluations.Evaluation;
import controller.evaluations.Source;

public class DecomposedFuzzyDistrustTest {
	private DecomposedFuzzyNumber dfn1, dfn2;
	private Source source1, source2, source3;
	private Evaluation eval1, eval2;
	private Competence comp1;
	
	@Before 
	public void setUp(){
		dfn1 = new DecomposedFuzzyNumber(5,1,1,FuzzySet.FUNCTION_EXPONENTIAL,FuzzySet.FUNCTION_EXPONENTIAL,100);
		dfn2 = new DecomposedFuzzyNumber(2,2,1,FuzzySet.FUNCTION_EXPONENTIAL,FuzzySet.FUNCTION_EXPONENTIAL,100);
		source1 = new Source("source1", 0.5f, 0.5f);
		source2 = new Source("source2", 1f, 1f);
		source3 = new Source("source3", 0f, 0f);
		comp1 = new Competence("comp1", 0);
	}
	
	@Test
	public void evalScalarDistrust1(){
		eval1 = new Evaluation(comp1, source1, dfn1);
		FuzzySet res = eval1.getMark();
		assertTrue(res instanceof DecomposedFuzzyNumber);
		DecomposedFuzzyNumber resFuzzy = (DecomposedFuzzyNumber)res;
		//Valores limite
		assertTrue(resFuzzy.getLeftBoundary()==2);
		assertTrue(resFuzzy.getRightBoundary()==8);
		assertTrue(resFuzzy.getModalValue()==5);
	}
	
	@Test
	public void evalScalarDistrust2(){
		eval1 = new Evaluation(comp1, source1, dfn2);
		FuzzySet res = eval1.getMark();
		assertTrue(res instanceof DecomposedFuzzyNumber);
		DecomposedFuzzyNumber resFuzzy = (DecomposedFuzzyNumber)res;
		//Valores limite
		assertTrue(resFuzzy.getLeftBoundary()==0);
		assertTrue(resFuzzy.getRightBoundary()==6.5);
		assertTrue(resFuzzy.getModalValue()==2);
	}
	
	@Test
	public void evalScalarDistrust3(){
		eval1 = new Evaluation(comp1, source2, dfn1);
		FuzzySet res = eval1.getMark();
		assertTrue(res instanceof DecomposedFuzzyNumber);
		DecomposedFuzzyNumber resFuzzy = (DecomposedFuzzyNumber)res;
		//Valores limite
		assertTrue(resFuzzy.getLeftBoundary()==0);
		assertTrue(resFuzzy.getRightBoundary()==10);
		assertTrue(resFuzzy.getModalValue()==5);
	}
	
	@Test
	public void evalScalarDistrust4(){
		eval1 = new Evaluation(comp1, source3, dfn1);
		FuzzySet res = eval1.getMark();
		assertTrue(res instanceof DecomposedFuzzyNumber);
		DecomposedFuzzyNumber resFuzzy = (DecomposedFuzzyNumber)res;
		//Valores limite
		assertTrue(resFuzzy.getLeftBoundary()==4);
		assertTrue(resFuzzy.getRightBoundary()==6);
		assertTrue(resFuzzy.getModalValue()==5);
	}
}
