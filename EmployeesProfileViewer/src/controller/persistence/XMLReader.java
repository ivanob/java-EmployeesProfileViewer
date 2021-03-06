package controller.persistence;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import rs.ac.ns.ftn.tmd.datatypes.CrispSet;
import rs.ac.ns.ftn.tmd.datatypes.FuzzySet;
import rs.ac.ns.ftn.tmd.datatypes.ScalarNumber;

import controller.competences.Competence;
import controller.competences.CompetenceFactory;
import controller.competences.FuzzySetBean;
import controller.competences.FuzzyVariableBean;
import controller.employees.Employee;
import controller.employees.EmployeeManager;
import controller.evaluations.Evaluation;
import controller.evaluations.SourceFactory;

/**
 * Esta clase implementa las operaciones de lectura de ficheros
 * de configuracion XML con todos los datos acerca de empleados,
 * competencias, fuentes y evaluaciones guardados previamente.
 * 
 * @author Ivan Obeso Aguera
 */
public class XMLReader {
	
	private Document doc;
	private CompetenceFactory cf;
	private SourceFactory sf;

	public XMLReader(String url){
		try {
			cf = CompetenceFactory.getInstance();
			sf = SourceFactory.getInstance();
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docBuilder;
			docBuilder = docBuilderFactory.newDocumentBuilder();
			//doc = docBuilder.parse (new File("./data/fuzzysets.xml"));
	        doc = docBuilder.parse(new File(url));
	        doc.getDocumentElement().normalize();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Es el metodo principal de la clase y el unico accesible desde
	 * fuera. Llama al resto de metodos para ir leyendo cada seccion
	 * del archivo XML.
	 */
	public void readConfiguration(){
		readCompetences();
		readSources();
		readFuzzySets();
		readEmployees();
	}
	
	private void readCompetences(){
		int contID=0;
		NodeList listOfCompetences = doc.getElementsByTagName("competences").item(0).getChildNodes();
		LinkedList<Competence> listCompetences = new LinkedList<Competence>();
		for(int s=0; s<listOfCompetences.getLength(); s++){
			if(listOfCompetences.item(s) instanceof Element){
				Element comp = (Element)listOfCompetences.item(s);
		        Competence ac = new Competence(comp.getAttribute("name"),contID);
		        contID++;
		        listCompetences.add(ac);
			}
        }
        cf.setListCompetences(listCompetences);
	}
	
	private int readFunctionType(String label){
		int type = -1;
		if(label.compareTo("Linear")==0){
			type = FuzzySet.FUNCTION_LINEAR;
		}else if(label.compareTo("Exponential")==0){
			type = FuzzySet.FUNCTION_EXPONENTIAL;
		}else if(label.compareTo("Gaussian")==0){
			type = FuzzySet.FUNCTION_GAUSSIAN;
		}else if(label.compareTo("Quadratic")==0){
			type = FuzzySet.FUNCTION_QUADRATIC;
		}
		return type;
	}
	
	private void readSources(){
		SourceFactory sf = SourceFactory.getInstance();
		NodeList listOfSources = doc.getElementsByTagName("sources");
		listOfSources = listOfSources.item(0).getChildNodes();
        for(int i=0; i<listOfSources.getLength(); i++){
        	if(listOfSources.item(i) instanceof Element){
        		Element s = (Element)listOfSources.item(i);
        		String name = s.getAttribute("name");
        		String maxDistrust = s.getAttribute("distrust_max");
        		String minDistrust = s.getAttribute("distrust_min");
        		sf.addSource(name, Float.parseFloat(maxDistrust),
        				Float.parseFloat(minDistrust));
        	}
        }
        
	}
	
	private void readEmployees(){
		EmployeeManager em = EmployeeManager.getInstance();
		NodeList listOfemployees = doc.getElementsByTagName("employees");
		listOfemployees = listOfemployees.item(0).getChildNodes();
        for(int i=0; i<listOfemployees.getLength(); i++){
        	if(listOfemployees.item(i) instanceof Element){
        		Element empl = (Element)listOfemployees.item(i);
        		String name = empl.getAttribute("name");
        		Employee emp = new Employee(name, cf.getNumCompetences());
        		em.addEmployee(emp);
        		NodeList listEvals=empl.getElementsByTagName("evaluations").item(0).getChildNodes();
        		for(int j=0; j<listEvals.getLength(); j++){
        			if(listEvals.item(j) instanceof Element){
        				Element eval = (Element)listEvals.item(j);
        				String nameComp = eval.getAttribute("competence");
        				String nameSource = eval.getAttribute("source");
        				String nameType = eval.getAttribute("type");
        				String evalStr = eval.getChildNodes().item(0).getNodeValue().toString();
        				Evaluation ev = buildEvaluation(nameType, nameComp, nameSource, evalStr);
        				emp.addEvaluation(ev);
        			}
        		}
        	}
        }
	}
	
	private Evaluation buildEvaluation(String type, String compName, 
			String sourceName, String value){
		if(type.compareTo("fuzzy")==0){
			Competence c = cf.getCompetence(compName);
			FuzzyVariableBean fvb = c.getFuzzyVariable(value);
			int pos = c.getPositionFuzzyVariable(value);
			return new Evaluation(c, sf.getSource(sourceName), fvb.getFuzzy(), pos);
		}else if(type.compareTo("scalar")==0){
			Competence c = cf.getCompetence(compName);
			ScalarNumber sn = new ScalarNumber(Double.parseDouble(value));
			return new Evaluation(c, sf.getSource(sourceName), sn);
		}else if(type.compareTo("crisp")==0){
			Competence c = cf.getCompetence(compName);
			String[] trozos = value.substring(1, value.length()-1).split(", ");
			double minVal = Double.parseDouble(trozos[0]);
			double maxVal = Double.parseDouble(trozos[1]);
			CrispSet cs = new CrispSet(minVal, maxVal);
			return new Evaluation(c, sf.getSource(sourceName), cs);
		}
		return null;
	}
	
	private void readFuzzySets(){
		NodeList listOfCompetences = doc.getElementsByTagName("fuzzysets");
        listOfCompetences = listOfCompetences.item(0).getChildNodes();
        for(int i=0; i<listOfCompetences.getLength(); i++){
        	if(listOfCompetences.item(i) instanceof Element){
	        	Element comp = (Element)listOfCompetences.item(i);
	        	NodeList listFuzzysets = comp.getElementsByTagName("fuzzyset");
	        	for(int j=0; j<listFuzzysets.getLength(); j++){
	        		FuzzySetBean fuzzyset = new FuzzySetBean();
	        		Element fuzzy = (Element)listFuzzysets.item(j);
	        		NodeList listLabels = fuzzy.getElementsByTagName("label");
	        		for(int l=0; l<listLabels.getLength(); l++){
	        			Element label = (Element)listLabels.item(l);
	        			
	        			int tipoFuzzy = -1;
	        			/* Es un fuzzyNumber bajo la tecnica Decomposed */
	        			if(label.getAttribute("type").compareTo("Decomposed")==0){
	        				tipoFuzzy = FuzzyVariableBean.DECOMPOSED;
	        			}else if(label.getAttribute("type").compareTo("LRFuzzyNumber")==0){
	        				/* Es un fuzzyNumber bajo la tecnica LRFuzzyNumber */
	        				tipoFuzzy = FuzzyVariableBean.LRNUMBER;
	        			}else if(label.getAttribute("type").compareTo("Interval")==0){
	        				tipoFuzzy = FuzzyVariableBean.INTERVAL;
	        			}
	        			
	        			int typeL = -1, typeR = -1;
	        			double alpha = -1, beta = -1, modal = -1, modalR = -1;
	        			String name = label.getAttribute("name");
	        			typeL = readFunctionType(label.getAttribute("typeL"));
	        			typeR = readFunctionType(label.getAttribute("typeR"));
	        			NodeList listPoints = label.getElementsByTagName("point");
	        			for(int k=0; k<listPoints.getLength(); k++){
	        				Element point = (Element)listPoints.item(k);
	        				if(point.getAttribute("alpha").compareTo("")!=0){
	        					alpha = Double.parseDouble(point.getAttribute("alpha"));
	        				}else if(point.getAttribute("modal").compareTo("")!=0){
	        					modal = Double.parseDouble(point.getAttribute("modal"));
	        				}else if(point.getAttribute("beta").compareTo("")!=0){
	        					beta = Double.parseDouble(point.getAttribute("beta"));
	        				}else if(point.getAttribute("modalLeft").compareTo("")!=0){
	        					modal = Double.parseDouble(point.getAttribute("modalLeft"));
	        				}else if(point.getAttribute("modalRight").compareTo("")!=0){
	        					modalR = Double.parseDouble(point.getAttribute("modalRight"));
	        				}
	        			}
	        			FuzzyVariableBean fuzzyvar=null;
	        			if(tipoFuzzy == FuzzyVariableBean.DECOMPOSED){
	        				fuzzyvar = new FuzzyVariableBean(tipoFuzzy, name, modal, 
	        					alpha, beta, typeL, typeR);
	        			}else if(tipoFuzzy == FuzzyVariableBean.INTERVAL){
	        				fuzzyvar = new FuzzyVariableBean(tipoFuzzy, name, modal, 
	        					modalR, alpha, beta, typeL, typeR);
	        			}
	        			fuzzyset.addFuzzyVariable(fuzzyvar);
	        		}
	        		//Aniado el fuzzyset a la competencia correspondiente 
	        		cf.getCompetence(comp.getAttribute("name")).addFuzzySet(fuzzyset);
	        	}
        	}
        }
	}
}
