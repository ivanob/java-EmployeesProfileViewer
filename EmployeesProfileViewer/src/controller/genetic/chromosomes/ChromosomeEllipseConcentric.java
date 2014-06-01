package controller.genetic.chromosomes;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import controller.competences.CompetenceFactory;
import controller.employees.Employee;
import controller.employees.EmployeeManager;
import controller.genetic.DistanceBean;
import controller.genetic.SimilarityMatrix;
import controller.genetic.genes.Gene;
import controller.genetic.genes.GeneEllipse;
import controller.genetic.genes.GeneEllipseConcentric;
import controller.geometry.Point;
import rs.ac.ns.ftn.tmd.datatypes.DecomposedFuzzyNumber;
import rs.ac.ns.ftn.tmd.datatypes.FuzzySet;

/**
 * Los mapas que tienen entre sus empleados a representar
 * alguno con incertidumbre borrosa se codifican como
 * cromosomas de tipo ChromosomeEllipseConcentric. Las
 * proyecciones de los empleados con incertidumbre
 * borrosa seran elipses rotadas (concentricas en caso
 * de haber definido alpha cortes para la solucion) mientras
 * que el resto de empleados cuyas evaluaciones sean todas
 * exactas son puntos.
 * 
 * Por esto, este cromosoma se forma por genes de tipo
 * Gene y GeneEllipseConcentric dependiendo de si la
 * proyeccion que representa es exacta o no.
 * 
 * @author Ivan Obeso Aguera
 */
public class ChromosomeEllipseConcentric extends Chromosome {

	//private Employer[] empl;
	private static EmployeeManager em = EmployeeManager.getInstance();
	private Gene[] genesEmpl;
	private float[] alphacuts;
	private int numAlphacuts;
	private Gene[] genesCaract;
	private CompetenceFactory cf = CompetenceFactory.getInstance();
	private float alpha; /* alpha=1 no se toca el punto.
	alpha=0 se recoloca el punto a su posicion ideal. */
	
	public ChromosomeEllipseConcentric(SimilarityMatrix matrix, 
			int probMutation, float[] alphacuts, float alpha) {
		super(em.getNumEmployees(), matrix, probMutation);
		this.alphacuts=alphacuts;
		this.numAlphacuts=alphacuts.length;
		genesEmpl = new Gene[em.getNumEmployees()];
		genesCaract = new Gene[cf.getNumCompetences()*2];
		this.alpha=alpha;//0.99f;
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
		/* Si la nube de puntos tiene un centroide por
		 * debajo de (5,5), entonces se fuerza a que
		 * el centroide sea este para impedir que haya
		 * puntos caracteristicos que caigan en el semieje
		 * y sea imposible codificarlo en binario, que
		 * solo acepta numeros positivos.
		 * Por ejemplo: si el centroide fuera (2,4) podria
		 * haber puntos que, al realizar la mutacion
		 * dirigida en torno a la circunferencia de radio 5,
		 * podría salir (2-5, 4-5) = (-3, -1)
		 */
		if(x<5){x=5;}
		if(y<5){y=5;}
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
		if(genesEmpl.length>0 && genesEmpl[0]!=null){
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
			if(em.getEmployee(i).existsFuzzyImprecision()){
				tam+=31*5+31*numAlphacuts;
			}else if(em.getEmployee(i).existsImprecision()){
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
			if(em.getEmployee(i).existsFuzzyImprecision()){
				genesEmpl[i] = new GeneEllipseConcentric(numAlphacuts);
			}else if(em.getEmployee(i).existsImprecision()){
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
	
	private void binaryToChromosome(String str){
		int index = 0;
		//Codifico los genes relativos a los empleados
		for(int i=0; i<genesEmpl.length; i++){
			Gene gene=null;
			if(em.getEmployee(i).existsFuzzyImprecision()){
				gene = new GeneEllipseConcentric(str.substring(index),numAlphacuts);
			}else if(em.getEmployee(i).existsImprecision()){
				gene = new GeneEllipse(str.substring(index));
			}else{
				gene = new Gene(str.substring(index));
			}
			this.setGeneEmpl(i, gene);
			index += gene.getCodedSize();
		}
		//Codifico los genes caracteristicos
		for(int i=0; i<genesCaract.length; i++){
			Gene gene = new Gene(str.substring(index));
			this.setGeneCaract(i, gene);
			index += gene.getCodedSize();
		}
		fitness();
	}

	private String chromosomeToBinary(){
		String str="";
		for(int i=0; i<genesEmpl.length; i++){
			str += genesEmpl[i].geneToString();
		}
		//Codifico los genes de los puntos caracteristicos
		for(int i=0; i<genesCaract.length; i++){
			//Gene ge = genesCaract[i];
			str += genesCaract[i].geneToString();
		}
		return str;
	}
	
	public Chromosome[] crossover(Chromosome other){
		ChromosomeEllipseConcentric otherC = (ChromosomeEllipseConcentric)other;
		ChromosomeEllipseConcentric[] children = new ChromosomeEllipseConcentric[2];
		int ptoCruce = -1;
		String strChro1 = this.chromosomeToBinary();
		String strChro2 = otherC.chromosomeToBinary();
		if(strChro1.length() == strChro2.length()){
			ptoCruce=intAleatorio(0,strChro1.length());
		}else{
			System.out.println("ERROR: Error en la codificacion de los cromosomas");
		}
		String trozo1Chro1 = strChro1.substring(0, ptoCruce);
		String trozo2Chro1 = strChro1.substring(ptoCruce, strChro1.length());
		String trozo1Chro2 = strChro2.substring(0, ptoCruce);
		String trozo2Chro2 = strChro2.substring(ptoCruce, strChro2.length());
		String strChild1 = trozo1Chro1 + trozo2Chro2;
		String strChild2 = trozo1Chro2 + trozo2Chro1;
		mutation(strChild1);
		mutation(strChild2);
		children[0] = new ChromosomeEllipseConcentric(this.matrix, probMutation, alphacuts, alpha);
		children[1] = new ChromosomeEllipseConcentric(this.matrix, probMutation, alphacuts, alpha);
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
	
	private void directedMutation(ChromosomeEllipseConcentric[] children) {
		/* Los puntos caracteristicos se atraen a la
		 * circunferencia de radio 5 con el mismo
		 * centroide que la nube de puntos 
		 * caracteristicos. 
		 */
		Point c=calculateCentroid();
		for(int i=0; i<genesCaract.length; i++){
			Gene g1 = children[0].genesCaract[i];
			Gene g2 = children[1].genesCaract[i];
			atraerPuntoARadioUnidad(g1, c);
			atraerPuntoARadioUnidad(g2, c);
		}
		/* Este codigo sirve para equidistanciar los puntos
		 * en torno a la circunferencia.
		 * Algoritmo:
		 * x[i] = x_centro + R * sin(i*2.0*Math.PI/N);
		 * y[i] = y_centro + R * cos(i*2.0*Math.PI/N);
		 */
		for(int i=0; i<genesCaract.length; i++){
			Gene g1 = children[0].genesCaract[i];
			Gene g2 = children[1].genesCaract[i];
			float R=5;
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
	
	private Geometry getCircunference(Point centroid, float radius){
		float yIter = centroid.getY() + radius;
		int numIters = 16;
		float incr = (radius*2)/(numIters+1);
		Coordinate[] coord = new Coordinate[numIters*2+1]; 
		for(int i=0; i<numIters; i++){
			yIter-=incr;
			float part = (float) Math.sqrt(Math.abs(Math.pow(radius,2)-Math.pow(yIter-centroid.getY(), 2)));
			float xIterPos = (float) centroid.getX()+part;
			float xIterNeg = (float) centroid.getX()-part;
			coord[i] = new Coordinate(xIterPos, yIter);
			coord[numIters*2-1-i] = new Coordinate(xIterNeg, yIter);
			
		}
		coord[numIters*2] = coord[0];
		Geometry geom = new GeometryFactory().createLinearRing(coord);
		return geom;
	}

	private void atraerPuntoARadioUnidad(Gene g1 , Point centroid) {
		/*Geometry circle = getCircunference(centroid, 5);
		Coordinate[] line = {new Coordinate(g1.x, g1.y), new Coordinate(centroid.getX(), centroid.getY())};
		Geometry lineGeometry = new GeometryFactory().createLineString(line);
		if(!lineGeometry.intersects(circle)){
			System.out.println("Fallo");
		}
		Geometry intersec = lineGeometry.intersection(circle);
		Coordinate[] resp = intersec.getCoordinates();
		g1.setX((float)resp[0].x);
		g1.setY((float)resp[0].y);
		System.out.println("X="+g1.getX() + ", Y="+g1.getY());*/
		double R=5;
		double vX = g1.getX() - centroid.getX();
		double vY = g1.getY() - centroid.getY();
		double magV = Math.sqrt(vX*vX + vY*vY);
		double aX = centroid.getX() + vX / magV * R;
		double aY = centroid.getY() + vY / magV * R;
		g1.setX((float) (alpha*g1.getX() + (1-alpha)*aX));
		g1.setY((float) (alpha*g1.getY() + (1-alpha)*aY));
	}
	
	public double getMinDistance(Gene g1, Gene g2){
		double minDist = Double.MAX_VALUE;
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
	
	private Point[] getMinDistancePoints(Gene g1, Gene g2){
		double minDist = Double.MAX_VALUE;
		Point[] ptosGene1, ptosGene2;
		Point[] resp = new Point[2];
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
					resp[0]=p1;
					resp[1]=p2;
				}
				if(g1.containsPoint(p2) || g2.containsPoint(p1)){
					minDist = 0;
					resp[0]=p1;
					resp[1]=p2;
				}
			}
		}
		return resp;
	}
	
	/**
	 * Este metodo recibe 2 genes del tipo que sean y devuelve
	 * los 2 puntos mas alejados de los perimetros de las 
	 * proyecciones de ambos genes. Devuelve un array de 2
	 * posiciones, en la primera guarda el punto del primer
	 * gen y en la segunda posicion el punto del segundo
	 * parametro.
	 * 
	 * @param g1
	 * @param g2
	 * @return
	 */
	private Point[] getMaxDistancePoints(Gene g1, Gene g2){
		double maxDist = -1;
		Point[] ptosGene1, ptosGene2;
		Point[] resp = new Point[2];
		ptosGene1 = g1.getPoints();
		ptosGene2 = g2.getPoints();
		for(int i=0; i<ptosGene1.length; i++){
			for(int j=0; j<ptosGene2.length; j++){
				Point p1 = ptosGene1[i];
				Point p2 = ptosGene2[j];
				double dist = euclideanDistance(p1.getX(),p1.getY(),
						p2.getX(),p2.getY());
				if(maxDist<dist){
					maxDist = dist;
					resp[0]=p1;
					resp[1]=p2;
				}
			}
		}
		return resp;
	}

	private double[] getMinDistanceCuts(Gene g1, Gene g2){
		double[] minDist = new double[this.numAlphacuts];
		Point[] minPoints = getMinDistancePoints(g1,g2);
		for(int i=0; i<this.numAlphacuts; i++){
			Point p1, p2;
			if(g1 instanceof GeneEllipseConcentric){
				p1 = ((GeneEllipseConcentric)g1).getPointAtDiv(i, minPoints[0]);
			}else{
				p1 = minPoints[0];
			}
			if(g2 instanceof GeneEllipseConcentric){
				p2 = ((GeneEllipseConcentric)g2).getPointAtDiv(i, minPoints[1]);
			}else{
				p2 = minPoints[1];
			}
			minDist[i] = euclideanDistance(p1.getX(), p1.getY(), p2.getX(), p2.getY());
		}
		return minDist;
	}
	
	/**
	 * Recibe 2 genes como parametros y devuelve un array con
	 * tantas posiciones como alpha-cortes haya establecidos.
	 * En cada posicion del array se guarda la distancia 
	 * existente entre los 2 puntos mas alejados de los 2
	 * genes para el alpha-corte en cuestion.
	 * 
	 * @param g1
	 * @param g2
	 * @return
	 */
	private double[] getMaxDistanceCuts(Gene g1, Gene g2){
		double[] maxDist = new double[this.numAlphacuts];
		Point[] maxPoints = getMaxDistancePoints(g1,g2);
		for(int i=0; i<this.numAlphacuts; i++){
			Point p1, p2;
			if(g1 instanceof GeneEllipseConcentric){
				p1 = ((GeneEllipseConcentric)g1).getPointAtDiv(i, maxPoints[0]);
			}else{
				p1 = maxPoints[0];
			}
			if(g2 instanceof GeneEllipseConcentric){
				p2 = ((GeneEllipseConcentric)g2).getPointAtDiv(i, maxPoints[1]);
			}else{
				p2 = maxPoints[1];
			}
			maxDist[i] = euclideanDistance(p1.getX(), p1.getY(), p2.getX(), p2.getY());
		}
		return maxDist;
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
		int i;
		for(i=0; i<genesEmpl.length; i++){
			mix[i] = genesEmpl[i];
		}
		for(int j=0; j<genesCaract.length; j++){
			mix[i] = genesCaract[j];
			i++;
		}
	}
	
	private double getMinValue(DistanceBean[][] matrix){
		double min=Double.MAX_VALUE;
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
				if(matrix.getDistance(i, j).existsFuzzyImprecision()){
					double[] minCuts=getMinDistanceCuts(mix[i], mix[j]);
					double[] maxCuts=getMaxDistanceCuts(mix[i], mix[j]);
					mapDist[i][j].setMaxCuts(maxCuts);
					mapDist[i][j].setMinCuts(minCuts);
				}
			}
		}
		//Normalizo la matriz
		double min=-1, max=-1;
		min = getMinValue(mapDist);
		max = getMaxValue(mapDist);
		for(int i=0; i<mix.length; i++){
			for(int j=0; j<mix.length; j++){
				mapDist[i][j].setMed((mapDist[i][j].getMed()-min)/(max-min)*1);
				if(mapDist[i][j].getMax()!=-1 && mapDist[i][j].getMin()!=-1){
					mapDist[i][j].setMin((mapDist[i][j].getMin()-min)/(max-min)*1);
					mapDist[i][j].setMax((mapDist[i][j].getMax()-min)/(max-min)*1);
				}
				if(mapDist[i][j].getMaxCuts()!=null && mapDist[i][j].getMinCuts()!=null){
					double[] maxCuts = mapDist[i][j].getMaxCuts();
					double[] minCuts = mapDist[i][j].getMinCuts();
					for(int k=0; k<maxCuts.length; k++){
						maxCuts[k] = (maxCuts[k]-min)/(max-min)*1;
						minCuts[k] = (minCuts[k]-min)/(max-min)*1;
					}
					mapDist[i][j].setMaxCuts(maxCuts);
					mapDist[i][j].setMinCuts(minCuts);
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
				//Evaluo las elipses concentricas de las evaluaciones fuzzy
				if(matrix.getDistance(i, j).existsFuzzyImprecision()){
					DecomposedFuzzyNumber dec = ((DecomposedFuzzyNumber)matrix.getDistance(i, j));
					double[] maxDist=matrixMapa[i][j].getMaxCuts();
					double[] minDist=matrixMapa[i][j].getMinCuts();
					for(int k=0; k<numAlphacuts; k++){
						double[] cuts=dec.getAlphaCut(this.alphacuts[k]);
						fitness += Math.pow(minDist[k]-cuts[0],2);
						fitness += Math.pow(maxDist[k]-cuts[1],2);
					}
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
	
}
