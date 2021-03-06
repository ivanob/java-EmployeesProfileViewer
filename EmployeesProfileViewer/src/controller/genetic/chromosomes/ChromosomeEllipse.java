package controller.genetic.chromosomes;

import controller.competences.CompetenceFactory;
import controller.employees.Employee;
import controller.genetic.DistanceBean;
import controller.genetic.SimilarityMatrix;
import controller.genetic.genes.Gene;
import controller.genetic.genes.GeneEllipse;
import controller.geometry.Point;
import rs.ac.ns.ftn.tmd.datatypes.CrispSet;
import rs.ac.ns.ftn.tmd.datatypes.FuzzySet;
import rs.ac.ns.ftn.tmd.datatypes.ScalarNumber;

public class ChromosomeEllipse extends Chromosome {

	private Employee[] empl;
	private Gene[] genesEmpl;
	public Gene[] genesCaract;
	private CompetenceFactory cf = CompetenceFactory.getInstance();
	private float alpha; /* alpha=1 no se toca el punto.
	alpha=0 se recoloca el punto a su posicion ideal. */
	
	public ChromosomeEllipse(Employee[] empl, SimilarityMatrix matrix, int probMutation) {
		super(empl.length, matrix, probMutation);
		this.empl = empl;
		genesEmpl = new Gene[empl.length];
		genesCaract = new Gene[cf.getNumCompetences()*2];
		alpha=0.99f;
	}
	
	public Gene getGeneEmpl(int i){
		return genesEmpl[i];
	}
	
	public Point calculateCentroid(){
		float x=0, y=0;
		for(int i=0; i<genesCaract.length; i++){
			x += genesCaract[i].getX();
			y += genesCaract[i].getY();
		}
		x/=genesCaract.length;
		y/=genesCaract.length;
		return new Point(x,y);
	}
	
	public Gene getGeneCaract(int i){
		return genesCaract[i];
	}
	
	public int getNumGenes(){
		return genesEmpl.length;
	}
	
	public int getNumGenesCaract(){
		return genesCaract.length;
	}
	
	public int getNumPuntos(){
		if(genesEmpl[0]!=null){
			return genesEmpl[0].getPoints().length;
		}
		return -1;
	}
	
	public void setGeneEmpl(int i, Gene g){
		genesEmpl[i] = g;
	}
	
	public void setGeneCaract(int i, Gene g){
		genesCaract[i] = g;
	}
	
	private int calculateSizeGenesEmplPart(){
		int tam=0;
		for(int i=0; i<genesEmpl.length; i++){
			if(empl[i].existsImprecision()){
				tam+=31*5;
			}else{
				tam+=31*2;
			}
		}
		return tam;
	}
	
	private int calculateSizeGenesCaractPart(){
		return genesCaract.length*31*2;
	}
	
	public void rellenarDatosAleatorios(){
		for(int i=0; i<genesEmpl.length; i++){
			if(empl[i].existsImprecision()){
				genesEmpl[i] = new GeneEllipse();
			}else{
				genesEmpl[i] = new Gene(floatAleatorio(0,10), floatAleatorio(0,10));
			}
		}
		for(int i=0; i<genesCaract.length; i++){
			genesCaract[i] = new Gene(floatAleatorio(0,10), floatAleatorio(0,10));
		}
		fitness();
	}
	
	public void binaryToChromosome(String str){
		int index = 0;
		for(int i=0; i<genesEmpl.length; i++){
			if(empl[i].existsImprecision()){
				float x= binaryToFloat(str.substring(index, index+31));
				index+=31;
				float y= binaryToFloat(str.substring(index, index+31));
				index+=31;
				float a= binaryToFloat(str.substring(index, index+31));
				index+=31;
				float b= binaryToFloat(str.substring(index, index+31));
				index+=31;
				float rad= binaryToFloat(str.substring(index, index+31));
				index+=31;
				this.setGeneEmpl(i,new GeneEllipse(x, y, a, b, rad));
			}else{
				float x= binaryToFloat(str.substring(index, index+31));
				index+=31;
				float y= binaryToFloat(str.substring(index, index+31));
				index+=31;
				this.setGeneEmpl(i,new Gene(x, y));
			}
		}
		for(int i=0; i<genesCaract.length; i++){
			float x= binaryToFloat(str.substring(index, index+31));
			index+=31;
			float y= binaryToFloat(str.substring(index, index+31));
			index+=31;
			this.setGeneCaract(i,new Gene(x, y));
		}
		fitness();
	}

	public String chromosomeToBinary(){
		String str="";
		for(int i=0; i<genesEmpl.length; i++){
			if(empl[i].existsImprecision()){
				GeneEllipse ge = (GeneEllipse)genesEmpl[i];
				for(int j=0; j<5; j++){
					String aux="";
					if(j==0){
						aux = floatToBinaryString(ge.getX());
					}else if(j==1){
						aux = floatToBinaryString(ge.getY());
					}else if(j==2){
						aux = floatToBinaryString(ge.getA());
					}else if(j==3){
						aux = floatToBinaryString(ge.getB());
					}else if(j==4){
						aux = floatToBinaryString(ge.getRad());
					}
					int tam = aux.length();
					for(int k=0; k<31-tam; k++){
						aux = "0" + aux;
					}
					str += aux;
				}
			}else{
				for(int j=0; j<2; j++){
					String aux="";
					if(j==0){
						aux = floatToBinaryString(genesEmpl[i].getX());
					}else if(j==1){
						aux = floatToBinaryString(genesEmpl[i].getY());
					}
					int tam = aux.length();
					for(int k=0; k<31-tam; k++){
						aux = "0" + aux;
					}
					str += aux;
				}
			}
		}
		for(int i=0; i<genesCaract.length; i++){
			Gene ge = genesCaract[i];
			for(int j=0; j<2; j++){
				//Codifico la X y la Y
				String aux="";
				if(j==0){
					aux = floatToBinaryString(ge.getX());
				}else if(j==1){
					aux = floatToBinaryString(ge.getY());
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
		ChromosomeEllipse otherC = (ChromosomeEllipse)other;
		ChromosomeEllipse[] children = new ChromosomeEllipse[2];
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
		children[0] = new ChromosomeEllipse(empl, this.matrix, probMutation);
		children[1] = new ChromosomeEllipse(empl, this.matrix, probMutation);
		children[0].binaryToChromosome(strChild1);
		children[1].binaryToChromosome(strChild2);
		if(ptoCruce>this.calculateSizeGenesEmplPart()){
			//int geneImplicado = ptoCruce-this.calculateSizeGenesPart();
			//geneImplicado /= (31*2);
			//directedMutation(geneImplicado, children);
			directedMutation(children);
		}
		return children;
	}
	
	private void enfrentarPuntos(Gene g1, Gene g2, Point centroid) {
		float xu=g1.getX()-centroid.getX();
		float yu=g1.getY()-centroid.getY();
		float xv=g2.getX()-centroid.getX();
		float yv=g2.getY()-centroid.getY();
		float modU = (float) Math.sqrt(Math.pow(xu,2) + Math.pow(yu,2));
		float modV = (float) Math.sqrt(Math.pow(xv,2) + Math.pow(yv,2));
		float theta= (float) Math.atan(yu/xu);
		float theta1= (float) Math.atan(yv/xv);
		float theta2= (float) (alpha*theta+(1-alpha)*(theta1+Math.PI));
		//Calculo nuevo punto
		float u_prima_x = (float) (modU*Math.cos(theta2));
		float u_prima_y = (float) (modU*Math.sin(theta2));
		float newx1 = centroid.getX()+u_prima_x;
		float newy1 = centroid.getY()+u_prima_y;
		g1.setX(newx1);
		g1.setY(newy1);
	}

	
	private void directedMutation(ChromosomeEllipse[] children) {
		Point c=calculateCentroid();
		for(int i=0; i<genesCaract.length; i++){
			Gene g1 = children[0].genesCaract[i];
			Gene g2 = children[1].genesCaract[i];
			atraerPuntoARadioUnidad(g1, c);
			atraerPuntoARadioUnidad(g2, c);
		}
		/*for(int i=0; i<genesCaract.length/2; i++){
			Gene g1_NO = children[0].genesCaract[i];
			Gene g2_NO = children[1].genesCaract[i];
			Gene g1_ONLY = children[0].genesCaract[i+genesCaract.length/2];
			Gene g2_ONLY = children[1].genesCaract[i+genesCaract.length/2];
		}*/
		/*
		 * Algoritmos:
		 * x[i] = x_centro + R * sin(i*2.0*Math.PI/N);
		 * y[i] = y_centro + R * cos(i*2.0*Math.PI/N);
		 */
		for(int i=0; i<genesCaract.length; i++){
			Gene g1 = children[0].genesCaract[i];
			Gene g2 = children[1].genesCaract[i];
			float R=1;
			g1.x = (float) (alpha*g1.x + (1-alpha)*(c.getX() + R*Math.sin(i*2.0*Math.PI/genesCaract.length)));
			g1.y = (float) (alpha*g1.y + (1-alpha)*(c.getY() + R*Math.cos(i*2.0*Math.PI/genesCaract.length)));
			g2.x = (float) (alpha*g2.x + (1-alpha)*(c.getX() + R*Math.sin(i*2.0*Math.PI/genesCaract.length)));
			g2.y = (float) (alpha*g2.y + (1-alpha)*(c.getY() + R*Math.cos(i*2.0*Math.PI/genesCaract.length)));
		}
	}
	
	private float calculateAngle(Point p1, Point p2, Point center){
		Point v = p1.calculateVector(center);
		Point u = p2.calculateVector(center);
		float modV = (float) Math.sqrt(Math.pow(v.getX(), 2) + Math.pow(v.getY(), 2));
		float modU = (float) Math.sqrt(Math.pow(u.getX(), 2) + Math.pow(u.getY(), 2));
		float cosAng = (u.getX()*v.getX()+u.getY()*v.getY())/(modU*modV);
		return (float)Math.acos(cosAng);
	}

	private void atraerPuntoARadioUnidad(Gene g1 , Point centroid) {
		float newX, newY;
		float x = g1.getX();
		float y = g1.getY();
		float xc = centroid.getX();
		float yc = centroid.getY();
		newX=(float) ((alpha*x)+(1-alpha)*(xc+(x-xc)/Math.sqrt(Math.pow(x-xc,2) + Math.pow(y-yc,2))));
		newY=(float) ((alpha*y)+(1-alpha)*(yc+(y-yc)/Math.sqrt(Math.pow(x-xc,2) + Math.pow(y-yc,2))));
		g1.setX(newX);
		g1.setY(newY);
	}

	public double getMinDistance(Gene g1, Gene g2){
		double minDist = 9999;
		Point[] ptosGene1, ptosGene2;
		ptosGene1 = g1.getPoints();
		ptosGene2 = g2.getPoints();
		for(int i=0; i<ptosGene1.length; i++){
			for(int j=0; j<ptosGene2.length; j++){
				Point p1 = ptosGene1[i];
				Point p2 = ptosGene2[j];
				double dist = euclideanDistance(p1.getX(),p1.getY(),
						p2.getX(),p2.getY());
				if(minDist>dist){
					minDist = dist;
				}
				if(g1.containsPoint(p2) || g2.containsPoint(p1)){
					minDist = 0;
					return minDist; //Menor que 0 ya no hay nada
				}
			}
		}
		return minDist;
	}
	
	public double getMaxDistance(Gene g1, Gene g2){
		double maxDist = 0;
		Point[] ptosGene1, ptosGene2;
		ptosGene1 = g1.getPoints();
		ptosGene2 = g2.getPoints();
		for(int i=0; i<ptosGene1.length; i++){
			for(int j=0; j<ptosGene2.length; j++){
				Point p1 = ptosGene1[i];
				Point p2 = ptosGene2[j];
				double dist = euclideanDistance(p1.getX(),p1.getY(),
					p2.getX(), p2.getY());
				if(maxDist<dist){
					maxDist = dist;
				}
			}
		}
		return maxDist;
	}
	
	private void juntarGenes(Gene[] mix){
		int i;/*
		if(genesEmpl[genesEmpl.length-1]==null){
			int a=2;
		}*/
		for(i=0; i<genesEmpl.length; i++){
			mix[i] = genesEmpl[i];
		}
		for(int j=0; j<genesCaract.length; j++){
			mix[i] = genesCaract[j];
			i++;
		}
	}
	
	private double getMinValue(DistanceBean[][] matrix){
		double min=9999;
		for(int i=0; i<matrix.length; i++){
			for(int j=0; j<matrix[i].length; j++){
				if(min>matrix[i][j].getMin() && matrix[i][j].getMin()!=-1){
					min=matrix[i][j].getMin();
				}
				if(min>matrix[i][j].getMed()){
					min=matrix[i][j].getMed();
				}
			}
		}
		return min;
	}
	
	private double getMaxValue(DistanceBean[][] matrix){
		double max=-1;
		for(int i=0; i<matrix.length; i++){
			for(int j=0; j<matrix[i].length; j++){
				if(max<matrix[i][j].getMax() && matrix[i][j].getMax()!=-1){
					max=matrix[i][j].getMax();
				}
				if(max<matrix[i][j].getMed()){
					max=matrix[i][j].getMed();
				}
			}
		}
		return max;
	}
	
	private DistanceBean[][] calculateMatrixEuclDistChromosome(){
		int dim = genesEmpl.length+genesCaract.length;
		DistanceBean[][] mapDist = new DistanceBean[dim][dim];
		Gene[] mix = new Gene[dim];
		juntarGenes(mix);
		for(int i=0; i<mix.length; i++){
			for(int j=0; j<mix.length; j++){
				double distMed=-1, distMin=-1, distMax=-1;
				distMed=this.euclideanDistance(mix[i], mix[j]);
				if(matrix.getDistance(i,j).existsImprecision()){
					distMin=getMinDistance(mix[i], mix[j]);
					distMax=getMaxDistance(mix[i], mix[j]);
				}
				mapDist[i][j]=new DistanceBean(distMin, distMax, distMed);
			}
		}
		//Normalizo la matriz
		double min=-1, max=-1;
		min = getMinValue(mapDist);
		max = getMaxValue(mapDist);
		for(int i=0; i<mix.length; i++){
			for(int j=0; j<mix.length; j++){
				mapDist[i][j].setMed((mapDist[i][j].getMed()-min)/(max-min)*2);
				if(mapDist[i][j].getMax()!=-1 && mapDist[i][j].getMin()!=-1){
					mapDist[i][j].setMin((mapDist[i][j].getMin()-min)/(max-min)*2);
					mapDist[i][j].setMax((mapDist[i][j].getMax()-min)/(max-min)*2);
				}
			}
		}
		return mapDist;
	}
	
	private double fitness(){
		double fitness=0;
		Gene[] mix=new Gene[genesEmpl.length+genesCaract.length];
		DistanceBean[][] matrixMapa = calculateMatrixEuclDistChromosome();
		juntarGenes(mix);
		for(int i=0; i<genesEmpl.length; i++){
			for(int j=i+1; j<mix.length; j++){
				fitness += Math.pow(matrixMapa[i][j].getMed()-(matrix.getDistance(i, j)).getModalValue(), 2);
				if(matrix.getDistance(i, j).existsImprecision()){
					fitness += Math.pow(matrixMapa[i][j].getMin()-(matrix.getDistance(i, j)).getLeftBoundary(), 2);
					fitness += Math.pow(matrixMapa[i][j].getMax()-(matrix.getDistance(i, j)).getRightBoundary(), 2);
				}
			}
		}
		//Fitness de los puntos caracteristicos entre si
		for(int i=0; i<genesCaract.length; i++){
			for(int j=0; j<genesCaract.length; j++){
				if(i!=j){
					DistanceBean distEucl = matrixMapa[i+genesEmpl.length][j+genesEmpl.length];
					FuzzySet distNum = matrix.getDistance(i+genesEmpl.length, j+genesEmpl.length);
					fitness += Math.pow(distEucl.getMed()-distNum.getModalValue(), 2);
				}
			}
		}
		this.fitness=fitness;
		return fitness;
	}
	
	/*private double fitness(){
		double fitness=0;
		Gene[] mix=new Gene[genes.length+genesCaract.length];
		double[][] matrixEuclDist2D = calculateMatrixEuclDistChromosome();
		juntarGenes(mix);
		for(int i=0; i<genes.length; i++){
			for(int j=i+1; j<mix.length; j++){
				double distEucl = euclideanDistance(genes[i], mix[j]);
				FuzzySet distNum = matrix.getDistance(i, j);
				Point[] otherPoints; //Guardara los puntos del otro empleado
				if(j<genes.length){ //El otro punto es un empleado
					otherPoints=((GeneElipse)mix[j]).getPuntos();
				}else{ //El otro punto es un punto caracteristico
					otherPoints=new Point[1];
					otherPoints[0]=new Point(mix[j].x, mix[j].y);
				}
				//Calculo del fitness
				if(distNum instanceof ScalarNumber){
					fitness += Math.pow(distEucl-((ScalarNumber)distNum).getModalValue(), 2);
					fitness += Math.pow(distEucl-getMinDistance(genes[i].getPuntos(), otherPoints), 2);
					fitness += Math.pow(distEucl-getMaxDistance(genes[i].getPuntos(), otherPoints), 2);
				}else if(distNum instanceof CrispSet){
					CrispSet eval = (CrispSet)distNum;
					double minDist = eval.getLeftBoundary();
					double maxDist = eval.getRightBoundary();
					double puntoModal = Math.abs(eval.getModalValue());
					fitness += Math.pow(distEucl-puntoModal, 2);
					double min = getMinDistance(genes[i].getPuntos(), otherPoints);
					fitness += Math.pow(minDist-min, 2);
					double max = getMaxDistance(genes[i].getPuntos(), otherPoints);
					fitness += Math.pow(maxDist-max, 2);
				}
//				if(distNum instanceof EvalNumeric){
//						fitness += Math.pow(distEucl-((EvalNumeric)distNum).getMark(), 2);
//						fitness += Math.pow(distEucl-getMinDistance(genes[i].getPuntos(), genes[j].getPuntos()), 2);
//						fitness += Math.pow(distEucl-getMaxDistance(genes[i].getPuntos(), genes[j].getPuntos()), 2);
//					}else if(distNum instanceof EvalCrisp){
//						EvalCrisp eval = (EvalCrisp)distNum;
//						double minDist = eval.getMin();
//						double maxDist = eval.getMax();
//						double puntoModal = Math.abs(eval.getModal());
//						fitness += Math.pow(distEucl-puntoModal, 2);
//						double min = getMinDistance(genes[i].getPuntos(), genes[j].getPuntos());
//						fitness += Math.pow(minDist-min, 2);
//						double max = getMaxDistance(genes[i].getPuntos(), genes[j].getPuntos());
//						fitness += Math.pow(maxDist-max, 2);
//						/*fitness += Math.pow(minDist-(distEucl - genes[i].radius), 2);
//						fitness += Math.pow(maxDist-(distEucl + genes[i].radius), 2);
//						fitness += Math.pow(minDist-(distEucl - genes[j].radius), 2);
//						fitness += Math.pow(maxDist-(distEucl + genes[j].radius), 2);*/
//					}
/*			}
		}
		//Fitness de los puntos caracteristicos entre si
		for(int i=0; i<genesCaract.length; i++){
			for(int j=0; j<genesCaract.length; j++){
				if(i!=j){
					double distEucl = euclideanDistance(genesCaract[i], genesCaract[j]);
					FuzzySet distNum = matrix.getDistance(i+genes.length, j+genes.length);
					fitness += Math.pow(distEucl-((ScalarNumber)distNum).getModalValue(), 2);
				}
			}
		}
		normalizeCaractPoints();
		this.fitness=fitness;
		return fitness;
	}*/

	
}
