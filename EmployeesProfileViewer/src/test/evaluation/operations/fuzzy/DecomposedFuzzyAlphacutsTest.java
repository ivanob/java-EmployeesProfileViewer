package test.evaluation.operations.fuzzy;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import rs.ac.ns.ftn.tmd.datatypes.DecomposedFuzzyNumber;
import rs.ac.ns.ftn.tmd.datatypes.FuzzySet;

public class DecomposedFuzzyAlphacutsTest {
	private DecomposedFuzzyNumber dfn1, dfn2;
	
	@Before 
	public void setUp(){
		dfn1 = new DecomposedFuzzyNumber(2,1,1,
				FuzzySet.FUNCTION_LINEAR,
				FuzzySet.FUNCTION_LINEAR, 100);
		dfn2 = new DecomposedFuzzyNumber(3,3,1,
				FuzzySet.FUNCTION_LINEAR,
				FuzzySet.FUNCTION_LINEAR, 100);
	}
	
	/* Este test evalua los alphacuts de un fuzzynumber */
	@Test
	public void evalFuzzyAlphacuts1(){
		//Alphacuts de la izquierda
		assertTrue(dfn1.getAlphaCut(0)[0]==1);
		assertTrue(dfn1.getAlphaCut(0.5)[0]==1.5);
		assertTrue(dfn1.getAlphaCut(0.25)[0]==1.25);
		assertTrue(dfn1.getAlphaCut(0.75)[0]==1.75);
		assertTrue(dfn1.getAlphaCut(0.99)[0]==2);
		//Alphacuts de la derecha
		assertTrue(dfn1.getAlphaCut(0)[1]==3);
		assertTrue(dfn1.getAlphaCut(0.5)[1]==2.5);
		assertTrue(dfn1.getAlphaCut(0.25)[1]==2.75);
		assertTrue(dfn1.getAlphaCut(0.75)[1]==2.25);
		assertTrue(dfn1.getAlphaCut(0.99)[1]==2);
	}
	
	/* Este test evalua los alphacuts de un fuzzynumber */
	@Test
	public void evalFuzzyAlphacuts2(){
		System.out.println(dfn2.getAlphaCut(0.6666)[0]);
		//Alphacuts de la izquierda
		assertTrue(dfn2.getAlphaCut(0)[0]==0);
		assertTrue(dfn2.getAlphaCut(0.3333)[0]>0.90 && dfn2.getAlphaCut(0.3333)[0]<1.1);
		assertTrue(dfn2.getAlphaCut(0.6666)[0]>1.90 && dfn2.getAlphaCut(0.6666)[0]<2.1);
		assertTrue(dfn2.getAlphaCut(0.99)[0]==3);
		//Alphacuts de la derecha
		assertTrue(dfn2.getAlphaCut(0.5)[1]==3.5);
		assertTrue(dfn2.getAlphaCut(0.25)[1]==3.75);
		assertTrue(dfn2.getAlphaCut(0.75)[1]==3.25);
	}
	
}
