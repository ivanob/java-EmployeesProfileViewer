package view.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import rs.ac.ns.ftn.tmd.datatypes.DecomposedFuzzyNumber;
import rs.ac.ns.ftn.tmd.datatypes.FuzzySet;

import controller.evaluations.Evaluation;

public class jDialogShowEvaluation extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		try {
			jDialogShowEvaluation dialog = new jDialogShowEvaluation();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	
	private XYSeries getFunctionSerie(FuzzySet arg){
		int numPoints = 1000;
		XYSeries serie = new XYSeries("serie1", true);
		DecomposedFuzzyNumber fn = (DecomposedFuzzyNumber)arg;
		double ini = fn.getLeftBoundary();
		double incr = (fn.getRightBoundary()-ini)/numPoints;
		double x = ini;
		for(int k=0; k<=numPoints; k++){
			serie.add(x, fn.getMembershipValue(x));
			x += incr;
		}
		return serie;
	}

	/**
	 * Create the dialog.
	 */
	public jDialogShowEvaluation(Evaluation ev, String emplName) {
		setBounds(100, 100, 700, 300);
		String title="Mean evaluation for employee " + emplName;
		title += " in competence " + ev.getCompetence().getName();
		this.setTitle(title);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		//Creo el panel con la grafica
		final XYSplineRenderer render = new XYSplineRenderer();
		XYSeries serie = getFunctionSerie(ev.getMark());
		this.setLocationRelativeTo(null);
		
		XYSeriesCollection collection = new XYSeriesCollection();
		collection.addSeries(serie);
		JFreeChart jfreechart = ChartFactory.createXYLineChart(
				"", "X", "Y",
				collection, PlotOrientation.VERTICAL,
				true, true, false);
		
		
		render.setSeriesShapesVisible(0, false);
		final XYPlot plot = jfreechart.getXYPlot();
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
		this.add(c, BorderLayout.LINE_START);
		
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton closeButton = new JButton("Close");
				closeButton.setActionCommand("Close");
				buttonPane.add(closeButton);
				closeButton.addActionListener(new ActionListener() {
				    public void actionPerformed(ActionEvent evt) {
				    	dispose();
				    }
				});
			}
		}
	}

}
