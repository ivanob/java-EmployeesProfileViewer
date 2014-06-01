package view.GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import controller.competences.FuzzyVariableBean;

import rs.ac.ns.ftn.tmd.datatypes.DecomposedFuzzyNumber;
import rs.ac.ns.ftn.tmd.datatypes.FuzzyInterval;
import rs.ac.ns.ftn.tmd.datatypes.FuzzySet;
import rs.ac.ns.ftn.tmd.datatypes.LRFuzzyNumber;


public class jDialogAddFuzzyVariable extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldName;
	private JLabel etiqLeftIntBound, etiqRightIntBound;
	private JSpinner spinnerLeftIntBound, spinnerRightIntBound;
	private JLabel lblModal;
	private JSpinner spinnerModal, spinnerLBound, spinnerRBound;
	private JComboBox comboBoxTypeFuzzy, comboBoxFuncTypeL, comboBoxFuncTypeR;
	private FuzzyVariableBean var;
	private JButton okButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			jDialogAddFuzzyVariable dialog = new jDialogAddFuzzyVariable("C++", null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public FuzzyVariableBean getFuzzyVariable(){
		return var;
	}
	
	private void setVisibleLabels(boolean number){
		if(number){
			etiqLeftIntBound.setVisible(false);
			etiqRightIntBound.setVisible(false);
			spinnerLeftIntBound.setVisible(false);
			spinnerRightIntBound.setVisible(false);
			lblModal.setVisible(true);
			spinnerModal.setVisible(true);
		}else{
			lblModal.setVisible(false);
			spinnerModal.setVisible(false);
			etiqLeftIntBound.setVisible(true);
			etiqRightIntBound.setVisible(true);
			spinnerLeftIntBound.setVisible(true);
			spinnerRightIntBound.setVisible(true);
		}
	}

	/**
	 * Si el segundo parametro es distinto de null, significa que el
	 * panel se va a rellenar con los datos de este objeto (es un edit).
	 * Si es null significa que el panel es para añadir una nueva
	 * variable borrosa.
	 */
	public jDialogAddFuzzyVariable(String compName, FuzzyVariableBean var2) {
		if(var2==null){
			this.setTitle("Add fuzzy variable to " + compName + " fuzzyset");
		}else{
			this.setTitle("Edit fuzzy variable to " + compName + " fuzzyset");
		}
		setBounds(100, 100, 585, 277);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 0, 571, 199);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		
		JLabel lblLinguisticLabel = new JLabel("Linguistic label: ");
		lblLinguisticLabel.setBounds(12, 34, 116, 15);
		contentPanel.add(lblLinguisticLabel);
		
		textFieldName = new JTextField();
		textFieldName.setBounds(129, 32, 134, 19);
		contentPanel.add(textFieldName);
		textFieldName.setColumns(10);
		
		JLabel lblType = new JLabel("Type: ");
		lblType.setBounds(281, 34, 43, 15);
		contentPanel.add(lblType);
		
		comboBoxTypeFuzzy = new JComboBox();
		comboBoxTypeFuzzy.addItem("Fuzzy number");
		comboBoxTypeFuzzy.addItem("Fuzzy interval");
		comboBoxTypeFuzzy.setBounds(335, 29, 154, 24);
		contentPanel.add(comboBoxTypeFuzzy);
		comboBoxTypeFuzzy.addActionListener(new ActionListener(){
			 public void actionPerformed(ActionEvent e) {
			        JComboBox cb = (JComboBox)e.getSource();
			        if(cb.getSelectedIndex()==0){
			        	setVisibleLabels(true);
			        	//okButton.setEnabled(true);
			        }else if(cb.getSelectedIndex()==1){
			        	setVisibleLabels(false);
			        	//okButton.setEnabled(false);
			        }
			    }
		});
		
		lblModal = new JLabel("Modal:");
		lblModal.setBounds(12, 89, 56, 15);
		contentPanel.add(lblModal);
		
		spinnerModal = new JSpinner(new SpinnerNumberModel(5,0,10,0.01));
		spinnerModal.setBounds(78, 87, 50, 20);
		contentPanel.add(spinnerModal);
		
		JLabel lblLeftBoundary = new JLabel("Left boundary: ");
		lblLeftBoundary.setBounds(12, 123, 109, 15);
		contentPanel.add(lblLeftBoundary);
		
		spinnerLBound = new JSpinner(new SpinnerNumberModel(5,0,10,0.01));
		spinnerLBound.setBounds(149, 121, 50, 20);
		contentPanel.add(spinnerLBound);
		
		JLabel lblRightBoundary = new JLabel("Right boundary: ");
		lblRightBoundary.setBounds(294, 123, 123, 15);
		contentPanel.add(lblRightBoundary);
		
		spinnerRBound = new JSpinner(new SpinnerNumberModel(5,0,10,0.01));
		spinnerRBound.setBounds(445, 121, 50, 20);
		contentPanel.add(spinnerRBound);
		
		JLabel lblNewLabel = new JLabel("Left function type:");
		lblNewLabel.setBounds(12, 158, 135, 15);
		contentPanel.add(lblNewLabel);
		
		comboBoxFuncTypeL = new JComboBox();
		comboBoxFuncTypeL.addItem("Linear");
		comboBoxFuncTypeL.addItem("Gaussian");
		comboBoxFuncTypeL.addItem("Quadratic");
		comboBoxFuncTypeL.addItem("Exponential");
		
		comboBoxFuncTypeL.setBounds(149, 153, 114, 24);
		contentPanel.add(comboBoxFuncTypeL);
		
		JLabel lblRightFunctionType = new JLabel("Right function type:");
		lblRightFunctionType.setBounds(294, 158, 140, 15);
		contentPanel.add(lblRightFunctionType);
		
		comboBoxFuncTypeR = new JComboBox();
		comboBoxFuncTypeR.addItem("Linear");
		comboBoxFuncTypeR.addItem("Gaussian");
		comboBoxFuncTypeR.addItem("Quadratic");
		comboBoxFuncTypeR.addItem("Exponential");
		comboBoxFuncTypeR.setBounds(445, 153, 114, 24);
		contentPanel.add(comboBoxFuncTypeR);
		
		etiqLeftIntBound = new JLabel("Left interval boundary: ");
		etiqLeftIntBound.setBounds(12, 89, 167, 15);
		contentPanel.add(etiqLeftIntBound);
		
		etiqRightIntBound = new JLabel("Right interval boundary: ");
		etiqRightIntBound.setBounds(294, 89, 182, 15);
		contentPanel.add(etiqRightIntBound);
		
		spinnerLeftIntBound = new JSpinner(new SpinnerNumberModel(5,0,10,0.01));
		spinnerLeftIntBound.setBounds(186, 87, 50, 20);
		contentPanel.add(spinnerLeftIntBound);
		
		spinnerRightIntBound = new JSpinner(new SpinnerNumberModel(5,0,10,0.01));
		spinnerRightIntBound.setBounds(476, 87, 50, 20);
		contentPanel.add(spinnerRightIntBound);
		
		etiqLeftIntBound.setVisible(false);
		etiqRightIntBound.setVisible(false);
		spinnerLeftIntBound.setVisible(false);
		spinnerRightIntBound.setVisible(false);
		if(var2!=null){
			rellenarCampos(var2);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 214, 571, 35);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane);
			{
				okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
				    public void actionPerformed(ActionEvent evt) {
				    	if(validateFields()){
				    		buildFuzzyVariable();
				    		dispose();
				    	}				    	
				    }
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent evt){
						setVisible(false);
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		this.setLocationRelativeTo(null);
	}
	
	private void rellenarCampos(FuzzyVariableBean var2) {
		this.textFieldName.setText(var2.getLabel());
		FuzzySet fuzzy = var2.getFuzzy();
		if(fuzzy instanceof DecomposedFuzzyNumber){
			this.spinnerModal.setValue(fuzzy.getModalValue());
			this.comboBoxTypeFuzzy.setSelectedIndex(0);
		}else if(fuzzy instanceof FuzzyInterval){
			FuzzyInterval fuzzyInt = (FuzzyInterval)fuzzy;
			this.spinnerLeftIntBound.setValue(fuzzyInt.getModalValue());
			this.spinnerRightIntBound.setValue(fuzzyInt.getModalValue());
			this.comboBoxTypeFuzzy.setSelectedIndex(1);
		}
		this.spinnerLBound.setValue(fuzzy.getLeftBoundary());
		this.spinnerRBound.setValue(fuzzy.getRightBoundary());
		this.comboBoxFuncTypeL.setSelectedIndex(fuzzy.getLeftFunction());
		this.comboBoxFuncTypeR.setSelectedIndex(fuzzy.getRightFunction());
	}

	private void buildFuzzyVariable(){
		FuzzySet fuzzy=null;
		if(comboBoxTypeFuzzy.getSelectedIndex()==0){ /* Se ha elegido
		el numero borroso */
			double modal = (Double)spinnerModal.getValue();
			double alpha = modal - (Double)spinnerLBound.getValue();
			double beta = (Double)spinnerRBound.getValue() - modal;
			int funcLType = comboBoxFuncTypeL.getSelectedIndex();
			int funcRType = comboBoxFuncTypeR.getSelectedIndex();
			fuzzy=new DecomposedFuzzyNumber(modal,alpha,beta,funcLType,funcRType, 50);
		}else if(comboBoxTypeFuzzy.getSelectedIndex()==1){ /* Se ha elegido
		el intervalo borroso */
			double modalLeft = (Double)spinnerLeftIntBound.getValue();
			double modalRight = (Double)spinnerRightIntBound.getValue();
			double alpha = modalLeft - (Double)spinnerLBound.getValue();
			double beta = (Double)spinnerRBound.getValue() - modalRight;
			int funcLType = comboBoxFuncTypeL.getSelectedIndex();
			int funcRType = comboBoxFuncTypeR.getSelectedIndex();
			fuzzy=new FuzzyInterval(modalLeft, modalRight,alpha,beta,funcLType,funcRType, 50);
		}
		var = new FuzzyVariableBean(textFieldName.getText(), fuzzy);
	}
	
	private boolean validateFields() {
		if(comboBoxTypeFuzzy.getSelectedIndex()==0){ /* Se ha elegido
		el numero borroso */
			if((Double)spinnerModal.getValue()<(Double)spinnerLBound.getValue()){
				JOptionPane.showMessageDialog(contentPanel,
					    "Left boundary value must be equal or "+
					    "\nless than modal value",
					    "Error",
					    JOptionPane.ERROR_MESSAGE);
				return false;
			}else if((Double)spinnerModal.getValue()>(Double)spinnerRBound.getValue()){
				JOptionPane.showMessageDialog(contentPanel,
					    "Right boundary value must be equal or "+
					    "\ngreather than modal value",
					    "Error",
					    JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}else if(comboBoxTypeFuzzy.getSelectedIndex()==1){ /* Se ha elegido
		el intervalo borroso */
			if((Double)spinnerLeftIntBound.getValue()<(Double)spinnerLBound.getValue()){
				JOptionPane.showMessageDialog(contentPanel,
					    "Left interval boundary value must be equal or "+
					    "\ngreather than left boundary value",
					    "Error",
					    JOptionPane.ERROR_MESSAGE);
				return false;
			}else if((Double)spinnerRightIntBound.getValue()>(Double)spinnerRBound.getValue()){
				JOptionPane.showMessageDialog(contentPanel,
					    "Right interval boundary value must be equal or "+
					    "\nless than right boundary value",
					    "Error",
					    JOptionPane.ERROR_MESSAGE);
				return false;
			}else if((Double)spinnerLeftIntBound.getValue()>(Double)spinnerRightIntBound.getValue()){
				JOptionPane.showMessageDialog(contentPanel,
					    "Left interval boundary value must be equal or "+
					    "\nless than right interval boundary value",
					    "Error",
					    JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		if(textFieldName.getText().compareTo("")==0){
			JOptionPane.showMessageDialog(contentPanel,
				    "Linguistic label for fuzzy variable "+
				    "\nis required",
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
}
