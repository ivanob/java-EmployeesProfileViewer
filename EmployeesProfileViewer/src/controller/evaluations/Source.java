package controller.evaluations;

/**
 * Representa una fuente de origen de una evaluacion. Puede ser
 * el origen de varias notas distintas. Tiene un nombre que la
 * identifica y un grado de desconfianza minimo y maximo. Estos
 * grados son numeros entre 0 y 1 y van desde la confianza
 * maxima hasta la desconfianza maxima respectivamente. Por ejemplo,
 * si una evaluacion es un 4 en una competencia y su fuente de origen
 * tiene unos grados de desconfianza maxima de 0.75 y minima de 0.1
 * quiere decir que entendemos que la nota inicial de 4 puede estar
 * muy cerca de 10, y sin embargo muy alejada de 0, puesto que la
 * desconfianza minima es muy pequeña.
 * 
 * @author Ivan Obeso Aguera
 * 
 */
public class Source {
	private String name;
	private float minDistrust, maxDistrust;
	
	public Source(String name, float minDistrust, float maxDistrust){
		this.setName(name);
		this.setMinDistrust(minDistrust);
		this.setMaxDistrust(maxDistrust);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getMinDistrust() {
		return minDistrust;
	}

	public void setMinDistrust(float minDistrust) {
		this.minDistrust = minDistrust;
	}

	public float getMaxDistrust() {
		return maxDistrust;
	}

	public void setMaxDistrust(float maxDistrust) {
		this.maxDistrust = maxDistrust;
	}
	
	
}
