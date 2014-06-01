package controller.genetic.chromosomes;

import controller.genetic.SimilarityMatrix;
import controller.genetic.genes.Gene;
import controller.geometry.Point;

/**
 * Esta clase es la raiz de la jerarquia de cromosomas. Un
 * cromosoma es un conjunto de genes, y cada gen representa
 * la codificacion de una proyeccion en el mapa (con todos sus
 * datos geometricos). El conjunto de todos los genes forma un
 * cromosoma, que es una solucion completa al problema abordado,
 * esto es, la representacion completa de un mapa con todas
 * las proyecciones que lo forman. El fitness es la medida
 * que indica como de buena es la solucion, siendo 0 la maxima
 * puntuacion.
 * 
 * @author Ivan Obeso Aguera
 */
public class Chromosome {
	private Gene[] genes;
	protected SimilarityMatrix matrix;
	protected double fitness;
	protected int probMutation;
	
	public void debug(){};
	
	public Chromosome(int numEmpl, SimilarityMatrix matrix, int probMutation){
		genes = new Gene[numEmpl];
		this.matrix = matrix;
		this.probMutation = probMutation;
	}
	
	public double getFitness(){
		return fitness;
	}
	
	public Gene getGene(int idEmpl){
		return genes[idEmpl];
	}
	
	public void setGene(int idEmpl, Gene g){
		genes[idEmpl] = g;
	}
	
	protected float floatAleatorio(int ini, int fin){
		return (float)(Math.random()*fin + ini);
	}
	
	/**
	 * Rellena aleatoriamente el cromosoma.
	 */
	public void rellenarDatosAleatorios(){
		for(int i=0; i<genes.length; i++){
			genes[i] = new Gene(floatAleatorio(0,10), floatAleatorio(0,10));
		}
		fitness();
	}
	
	protected int intAleatorio(int ini, int fin){
		return (int)(Math.random()*fin + ini);
	}
	
	/**
	 * @param d Un valor flotante
	 * @return String con la codificacion en String del flotante
	 */
	protected String floatToBinaryString(float d){
		int intBits = Float.floatToIntBits(d); 
		String binary = Integer.toBinaryString(intBits);
		return binary;
	}
	
	/**
	 * @param binary String binario con la codificacion de un flotante
	 * @return El valor flotante codificado en el parametro
	 */
	protected float binaryToFloat(String binary){
		int intBits2 = Integer.parseInt(binary, 2);
		float myFloat = Float.intBitsToFloat(intBits2);
		return myFloat;
	}
	
	/**
	 * Recibe una cadena binaria y la descodifica para construir
	 * un cromosoma con los datos que contiene.
	 * 
	 * @param str String con la cadena binaria codificacion
	 * del cromosoma.
	 */
	private void binaryToChromosome(String str){
		int index = 0;
		for(int i=0; i<genes.length; i++){
			float x= binaryToFloat(str.substring(index, index+31));
			index+=31;
			float y= binaryToFloat(str.substring(index, index+31));
			index+=31;
			this.setGene(i,new Gene(x, y));
		}
		fitness();
	}
	
	/**
	 * Codifica el cromosoma en una cadena binaria para
	 * realizar las operaciones geneticas sobre el.
	 * 
	 * @return String con la codificacion binaria del cromosoma.
	 */
	private String chromosomeToBinary(){
		String str="";
		for(int i=0; i<genes.length; i++){
			for(int j=0; j<2; j++){
				//Codifico la X y la Y
				String aux="";
				if(j==0){
					aux = floatToBinaryString(genes[i].getX());
				}else if(j==1){
					aux = floatToBinaryString(genes[i].getY());
				}
				int tam = aux.length();
				for(int k=0; k<31-tam; k++){
					aux = "0" + aux;
				}
				str += aux;
			}
		}
		return str;
	}
	
	protected void mutation(String chromosome){
		boolean mutation = (intAleatorio(0,this.probMutation)==0)?true:false;
		if(mutation){
			int pos = intAleatorio(1,chromosome.length()-1);
			String str = chromosome.substring(0,pos-1);
			if(chromosome.charAt(pos)=='0'){
				str += "1";
			}else{
				str += "0";
			}
			str += chromosome.substring(pos, chromosome.length());
			chromosome = str;
			//System.out.println("MUTACION!!");
		}
	}
	
	/**
	 * Este metodo implementa la operacion de cruce entre
	 * 2 cromosomas. El cruce es totalmente aleatorio y
	 * los hijos pueden sufrir mutaciones tambien aleatorias
	 * dependiendo de la probabilidad de que estas se den.
	 * 
	 * @param other Cromosoma con el que cruzarse
	 * @return Array de 2 posiciones con los 2 hijos generados
	 */
	public Chromosome[] crossover(Chromosome other){
		Chromosome[] children = new Chromosome[2];
		int ptoCruce = -1;
		String strChro1 = this.chromosomeToBinary();
		String strChro2 = other.chromosomeToBinary();
		if(strChro1.length() == strChro2.length()){
			ptoCruce=intAleatorio(0,strChro1.length());
		}else{
			System.out.println("ERROR");
		}
		String trozo1Chro1 = strChro1.substring(0, ptoCruce);
		String trozo2Chro1 = strChro1.substring(ptoCruce, strChro1.length());
		String trozo1Chro2 = strChro2.substring(0, ptoCruce);
		String trozo2Chro2 = strChro2.substring(ptoCruce, strChro2.length());
		String strChild1 = trozo1Chro1 + trozo2Chro2;
		String strChild2 = trozo1Chro2 + trozo2Chro1;
		mutation(strChild1);
		mutation(strChild2);
		children[0] = new Chromosome(genes.length, this.matrix, probMutation);
		children[1] = new Chromosome(genes.length, this.matrix, probMutation);
		children[0].binaryToChromosome(strChild1);
		children[1].binaryToChromosome(strChild2);
		return children;
	}
	
	/**
	 * Este metodo calcula la distancia euclidea entre 2 genes,
	 * esto es, entre 2 figuras geometricas del mapa.
	 * 
	 * @param g1 Gen numero 1
	 * @param g2 Gen numero 2
	 * @return Distancia euclidea entre los 2 genes que recibe
	 */
	public double euclideanDistance(Gene g1, Gene g2){
		return Math.sqrt(Math.pow(g1.getX()-g2.getX(), 2) + 
			Math.pow(g1.getY()-g2.getY(), 2));
	}
	
	public double euclideanDistance(Gene g1, Point p2){
		return Math.sqrt(Math.pow(g1.getX()-p2.getX(), 2) + 
			Math.pow(g1.getY()-p2.getY(), 2));
	}
	
	public double euclideanDistance(float x1, float y1, float x2, float y2){
		return Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
	}
	
	/*private double fitness(){
		double fitness=0;
		for(int i=0; i<genes.length; i++){
			for(int j=i+1; j<genes.length; j++){
				if(i!=j){
					double distEucl = euclideanDistance(genes[i], genes[j]);
					FuzzySet distNum = matrix.getDistance(i, j);
					if(distNum instanceof EvalNumeric){
						fitness += Math.pow(distEucl-((EvalNumeric)distNum).getMark(), 2);
					}
				}
			}
		}
		this.fitness=fitness;
		return fitness;
	}*/
	
	private double fitness(){
		return -1;
	}
}
