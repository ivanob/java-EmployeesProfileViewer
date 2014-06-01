package test.evaluation.operations.scalar;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import controller.competences.Competence;
import controller.evaluations.Evaluation;
import controller.evaluations.Source;

import rs.ac.ns.ftn.tmd.datatypes.CrispSet;
import rs.ac.ns.ftn.tmd.datatypes.FuzzySet;
import rs.ac.ns.ftn.tmd.datatypes.ScalarNumber;

public class ScalarDistrustTest {
	private ScalarNumber sc1, sc2;
	private Source source1;
	private Evaluation eval1, eval2;
	private Competence comp1;
	
	@Before 
	public void setUp(){
		sc1 = new ScalarNumber(5);
		sc2 = new ScalarNumber(0);
		source1 = new Source("source1", 0.5f, 0.5f);
		comp1 = new Competence("comp1", 0);
	}
	
	@Test
	public void evalScalarDistrust1(){
		eval1 = new Evaluation(comp1, source1, sc1);
		FuzzySet res = eval1.getMark();
		assertTrue(res instanceof CrispSet);
		CrispSet resCrisp = (CrispSet)res;
		//Valores limite
		assertTrue(resCrisp.getLeftBoundary()==2.5);
		assertTrue(resCrisp.getRightBoundary()==7.5);
		assertTrue(resCrisp.getModalValue()==5);
		//Valores internos
		assertTrue(resCrisp.getMembershipValue(3)==1);
		assertTrue(resCrisp.getMembershipValue(4)==1);
		assertTrue(resCrisp.getMembershipValue(5)==1);
		assertTrue(resCrisp.getMembershipValue(6)==1);
		assertTrue(resCrisp.getMembershipValue(7)==1);
		//Valores externos
		assertTrue(resCrisp.getMembershipValue(2)==0);
		assertTrue(resCrisp.getMembershipValue(8)==0);
	}
	
	@Test
	public void evalScalarDistrust2(){
		eval2 = new Evaluation(comp1, source1, sc2);
		FuzzySet res = eval2.getMark();
		assertTrue(res instanceof CrispSet);
		CrispSet resCrisp = (CrispSet)res;
		//Valores limite
		assertTrue(resCrisp.getLeftBoundary()==0);
		assertTrue(resCrisp.getRightBoundary()==5);
		assertTrue(resCrisp.getModalValue()==2.5);
		//Valores internos
		assertTrue(resCrisp.getMembershipValue(1)==1);
		assertTrue(resCrisp.getMembershipValue(2)==1);
		assertTrue(resCrisp.getMembershipValue(3)==1);
		assertTrue(resCrisp.getMembershipValue(4)==1);
		assertTrue(resCrisp.getMembershipValue(5)==1);
		//Valores externos
		assertTrue(resCrisp.getMembershipValue(6)==0);
		assertTrue(resCrisp.getMembershipValue(10)==0);
	}
}
