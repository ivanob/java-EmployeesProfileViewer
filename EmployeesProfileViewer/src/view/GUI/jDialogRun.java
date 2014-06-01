package view.GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.SpinnerNumberModel;
import javax.swing.JProgressBar;

import controller.genetic.Genetic;
import controller.genetic.GeneticConfigBean;
import controller.genetic.SimilarityMatrix;

import model.GUIDataBean;
import javax.swing.JComboBox;



public class jDialogRun extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JSpinner spinnerN, spinnerS, spinnerNumIter;
	private JSpinner spinnerAlpha, spinnerMutation, spinnerNumPoints;
	private final JSpinner spinnerAlphaCuts;
	private JComboBox comboBoxType;
	private JButton runButton, cancelButton, buttonAdd;
	private final JList list;
	private GUIDataBean GUIData;
	private Genetic task=null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			jDialogRun dialog = new jDialogRun();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public GUIDataBean getGUIDataBean(){
		return GUIData;
	}
	
	private int getTypeOfShape(){
		int type=-1;
		switch(comboBoxType.getSelectedIndex()){
		case 0:
			type = Genetic.ELIPSE;
			break;
		case 1:
			type = Genetic.CIRCLE;
			break;
		case 2:
			type = Genetic.POINT;
			break;
		}
		return type;
	}

	/**
	 * Create the dialog.
	 */
	public jDialogRun() {
		setTitle("Run parameters");
		setBounds(100, 100, 492, 477);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{483, 0};
		gridBagLayout.rowHeights = new int[]{383, 35, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(null);
		GridBagConstraints gbc_contentPanel = new GridBagConstraints();
		gbc_contentPanel.gridheight = 2;
		gbc_contentPanel.fill = GridBagConstraints.BOTH;
		gbc_contentPanel.insets = new Insets(0, 0, 5, 0);
		gbc_contentPanel.gridx = 0;
		gbc_contentPanel.gridy = 0;
		getContentPane().add(contentPanel, gbc_contentPanel);
		this.setLocationRelativeTo(null);
		
		JLabel lblAlphacuts = new JLabel("Alpha-cuts: ");
		lblAlphacuts.setBounds(12, 36, 89, 15);
		contentPanel.add(lblAlphacuts);
		
		spinnerAlphaCuts = new JSpinner(new SpinnerNumberModel(0.025, 0.025, 0.975, 0.025));
		spinnerAlphaCuts.setBounds(103, 34, 65, 20);
		contentPanel.add(spinnerAlphaCuts);
		
		list = new JList(new DefaultListModel());
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setBounds(262, 12, 76, 69);
		contentPanel.add(scrollPane);
		
		buttonAdd = new JButton("+");
		buttonAdd.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent evt) {
		    	double f = (Double)spinnerAlphaCuts.getValue();
		    	DefaultListModel model = (DefaultListModel) list.getModel();
		    	boolean error=false;
		    	for(int i=0; i<model.getSize(); i++){
		    		if(f==(Double)model.get(i)){
		    			JOptionPane.showMessageDialog(contentPanel,
		    				    f + " alphacut is already added",
		    				    "Error",
		    				    JOptionPane.ERROR_MESSAGE);
		    			error=true;
		    		}
		    	}
		    	if(!error){
	    			model.addElement(f);
	    		}
		    }
		});
		buttonAdd.setBounds(180, 33, 44, 20);
		contentPanel.add(buttonAdd);
		
		JLabel lblNumEllipses = new JLabel("Num points ellipses perimeter:");
		lblNumEllipses.setBounds(12, 106, 228, 15);
		contentPanel.add(lblNumEllipses);
		
		spinnerNumPoints = new JSpinner(new SpinnerNumberModel(16, 4, 512, 1));
		spinnerNumPoints.setBounds(262, 104, 58, 20);
		contentPanel.add(spinnerNumPoints);
		
		JLabel lblAlphaSmoothing = new JLabel("Alpha smoothing: ");
		lblAlphaSmoothing.setBounds(12, 133, 134, 15);
		contentPanel.add(lblAlphaSmoothing);
		
		spinnerAlpha = new JSpinner(new SpinnerNumberModel(0.99, 0, 1, 0.01));
		spinnerAlpha.setBounds(262, 131, 58, 20);
		contentPanel.add(spinnerAlpha);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder("Genetic"));
		panel.setBounds(12, 216, 452, 150);
		contentPanel.add(panel);
		panel.setLayout(null);
		
		JLabel lblNumPopulationn = new JLabel("Num. population (N): ");
		lblNumPopulationn.setBounds(12, 35, 162, 15);
		panel.add(lblNumPopulationn);
		
		JLabel lblNum = new JLabel("Num. selected individuals (S): ");
		lblNum.setBounds(12, 62, 213, 15);
		panel.add(lblNum);
		
		JLabel lblNumIterations = new JLabel("Num. Iterations: ");
		lblNumIterations.setBounds(12, 89, 129, 15);
		panel.add(lblNumIterations);
		
		spinnerN = new JSpinner(new SpinnerNumberModel(1000, 1, 99999, 1));
		spinnerN.setBounds(243, 33, 68, 20);
		panel.add(spinnerN);
		
		spinnerS = new JSpinner(new SpinnerNumberModel(500, 1, 99999, 1));
		spinnerS.setBounds(243, 60, 68, 20);
		panel.add(spinnerS);
		
		spinnerNumIter = new JSpinner(new SpinnerNumberModel(100, 1, 99999, 1));
		spinnerNumIter.setBounds(243, 87, 68, 20);
		panel.add(spinnerNumIter);
		
		JLabel lblMutationProbability = new JLabel("Mutation probability:");
		lblMutationProbability.setBounds(12, 116, 162, 15);
		panel.add(lblMutationProbability);
		
		spinnerMutation = new JSpinner(new SpinnerNumberModel(0.001, 0.001, 1, 0.001));
		spinnerMutation.setBounds(243, 114, 68, 20);
		panel.add(spinnerMutation);
		
		final JProgressBar progressBar = new JProgressBar();
		progressBar.setBounds(0, 378, 483, 33);
		progressBar.setIndeterminate(false);
		progressBar.setStringPainted(true);
		contentPanel.add(progressBar);
		
		JLabel labelTypeShape = new JLabel("Type of shape:");
		labelTypeShape.setBounds(12, 172, 121, 15);
		contentPanel.add(labelTypeShape);
		
		comboBoxType = new JComboBox();
		comboBoxType.addItem("Ellipse");
		comboBoxType.addItem("Circle");
		comboBoxType.addItem("Point");
		comboBoxType.disable();
		comboBoxType.setBounds(180, 167, 140, 24);
		contentPanel.add(comboBoxType);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			GridBagConstraints gbc_buttonPane = new GridBagConstraints();
			gbc_buttonPane.anchor = GridBagConstraints.NORTH;
			gbc_buttonPane.fill = GridBagConstraints.HORIZONTAL;
			gbc_buttonPane.gridx = 0;
			gbc_buttonPane.gridy = 2;
			getContentPane().add(buttonPane, gbc_buttonPane);
			
			{
				runButton = new JButton("Run");
				runButton.addActionListener(new ActionListener() {
				    public void actionPerformed(ActionEvent evt) {
				    	if(validateFields()){
				    		enableDisableAllComponents(false);
				    		SimilarityMatrix matrix = new SimilarityMatrix();
				    		GeneticConfigBean config = buildConfigBean();
				    		task = new Genetic(matrix, getTypeOfShape(), config);
				    		task.addPropertyChangeListener(new PropertyChangeListener() {
				                public void propertyChange(PropertyChangeEvent event) {
				                    if("progress".equals(event.getPropertyName())){
				                    	int percent =(Integer)event.getNewValue();
				                        progressBar.setValue((Integer)event.getNewValue());
				                        progressBar.setString(percent + "%");
				                        if(percent==100){
				                        	enableDisableAllComponents(true);
				                        	try {
												GUIData = task.get();
												dispose();
											}catch (CancellationException e){
												JOptionPane.showMessageDialog(contentPanel,
													    "Operation aborted by user",
													    "Operation aborted",
													    JOptionPane.ERROR_MESSAGE);
											} catch (InterruptedException e) {
												e.printStackTrace();
											} catch (ExecutionException e) {
												e.printStackTrace();
											}
				                        }
				                    }
				                }
				            });
				    		task.execute();
				    		
				    	}
				    }
				});
				runButton.setActionCommand("OK");
				buttonPane.add(runButton);
				getRootPane().setDefaultButton(runButton);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(new ActionListener() {
				    public void actionPerformed(ActionEvent evt) {
				    	if(!runButton.isEnabled()){
				    		task.cancel(true);
				    		enableDisableAllComponents(true);
				    	}else{
				    		dispose();
				    	}
				    }
				});
				buttonPane.add(cancelButton);
			}
		}
	}
	
	private GeneticConfigBean buildConfigBean(){
		float[] alphacuts = new float[list.getModel().getSize()];
		for(int i=0; i<list.getModel().getSize(); i++){
			double d=(Double)list.getModel().getElementAt(i);
			alphacuts[i]=(float) d;
		}
		int numPointsShapes = (Integer)spinnerNumPoints.getValue();
		double alphaSmoothing = (Double)spinnerAlpha.getValue();
		int numPopulation = (Integer)spinnerN.getValue();
		int numSelected = (Integer)spinnerS.getValue();
		int numIter = (Integer)spinnerNumIter.getValue();
		double mutation = (Double)spinnerMutation.getValue();
		GeneticConfigBean res = new GeneticConfigBean(alphacuts, numPointsShapes,
				(float)alphaSmoothing, numIter, numSelected,
				numPopulation, mutation);
		return res;
	}
	
	private void enableDisableAllComponents(boolean enable){
		spinnerN.setEnabled(enable);
		spinnerS.setEnabled(enable);
		spinnerAlpha.setEnabled(enable);
		spinnerMutation.setEnabled(enable);
		spinnerNumPoints.setEnabled(enable);
		spinnerAlphaCuts.setEnabled(enable);
		runButton.setEnabled(enable);
		spinnerNumIter.setEnabled(enable);
		list.setEnabled(enable);
		buttonAdd.setEnabled(enable);
	}
	
	private boolean validateFields() {
		if((Integer)spinnerS.getValue()>(Integer)spinnerN.getValue()){
			JOptionPane.showMessageDialog(contentPanel,
				    "Selected population (S) must be "+
				    "\nequal or less than total population (N)",
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
}
