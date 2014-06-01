package test.evaluation.operations.fuzzy;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import controller.competences.Competence;
import controller.employees.Employee;
import controller.evaluations.Evaluation;
import controller.evaluations.Source;


import rs.ac.ns.ftn.tmd.datatypes.CrispSet;
import rs.ac.ns.ftn.tmd.datatypes.DecomposedFuzzyNumber;
import rs.ac.ns.ftn.tmd.datatypes.FuzzyInterval;
import rs.ac.ns.ftn.tmd.datatypes.FuzzySet;
import rs.ac.ns.ftn.tmd.datatypes.ScalarNumber;

public class DecomposedFuzzyMeansTest {
	private DecomposedFuzzyNumber f1,f2;
	private ScalarNumber s1;
	private CrispSet cs1;
	private Employee empl;
	private Source source;
	
	@Before 
	public void setUp(){
		f1 = new DecomposedFuzzyNumber(2,1,1,
				FuzzySet.FUNCTION_LINEAR,
				FuzzySet.FUNCTION_LINEAR, 100);
		f2 = new DecomposedFuzzyNumber(2,1,1,
				FuzzySet.FUNCTION_LINEAR,
				FuzzySet.FUNCTION_LINEAR, 100);
		s1 = new ScalarNumber(4);
		cs1 = new CrispSet(2,5);
		source=new Source("max", 0,0);
	}
	
	@Test
	public void evalFuzzyNumberMeans1(){
		empl = new Employee("",1);
		Competence c = new Competence("", 0);
		empl.addEvaluation(new Evaluation(c,source,f1));
		empl.addEvaluation(new Evaluation(c,source,s1));
		FuzzySet res = empl.getEvaluation(0).getMark();
		assertTrue(res instanceof DecomposedFuzzyNumber);
		assertTrue(res.getLeftBoundary()==2.5);
		assertTrue(res.getRightBoundary()==3.5);
		//Valores intermedios
		assertTrue(res.getMembershipValue(2.75)==0.5);
		assertTrue(res.getMembershipValue(3.25)==0.5);
	}
	
	@Test
	public void evalFuzzyNumberMeans2(){
		empl = new Employee("",1);
		Competence c = new Competence("", 0);
		empl.addEvaluation(new Evaluation(c,source,f1));
		empl.addEvaluation(new Evaluation(c,source,cs1));
		FuzzySet res = empl.getEvaluation(0).getMark();
		assertTrue(res instanceof FuzzyInterval);
		FuzzyInterval fuzzyInt = (FuzzyInterval)res;
		//Valores extremos
		assertTrue(fuzzyInt.getLeftBoundary()==1.5);
		assertTrue(fuzzyInt.getRightBoundary()==4);
		assertTrue(fuzzyInt.getModalLeft()==2);
		assertTrue(fuzzyInt.getModalRight()==3.5);
		//Valores intermedios
		assertTrue(res.getMembershipValue(3)==1);
	}
	
	@Test
	public void evalFuzzyNumberMeans3(){
		empl = new Employee("",1);
		Competence c = new Competence("", 0);
		empl.addEvaluation(new Evaluation(c,source,f1));
		empl.addEvaluation(new Evaluation(c,source,cs1));
		empl.addEvaluation(new Evaluation(c,source,s1));
		FuzzySet res = empl.getEvaluation(0).getMark();
		assertTrue(res instanceof FuzzyInterval);
		FuzzyInterval fuzzyInt = (FuzzyInterval)res;
		//Valores extremos
		assertTrue(fuzzyInt.getLeftBoundary()>2.33 && fuzzyInt.getLeftBoundary()<2.34);
		assertTrue(fuzzyInt.getModalLeft()>2.66 && fuzzyInt.getModalLeft()<2.67);
		assertTrue(fuzzyInt.getModalRight()>3.66 && fuzzyInt.getModalRight()<3.67);
		assertTrue(fuzzyInt.getRightBoundary()==4);
		//Valores intermedios
		assertTrue(res.getMembershipValue(2.7)==1);
		assertTrue(res.getMembershipValue(3)==1);
		assertTrue(res.getMembershipValue(3.5)==1);
		//Valores extremos
		assertTrue(res.getMembershipValue(2)==0);
		assertTrue(res.getMembershipValue(5)==0);
		assertTrue(res.getMembershipValue(1)==0);
	}
}
