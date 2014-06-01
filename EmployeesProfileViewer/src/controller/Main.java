package controller;

import controller.competences.CompetenceFactory;
import controller.employees.Employee;
import controller.employees.EmployeeManager;
import controller.evaluations.Evaluation;
import controller.evaluations.Source;
import controller.evaluations.SourceFactory;
import controller.genetic.Genetic;
import controller.genetic.SimilarityMatrix;
import controller.persistence.XMLReader;
import rs.ac.ns.ftn.tmd.datatypes.CrispSet;
import rs.ac.ns.ftn.tmd.datatypes.ScalarNumber;
import view.GUI.Grafica;

public class Main {
	//private Employer[] empl;
	private int numEmpl=4;//10;
	//private int numComp=4;
	private SimilarityMatrix matrix;
	private CompetenceFactory cf=CompetenceFactory.getInstance();
	private SourceFactory sf=SourceFactory.getInstance();
	private EmployeeManager em=EmployeeManager.getInstance();

	public static void main(String[] args) {
		new Main();
	}
	
	public Main(){
		System.out.println("*** Competence profile prototype ***");
		/*XMLReader reader = new XMLReader("./data/muestra3.xml");
		reader.readConfiguration();*/
		//crearEmpleados();
		//rellenarEvalEmpleados();
		matrix = new SimilarityMatrix();
		Grafica gr = new Grafica(null, Genetic.ELIPSE);
		//new Genetic(matrix, Genetic.ELIPSE);
	}
	
	private void rellenarEvalEmpleados(){
		Source s = sf.getSource("aaa");
		
		Employee e=em.getEmployee(0);
		e.addEvaluation(new Evaluation(cf.getCompetence(0), s, 0));
		e.addEvaluation(new Evaluation(cf.getCompetence(1), s, new ScalarNumber(5)));
		e.addEvaluation(new Evaluation(cf.getCompetence(2), s, new ScalarNumber(5)));
		
		e=em.getEmployee(1);
		e.addEvaluation(new Evaluation(cf.getCompetence(0), s, new ScalarNumber(5)));
		e.addEvaluation(new Evaluation(cf.getCompetence(1), s, new ScalarNumber(5)));
		e.addEvaluation(new Evaluation(cf.getCompetence(2), s, new ScalarNumber(0)));
		
		e=em.getEmployee(2);
		e.addEvaluation(new Evaluation(cf.getCompetence(0), s, new ScalarNumber(9)));
		e.addEvaluation(new Evaluation(cf.getCompetence(1), s, new ScalarNumber(9)));
		e.addEvaluation(new Evaluation(cf.getCompetence(2), s, new ScalarNumber(9)));
		
		e=em.getEmployee(3);
		e.addEvaluation(new Evaluation(cf.getCompetence(0), s, new ScalarNumber(3)));
		e.addEvaluation(new Evaluation(cf.getCompetence(1), s, new ScalarNumber(3)));
		e.addEvaluation(new Evaluation(cf.getCompetence(2), s, new ScalarNumber(2)));
	}
	
	private int intAleatorio(int ini, int fin){
		return (int)(Math.random()*fin + ini);
	}
	
	private void crearEmpleados() {
		for(int i=0; i<numEmpl; i++){
			em.addEmployee(new Employee("Empl " + i, cf.getNumCompetences()));
		}
	}

}
