package view.GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.SpinnerNumberModel;

import controller.evaluations.SourceFactory;

public class jDialogEditSources extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JSpinner spinnerMin, spinnerMax;
	private final SourceFactory sf;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			jDialogEditSources dialog = new jDialogEditSources();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public jDialogEditSources() {
		this.setTitle("Edit sources");
		setBounds(100, 100, 387, 375);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		this.setLocationRelativeTo(null);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(12, 24, 53, 15);
		contentPanel.add(lblName);
		
		textField = new JTextField();
		textField.setBounds(67, 22, 126, 19);
		contentPanel.add(textField);
		textField.setColumns(10);
		
		JLabel lblMinDistrust = new JLabel("Min. distrust:");
		lblMinDistrust.setBounds(12, 51, 103, 15);
		contentPanel.add(lblMinDistrust);
		
		JLabel lblMaxDistrust = new JLabel("Max. distrust:");
		lblMaxDistrust.setBounds(12, 78, 103, 15);
		contentPanel.add(lblMaxDistrust);
		
		spinnerMin = new JSpinner(new SpinnerNumberModel(0.5, 0.0, 1, 0.01));
		spinnerMin.setBounds(119, 49, 53, 20);
		contentPanel.add(spinnerMin);
		
		spinnerMax = new JSpinner(new SpinnerNumberModel(0.5, 0.0, 1, 0.01));
		spinnerMax.setBounds(119, 76, 53, 20);
		contentPanel.add(spinnerMax);
		
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 159, 181, 141);
		contentPanel.add(scrollPane);
		
		final JList list = new JList();
		scrollPane.setViewportView(list);
		DefaultListModel model = new DefaultListModel();
		sf = SourceFactory.getInstance();
		for(int i=0; i<sf.getNumSources(); i++){
			model.add(i, sf.getSource(i).getName());
		}
		list.setModel(model);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.setBounds(256, 46, 81, 25);
		contentPanel.add(btnAdd);
		btnAdd.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent evt) {
		    	if(validateFields()){
		    		DefaultListModel model = (DefaultListModel) list.getModel();
		    		model.addElement(textField.getText());
		    		double min = (Double)spinnerMin.getValue();
		    		double max = (Double)spinnerMax.getValue();
		    		sf.addSource(textField.getText(), (float)max, (float)min);
		    	}
		    }
		});
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent evt) {
		    	int selected=list.getSelectedIndex();
		    	if(selected!=-1){
		    		String name=(String) list.getModel().getElementAt(selected);
		    		sf.removeSource(name);
		    		DefaultListModel model = (DefaultListModel) list.getModel();
		    		model.remove(selected);
		    	}else{
		    		JOptionPane.showMessageDialog(contentPanel,
						    "There is not a source "+
						    "\nselected in the list",
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
		    	}
		    }
		});
		btnDelete.setBounds(256, 204, 81, 25);
		contentPanel.add(btnDelete);
		
		JLabel lblSources = new JLabel("Sources:");
		lblSources.setBounds(12, 132, 70, 15);
		contentPanel.add(lblSources);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
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
	
	private boolean validateFields(){
		String name = textField.getText();
		if(name.compareTo("")==0){
			JOptionPane.showMessageDialog(contentPanel,
				    "Source's name is required",
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
			return false;
		}else if((Double)spinnerMin.getValue()>(Double)spinnerMax.getValue()){
			JOptionPane.showMessageDialog(contentPanel,
				    "Minimum distrust must be equal "+
				    "\nor less than maximum distrust",
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
			return false;
		}else if(sf.getSource(name)!=null){
			JOptionPane.showMessageDialog(contentPanel,
				    "There is already a source "+
				    "\nregistered with name " + name,
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
}
