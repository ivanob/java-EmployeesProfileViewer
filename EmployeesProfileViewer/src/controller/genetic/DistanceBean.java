package controller.genetic;

/**
 * Un objeto de esta clase representa la distancia entre 2 proyecciones
 * del mapa. Guarda todas las distancias posibles entre 2 proyecciones
 * sean del tipo que sean: distancia minima, maxima y distancias para
 * los posibles alpha cortes en caso de que una de las proyecciones
 * tenga evaluaciones borrosas.
 * 
 * @author Ivan Obeso Aguera
 */
public class DistanceBean {
	private double min, max, med;
	private double[] minCuts, maxCuts;
	
	public DistanceBean(double min, double max, double med){
		this.setMin(min);
		this.setMax(max);
		this.setMed(med);
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public double getMed() {
		return med;
	}

	public void setMed(double med) {
		this.med = med;
	}

	public double[] getMinCuts() {
		return minCuts;
	}

	public void setMinCuts(double[] minCuts) {
		this.minCuts = minCuts;
	}

	public double[] getMaxCuts() {
		return maxCuts;
	}

	public void setMaxCuts(double[] maxCuts) {
		this.maxCuts = maxCuts;
	}
}
