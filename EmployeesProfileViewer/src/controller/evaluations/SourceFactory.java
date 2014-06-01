package controller.evaluations;

import java.util.LinkedList;

/**
 * Es el gestor de fuentes de evaluaciones. Esta clase es un
 * singleton para garantizar que los objetos fuente son los
 * mismos sea cual sea la zona de la aplicacion desde la que
 * se accede a ellos.
 * 
 * @author Ivan Obeso Aguera
 */
public class SourceFactory {
	private static SourceFactory INSTANCE = new SourceFactory();
	private LinkedList<Source> listSources;
	
	public static SourceFactory getInstance(){
		return INSTANCE;
	}
	
	public void removeAllSources(){
		listSources = new LinkedList<Source>();
	}
	
	public int getNumSources(){
		return listSources.size();
	}
	
	public Source getSource(int i){
		return listSources.get(i);
	}
	
	public void addSource(String name, float maxDistrust, float minDistrust){
		listSources.add(new Source(name, minDistrust, maxDistrust));
	}
	
	private SourceFactory(){
		listSources = new LinkedList<Source>();
		//loadSources();
	}
	
	public Source getSource(String name){
		for(Source s : listSources){
			if(name.compareTo(s.getName())==0){
				return s;
			}
		}
		return null;
	}
	
	public Source getMaximunCertainSource(){
		return new Source("Max certain", 0 ,0);
	}

	public void removeSource(String name) {
		this.listSources.remove(getSource((name)));
	}
	
	/*private void loadSources(){
		try{
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
	        Document doc = docBuilder.parse (new File("./data/source.xml"));
	        doc.getDocumentElement().normalize();
	        
	        NodeList listOfSources = doc.getElementsByTagName("source");
	        for(int i=0; i<listOfSources.getLength(); i++){
	        	Element s = (Element)listOfSources.item(i);
	        	String name=s.getAttribute("name");
	        	float minDistrust=Float.parseFloat(s.getAttribute("distrust_min"));
	        	float maxDistrust=Float.parseFloat(s.getAttribute("distrust_max"));
	        	listSources.add(new Source(name, minDistrust, maxDistrust));
	        }
		}catch (SAXParseException err) {
	    }catch (SAXException e) {
	    }catch (Throwable t) {
	    }
        
	}*/
}
