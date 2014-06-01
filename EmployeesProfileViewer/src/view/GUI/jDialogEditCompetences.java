package view.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.TextAnchor;

import controller.competences.Competence;
import controller.competences.CompetenceFactory;
import controller.competences.FuzzyVariableBean;
import controller.geometry.Point;


import rs.ac.ns.ftn.tmd.datatypes.FuzzySet;

import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;

public class jDialogEditCompetences extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private CompetenceFactory cf = CompetenceFactory.getInstance();
	private XYSeriesCollection[] xyseries;
	private LinkedList<LinkedList<XYTextAnnotation>> listAnnotations;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			jDialogEditCompetences dialog = new jDialogEditCompetences();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public jDialogEditCompetences() {
		this.setTitle("Edit competences fuzzysets");
		setBounds(100, 100, 983, 585);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		this.setLocationRelativeTo(null);
		contentPanel.setLayout(new GridLayout(1, 0, 0, 0));
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane);
			{
				JPanel panelPrincipal = new JPanel();
				scrollPane.setViewportView(panelPrincipal);
				GridBagLayout gbl_panelPrincipal = new GridBagLayout();
				gbl_panelPrincipal.columnWidths = new int[]{0};
				gbl_panelPrincipal.rowHeights = new int[]{0};
				gbl_panelPrincipal.columnWeights = new double[]{Double.MIN_VALUE};
				gbl_panelPrincipal.rowWeights = new double[]{Double.MIN_VALUE};
				panelPrincipal.setLayout(gbl_panelPrincipal);
				xyseries = new XYSeriesCollection[cf.getNumCompetences()];
				listAnnotations = new LinkedList();
				
				for(int i=0; i<cf.getNumCompetences(); i++){
					XYSeriesCollection xyseriescollection =	new XYSeriesCollection();
					xyseries[i]=xyseriescollection;
				}
				
				for(int i=0; i<cf.getNumCompetences(); i++){
					listAnnotations.add(new LinkedList());
					JPanel panelInterior = new JPanel(new BorderLayout());
					panelInterior.setBorder(BorderFactory.createTitledBorder(cf.getCompetence(i).getName()));
					GridBagConstraints constr = new GridBagConstraints();
					constr.gridx = 0; // Columna 0. No necesita estirarse, no ponemos weightx
					constr.gridy = i; // Fila 0. Necesita estirarse, hay que poner weighty
					constr.weightx = 1.0; // La fila 0 debe estirarse, le ponemos un 1.0
					constr.weighty = 1.0;
					constr.fill = GridBagConstraints.HORIZONTAL;
					panelPrincipal.add(panelInterior, constr);
					
					//Panel de la izquierda (grafica)
					Competence comp = cf.getCompetence(i);
					final XYSplineRenderer render = new XYSplineRenderer();
					
					JFreeChart jfreechart = ChartFactory.createXYLineChart(
							"", "X", "Y",
							xyseries[i], PlotOrientation.VERTICAL,
							true, true, false);
					
					
					render.setSeriesShapesVisible(i, false);
					final XYPlot plot = jfreechart.getXYPlot();
					loadFuzzysets(xyseries[i], plot, i, render);
					jfreechart.getXYPlot().setRenderer(render);
					jfreechart.getXYPlot().setBackgroundPaint(Color.white);
					jfreechart.getXYPlot().setRangeGridlinePaint(Color.gray);
					jfreechart.getXYPlot().setDomainGridlinePaint(Color.gray);
					jfreechart.removeLegend();
					
					ValueAxis axis = jfreechart.getXYPlot().getDomainAxis();
					axis.setRange(0, 10);
					
					axis = jfreechart.getXYPlot().getRangeAxis();
					axis.setRange(0, 1.2);
					
					ChartPanel c = new ChartPanel(jfreechart);
					c.setPreferredSize(new Dimension(700, 200));
					panelInterior.add(c, BorderLayout.LINE_START);
					
					//Panel de la derecha (edicion)
					JPanel panelControl = new JPanel(new GridBagLayout());
					GridBagConstraints cons = new GridBagConstraints();
					cons.fill = GridBagConstraints.HORIZONTAL;
					final JList lista = new JList(new DefaultListModel());
					DefaultListModel model = (DefaultListModel) lista.getModel();
					for(int j=0; j<comp.getNumFuzzyVariables(); j++){
						model.addElement(comp.getFuzzyVariable(j).getLabel());
					}
					
					lista.setBorder(BorderFactory.createLoweredBevelBorder());
					//lista.setMinimumSize(new Dimension(150,100));
					//lista.setPreferredSize(new Dimension(100,100));
					cons.gridx = 0; // Columna 2. No necesita estirarse, no ponemos weightx
					cons.gridy = 0; // Fila 0. Necesita estirarse, hay que poner weighty
					cons.gridwidth = 3;
					cons.gridheight = 1;
					JPanel p = new JPanel();
					JScrollPane scrollPanel=new JScrollPane(lista);
					scrollPanel.setPreferredSize(new Dimension(150,150));
					p.add(scrollPanel);
					panelControl.add(p,cons);
					//cons.fill = GridBagConstraints.HORIZONTAL;
					cons.gridx = 0; // Columna 2. No necesita estirarse, no ponemos weightx
					cons.gridy = 1; // Fila 0. Necesita estirarse, hay que poner weighty
					cons.gridwidth = 1;
					cons.gridheight = 1;
					
					
					JButton botonAdd = new JButton("Add");
					panelControl.add(botonAdd, cons);
					final int j=i;
					botonAdd.addActionListener(new ActionListener() {
					    public void actionPerformed(ActionEvent evt) {
					    	jDialogAddFuzzyVariable dialogAdd = new jDialogAddFuzzyVariable(cf.getCompetence(j).getName(), null);
					    	dialogAdd.setModal(true);
					    	dialogAdd.setVisible(true);
					    	FuzzyVariableBean var = dialogAdd.getFuzzyVariable();
					    	if(var!=null){
						    	DefaultListModel model = (DefaultListModel) lista.getModel();
						    	model.addElement(var.getLabel());
						    	addFuzzyset(j,var,render, plot);
						    	cf.getCompetence(j).addFuzzyVariable(var);
						    	//Aniado la anotacion
						    	
						    }
					    }
					});
					cons.gridx = 1; // Columna 2. No necesita estirarse, no ponemos weightx
					cons.gridy = 1; // Fila 0. Necesita estirarse, hay que poner weighty
					cons.gridwidth = 1;
					cons.gridheight = 1;
					//cons.weighty = 1.0;
					JButton botonEdit = new JButton("Edit");
					panelControl.add(botonEdit, cons);
					botonEdit.addActionListener(new ActionListener() {
					    public void actionPerformed(ActionEvent evt) {
					    	int selected = lista.getSelectedIndex();
					    	if(selected>=0){
					    		FuzzyVariableBean var = cf.getCompetence(j).getFuzzyVariable(selected);
					    		String lastName = cf.getCompetence(j).getName();
					    		jDialogAddFuzzyVariable dialogEdit = new jDialogAddFuzzyVariable(
					    			cf.getCompetence(j).getName(), var);
					    		dialogEdit.setModal(true);
						    	dialogEdit.setVisible(true);
						    	FuzzyVariableBean varNew = dialogEdit.getFuzzyVariable();
						    	if(varNew!=null){
						    		deleteFuzzyset(j,var,render, plot);
						    		cf.getCompetence(j).modifyFuzzyVariable(var.getLabel(), varNew);
							    	/*cf.getCompetence(j).removeFuzzyVariable(var.getLabel());
							    	cf.getCompetence(j).addFuzzyVariable(varNew);*/
							    	addFuzzyset(j,varNew,render, plot);
							    	//Aniado la anotacion
							    	Point p=new Point((float) varNew.getFuzzy().getModalValue(), 1f);
							    	//aniadirAnotacion(p, varNew.getLabel(), plot);
							    	//Aniado el nombre a la lista
							    	DefaultListModel model = (DefaultListModel) lista.getModel();
							    	model.setElementAt(varNew.getLabel(), selected);
						    	}
					    	}
					    }
					});
					cons.gridx = 2; // Columna 2. No necesita estirarse, no ponemos weightx
					cons.gridy = 1; // Fila 0. Necesita estirarse, hay que poner weighty
					cons.gridwidth = 1;
					cons.gridheight = 1;
					//cons.weigdeleteFuzzysethty = 1.0;
					JButton botonRemove = new JButton("Remove");
					panelControl.add(botonRemove, cons);
					botonRemove.addActionListener(new ActionListener() {
					    public void actionPerformed(ActionEvent evt) {
					    	int selected = lista.getSelectedIndex();
					    	if(selected>=0){
					    		FuzzyVariableBean var = cf.getCompetence(j).getFuzzyVariable(selected);
					    		cf.getCompetence(j).removeFuzzyVariable(var.getLabel());
					    		String lastName = cf.getCompetence(j).getName();
					    		deleteFuzzyset(j,var,render, plot);
					    		DefaultListModel model = (DefaultListModel) lista.getModel();
					    		model.remove(selected);
					    	}
					    }
					});
					
					constr.gridx = 1; // Columna 0. No necesita estirarse, no ponemos weightx
					constr.gridy = i; // Fila 0. Necesita estirarse, hay que poner weighty
					constr.weightx = 1.0; // La fila 0 debe estirarse, le ponemos un 1.0
					constr.weighty = 1.0;
					panelInterior.add(panelControl, BorderLayout.AFTER_LINE_ENDS);
				}
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JCheckBox checkAreas = new JCheckBox("Show areas");
				buttonPane.add(checkAreas);
			}
			{
				JButton closeButton = new JButton("Close");
				closeButton.addActionListener(new ActionListener() {
				    public void actionPerformed(ActionEvent evt) {
				    	dispose();
				    }
				});
				closeButton.setActionCommand("Cancel");
				buttonPane.add(closeButton);
			}
		}
	}
	
	private void aniadirAnotacion(Point p, String nombrePunto, XYPlot xyplot, int numComp){
		//Establezco las etiquetas
        XYTextAnnotation annotation = null;   
        Font font = new Font("SansSerif", Font.PLAIN, 9);
        /* Sumo un pequeño desplazamiento para que la etiqueta no salga
        justo encima del punto */
        annotation = new XYTextAnnotation(nombrePunto, p.getX(), p.getY()+0.05);   
        annotation.setFont(font);
        annotation.setTextAnchor(TextAnchor.HALF_ASCENT_LEFT);   
        xyplot.addAnnotation(annotation);
        listAnnotations.get(numComp).add(annotation);
	}
	
	private int getSerieIndex(List<XYSeries> series, String serieLabel){
		int index=-1;
		int i=0;
		for(XYSeries s : series){
			if(s.getKey().compareTo(serieLabel)==0){
				index=i;
			}
			i++;
		}
		return index;
	}
	
	private void deleteFuzzyset(int i, FuzzyVariableBean fuzzy, XYSplineRenderer render, XYPlot xyplot){
		int index=getSerieIndex(xyseries[i].getSeries(), fuzzy.getLabel());
		xyseries[i].removeSeries(index);
		//Remove annotation
		for(int j=0; j<listAnnotations.get(i).size(); j++){
			String annotationText = listAnnotations.get(i).get(j).getText();
			if(annotationText.compareTo(fuzzy.getLabel())==0){
				xyplot.removeAnnotation(listAnnotations.get(i).get(j));
				listAnnotations.get(i).remove(j);
			}
		}
	}
	
	private void addFuzzyset(int i, FuzzyVariableBean fuzzy, XYSplineRenderer render, XYPlot plot){
		int numPoints = 1000;
		XYSeries serie = new XYSeries(fuzzy.getLabel(), true);
		FuzzySet fn = fuzzy.getFuzzy();
		double ini = fn.getLeftBoundary();
		double incr = (fn.getRightBoundary()-ini)/numPoints;
		double x = ini;
		for(int k=0; k<=numPoints; k++){
			serie.add(x, fn.getMembershipValue(x));
			x += incr;
		}

		render.setSeriesShapesVisible(xyseries[i].getSeriesCount(), false);
		
		xyseries[i].addSeries(serie);
		
		Point p=new Point((float) fuzzy.getFuzzy().getModalValue(), 1f);
		aniadirAnotacion(p, fuzzy.getLabel(), plot, i);
	}

	private void loadFuzzysets(XYSeriesCollection xyseriescollection, XYPlot plot, int i, XYSplineRenderer render) {
		Competence c = cf.getCompetence(i);
		for(int j=0; j<c.getNumFuzzyVariables(); j++){
			FuzzyVariableBean aux = c.getFuzzyVariable(j);
			addFuzzyset(i, aux, render, plot);
			/*XYSeries serie = new XYSeries(aux.getLabel(), true);
			FuzzySet fn = aux.getFuzzy();
			double ini = fn.getLeftBoundary();
			double incr = (fn.getRightBoundary()-ini)/numPoints;
			double x = ini;
			for(int k=0; k<=numPoints; k++){
				serie.add(x, fn.getMembershipValue(x));
				x += incr;
			}
			render.setSeriesShapesVisible(j, false);
			xyseriescollection.addSeries(serie);
			//Aniado la anotacion
			Point p=new Point((float) fn.getModalValue(), 1f);
			aniadirAnotacion(p, c.getFuzzyVariable(j).getLabel(), plot);*/
		}
		xyseries[i] = xyseriescollection;
	}

}
