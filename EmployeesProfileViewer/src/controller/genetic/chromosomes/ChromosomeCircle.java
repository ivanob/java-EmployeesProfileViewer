package controller.genetic.chromosomes;

import controller.genetic.SimilarityMatrix;
import controller.genetic.genes.GeneCircle;

public class ChromosomeCircle extends Chromosome {
	
	private GeneCircle[] genes;
	
	public ChromosomeCircle(int numEmpl, SimilarityMatrix matrix, int probMutation){
		super(numEmpl, matrix, probMutation);
		genes = new GeneCircle[numEmpl];
	}
	
	public GeneCircle getGene(int i){
		return this.genes[i];
	}
	
	public void rellenarDatosAleatorios(){
		for(int i=0; i<genes.length; i++){
			genes[i] = new GeneCircle(floatAleatorio(0,10), floatAleatorio(0,10),
					floatAleatorio(0,10));
		}
		fitness();
	}
	
	public void setGene(int i, GeneCircle g){
		genes[i] = g;
	}
	
	private void binaryToChromosome(String str){
		int index = 0;
		for(int i=0; i<genes.length; i++){
			float x= binaryToFloat(str.substring(index, index+31));
			index+=31;
			float y= binaryToFloat(str.substring(index, index+31));
			index+=31;
			float r= binaryToFloat(str.substring(index, index+31));
			index+=31;
			this.setGene(i,new GeneCircle(x, y, r));
		}
		fitness();
	}
	
	private String chromosomeToBinary(){
		String str="";
		for(int i=0; i<genes.length; i++){
			for(int j=0; j<3; j++){
				//Codifico la X y la Y
				String aux="";
				if(j==0){
					aux = floatToBinaryString(genes[i].getX());
				}else if(j==1){
					aux = floatToBinaryString(genes[i].getY());
				}else if(j==2){
					aux = floatToBinaryString(genes[i].getRadius());
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
	
	
	
	public Chromosome[] crossover(Chromosome other){
		ChromosomeCircle otherC = (ChromosomeCircle)other;
		ChromosomeCircle[] children = new ChromosomeCircle[2];
		int ptoCruce = -1;
		String strChro1 = this.chromosomeToBinary();
		String strChro2 = otherC.chromosomeToBinary();
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
		children[0] = new ChromosomeCircle(genes.length, this.matrix, probMutation);
		children[1] = new ChromosomeCircle(genes.length, this.matrix, probMutation);
		children[0].binaryToChromosome(strChild1);
		children[1].binaryToChromosome(strChild2);
		return children;
	}
	
	
//	private double fitness(){
//		double fitness=0;
//		for(int i=0; i<genes.length; i++){
//			for(int j=i+1; j<genes.length; j++){
//				if(i!=j){
//					double distEucl = euclideanDistance(genes[i], genes[j]);
//					Evaluation distNum = matrix.getEvaluation(i, j);
//					if(distNum instanceof EvalNumeric){
//						fitness += Math.pow(distEucl-((EvalNumeric)distNum).getMark(), 2);
//					}else if(distNum instanceof EvalCrisp){
//						EvalCrisp eval = (EvalCrisp)distNum;
//						double minDist = eval.getMin();
//						double maxDist = eval.getMax();
//						double puntoModal = Math.abs(eval.getModal());
//						fitness += Math.pow(distEucl-puntoModal, 2);
//						fitness += Math.pow(minDist-(distEucl - (genes[i].radius+genes[j].radius)), 2);
//						fitness += Math.pow(maxDist-(distEucl + (genes[i].radius+genes[j].radius)), 2);
//						/*fitness += Math.pow(minDist-(distEucl - genes[i].radius), 2);
//						fitness += Math.pow(maxDist-(distEucl + genes[i].radius), 2);
//						fitness += Math.pow(minDist-(distEucl - genes[j].radius), 2);
//						fitness += Math.pow(maxDist-(distEucl + genes[j].radius), 2);*/
//					}
//				}
//			}
//		}
//		this.fitness=fitness;
//		return fitness;
//	}
	
	private double fitness(){
		return -1;
	}
}
