package view.GUI;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.ElipseGUIBean;
import model.GUIDataBean;
import model.PointGUIBean;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.MatrixSeriesCollection;
import org.jfree.data.xy.NormalizedMatrixSeries;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.XYZDataset;
import org.jfree.ui.ExtensionFileFilter;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.TextAnchor;

import controller.competences.CompetenceFactory;
import controller.employees.Employee;
import controller.employees.EmployeeManager;
import controller.evaluations.SourceFactory;
import controller.genetic.Genetic;
import controller.genetic.SimilarityMatrix;
import controller.geometry.Circle;
import controller.geometry.Point;
import controller.persistence.XMLReader;
import controller.persistence.XMLWriter;



public class Grafica extends JFrame{

	private static final long serialVersionUID = 1L;
	private XYPlot xyplot;
	private XYSeriesCollection xyseriescollection;
	private LinkedList<XYTextAnnotation> listAnotations = new LinkedList<XYTextAnnotation>();
	private JFreeChart chart;
	private LegendTitle leyenda;
	private XYLineAndShapeRenderer renderer1;
	private GUIDataBean graphicBean;
	private LinkedList<Point[]>puntos;
	private int tipoGrafico;
	private int numPuntos;
	private Grafica ref = this;
	private JTextField statusLabelFitness, statusLabelNroEmpl;
	private JTextField statusLabelNroComp, statusLabelTime;
	private JCheckBoxMenuItem opcAnotacionesEmpl;
	private Color[] colors = {Color.BLUE, Color.ORANGE, Color.RED, 
			Color.GREEN, Color.MAGENTA, Color.BLACK, Color.GRAY, 
			Color.PINK, Color.YELLOW, Color.CYAN};
		
	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Grafica window = new Grafica();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/
	
	private void borrarSesion(){
		EmployeeManager.getInstance().removeAllEmployees();
    	CompetenceFactory.getInstance().removeAllCompetences();
    	SourceFactory.getInstance().removeAllSources();
    	limpiarGrafico();
	}
	
	private void limpiarGrafico(){
		xyseriescollection.removeAllSeries();
		for(XYTextAnnotation annotation : listAnotations){
			xyplot.removeAnnotation(annotation);
		}
		listAnotations = new LinkedList<XYTextAnnotation>();
	}
	
	private void crearMenuAplicacion(){
		JMenuBar menubar = new JMenuBar();
		//Item File
		JMenu menuFile = new JMenu("File");
		JMenuItem eMenuNew = new JMenuItem("New");
		eMenuNew.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	borrarSesion();
            }

        });
		menuFile.add(eMenuNew);
		JMenuItem eMenuOpen = new JMenuItem("Open configuration...");
		eMenuOpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	JFileChooser chooser = new JFileChooser();
            	FileNameExtensionFilter filter1 = new FileNameExtensionFilter("xml", new String[] { "xml"});
                chooser.setFileFilter(filter1);
                int returnVal = chooser.showOpenDialog(null);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                	borrarSesion();
                	System.out.println("You chose to open this file: " +
                        chooser.getSelectedFile().getAbsolutePath());
                	XMLReader reader = new XMLReader(chooser.getSelectedFile().getAbsolutePath());
            		reader.readConfiguration();
                }
            }

        });
		menuFile.add(eMenuOpen);
		JMenuItem eMenuSave = new JMenuItem("Save configuration...");
		eMenuSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	JFileChooser chooser = new JFileChooser();
            	FileNameExtensionFilter filter1 = new FileNameExtensionFilter("xml", new String[] { "xml"});
                chooser.setFileFilter(filter1);
                int returnVal = chooser.showSaveDialog(null);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                	System.out.println("You chose to save this file: " +
                        chooser.getSelectedFile().getAbsolutePath());
                	String url = chooser.getSelectedFile().getAbsolutePath();
                	if(url.substring(url.length()-4, url.length()-1).compareTo(".xml")!=0){
                		url += ".xml";
                	}
                	XMLWriter writer = new XMLWriter(url);
                	writer.writeConfiguration();
                }
            }

        });
		menuFile.add(eMenuSave);
		
		menuFile.addSeparator();
		JMenuItem eMenuItem = new JMenuItem("Exit");
		eMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }

        });
		menuFile.add(eMenuItem);
		menubar.add(menuFile);
		//Item Edit
		JMenu menuEdit = new JMenu("Edit");
		//Opcion para editar fuzzysets competencias
		JMenuItem eMenuEditComp = new JMenuItem("Competences fuzzysets...");
		eMenuEditComp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				jDialogEditCompetences dialog = new jDialogEditCompetences();
				dialog.show();
			}
			
		});
		menuEdit.add(eMenuEditComp);
		//Opcion para editar empleados
		JMenuItem eMenuEditEmpl = new JMenuItem("Employees/Evaluations...");
		eMenuEditEmpl.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				jDialogEditEmployee dialog = new jDialogEditEmployee();
				dialog.show();
			}
			
		});
		menuEdit.add(eMenuEditEmpl);
		//Opcion para editar sources
		JMenuItem eMenuEditSources = new JMenuItem("Sources...");
		eMenuEditSources.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				jDialogEditSources dialog = new jDialogEditSources();
					dialog.show();
				}
			});
		menuEdit.add(eMenuEditSources);
		//Opcion para añadir competencias
		JMenuItem eMenuAddComp = new JMenuItem("Competences...");
		eMenuAddComp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				jDialogAddCompetence dialog = new jDialogAddCompetence();
				dialog.show();
			}
		});
		menuEdit.add(eMenuAddComp);
		menubar.add(menuEdit);
		
		//Item Run
		JMenu menuRun = new JMenu("Run");
		JMenuItem eMenuRun = new JMenuItem("Run...");
		menuRun.add(eMenuRun);
		menubar.add(menuRun);
		eMenuRun.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				jDialogRun dialogRun = new jDialogRun();
				dialogRun.setModal(true);
		    	dialogRun.setVisible(true);
		    	graphicBean = dialogRun.getGUIDataBean();
		    	if(graphicBean != null){
		    		repaintGraphic();
		    	}
			}
		});
		
		//Item View
		JMenu menuView = new JMenu("View");
		
		JMenu submenuLeyenda = new JMenu("Leyend");
		menuView.add(submenuLeyenda);
		final JCheckBoxMenuItem opcionNoLeyenda = new JCheckBoxMenuItem("Hide leyend");
		final JCheckBoxMenuItem opcionLeyendaDcha = new JCheckBoxMenuItem("Show right");
		final JCheckBoxMenuItem opcionLeyendaAbajo = new JCheckBoxMenuItem("Show down");
		opcionNoLeyenda.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(chart.getLegend()==null){
					chart.addLegend(leyenda);
				}
				chart.removeLegend();
				opcionLeyendaAbajo.setSelected(false);
				opcionLeyendaDcha.setSelected(false);
				opcionNoLeyenda.setSelected(true);
			}
		});
	    submenuLeyenda.add(opcionNoLeyenda);
	    submenuLeyenda.addSeparator();
	    
	    opcionLeyendaAbajo.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(chart.getLegend()==null){
					chart.addLegend(leyenda);
				}
				chart.getLegend().setPosition(RectangleEdge.BOTTOM);
				opcionLeyendaAbajo.setSelected(true);
				opcionLeyendaDcha.setSelected(false);
				opcionNoLeyenda.setSelected(false);
			}
		});
	    submenuLeyenda.add(opcionLeyendaAbajo);
	    opcionLeyendaAbajo.setSelected(true);
	    opcionLeyendaDcha.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(chart.getLegend()==null){
					chart.addLegend(leyenda);
				}
				chart.getLegend().setPosition(RectangleEdge.RIGHT);
				opcionLeyendaDcha.setSelected(true);
				opcionLeyendaAbajo.setSelected(false);
				opcionNoLeyenda.setSelected(false);
			}
			
		});
	    submenuLeyenda.add(opcionLeyendaDcha);
	    
//	    final JCheckBoxMenuItem opcionConexiones = new JCheckBoxMenuItem("Show connections");
//	    opcionConexiones.addActionListener(new ActionListener(){
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				mostrarPuntosDePoligonos(opcionConexiones.isSelected());
//			}
//	    });
//	    menuView.add(opcionConexiones);
	    
	    //Submenu 
	    opcAnotacionesEmpl = new JCheckBoxMenuItem("Show labels");
	    opcAnotacionesEmpl.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mostrarAnotaciones(opcAnotacionesEmpl.isSelected());
			}
	    });
	    menuView.add(opcAnotacionesEmpl);
		menubar.add(menuView);
		
		JMenu menuHelp = new JMenu("Help");
		JMenuItem eMenuAbout = new JMenuItem("About");
		eMenuAbout.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				jDialogAbout dialog = new jDialogAbout();
				dialog.show();
			}
		});
		menuHelp.add(eMenuAbout);
		menubar.add(menuHelp);
		
		//Menu de Opciones
//		JMenu menuOpciones = new JMenu("Options");
//		JMenu submenuDistancias = new JMenu("Distance method");
//		menuOpciones.add(submenuDistancias);
//		menubar.add(menuOpciones);
//		final JCheckBoxMenuItem opcionEuclideanDist = new JCheckBoxMenuItem("Euclidean");
//		final JCheckBoxMenuItem opcionTchebyDist = new JCheckBoxMenuItem("Tchebyschevt");
//		submenuDistancias.add(opcionEuclideanDist);
//		submenuDistancias.add(opcionTchebyDist);
//		opcionEuclideanDist.setSelected(true);
//		opcionEuclideanDist.addActionListener(new ActionListener(){
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				select.setMetodoDistancias(FuzzyEvaluation.EUCLIDEAN_DIST);
//				opcionEuclideanDist.setSelected(true);
//				opcionTchebyDist.setSelected(false);
//				repintarGrafico();
//			}
//		});
//		opcionTchebyDist.addActionListener(new ActionListener(){
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				select.setMetodoDistancias(FuzzyEvaluation.TCHEBYSCHEV_DIST);
//				opcionEuclideanDist.setSelected(false);
//				opcionTchebyDist.setSelected(true);
//				repintarGrafico();
//			}
//		});
//		final JCheckBoxMenuItem opcionUsarCertezas = new JCheckBoxMenuItem("Use certainties");
//		menuOpciones.add(opcionUsarCertezas);
//		opcionUsarCertezas.setSelected(true);
//		opcionUsarCertezas.addActionListener(new ActionListener(){
//
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				select.setUsarCertezaEvaluaciones(opcionUsarCertezas.isSelected());
//				repintarGrafico();
//			}
//			
//		});
		
		setJMenuBar(menubar);
		
	}
	
	private void mostrarAnotaciones(boolean mostrar){
		if(mostrar && graphicBean!=null){
			/*for(int i=0; i<ef.getNumEmployers(); i++){
				CartesianPoint medio = centrosEmpl.get(i);
				listEmployAnotations.add(this.aniadirAnotacion(medio.getX(), medio.getY(), ef.getEmployer(i).getName()));
			}*/
			for(int i=0; i<graphicBean.getNumCP(); i++){
				PointGUIBean pGUI = graphicBean.getCP(i);
				Point p=pGUI.getPoint();
				listAnotations.add(aniadirAnotacion(p, pGUI.getName()));
			}
			for(int i=0; i<graphicBean.getNumPointEmpl(); i++){
				PointGUIBean pGUI = graphicBean.getPointEmpl(i);
				Point p=pGUI.getPoint();
				listAnotations.add(aniadirAnotacion(p, pGUI.getName()));
			}
			for(int i=0; i<graphicBean.getNumEmplEllipse(); i++){
				ElipseGUIBean eGUI = graphicBean.getElipseBean(i);
				Point p=eGUI.getPerimetro()[0];
				listAnotations.add(aniadirAnotacion(p, eGUI.getName()));
				for(int j=0; j<eGUI.getNumConcentricElipse(); j++){
					Point p2=eGUI.getConcentricElipse(j)[0];
					listAnotations.add(aniadirAnotacion(p2, eGUI.getLevelName(j)));
				}
			}
		}else{
			for(XYTextAnnotation a : listAnotations){
				xyplot.removeAnnotation(a);
			}
		}
	}
	
	private void mostrarPuntosDePoligonos(boolean mostrar){
		/*for(int i=0; i<graphicBean.getNumEmplEllipse(); i++){
			renderer1.setSeriesShapesVisible(i, mostrar);
			for(int j=0; j<graphicBean.getElipseBean(i).getNumConcentricElipse(); j++){
				renderer1.setSeriesShapesVisible(j+1, mostrar);
			}
		}*/
		int num=0;
		for(int i=0; i<graphicBean.getNumEmplEllipse(); i++){
			num++;
			for(int j=0; j<graphicBean.getElipseBean(i).getNumConcentricElipse(); j++){
				num++;
			}
		}
		for(int i=0; i<num; i++){
			renderer1.setSeriesShapesVisible(i, false);
		}
	}
	
	private Color getColor(int i){
		if(i>colors.length-1){
			i%=colors.length;
		}
		return colors[i];
	}
	
	public void repintarGrafico(){
		//Limpio el grafico
		xyseriescollection.removeAllSeries();
		for(int i=0; i<listAnotations.size(); i++){
			xyplot.removeAnnotation(listAnotations.get(i));
		}
		for(int i=0; i<listAnotations.size(); i++){
			listAnotations.remove();
		}
		//Repinto los puntos
		if(tipoGrafico == Genetic.POINT){
			for(int i=0; i<puntos.size(); i++){
				//aniadirPunto(puntos[i].getX(), puntos[i].getY(), "Empl " + i);
			}
		}else if(tipoGrafico == Genetic.CIRCLE){
			for(int i=0; i<puntos.size(); i++){
				//aniadirBurbuja(puntos.get(i)[0].getX(), puntos.get(i)[0].getY(), puntos.get(i)[0].getRadius(), "Empl " + i);
			}
		}else if(tipoGrafico == Genetic.ELIPSE){
			int numSerie=0;
			Paint color;
			for(int i=0; i<graphicBean.getNumEmplEllipse(); i++){
				ElipseGUIBean elipseBean = graphicBean.getElipseBean(i);
				Point[] perim = elipseBean.getPerimetro();
				color = getColor(i);
				pintarPoligono(getXArray(perim), getYArray(perim), elipseBean.getName(), false, numSerie,color);
				numSerie++;
				//Pinto las posibles elipses concentricas
				for(int j=0; j<elipseBean.getNumConcentricElipse(); j++){
					Point[] concentric = elipseBean.getConcentricElipse(j);
					pintarPoligono(getXArray(concentric), getYArray(concentric), 
						elipseBean.getName() + "(" +elipseBean.getLevelName(j) + ")",
						true, numSerie, color);
					numSerie++;
				}
			}
			//Pinto los empleados cuya representacion es un punto
			for(int i=0; i<graphicBean.getNumPointEmpl(); i++){
				PointGUIBean p = graphicBean.getPointEmpl(i);
				this.aniadirPunto(p.getPoint().getX(), p.getPoint().getY(), p.getName());
			}
		}
		mostrarPuntosDePoligonos(false);
		mostrarAnotaciones(opcAnotacionesEmpl.isSelected());
		//puntosCaracteristicos=new CartesianPoint[select.getNumSelectedCompetences()*2];
		//dibujarPuntosCaracteristicos();
		/*double[] xs = new double[polygon.length];
		double[] ys = new double[polygon.length];
		for(int i=0; i<polygon.length;i++){
			xs[i] = polygon[i].getX();
			ys[i] = polygon[i].getY();
		}
		this.pintarPoligono(xs, ys, "Luis");*/
		
		//dibujarEmpleados();
		//mostrarPuntosDePoligonos(false);
	}
	
	private double[] getXArray(Point[] p){
		double[] x = new double[p.length];
		for(int i=0; i<p.length; i++){
			x[i] = p[i].getX();
		}
		return x;
	}
	
	private double[] getYArray(Point[] p){
		double[] y = new double[p.length];
		for(int i=0; i<p.length; i++){
			y[i] = p[i].getY();
		}
		return y;
	}

	private void aniadirBurbuja(float x, float y, float radius, String pointName) {
		String nombre= pointName;
	    XYSeries xyseries = new XYSeries(nombre, false);
	    xyseries.add(x, y);
	    xyseriescollection.addSeries(xyseries);	
	}

	private NormalizedMatrixSeries createInitialBubbleSeries() {
        final NormalizedMatrixSeries newSeries =
            new NormalizedMatrixSeries("Sample Grid 1", 10, 10);

        // seed a few random bubbles
        for (int count = 0; count < 10; count++) {
            final int i = (int) (Math.random() * 10);
            final int j = (int) (Math.random() * 10);

            final int mij = (int) (Math.random() * 10);
            newSeries.update(i, j, mij);
        }

        newSeries.setScaleFactor(newSeries.getItemCount());

        return newSeries;
    }
	
	private static JFreeChart createChart(XYZDataset dataset) {   
        JFreeChart chart = ChartFactory.createBubbleChart(   
            "Bubble Chart Demo 1",   
            "X",    
            "Y",    
            dataset,    
            PlotOrientation.HORIZONTAL,   
            true,    
            true,    
            false   
        );   
        XYPlot plot = (XYPlot)chart.getPlot();   
        plot.setForegroundAlpha(0.65f);
           
        XYItemRenderer renderer = plot.getRenderer();   
        renderer.setSeriesPaint(0, Color.blue);   
   
        // increase the margins to account for the fact that the auto-range    
        // doesn't take into account the bubble size...   
        ValueAxis va = plot.getDomainAxis();
		va.setRange(-1,11);
		va = plot.getRangeAxis();
		va.setRange(-1,11);  
        return chart;   
    }
	
	public Grafica() {
		crearMenuAplicacion();
		this.setTitle("Map of Profiles");
		this.tipoGrafico = Genetic.ELIPSE;
		
		initialize();
		this.numPuntos = numPuntos;
		this.puntos = puntos;
		xyplot = createInitialSeries();
		
		ValueAxis va = xyplot.getDomainAxis();
		va.setRange(-1,11);
		va = xyplot.getRangeAxis();
		va.setRange(-1,11);
		if(tipoGrafico == Genetic.POINT || tipoGrafico == Genetic.ELIPSE){
			chart = new JFreeChart("Prototipo competencias", JFreeChart.DEFAULT_TITLE_FONT, xyplot, true);
		}
		leyenda = chart.getLegend();
		
        // we put the chart into a panel
        ChartPanel chartPanel = new ChartPanel(chart);
        
        // default size
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        // add it to our application
        //frame.setContentPane(chartPanel);
        setContentPane(chartPanel);
		
		setVisible(true);
		this.setLocationRelativeTo(null);
	}
	
	/**
	 * Create the application.
	 */
	public Grafica(GUIDataBean graphicBean, int tipoGrafico) {
		this.setTitle("Prototype Employees Profile Viewer");
		this.tipoGrafico = tipoGrafico;
		crearMenuAplicacion();
		this.setLayout(new BorderLayout());
		
		this.graphicBean = graphicBean;
		initialize();
		xyplot = createInitialSeries();
		
		ValueAxis va = xyplot.getDomainAxis();
		va.setRange(-1,11);
		va = xyplot.getRangeAxis();
		va.setRange(-1,11);
		
		if(tipoGrafico == Genetic.POINT || tipoGrafico == Genetic.ELIPSE){
			chart = new JFreeChart("Map of Profiles", JFreeChart.DEFAULT_TITLE_FONT, xyplot, true);
		}else if(tipoGrafico == Genetic.CIRCLE){
			chart = new JFreeChart("Map of Profiles", JFreeChart.DEFAULT_TITLE_FONT, xyplot, true);
			/*DefaultXYZDataset dataset = new DefaultXYZDataset();
			
			for(int i=0; i<puntos.length; i++){
				double[][] data = new double[3][1];
				for(int j=0; j<3; j++){
					if(j==2 && puntos[i].get(j)<0.1){
						data[j][0] = 0.1;
					}else{
						data[j][0] = puntos[i].get(j);
					}
				}
				dataset.addSeries(i, data);
			}
			
			chart = createChart(dataset);*/
		}
		leyenda = chart.getLegend();
		
        // we put the chart into a panel
        ChartPanel chartPanel = new ChartPanel(chart);
        
        // default size
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        
        //setContentPane(chartPanel);
        this.add(chartPanel, BorderLayout.CENTER);
//		repintarGrafico(); //Dibujo todos los elementos del grafico
//		dibujarPuntosCaracteristicos();
		
        crearBarraStatus();
		setVisible(true);
		this.setLocationRelativeTo(null);
	}
	
	private void crearBarraStatus(){
		JPanel barraEstado = new JPanel(new FlowLayout());
        barraEstado.setBorder(new BevelBorder(BevelBorder.LOWERED));
        barraEstado.setPreferredSize(new Dimension(this.getWidth(), 25));
        barraEstado.setLayout(new BoxLayout(barraEstado, BoxLayout.X_AXIS));
        //Fitness
        statusLabelFitness = new JTextField("");
        statusLabelFitness.setEditable(false);
        statusLabelFitness.setHorizontalAlignment(SwingConstants.LEFT);
        //Numero de empleados
        statusLabelNroEmpl = new JTextField("");
        statusLabelNroEmpl.setEditable(false);
        statusLabelNroEmpl.setHorizontalAlignment(SwingConstants.LEFT);
        //Numero de competencias
        statusLabelNroComp = new JTextField("");
        statusLabelNroComp.setEditable(false);
        statusLabelNroComp.setHorizontalAlignment(SwingConstants.LEFT);
        //Tiempo de ejecucion
        statusLabelTime = new JTextField("");
        statusLabelTime.setEditable(false);
        statusLabelTime.setHorizontalAlignment(SwingConstants.LEFT);
        //Aniado las etiquetas a la barra de estado
        barraEstado.add(statusLabelFitness);
        barraEstado.add(statusLabelNroEmpl);
        barraEstado.add(statusLabelNroComp);
        barraEstado.add(statusLabelTime);
        this.add(barraEstado, BorderLayout.PAGE_END);
	}
	
	private void repaintGraphic(){
		repintarGrafico(); //Dibujo todos los elementos del grafico
		dibujarPuntosCaracteristicos();
		actualizarBarraEstado();
	}
	
	private void actualizarBarraEstado(){
		statusLabelFitness.setText("Fitness: " + this.graphicBean.getFitness());
		int numEmpl=EmployeeManager.getInstance().getNumEmployees();
		statusLabelNroEmpl.setText("Number of employees: " + numEmpl);
		int numComp=CompetenceFactory.getInstance().getNumCompetences();
		statusLabelNroComp.setText("Number of competences: " + numComp);
		statusLabelTime.setText("Map generated in " + this.graphicBean.getTime() + " ms");
	}

	private void pintarPoligono(double[]x, double[]y, String employeeName, boolean dashed, int numSerie, Paint color){
		String nombreSerie = employeeName;
		XYSeries xyseries = new XYSeries(nombreSerie, false);
		for(int i=0; i<x.length; i++){
			xyseries.add(x[i],y[i]);
		}
		xyseries.add(x[0], y[0]);
		if(!dashed){
			renderer1.setSeriesStroke(numSerie,new BasicStroke(2.0f));
			renderer1.getSeriesPaint(numSerie);
		}else{
			renderer1.setSeriesStroke(numSerie,new BasicStroke(1.0f));
			//renderer1.setStroke(new BasicStroke(1.0f)); //Lineas finas
		}
		renderer1.setSeriesPaint(numSerie, color);
		xyseriescollection.addSeries(xyseries);
		
	}
	
	private void aniadirPunto(double x, double y, String pointName){
		String nombre= pointName;
        XYSeries xyseries = new XYSeries(nombre, false);
        xyseries.add(x, y);
        xyseriescollection.addSeries(xyseries);
	}
	
	private XYTextAnnotation aniadirAnotacion(Point p, String nombrePunto){
		//Establezco las etiquetas
        XYTextAnnotation annotation = null;   
        Font font = new Font("SansSerif", Font.PLAIN, 9);
        /* Sumo un pequeño desplazamiento para que la etiqueta no salga
        justo encima del punto */
        annotation = new XYTextAnnotation(nombrePunto, p.getX()+0.05, p.getY()+0.05);   
        annotation.setFont(font);
        annotation.setTextAnchor(TextAnchor.HALF_ASCENT_LEFT);   
        xyplot.addAnnotation(annotation);
        //listAnotations.add(annotation);
        return annotation;
	}
	
	private void dibujarPuntosCaracteristicos(){
		for(int i=0; i<graphicBean.getNumCP(); i++){
			Point p = graphicBean.getCP(i).getPoint();
			aniadirPunto(p.getX(), p.getY(), graphicBean.getCP(i).getName());
		}
	}
	
	private XYPlot createInitialSeries() {
		renderer1 = new XYLineAndShapeRenderer(true, true);
		NumberAxis numberaxis = new NumberAxis("X");
        numberaxis.setAutoRangeIncludesZero(false);
        NumberAxis numberaxis1 = new NumberAxis("Y");
        numberaxis1.setAutoRangeIncludesZero(false);
        xyseriescollection = new XYSeriesCollection();
		XYPlot xy = new XYPlot(xyseriescollection, numberaxis, numberaxis1, renderer1);
		return xy;
    }

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.setBounds(100, 100, 1000, 700);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}

