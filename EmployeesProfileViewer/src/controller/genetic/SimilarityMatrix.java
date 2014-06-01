package controller.genetic;

import controller.competences.Competence;
import controller.competences.CompetenceFactory;
import controller.employees.Employee;
import controller.employees.EmployeeManager;
import controller.evaluations.Evaluation;
import controller.evaluations.SourceFactory;
import rs.ac.ns.ftn.tmd.datatypes.FuzzySet;
import rs.ac.ns.ftn.tmd.datatypes.ScalarNumber;

/**
 * Es la clase que encapsula la matriz de similaridad entre
 * los empleados y los puntos caracteristicos. Esta matriz
 * representa las distancias numericas entre los trabajadores
 * registrados en el sistema de acuerdo a sus evaluaciones. A
 * partir de esta matriz se calculan las distancias euclideas
 * 2 a 2 de los empleados entre si y con respecto a los
 * puntos caracteristicos utilizando los vectores multidimensionales
 * de evaluaciones. La matriz distancias servira para comparar las
 * distancias reales entre las proyecciones del mapa con las distancias
 * numericas y buscar una distribucion que maximice la similitud entre
 * ambas.
 * 
 * @author Ivan Obeso Aguera
 * 
 */
public class SimilarityMatrix {
	/** La matriz es cuadrada, con
	el mismo numero de filas que de columnas. La fila tiene como 
	estructura primero los empleados no caracteristicos y despues los 
	puntos caract. (en los que aparece primero todos los NO y luego 
	los ONLY. Es la matriz que representa las diferencias entre los
	empleados para cada una de las evaluaciones que tienen para
	cada competencia. */
	private Evaluation[][][] matrixDifer; 
	/** La matriz se genera a partir
	de la matriz matrixDifer y contiene las distancias euclideas entre
	los empleados atendiendo a todas sus competencias en el plano
	vectorial. */
	private FuzzySet[][] euclDistances; 
	private CompetenceFactory cf=CompetenceFactory.getInstance();
	private int numEmpl, numComp;
	public static int TYPE_NO=2, TYPE_ONLY=3, TYPE_EMPL=1;
	private EmployeeManager em=EmployeeManager.getInstance();
	
	public SimilarityMatrix(){
		this.numEmpl = em.getNumEmployees();
		this.numComp = cf.getNumCompetences();
		fillMatrix();
		calculateEuclideanDistances();
		normalizeDistanceMatrix();
	}
	
	private void normalizeDistanceMatrix() {
		double minValue = calculateMinValue();
		double maxValue = calculateMaxValue();
		for(int i=0; i<euclDistances.length; i++){
			for(int j=0; j<euclDistances[i].length; j++){
				//ScalarNumber fsetMin = new ScalarNumber(minValue);
				//ScalarNumber fsetMax = new ScalarNumber(maxValue);
				euclDistances[i][j] = euclDistances[i][j].substractScalar(minValue).divideScalar(maxValue-minValue);//.multiplyScalar(2);
			}
		}
	}

	private double calculateMaxValue() {
		double max = -1;
		for(int i=0; i<euclDistances.length; i++){
			for(int j=0; j<euclDistances[i].length; j++){
				if(max<euclDistances[i][j].getRightBoundary()){
					max=euclDistances[i][j].getRightBoundary();
				}
			}
		}
		return max;
	}

	private double calculateMinValue() {
		double min = 9999;
		for(int i=0; i<euclDistances.length; i++){
			for(int j=0; j<euclDistances[i].length; j++){
				if(min>euclDistances[i][j].getLeftBoundary()){
					min=euclDistances[i][j].getLeftBoundary();
				}
			}
		}
		return min;
	}

	private Employee[] generateCaractPointsEvals(){
		int numComp = cf.getNumCompetences();
		SourceFactory sf = SourceFactory.getInstance();
		Employee[] caractList = new Employee[numComp*2];
		for(int i=0; i<numComp; i++){
			Competence c = cf.getCompetence(i);
			String name = c.getName();
			caractList[i] = new Employee("NO " + name, numComp);
			caractList[i+cf.getNumCompetences()] = new Employee("ONLY " + name, numComp);
			for(int j=0; j<numComp; j++){
				double notaONLY=0;
				double notaNO=10;
				if(i==j){
					notaONLY=10;
					notaNO=0;
				}					
				Evaluation evONLY = new Evaluation(cf.getCompetence(j), sf.getMaximunCertainSource(), new ScalarNumber(notaONLY));
				Evaluation evNO = new Evaluation(cf.getCompetence(j), sf.getMaximunCertainSource(), new ScalarNumber(notaNO));
				caractList[i].addEvaluation(evNO);
				caractList[i+cf.getNumCompetences()].addEvaluation(evONLY);
			}
		}
		return caractList;
	}
	
	public FuzzySet getDistance(int idEmpl1, int idEmpl2){
		return euclDistances[idEmpl1][idEmpl2];
	}
	
	private void calculateEuclideanDistances(){
		int ladoMatriz = matrixDifer.length;
		euclDistances = new FuzzySet[ladoMatriz][ladoMatriz];
		for(int i=0; i<ladoMatriz; i++){
			for(int j=0; j<ladoMatriz; j++){
				FuzzySet ev=null;
				boolean first=true;
				for(int k=0; k<numComp; k++){
					if(first){
						ev = matrixDifer[i][j][k].getMark().pow(2);
						first=false;
					}else{
						ev = ev.add(matrixDifer[i][j][k].getMark().pow(2));
					}
				}
				ev = ev.sqrt();
				euclDistances[i][j] = ev;
			}
		}
	}
	
	private Employee[] mezclarArrays(Employee[] empl, Employee[] caract){
		Employee[] mix = new Employee[empl.length+caract.length];
		int i;
		for(i=0; i<empl.length; i++){
			mix[i] = empl[i];
		}
		for(int j=0; j<caract.length; j++){
			mix[i] = caract[j];
			i++;
		}
		return mix;
	}
	
	public void fillMatrix(){
		int numComp = cf.getNumCompetences();
		SourceFactory sf = SourceFactory.getInstance();
		int numPtosCaract = numComp*2;
		int ladoMatriz=em.getNumEmployees()+numPtosCaract;
		Employee[] caractList = this.generateCaractPointsEvals();
		matrixDifer = new Evaluation[ladoMatriz][ladoMatriz][numComp];
		Employee[] mix = mezclarArrays(em.getEmployeeArray(), caractList);
		//Relleno las evaluaciones de los empleados normales
		for(int i=0; i<ladoMatriz; i++){
			for(int j=0; j<ladoMatriz; j++){
				for(int k=0; k<numComp; k++){
					Evaluation ev1 = mix[i].getEvaluation(k);
					Evaluation ev2 = mix[j].getEvaluation(k);
					FuzzySet result = ev1.getMark().distance(ev2.getMark());
					matrixDifer[i][j][k] = new Evaluation(ev1.getCompetence(), sf.getMaximunCertainSource(), result);
				}
			}
		}
	}
	
}
