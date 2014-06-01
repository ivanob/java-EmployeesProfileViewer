package controller.employees;

import java.util.LinkedList;

/**
 * Es el gestor de empleados registrados en el sistema. De forma
 * analoga a CompetenceFactory, esta clase es un singleton para
 * que se pueda acceder a ella desde cualquier punto del codigo
 * y que los objetos Employee manejados sean siempre los mismos.
 * 
 * @author Ivan Obeso Aguera
 */
public class EmployeeManager {
	private static EmployeeManager INSTANCE = new EmployeeManager();
	private LinkedList<Employee> listEmployees;

	public static EmployeeManager getInstance(){
		return INSTANCE;
	}
	
	public void removeAllEmployees(){
		listEmployees = new LinkedList<Employee>();
	}
	
	private EmployeeManager(){
		listEmployees = new LinkedList<Employee>();
	}

	public void addEmployee(Employee e) {
		listEmployees.add(e);
	}
	
	public Employee getEmployee(int id){
		return listEmployees.get(id);
	}

	public int getNumEmployees() {
		return listEmployees.size();
	}

	public Employee[] getEmployeeArray() {
		Employee[] array = new Employee[this.getNumEmployees()];
		int i=0;
		for(Employee e : listEmployees){
			array[i] = e;
			i++;
		}
		return array;
	}

	public void removeEmployee(int index) {
		this.listEmployees.remove(index);		
	}

	/**
	 * Añade una nueva lista de evaluaciones a cada empleado
	 * cuando se invoca este metodo, que es en el momento en 
	 * el que se ha creado una nueva competencia en el sistema.
	 */
	public void addCompetenceEvaluations() {
		for(Employee e : listEmployees){
			e.addNewCompetenceEvaluations();
		}
	}
}
