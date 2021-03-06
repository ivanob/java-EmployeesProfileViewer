package test.evaluation.operations.fuzzy;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import controller.competences.FuzzySetBean;
import controller.competences.FuzzyVariableBean;

import rs.ac.ns.ftn.tmd.datatypes.CrispSet;
import rs.ac.ns.ftn.tmd.datatypes.DecomposedFuzzyNumber;
import rs.ac.ns.ftn.tmd.datatypes.FuzzyInterval;
import rs.ac.ns.ftn.tmd.datatypes.FuzzySet;
import rs.ac.ns.ftn.tmd.datatypes.LRFuzzyNumber;
import rs.ac.ns.ftn.tmd.datatypes.ScalarNumber;


public class DecomposedFuzzyDistancesTest {
	
	private DecomposedFuzzyNumber dfn1, dfn2, dfn3, dfn4, dfn5, dfn6;
	private DecomposedFuzzyNumber dfn7, dfn8;
	private FuzzyInterval fi1;
	private CrispSet cs1;
	private ScalarNumber sc1, sc2, sc3;

	@Before 
	public void setUp(){
		dfn1 = new DecomposedFuzzyNumber(2,1,1,
				FuzzySet.FUNCTION_LINEAR,
				FuzzySet.FUNCTION_LINEAR, 100);
		dfn2 = new DecomposedFuzzyNumber(4,1,1,
				FuzzySet.FUNCTION_LINEAR,
				FuzzySet.FUNCTION_LINEAR, 100);
		dfn3 = new DecomposedFuzzyNumber(1,1,1,
				FuzzySet.FUNCTION_LINEAR,
				FuzzySet.FUNCTION_LINEAR, 100);
		dfn4 = new DecomposedFuzzyNumber(2,1,1,
				FuzzySet.FUNCTION_LINEAR,
				FuzzySet.FUNCTION_LINEAR, 100);
		dfn5 = new DecomposedFuzzyNumber(2,1,1,
				FuzzySet.FUNCTION_LINEAR,
				FuzzySet.FUNCTION_LINEAR, 100);
		dfn6 = new DecomposedFuzzyNumber(2,1,3,
				FuzzySet.FUNCTION_LINEAR,
				FuzzySet.FUNCTION_LINEAR, 100);
		dfn7 = new DecomposedFuzzyNumber(1,1,1,
				FuzzySet.FUNCTION_LINEAR,
				FuzzySet.FUNCTION_LINEAR, 100);
		dfn8 = new DecomposedFuzzyNumber(5,2,1,
				FuzzySet.FUNCTION_LINEAR,
				FuzzySet.FUNCTION_LINEAR, 100);
		fi1 = new FuzzyInterval(5,6,1,1,
				FuzzySet.FUNCTION_LINEAR,FuzzySet.FUNCTION_LINEAR,100);
		sc1 = new ScalarNumber(2);
		sc2 = new ScalarNumber(0);
		sc3 = new ScalarNumber(8);
		cs1 = new CrispSet(2,4);
	}
	
	/* Este test evalua la operacion de distancia entre
	 * 2 objetos FuzzyEval */
	@Test
	public void evalFuzzyDistance1(){
		DecomposedFuzzyNumber res = (DecomposedFuzzyNumber)dfn1.distance(dfn2);
		//Puntos limite		
		assertTrue(res.getModalValue()==2);
		assertTrue(res.getLeftBoundary()==0);
		assertTrue(res.getRightBoundary()==4);
		//Puntos intermedios
		assertTrue(res.getMembershipValue(1)==0.5); //En x=1 alcanza 0.5 
		assertTrue(res.getMembershipValue(3)==0.5); //En x=3 alcanza 0.5
		//Rama izquierda
		assertTrue(res.getMembershipValue(0.5)<0.5 && res.getMembershipValue(0.5)>0);
		assertTrue(res.getMembershipValue(1.5)>0.5 && res.getMembershipValue(0.5)<1);
		//Rama derecha
		assertTrue(res.getMembershipValue(2.5)>0.5 && res.getMembershipValue(2.5)<1);
		assertTrue(res.getMembershipValue(3.5)>0 && res.getMembershipValue(3.5)<0.5);
	}
	
	/* Este test evalua la operacion de distancia entre
	 * 2 objetos FuzzyEval */
	@Test
	public void evalFuzzyDistance2(){
		DecomposedFuzzyNumber res = (DecomposedFuzzyNumber)dfn3.distance(dfn4);
		//Puntos limite			
		assertTrue(res.getModalValue()==1);
		assertTrue(res.getLeftBoundary()==0);
		assertTrue(res.getRightBoundary()==3);
		//Puntos intermedios
		assertTrue(res.getMembershipValue(0)==0.5); //En x=0 alcanza 0.5 
		assertTrue(res.getMembershipValue(2)==0.5); //En x=3 alcanza 0.5
		//Rama izquierda
		assertTrue(res.getMembershipValue(0.5)==0.75);
		assertTrue(res.getMembershipValue(0)==0.5);
		//Rama derecha
		assertTrue(res.getMembershipValue(1.5)==0.75);
		assertTrue(res.getMembershipValue(2)==0.5);
		assertTrue(res.getMembershipValue(2.5)==0.25);
	}
	
	/* Igual que el test evalFuzzyDistance2 pero invirtiendo los fuzzy numbers
	 * 2 objetos FuzzyEval */
	@Test
	public void evalFuzzyDistance3(){
		DecomposedFuzzyNumber res = (DecomposedFuzzyNumber)dfn4.distance(dfn3);
		//Puntos limite			
		assertTrue(res.getModalValue()==1);
		assertTrue(res.getLeftBoundary()==0);
		assertTrue(res.getRightBoundary()==3);
		//Puntos intermedios
		assertTrue(res.getMembershipValue(0)==0.5); //En x=0 alcanza 0.5 
		assertTrue(res.getMembershipValue(2)==0.5); //En x=3 alcanza 0.5
		//Rama izquierda
		assertTrue(res.getMembershipValue(0.5)==0.75);
		assertTrue(res.getMembershipValue(0)==0.5);
		//Rama derecha
		assertTrue(res.getMembershipValue(1.5)==0.75);
		assertTrue(res.getMembershipValue(2)==0.5);
		assertTrue(res.getMembershipValue(2.5)==0.25);
	}
	
	
	/* Este test evalua la operacion de distancia entre 2 conjuntos
	 * que dan como resultado un fuzzynumber cuyo valor modal es 0
	 * 2 objetos FuzzyEval */
	@Test
	public void evalFuzzyDistance4(){
		DecomposedFuzzyNumber res = (DecomposedFuzzyNumber)dfn5.distance(dfn6);
		//Puntos limite			
		assertTrue(res.getModalValue()==0);
		assertTrue(res.getLeftBoundary()==0);
		assertTrue(res.getRightBoundary()==4);
		//Rama derecha
		assertTrue(res.getMembershipValue(1)==0.75);
		assertTrue(res.getMembershipValue(2)==0.5);
		assertTrue(res.getMembershipValue(3)==0.25);
		//Valores extremos
		assertTrue(res.getMembershipValue(-1)==0);
		assertTrue(res.getMembershipValue(-0.01)==0);
		assertTrue(res.getMembershipValue(5)==0);
		assertTrue(res.getMembershipValue(5.001)==0);
	}
	
	/* Este test evalua la operacion de distancia entre 2 conjuntos.
	 * 2 objetos FuzzyEval */
	@Test
	public void evalFuzzyDistance5(){
		DecomposedFuzzyNumber res = (DecomposedFuzzyNumber)dfn7.distance(dfn8);
		//Puntos limite			
		assertTrue(res.getModalValue()==4);
		assertTrue(res.getLeftBoundary()==1);
		assertTrue(res.getRightBoundary()==6);
		//Rama izquierda
		assertTrue(res.getMembershipValue(2)>0.3332 && res.getMembershipValue(2)<0.3334);
		assertTrue(res.getMembershipValue(3)>0.6665 && res.getMembershipValue(3)<0.6667);
		//Rama derecha
		assertTrue(res.getMembershipValue(5)==0.5);
	}
	
	/* Este test evalua la operacion de distancia entre 2 conjuntos.
	 * 2 objetos FuzzyEval */
	@Test
	public void evalFuzzyDistanceOverlaped1(){
		DecomposedFuzzyNumber res = (DecomposedFuzzyNumber)dfn8.distance(dfn7);
		//Puntos limite			
		assertTrue(res.getModalValue()==4);
		assertTrue(res.getLeftBoundary()==1);
		assertTrue(res.getRightBoundary()==6);
		//Rama izquierda
		assertTrue(res.getMembershipValue(2)>0.3332 && res.getMembershipValue(2)<0.3334);
		assertTrue(res.getMembershipValue(3)>0.6665 && res.getMembershipValue(3)<0.6667);
		//Rama derecha
		assertTrue(res.getMembershipValue(5)==0.5);
		//Valores extremos
		assertTrue(res.getMembershipValue(0)==0);
		assertTrue(res.getMembershipValue(-1)==0);
		assertTrue(res.getMembershipValue(6)==0);
		assertTrue(res.getMembershipValue(7)==0);
		assertTrue(res.getMembershipValue(-100)==0);
		assertTrue(res.getMembershipValue(100)==0);
	}
	
	/* Este test evalua la operacion de distancia entre 2 conjuntos.
	 * 2 objetos FuzzyEval */
	@Test
	public void evalFuzzyDistanceWithFuzzyInt1(){
		FuzzyInterval res = (FuzzyInterval)dfn1.distance(fi1);
		//Puntos limite			
		assertTrue(res.getMembershipValue(3)==1);
		assertTrue(res.getMembershipValue(4)==1);
		//Rama izquierda
		assertTrue(res.getMembershipValue(1)==0);
		assertTrue(res.getMembershipValue(2)==0.5);
		//Rama derecha
		assertTrue(res.getMembershipValue(5)==0.5);
		assertTrue(res.getMembershipValue(6)==0);
	}
	
	/* Este test evalua la operacion de distancia entre 2 conjuntos.
	 * 2 objetos FuzzyEval */
	@Test
	public void evalFuzzyDistanceWithScalar1(){
		DecomposedFuzzyNumber res = (DecomposedFuzzyNumber)dfn2.distance(sc1);
		assertTrue(res instanceof DecomposedFuzzyNumber);
		//Puntos limite
		assertTrue(res.getMembershipValue(1)==0);
		assertTrue(res.getMembershipValue(3)==0);
		assertTrue(res.getMembershipValue(2)==1);
		//Rama izquierda
		assertTrue(res.getMembershipValue(1.5)==0.5);
		//Rama derecha
		assertTrue(res.getMembershipValue(2.5)==0.5);
	}
	
	/* Este test evalua la operacion de distancia entre 2 conjuntos.
	 * 2 objetos FuzzyEval */
	@Test
	public void evalFuzzyDistanceWithScalar2(){
		DecomposedFuzzyNumber res = (DecomposedFuzzyNumber)dfn6.distance(sc1);
		assertTrue(res instanceof DecomposedFuzzyNumber);
		//Puntos limite
		assertTrue(res.getMembershipValue(0)==1);
		assertTrue(res.getMembershipValue(3)==0);
		assertTrue(res.getMembershipValue(-1)==0);
		//Rama izquierda
		//Rama derecha
		
		assertTrue(res.getMembershipValue(1)>0.6665 && res.getMembershipValue(1)<0.6667);
		assertTrue(res.getMembershipValue(2)>0.3332 && res.getMembershipValue(2)<0.3334);
		assertTrue(res.getMembershipValue(1.5)==0.5);
	}
	
	/* Este test evalua la operacion de distancia entre 2 conjuntos.
	 * 2 objetos FuzzyEval */
	@Test
	public void evalFuzzyDistanceWithScalar3(){
		DecomposedFuzzyNumber res = (DecomposedFuzzyNumber)dfn4.distance(sc3);
		assertTrue(res instanceof DecomposedFuzzyNumber);
		//Puntos limite
		System.out.println(res.getMembershipValue(5.5));
		assertTrue(res.getMembershipValue(5)==0);
		assertTrue(res.getMembershipValue(7)==0);
		assertTrue(res.getMembershipValue(6)==1);
		//Rama izquierda
		assertTrue(res.getMembershipValue(5.5)==0.5);
		//Rama derecha
		assertTrue(res.getMembershipValue(6.5)==0.5);
	}
	
	/* Este test evalua la operacion de distancia entre 2 conjuntos.
	 * Un fuzzynumber y un scalar */
	@Test
	public void evalFuzzyDistanceWithScalar4(){
		DecomposedFuzzyNumber res = (DecomposedFuzzyNumber)dfn2.distance(sc2);
		assertTrue(res instanceof DecomposedFuzzyNumber);
		//Puntos limite
		assertTrue(res.getMembershipValue(3)==0);
		assertTrue(res.getMembershipValue(4)==1);
		assertTrue(res.getMembershipValue(5)==0);
		//Rama izquierda
		assertTrue(res.getMembershipValue(3.5)==0.5);
		//Rama derecha
		assertTrue(res.getMembershipValue(4.5)==0.5);
		//Valores extremos
		assertTrue(res.getMembershipValue(2)==0);
		assertTrue(res.getMembershipValue(6)==0);
	}
	
	/* Este test evalua la operacion de distancia entre un crispset
	 * y un fuzzynumber. */
	@Test
	public void evalFuzzyDistanceWithCrispSet1(){
		FuzzySet res = (DecomposedFuzzyNumber)dfn6.distance(cs1);
		assertTrue(res instanceof FuzzyInterval);
		//Puntos limite
		assertTrue(res.getMembershipValue(0)==1);
		assertTrue(res.getMembershipValue(2)==1);
		assertTrue(res.getMembershipValue(1)==1);
		//Rama izquierda
		//Rama derecha
		assertTrue(res.getMembershipValue(3)==0);
		assertTrue(res.getMembershipValue(2.5)==0.5);
	}
	
}
