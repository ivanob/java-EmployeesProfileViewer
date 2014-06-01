package controller.persistence;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import rs.ac.ns.ftn.tmd.datatypes.DecomposedFuzzyNumber;
import rs.ac.ns.ftn.tmd.datatypes.FuzzyInterval;
import rs.ac.ns.ftn.tmd.datatypes.FuzzySet;
import rs.ac.ns.ftn.tmd.datatypes.ScalarNumber;

import controller.competences.Competence;
import controller.competences.CompetenceFactory;
import controller.competences.FuzzyVariableBean;
import controller.employees.Employee;
import controller.employees.EmployeeManager;
import controller.evaluations.Evaluation;
import controller.evaluations.Source;
import controller.evaluations.SourceFactory;

/**
 * Esta clase implementa las operaciones de guardado del estado
 * completo de la aplicacion en cuanto a datos de empleados,
 * competencias, fuentes y evaluaciones.
 * 
 * @author Ivan Obeso Aguera
 */
public class XMLWriter {

	private Document doc;
	private Element root;
	private CompetenceFactory cf;
	private SourceFactory sf;
	private EmployeeManager em;
	private String url;
	
	public XMLWriter(String url){
		try {
			cf = CompetenceFactory.getInstance();
			sf = SourceFactory.getInstance();
			em = EmployeeManager.getInstance();
			this.url=url;
			DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            doc = docBuilder.newDocument();
            root = doc.createElement("configuration");
            doc.appendChild(root);
		}catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Es el unico metodo accesible desde fuera de la clase y se encarga
	 * de llamar al resto de metodos privados para ir escribiendo
	 * cada seccion del fichero XML.
	 */
	public void writeConfiguration(){
		writeCompetences();
		writeSources();
		writeFuzzySets();
		writeemployees();
		volcarResultados();
	}
		
	private void writeemployees() {
		Element childemployees = doc.createElement("employees");
		root.appendChild(childemployees);
		for(int i=0; i<em.getNumEmployees(); i++){
			Element child = doc.createElement("employee");
			Employee e = em.getEmployee(i);
			child.setAttribute("name", e.getName());
			childemployees.appendChild(child);
			Element childEvaluations = doc.createElement("evaluations");
			child.appendChild(childEvaluations);
			for(int j=0; j<cf.getNumCompetences(); j++){
				Element eval = doc.createElement("evaluation");
				Competence c = cf.getCompetence(j);
				LinkedList<Evaluation> listEvals = e.getEvaluations(j);
				for(Evaluation ev : listEvals){
					eval.setAttribute("competence", c.getName());
					eval.setAttribute("source", ev.getSource().getName());
					eval.setAttribute("type", ev.getMark().getStrType());
					eval.appendChild(doc.createTextNode(ev.getStrValue()));
					childEvaluations.appendChild(eval);
				}
			}
		}
	}

	private void writeFuzzySets() {
		Element childFuzzysets = doc.createElement("fuzzysets");
		root.appendChild(childFuzzysets);
		for(int j=0; j<cf.getNumCompetences(); j++){
			Element child = doc.createElement("competence");
			Competence c = cf.getCompetence(j);
			child.setAttribute("name", c.getName());
			childFuzzysets.appendChild(child);
	        Element fuzzyvar = doc.createElement("fuzzyset");
	        child.appendChild(fuzzyvar);
	        for(int i=0; i<c.getNumFuzzyVariables(); i++){
		        FuzzyVariableBean fvb = c.getFuzzyVariable(i);
		        //fuzzyvar.setAttribute("type", "Decomposed");
		        Element label = doc.createElement("label");
		        if(fvb.getFuzzy() instanceof FuzzyInterval){
		        	label.setAttribute("type", "Interval");
		        }else if(fvb.getFuzzy() instanceof DecomposedFuzzyNumber){
		        	label.setAttribute("type", "Decomposed");
		        }
		        label.setAttribute("name", fvb.getLabel());
		        label.setAttribute("typeL", fvb.getLeftFunctionName());
		        label.setAttribute("typeR", fvb.getRightFunctionName());
		        //Escribo los puntos
		        Element point1 = doc.createElement("point");
		        Element point3 = doc.createElement("point");
		        double alpha=-1, beta=-1;
		        if(fvb.getFuzzy() instanceof FuzzyInterval){
		        	FuzzyInterval fi = (FuzzyInterval)fvb.getFuzzy();
		        	Element pointModalL = doc.createElement("point");
		        	Element pointModalR = doc.createElement("point");
		        	pointModalL.setAttribute("modalLeft", Double.toString(fi.getModalLeft()));
		        	pointModalR.setAttribute("modalRight", Double.toString(fi.getModalRight()));
		        	label.appendChild(pointModalL);
		        	label.appendChild(pointModalR);
		        	//alpha y beta
		        	alpha = fi.getModalLeft() - fvb.getFuzzy().getLeftBoundary();
			        beta = fvb.getFuzzy().getRightBoundary() - fi.getModalRight();
		        }else if(fvb.getFuzzy() instanceof DecomposedFuzzyNumber){
		        	Element pointModal = doc.createElement("point");
		        	pointModal.setAttribute("modal", Double.toString(fvb.getFuzzy().getModalValue()));
		        	label.appendChild(pointModal);
		        	alpha = fvb.getFuzzy().getModalValue() - fvb.getFuzzy().getLeftBoundary();
			        beta = fvb.getFuzzy().getRightBoundary() - fvb.getFuzzy().getModalValue();
		        }
		        point1.setAttribute("alpha", Double.toString(alpha));
		        point3.setAttribute("beta", Double.toString(beta));
		        label.appendChild(point1);
		        label.appendChild(point3);
		        fuzzyvar.appendChild(label);
	        }
		}
	}
	
	private void writeSources() {
		Element child = doc.createElement("sources");
        root.appendChild(child);
        for(int i=0; i<sf.getNumSources(); i++){
	        Element comp = doc.createElement("source");
	        Source s = sf.getSource(i);
	        comp.setAttribute("name", s.getName());
	        comp.setAttribute("distrust_max", Float.toString(s.getMaxDistrust()));
	        comp.setAttribute("distrust_min", Float.toString(s.getMinDistrust()));
	        child.appendChild(comp);
        }
	}

	private void volcarResultados(){
		try{
			//set up a transformer
	        TransformerFactory transfac = TransformerFactory.newInstance();
	        Transformer trans = transfac.newTransformer();
	        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	        trans.setOutputProperty(OutputKeys.INDENT, "yes");
	        //create string from xml tree
			StringWriter sw = new StringWriter();
	        StreamResult result = new StreamResult(sw);
	        DOMSource source = new DOMSource(doc);
	        trans.transform(source, result);
	        String xmlString = sw.toString();
	        //Write file
	        FileWriter fstream = new FileWriter(url);
	        BufferedWriter out = new BufferedWriter(fstream);
	        out.write(xmlString);
	        out.close();
	        fstream.close();
		}catch(TransformerConfigurationException ex){
			ex.printStackTrace();
		}catch(TransformerException ex){
			ex.printStackTrace();
		}catch(IOException ex){
			ex.printStackTrace();
		}
        
	}
	
	private void writeCompetences(){
		Element child = doc.createElement("competences");
        //child.setAttribute("name", "value");
        root.appendChild(child);
        for(int i=0; i<cf.getNumCompetences(); i++){
	        Element comp = doc.createElement("competence");
	        comp.setAttribute("name", cf.getCompetence(i).getName());
	        child.appendChild(comp);
        }
	}
}
