package test.evaluation.operations.crisp;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import rs.ac.ns.ftn.tmd.datatypes.CrispSet;
import rs.ac.ns.ftn.tmd.datatypes.FuzzySet;
import controller.competences.Competence;
import controller.evaluations.Evaluation;
import controller.evaluations.Source;

public class CrispDistrustTest {
	private CrispSet crisp1, crisp2;
	private Source source1;
	private Evaluation eval1, eval2;
	private Competence comp1;
	
	@Before 
	public void setUp(){
		crisp1 = new CrispSet(4,6);
		crisp2 = new CrispSet(8,10);
		source1 = new Source("source1", 0.5f, 0.5f);
		comp1 = new Competence("comp1", 0);
	}
	
	@Test
	public void evalCrispDistrust1(){
		eval1 = new Evaluation(comp1, source1, crisp1);
		FuzzySet res = eval1.getMark();
		assertTrue(res instanceof CrispSet);
		CrispSet resCrisp = (CrispSet)res;
		//Valores limite
		assertTrue(resCrisp.getLeftBoundary()==2);
		assertTrue(resCrisp.getRightBoundary()==8);
		assertTrue(resCrisp.getModalValue()==5);
		//Valores intermedios
		assertTrue(resCrisp.getMembershipValue(3)==1);
		assertTrue(resCrisp.getMembershipValue(4)==1);
		assertTrue(resCrisp.getMembershipValue(5)==1);
		assertTrue(resCrisp.getMembershipValue(6)==1);
		assertTrue(resCrisp.getMembershipValue(7)==1);
		assertTrue(resCrisp.getMembershipValue(2)==1);
		//Valores exteriores
		assertTrue(resCrisp.getMembershipValue(1)==0);
		assertTrue(resCrisp.getMembershipValue(9)==0);
		assertTrue(resCrisp.getMembershipValue(10)==0);
	}
	
	@Test
	public void evalCrispDistrust2(){
		eval1 = new Evaluation(comp1, source1, crisp2);
		FuzzySet res = eval1.getMark();
		assertTrue(res instanceof CrispSet);
		CrispSet resCrisp = (CrispSet)res;
		//Valores limite
		assertTrue(resCrisp.getLeftBoundary()==4);
		assertTrue(resCrisp.getRightBoundary()==10);
		assertTrue(resCrisp.getModalValue()==7);
		//Valores intermedios
		assertTrue(resCrisp.getMembershipValue(4)==1);
		assertTrue(resCrisp.getMembershipValue(5)==1);
		assertTrue(resCrisp.getMembershipValue(6)==1);
		assertTrue(resCrisp.getMembershipValue(7)==1);
		assertTrue(resCrisp.getMembershipValue(8)==1);
		assertTrue(resCrisp.getMembershipValue(9)==1);
		assertTrue(resCrisp.getMembershipValue(10)==1);
		//Valores exteriores
		assertTrue(resCrisp.getMembershipValue(3)==0);
		assertTrue(resCrisp.getMembershipValue(0)==0);
	}
}
