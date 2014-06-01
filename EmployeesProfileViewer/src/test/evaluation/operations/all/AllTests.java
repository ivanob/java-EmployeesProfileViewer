package test.evaluation.operations.all;

import test.evaluation.operations.crisp.CrispDistrustTest;
import test.evaluation.operations.crisp.CrispSetDistancesTest;
import test.evaluation.operations.crisp.CrispSetMeansTest;
import test.evaluation.operations.fuzzy.DecomposedFuzzyAlphacutsTest;
import test.evaluation.operations.fuzzy.DecomposedFuzzyDistancesTest;
import test.evaluation.operations.fuzzy.DecomposedFuzzyDistrustTest;
import test.evaluation.operations.fuzzy.DecomposedFuzzyMeansTest;
import test.evaluation.operations.fuzzy.FuzzyIntervalConstructorTest;
import test.evaluation.operations.fuzzy.FuzzyIntervalDistancesTest;
import test.evaluation.operations.fuzzy.FuzzyIntervalDistrustTest;
import test.evaluation.operations.fuzzy.FuzzyIntervalMeansTest;
import test.evaluation.operations.scalar.ScalarDistancesTest;
import test.evaluation.operations.scalar.ScalarDistrustTest;
import test.evaluation.operations.scalar.ScalarMeansTest;
import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Esta clase lanza todos los test de pruebas unitarias hechas
 * en la aplicacion. Se centra concretamente en testear los tipos
 * de datos implementados y sus operaciones, al ser los puntos
 * mas criticos del sistema.
 * @author Ivan Obeso Aguera
 */
public class AllTests {
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for test.evaluation.operations");
		suite.addTest(new JUnit4TestAdapter(CrispSetDistancesTest.class));
		suite.addTest(new JUnit4TestAdapter(CrispSetMeansTest.class));
		
		suite.addTest(new JUnit4TestAdapter(DecomposedFuzzyDistancesTest.class));
		suite.addTest(new JUnit4TestAdapter(FuzzyIntervalConstructorTest.class));
		suite.addTest(new JUnit4TestAdapter(FuzzyIntervalDistancesTest.class));
		suite.addTest(new JUnit4TestAdapter(DecomposedFuzzyAlphacutsTest.class));
		suite.addTest(new JUnit4TestAdapter(DecomposedFuzzyMeansTest.class));
		suite.addTest(new JUnit4TestAdapter(FuzzyIntervalConstructorTest.class));
		suite.addTest(new JUnit4TestAdapter(FuzzyIntervalMeansTest.class));
		
		suite.addTest(new JUnit4TestAdapter(ScalarDistancesTest.class));
		suite.addTest(new JUnit4TestAdapter(ScalarMeansTest.class));
		
		suite.addTest(new JUnit4TestAdapter(ScalarDistrustTest.class));
		suite.addTest(new JUnit4TestAdapter(CrispDistrustTest.class));
		suite.addTest(new JUnit4TestAdapter(DecomposedFuzzyDistrustTest.class));
		suite.addTest(new JUnit4TestAdapter(FuzzyIntervalDistrustTest.class));
		return suite;
	}
}
