package controller.genetic;

import java.util.LinkedList;

/**
 * Esta clase encapsula la configuracion que el usuario
 * ha seleccionado a la hora de lanzar una ejecucion
 * del algoritmo de representacion del mapa. Es simplemente
 * un bean contenedor de esos datos.
 * 
 * @author Ivan Obeso Aguera
 */
public class GeneticConfigBean {
	private float[] alphacuts;
	private int numPointsPerimeterShapes;
	private float alphaSmoothing;
	private int numIterations;
	private int numSelectedIndividuals;
	private int numPopulation;
	private double mutationProbability;
	
	public GeneticConfigBean(){
		float[] alphacuts = new float[1];
		alphacuts[0]=0.5f;
		this.setAlphacuts(alphacuts);
		this.setNumPointsPerimeterShapes(16);
		this.setAlphaSmoothing(0.99f);
		this.setNumIterations(100);
		this.setNumSelectedIndividuals(500);
		this.setNumPopulation(1000);
		this.setMutationProbability(0.001);
	}
	
	public GeneticConfigBean(float[] alphacuts, int numPointsPerimeterShapes,
			float alphaSmoothing, int numIterations, int numSelectedIndividuals,
			int numPopulation, double mutationProbability){
		this.setAlphacuts(alphacuts);
		this.setNumPointsPerimeterShapes(numPointsPerimeterShapes);
		this.setAlphaSmoothing(alphaSmoothing);
		this.setNumIterations(numIterations);
		this.setNumSelectedIndividuals(numSelectedIndividuals);
		this.setNumPopulation(numPopulation);
		this.setMutationProbability(mutationProbability);
	}

	public float[] getAlphacuts() {
		return alphacuts;
	}

	public void setAlphacuts(float[] alphacuts) {
		this.alphacuts = alphacuts;
	}

	public int getNumPointsPerimeterShapes() {
		return numPointsPerimeterShapes;
	}

	public void setNumPointsPerimeterShapes(int numPointsPerimeterShapes) {
		this.numPointsPerimeterShapes = numPointsPerimeterShapes;
	}

	public float getAlphaSmoothing() {
		return alphaSmoothing;
	}

	public void setAlphaSmoothing(float alphaSmoothing) {
		this.alphaSmoothing = alphaSmoothing;
	}

	public int getNumIterations() {
		return numIterations;
	}

	public void setNumIterations(int numIterations) {
		this.numIterations = numIterations;
	}

	public int getNumSelectedIndividuals() {
		return numSelectedIndividuals;
	}

	public void setNumSelectedIndividuals(int numSelectedIndividuals) {
		this.numSelectedIndividuals = numSelectedIndividuals;
	}

	public int getNumPopulation() {
		return numPopulation;
	}

	public void setNumPopulation(int numPopulation) {
		this.numPopulation = numPopulation;
	}

	public double getMutationProbability() {
		return mutationProbability;
	}

	public void setMutationProbability(double mutationProbability) {
		this.mutationProbability = mutationProbability;
	}
}
