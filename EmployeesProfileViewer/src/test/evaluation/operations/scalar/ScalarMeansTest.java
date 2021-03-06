package test.evaluation.operations.scalar;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import controller.competences.Competence;
import controller.employees.Employee;
import controller.evaluations.Evaluation;
import controller.evaluations.Source;


import rs.ac.ns.ftn.tmd.datatypes.CrispSet;
import rs.ac.ns.ftn.tmd.datatypes.FuzzyInterval;
import rs.ac.ns.ftn.tmd.datatypes.FuzzySet;
import rs.ac.ns.ftn.tmd.datatypes.ScalarNumber;

public class ScalarMeansTest {
	private ScalarNumber s1, s2, s3, s4, s5;
	private CrispSet cs1;
	private Employee empl;
	private FuzzyInterval fi1;
	private Source source;
	
	@Before 
	public void setUp(){
		s1 = new ScalarNumber(1);
		s2 = new ScalarNumber(4);
		s3 = new ScalarNumber(10);
		s4 = new ScalarNumber(0);
		s5 = new ScalarNumber(0);
		cs1= new CrispSet(0,4);
		fi1=new FuzzyInterval(6,7,1,1,FuzzySet.FUNCTION_LINEAR, FuzzySet.FUNCTION_LINEAR,100);
		source=new Source("max", 0,0);
	}
	
	@Test
	public void evalScalarMean1(){
		empl = new Employee("",1);
		Competence c = new Competence("", 0);
		empl.addEvaluation(new Evaluation(c,source,s1));
		empl.addEvaluation(new Evaluation(c,source,s2));
		empl.addEvaluation(new Evaluation(c,source,s3));
		FuzzySet res = empl.getEvaluation(0).getMark();
		assertTrue(res instanceof ScalarNumber);
		//Puntos limite		
		assertTrue(res.getModalValue()==5);
	}
	
	@Test
	public void evalScalarMean2(){
		empl = new Employee("",1);
		Competence c = new Competence("", 0);
		empl.addEvaluation(new Evaluation(c,source,s4));
		empl.addEvaluation(new Evaluation(c,source,s5));
		FuzzySet res = empl.getEvaluation(0).getMark();
		assertTrue(res instanceof ScalarNumber);
		//Puntos limite		
		assertTrue(res.getModalValue()==0);
	}
	
	@Test
	public void evalScalarMean3(){
		empl = new Employee("",1);
		Competence c = new Competence("", 0);
		empl.addEvaluation(new Evaluation(c,source,s2));
		empl.addEvaluation(new Evaluation(c,source,cs1));
		FuzzySet mean = empl.getEvaluation(0).getMark();
		assertTrue(mean instanceof CrispSet);
		CrispSet res = (CrispSet)mean;
		//Puntos limite		
		assertTrue(res.getMembershipValue(2)==1);
		assertTrue(res.getMembershipValue(4)==1);
		assertTrue(res.getMembershipValue(3)==1);
		assertTrue(res.getMembershipValue(1)==0);
		assertTrue(res.getMembershipValue(5)==0);
	}
	
	@Test
	public void evalScalarMean4(){
		empl = new Employee("",1);
		Competence c = new Competence("", 0);
		empl.addEvaluation(new Evaluation(c,source,s3));
		empl.addEvaluation(new Evaluation(c,source,fi1));
		FuzzySet mean = empl.getEvaluation(0).getMark();
		assertTrue(mean instanceof FuzzyInterval);
		FuzzyInterval res = (FuzzyInterval)mean;
		//Puntos limite		
		assertTrue(res.getMembershipValue(7.5)==0);
		assertTrue(res.getMembershipValue(9)==0);
		assertTrue(res.getMembershipValue(8)==1);
		assertTrue(res.getMembershipValue(8.5)==1);
		assertTrue(res.getMembershipValue(8.25)==1);
	}
}
