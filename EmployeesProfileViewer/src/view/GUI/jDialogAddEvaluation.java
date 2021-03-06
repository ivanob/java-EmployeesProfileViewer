package view.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JComboBox;

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
import controller.employees.EmployeeManager;
import controller.evaluations.Evaluation;
import controller.evaluations.SourceFactory;
import controller.geometry.Point;

import rs.ac.ns.ftn.tmd.datatypes.CrispSet;
import rs.ac.ns.ftn.tmd.datatypes.FuzzySet;
import rs.ac.ns.ftn.tmd.datatypes.ScalarNumber;

import javax.swing.JSpinner;
import javax.swing.border.MatteBorder;

public class jDialogAddEvaluation extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JLabel labelEval, labelMinVal, labelMaxVal;
	private JComboBox comboBoxFuzzy;
	private JSpinner spinnerScalar, spinnerMin, spinnerMax;
	private final JComboBox comboBoxType, comboBoxSource;
	private Evaluation eval=null;
	private int idComp, idEmpl;
	private SourceFactory sf;
	private CompetenceFactory cf;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			jDialogAddEvaluation dialog = new jDialogAddEvaluation(0,0);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Evaluation getEvaluation(){
		return eval;
	}
	
	private void mostrarEvaluationCombo(int num){
		switch(num){
			case 0:
				labelEval.setVisible(true);
				comboBoxFuzzy.setVisible(false);
				spinnerMin.setVisible(false);
				spinnerMax.setVisible(false);
				labelMinVal.setVisible(false);
				labelMaxVal.setVisible(false);
				spinnerScalar.setVisible(true);
				break;
			case 1:
				labelEval.setVisible(true);
				comboBoxFuzzy.setVisible(false);
				spinnerMin.setVisible(true);
				spinnerMax.setVisible(true);
				labelMinVal.setVisible(true);
				labelMaxVal.setVisible(true);
				spinnerScalar.setVisible(false);
				break;
			case 2:
				labelEval.setVisible(true);
				comboBoxFuzzy.setVisible(true);
				spinnerMin.setVisible(false);
				spinnerMax.setVisible(false);
				labelMinVal.setVisible(false);
				labelMaxVal.setVisible(false);
				spinnerScalar.setVisible(false);
				break;
		}
	}

	/**
	 * Create the dialog.
	 */
	public jDialogAddEvaluation(int idComp, int idEmpl) {
		this.idComp = idComp;
		this.idEmpl = idEmpl;
		String nombreEmpl = EmployeeManager.getInstance().getEmployee(idEmpl).getName();
		cf=CompetenceFactory.getInstance();
		String nombreComp = cf.getCompetence(idComp).getName();
		setTitle("Add new Evaluation for Employer " + nombreEmpl + " in Competence " + nombreComp);
		setBounds(100, 100, 747, 457);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		this.setLocationRelativeTo(null);
		
		JLabel labelSource = new JLabel("Source:");
		labelSource.setBounds(15, 54, 67, 15);
		contentPanel.add(labelSource);
		
		sf = SourceFactory.getInstance();
		comboBoxSource = new JComboBox();
		for(int i=0; i<sf.getNumSources(); i++){
			comboBoxSource.addItem(sf.getSource(i).getName());
		}
		
		comboBoxSource.setBounds(85, 49, 160, 24);
		contentPanel.add(comboBoxSource);
		
		JLabel labelType = new JLabel("Type:");
		labelType.setBounds(15, 103, 55, 15);
		contentPanel.add(labelType);
		
		comboBoxType = new JComboBox();
		comboBoxType.setBounds(85, 98, 160, 24);
		contentPanel.add(comboBoxType);
		
		JPanel panelGraphic = new JPanel(new BorderLayout());
		panelGraphic.setBounds(15, 179, 700, 200);
		contentPanel.add(panelGraphic);
		
		//Relleno el combobox de los tipos
		comboBoxType.addItem("Scalar evaluation");
		comboBoxType.addItem("Crisp evaluation");
		comboBoxType.addItem("Fuzzy evaluation");
		comboBoxType.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	int selected = comboBoxType.getSelectedIndex();
            	mostrarEvaluationCombo(selected);
            }
		});
		
		//Inserto la grafica de fuzzysets
		final XYSplineRenderer render = new XYSplineRenderer();
		XYSeriesCollection xyseriescollection =	new XYSeriesCollection();
		
		JFreeChart jfreechart = ChartFactory.createXYLineChart(
				"", "X", "Y",
				xyseriescollection, PlotOrientation.VERTICAL,
				true, true, false);
		final XYPlot plot = jfreechart.getXYPlot();
		this.loadFuzzysets(xyseriescollection, plot, idComp, render);
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
		c.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		c.setPreferredSize(new Dimension(700, 200));
		panelGraphic.add(c, BorderLayout.LINE_START);
		
		JLabel labelGraphic = new JLabel("Competence fuzzysets:");
		labelGraphic.setBounds(15, 152, 188, 15);
		contentPanel.add(labelGraphic);
		
		labelEval = new JLabel("Evaluation:");
		labelEval.setBounds(292, 103, 93, 15);
		contentPanel.add(labelEval);
		
		comboBoxFuzzy = new JComboBox();
		comboBoxFuzzy.setBounds(391, 98, 124, 24);
		contentPanel.add(comboBoxFuzzy);
		for(int i=0; i<cf.getCompetence(idComp).getNumFuzzyVariables(); i++){
			comboBoxFuzzy.addItem(cf.getCompetence(idComp).getFuzzyVariable(i).getLabel());
		}
		
		spinnerScalar = new JSpinner(new SpinnerNumberModel(5.00, 0.00, 10, 0.01));
		spinnerScalar.setBounds(391, 101, 55, 20);
		contentPanel.add(spinnerScalar);
		
		labelMinVal = new JLabel("Minimum: ");
		labelMinVal.setBounds(391, 76, 77, 15);
		contentPanel.add(labelMinVal);
		
		labelMaxVal = new JLabel("Maximum: ");
		labelMaxVal.setBounds(507, 76, 77, 15);
		contentPanel.add(labelMaxVal);
		
		spinnerMin = new JSpinner(new SpinnerNumberModel(5.00, 0.00, 10, 0.01));
		spinnerMin.setBounds(391, 101, 55, 20);
		contentPanel.add(spinnerMin);
		
		spinnerMax = new JSpinner(new SpinnerNumberModel(5.00, 0.00, 10, 0.01));
		spinnerMax.setBounds(507, 101, 55, 20);
		contentPanel.add(spinnerMax);
		
		mostrarEvaluationCombo(0);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				okButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent evt){
						if(validateFields()){
							createEvaluation();
							setVisible(false);
							dispose();
						}
					}

				});
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
				cancelButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent evt){
						setVisible(false);
						dispose();
					}
				});
			}
		}
	}
	
	private void createEvaluation() {
		switch(comboBoxType.getSelectedIndex()){
			case 0:
				double f = (Double)spinnerScalar.getValue();
				ScalarNumber scalar = new ScalarNumber(f);
				eval=new Evaluation(cf.getCompetence(idComp), 
						sf.getSource(comboBoxSource.getSelectedIndex()),
						scalar);
				break;
			case 1:
				double min,max;
				min=(Double) spinnerMin.getValue();
				max=(Double) spinnerMax.getValue();
				CrispSet crisp = new CrispSet(min,max);
				eval=new Evaluation(cf.getCompetence(idComp), 
						sf.getSource(comboBoxSource.getSelectedIndex()),
						crisp);
				break;
			case 2:
				FuzzySet fuzzy=cf.getCompetence(idComp).getFuzzyVariable(comboBoxFuzzy.getSelectedIndex()).getFuzzy();
				eval=new Evaluation(cf.getCompetence(idComp), 
						sf.getSource(comboBoxSource.getSelectedIndex()),
						fuzzy,
						comboBoxFuzzy.getSelectedIndex());						
				break;
		}
		
	}
	
	private boolean validateFields(){
		if(comboBoxType.getSelectedIndex()==1){
			if((Double)spinnerMin.getValue()>(Double)spinnerMax.getValue()){
				JOptionPane.showMessageDialog(contentPanel,
					    "Minimum value of a crisp set must be "+
					    "\nequal or less than maximum value",
					    "Error",
					    JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		return true;
	}
	
	private void loadFuzzysets(XYSeriesCollection xyseriescollection, XYPlot plot, int i, XYSplineRenderer render) {
		Competence c = CompetenceFactory.getInstance().getCompetence(i);
		int numPoints = 1000;
		for(int j=0; j<c.getNumFuzzyVariables(); j++){
			FuzzyVariableBean aux = c.getFuzzyVariable(j);
			XYSeries serie = new XYSeries(aux.getLabel(), true);
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
	    	aniadirAnotacion(p, c.getFuzzyVariable(j).getLabel(), plot);
		}
	}
	
	private void aniadirAnotacion(Point p, String nombrePunto, XYPlot xyplot){
		//Establezco las etiquetas
        XYTextAnnotation annotation = null;   
        Font font = new Font("SansSerif", Font.PLAIN, 9);
        /* Sumo un pequeño desplazamiento para que la etiqueta no salga
        justo encima del punto */
        annotation = new XYTextAnnotation(nombrePunto, p.getX(), p.getY()+0.05);   
        annotation.setFont(font);
        annotation.setTextAnchor(TextAnchor.HALF_ASCENT_LEFT);   
        xyplot.addAnnotation(annotation);
	}
}
