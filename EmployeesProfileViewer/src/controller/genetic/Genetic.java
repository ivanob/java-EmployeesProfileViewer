package controller.genetic;

import java.util.LinkedList;

import javax.swing.SwingWorker;

import controller.competences.CompetenceFactory;
import controller.employees.EmployeeManager;
import controller.genetic.chromosomes.Chromosome;
import controller.genetic.chromosomes.ChromosomeCircle;
import controller.genetic.chromosomes.ChromosomeEllipseConcentric;
import controller.geometry.Point;
import controller.genetic.genes.Gene;
import controller.genetic.genes.GeneEllipse;
import controller.genetic.genes.GeneEllipseConcentric;

import model.ElipseGUIBean;
import model.GUIDataBean;
import model.PointGUIBean;

/**
 * Implementa el algoritmo genetico que busca el mapa
 * de proyecciones para un conjunto de datos dado: 
 * competencias, empleados, evaluaciones y fuentes.
 * Hereda de la clase SwingWorker para que en el
 * interfaz grafico se muestre una barra de progreso
 * que se llena a medida que avanza el algoritmo.
 * 
 * @author Ivan Obeso Aguera
 */
public class Genetic extends SwingWorker<GUIDataBean, Integer>{
	private int N, S, iter;
	private EmployeeManager em = EmployeeManager.getInstance();
	private float[] alphacuts;
	public final static int POINT = 1, CIRCLE = 2;
	public static final int ELIPSE = 3;
	private int typeGenetic;
	private int probMutation;
	private boolean DEBUG=false;
	private SimilarityMatrix matrix;
	private int type;
	private GeneticConfigBean config;
	private float alphaSmoothing;
	
	
	public int numGenes(){
		return em.getNumEmployees();
	}
	
	protected GUIDataBean doInBackground() throws Exception{
		//new Genetic(matrix,this.ELIPSE);
		return Evolution();
	}	
	
	/**
	 * Este metodo inicializa todas las variables y lanza el 
	 * algoritmo genetico. Recoge al mejor individuo de la ultima
	 * generacion obtenida, que es el que tiene el fitness mas bajo
	 * de toda la evolucion.
	 * 
	 * @return Un objeto que encapsula toda la informacion geometrica
	 * para representar las proyecciones del mejor mapa encontrado.
	 */
	public GUIDataBean Evolution(){
		long start=System.currentTimeMillis();
		setProgress(0);
		this.probMutation = (int)(config.getMutationProbability()*1000000);
		//1000; //1 cada 1000 reproducciones
		this.typeGenetic = type;
		this.N = config.getNumPopulation();//1000;//1000;
		this.S = config.getNumSelectedIndividuals();//500;
		this.iter = config.getNumIterations();//100;
		this.alphacuts= config.getAlphacuts();
		this.alphaSmoothing = config.getAlphaSmoothing();
		
		Chromosome[] population = this.generarPoblacionInicial();
		population = run(iter, population);
		System.out.println("FIN");
		System.out.println("Fitness: " + population[0].getFitness());
		ChromosomeEllipseConcentric best = (ChromosomeEllipseConcentric) population[0];
		for(int i=0; i<em.getNumEmployees(); i++){
			System.out.println("Empl " + i + ": " + best.getGeneEmpl(i));
		}
		long end=System.currentTimeMillis();
		GUIDataBean graphicBean = rellenarResultados((ChromosomeEllipseConcentric)population[0], end-start);
		return graphicBean;
	}
	
	public Genetic(SimilarityMatrix matrix, int type, GeneticConfigBean config) {
		this.matrix=matrix;
		this.type=type;
		this.config=config;
	}

	/**
	 * Construye un objeto GUIDataBean que encapsula todos los datos
	 * del resultado que ha generado el algoritmo genetico para
	 * pasarselos a la vista y que los represente en un mapa
	 * de proyecciones.
	 */
	private GUIDataBean rellenarResultados(ChromosomeEllipseConcentric chro, long time){
		GUIDataBean result = new GUIDataBean();
		int numPuntos = chro.getNumPuntos();
		for(int i=0; i<chro.getNumGenes(); i++){
			Gene gene = chro.getGeneEmpl(i);
			if(gene instanceof GeneEllipseConcentric){
				GeneEllipseConcentric gec=(GeneEllipseConcentric)gene;
				LinkedList<Point[]> points=new LinkedList<Point[]>();
				points.add(gec.getPoints(500));
				for(int j=0; j<alphacuts.length; j++){
					points.add(gec.getPointsCut(j));
				}
				ElipseGUIBean ellipseBean = new ElipseGUIBean(points,
					em.getEmployee(i).getName(), this.alphacuts);
				result.addEllipseEmployer(ellipseBean);
			}
			else if(gene instanceof GeneEllipse){
				GeneEllipse ge=(GeneEllipse)gene;
				LinkedList<Point[]> points=new LinkedList<Point[]>();
				points.add(ge.getPoints(500));
				ElipseGUIBean ellipseBean = new ElipseGUIBean(points,
					em.getEmployee(i).getName(), null);
				result.addEllipseEmployer(ellipseBean);
			}else if(gene instanceof Gene){
				PointGUIBean pointBean = new PointGUIBean(gene.getPoints()[0],
						em.getEmployee(i).getName());
				result.addPointEmployer(pointBean);
			}
		}
		int i=0;
		CompetenceFactory cf = CompetenceFactory.getInstance();
		for(int j=0; j<cf.getNumCompetences()*2; j++){
			Gene gene = chro.getGeneCaract(i);
			int idComp = j;
			String name = "NO ";
			if(j>=cf.getNumCompetences()){
				idComp -= cf.getNumCompetences();
				name = "ONLY ";
			}
			name += cf.getCompetence(idComp).getName();
			i++;
			result.addPointCaracteristic(new PointGUIBean(gene.getPoints()[0],
				name));
		}
		//Datos de status
		result.setFitness(chro.getFitness());
		result.setTime(time);
		return result;
	}
	
	/**
	 * Este metodo recibe 2 cromosomas y genera un numero numHijos de
	 * descendientes aplicando un cruce aleatorio.
	 * 
	 * @param parents un array de 2 posiciones con los cromosomas a cruzarse
	 * @param numHijos el numero de descendientes a generar
	 * @return Un array con numHijos posiciones y los hijos que se han
	 * formado.
	 */
	private Chromosome[] generarDescendencia(Chromosome[] parents, int numHijos){
		Chromosome[] child = null;
		if(this.typeGenetic==POINT){
			child = new Chromosome[numHijos];
		}else if(this.typeGenetic==CIRCLE){
			child = new ChromosomeCircle[numHijos];
		}else if(this.typeGenetic==ELIPSE){
			child = new ChromosomeEllipseConcentric[numHijos];
		}
		int j=0;
		for(int i=0; i<numHijos/2; i++){
			int idParent1 = intAleatorio(0, parents.length);
			int idParent2 = intAleatorio(0, parents.length);
			Chromosome[] children = parents[idParent1].crossover(parents[idParent2]);
			child[j] = children[0];
			child[j+1] = children[1];
			j+=2;
		}
		return child;
	}
	
	private int numRepeticiones(Chromosome[] crom){
		int rep=0;
		for(int i=0; i<crom.length; i++){
			for(int j=0; j<crom.length; j++){
				if(i!=j){
					if(crom[i].getFitness() == crom[j].getFitness()){
						rep++;
					}
				}
			}
		}
		return rep;
	}
	
	/**
	 * Este metodo implementa los pasos del algoritmo genetico
	 * propiamente dicho. Realiza una serie de iteraciones a
	 * partir de la poblacion inicial en las que selecciona
	 * a los mejores individuos, los cruza entre ellos y reemplaza
	 * los peores individuos de la poblacion por esos hijos.
	 * 
	 * @param numIter numero de iteraciones o generaciones
	 * @param population poblacion inicial de individuos
	 * @return la poblacion de la ultima generacion
	 * 
	 */
	private Chromosome[] run(int numIter, Chromosome[] population){
		for(int i=0; i<numIter && !Thread.interrupted(); i++){
		//while(population[0].fitness>10){
			//int n1 = numRepeticiones(population);
			ordenarPopulation(population);
			//int n2 = numRepeticiones(population);
			Chromosome[] best = selectBestChromos(S, population);
			//int n3 = numRepeticiones(best);
			Chromosome[] children = this.generarDescendencia(best, 400);
			//int n4 = numRepeticiones(children);
			ordenarPopulation(children);
			//int n5 = numRepeticiones(children);
			population = this.replacePopulation(population, children);
			System.out.println("Iteration " + i + ", Best fitness: " + population[0].getFitness());
			if(DEBUG){
				population[0].debug();
			}
			//Actualizo la barra de progreso
			int progress=(i*100)/numIter;
			setProgress(progress);
		}
		setProgress(100);
		return population;
	}
	
	/**
	 * Este metodo implementa la operacion de reemplazo de individuos
	 * @param population poblacion de individuos
	 * @param children grupo de hijos generados a partir de la poblacion
	 * @return nueva poblacion en la que se han sustituido los peores
	 * individuos de la poblacion por los descendientes generados.
	 */
	private Chromosome[] replacePopulation(Chromosome[] population, Chromosome[] children){
		Chromosome[] newPopulation = null;
		if(typeGenetic == this.CIRCLE){
			newPopulation = new ChromosomeCircle[N];
		}else if(typeGenetic == this.POINT){
			newPopulation = new Chromosome[N];
		}else if(typeGenetic == this.ELIPSE){
			newPopulation = new ChromosomeEllipseConcentric[N];
		}
		for(int i=0; i<N-children.length; i++){
			newPopulation[i] = population[i];
		}
		int j=0;
		for(int i=N-children.length; i<N; i++){
			newPopulation[i] = children[j];
			j++;
		}
		/*Chromosome[] res = new Chromosome[N];
		for(int i=0; i<N; i++){
			res[i] = newPopulation[i];
		}
		return res;*/
		return newPopulation;
	}
	
	/**
	 * Este metodo recibe una poblacion de individuos y los
	 * ordena en funcion de su fitness, dejando al que tenga
	 * el fitness menor y por lo tanto sea el mejor en la primera
	 * posicion.
	 * 
	 * @param population poblacion de individuos
	 */
	private void ordenarPopulation(Chromosome[] population){
		//Ordenacion burbuja
		for(int i=population.length-1;i>1;i--){
			for(int j=1;j<=i;j++){
				if(population[j-1].getFitness()>population[j].getFitness()){
					Chromosome aux2 = population[j-1];
					population[j-1]=population[j];
					population[j]=aux2;
				}
			}
		}
	}
	
	/**
	 * @param numSel Numero de individuos a seleccionar
	 * @param population Poblacion de individuos entre los que seleccionar
	 * @return Devuelve los numSel mejores individuos de la poblacion
	 */
	private Chromosome[] selectBestChromos(int numSel, Chromosome[] population){
		Chromosome[] res = null;
		if(typeGenetic == CIRCLE){
			res = new ChromosomeCircle[numSel];
		}else if(typeGenetic == POINT){
			res = new Chromosome[numSel];
		}else if(typeGenetic == ELIPSE){
			res = new ChromosomeEllipseConcentric[numSel];
		}
		//ordenarPopulation(population);
		for(int i=0; i<numSel; i++){
			res[i] = population[i];
		}
		return res;
	}
	
	private int intAleatorio(int ini, int fin){
		return (int)(Math.random()*fin + ini);
	}
	
	/**
	 * Este metodo crea una poblacion de individuos inicial
	 * sobre la que empezar a converger a generaciones
	 * mejores.
	 * 
	 * @return Devuelve una poblacion inicial de N individuos
	 * generados aleatoriamente.
	 */
	private Chromosome[] generarPoblacionInicial(){
		Chromosome[] population = null;
		if(this.typeGenetic==this.POINT){
			population = new Chromosome[N];
			for(int i=0; i<N; i++){
				population[i] = new Chromosome(numGenes(), this.matrix, probMutation);
				population[i].rellenarDatosAleatorios();
			}
		}else if(this.typeGenetic==this.CIRCLE){
			population = new ChromosomeCircle[N];
			for(int i=0; i<N; i++){
				population[i] = new ChromosomeCircle(numGenes(), this.matrix, probMutation);
				population[i].rellenarDatosAleatorios();
			}
		}else if(this.typeGenetic==this.ELIPSE){
			population = new ChromosomeEllipseConcentric[N];
			for(int i=0; i<N; i++){
				population[i] = new ChromosomeEllipseConcentric(this.matrix, probMutation, alphacuts, alphaSmoothing);
				population[i].rellenarDatosAleatorios();
			}
		}
		return population;
	}

	
}
