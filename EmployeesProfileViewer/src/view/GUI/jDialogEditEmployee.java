package view.GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import javax.swing.JComboBox;

import controller.competences.CompetenceFactory;
import controller.employees.Employee;
import controller.employees.EmployeeManager;
import controller.evaluations.Evaluation;
import controller.evaluations.SourceFactory;

import rs.ac.ns.ftn.tmd.datatypes.ScalarNumber;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class jDialogEditEmployee extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JList[] listas=null;
	private final CompetenceFactory cf;
	//private Employer[] empl;
	private static EmployeeManager em=EmployeeManager.getInstance();
	private JTextField[] textMeans;
	private JButton[] showEvalButtons;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Employee[] empl = new Employee[4];
			empl[0] = new Employee("aaa", 3);
			empl[1] = new Employee("bbb", 3);
			empl[2] = new Employee("ccc", 3);
			empl[3] = new Employee("ddd", 3);
			SourceFactory sf = SourceFactory.getInstance();
			CompetenceFactory cf = CompetenceFactory.getInstance();
			empl[0].addEvaluation(new Evaluation(cf.getCompetence(0), sf.getMaximunCertainSource(),  new ScalarNumber(1f)));
			empl[0].addEvaluation(new Evaluation(cf.getCompetence(0), sf.getMaximunCertainSource(),  new ScalarNumber(3f)));
			empl[0].addEvaluation(new Evaluation(cf.getCompetence(1), sf.getMaximunCertainSource(),  new ScalarNumber(1f)));
			empl[0].addEvaluation(new Evaluation(cf.getCompetence(2), sf.getMaximunCertainSource(),  new ScalarNumber(1f)));
			
			empl[1].addEvaluation(new Evaluation(cf.getCompetence(0), sf.getMaximunCertainSource(),  new ScalarNumber(1f)));
			empl[1].addEvaluation(new Evaluation(cf.getCompetence(1), sf.getMaximunCertainSource(),  new ScalarNumber(1f)));
			empl[1].addEvaluation(new Evaluation(cf.getCompetence(2), sf.getMaximunCertainSource(),  new ScalarNumber(1f)));
			
			empl[2].addEvaluation(new Evaluation(cf.getCompetence(0), sf.getMaximunCertainSource(),  new ScalarNumber(1f)));
			empl[2].addEvaluation(new Evaluation(cf.getCompetence(0), sf.getMaximunCertainSource(),  new ScalarNumber(3f)));
			empl[2].addEvaluation(new Evaluation(cf.getCompetence(1), sf.getMaximunCertainSource(),  new ScalarNumber(1f)));
			empl[2].addEvaluation(new Evaluation(cf.getCompetence(2), sf.getMaximunCertainSource(),  new ScalarNumber(1f)));
			
			empl[3].addEvaluation(new Evaluation(cf.getCompetence(0), sf.getMaximunCertainSource(),  new ScalarNumber(1f)));
			empl[3].addEvaluation(new Evaluation(cf.getCompetence(0), sf.getMaximunCertainSource(),  new ScalarNumber(3f)));
			empl[3].addEvaluation(new Evaluation(cf.getCompetence(1), sf.getMaximunCertainSource(),  new ScalarNumber(1f)));
			empl[3].addEvaluation(new Evaluation(cf.getCompetence(2), sf.getMaximunCertainSource(),  new ScalarNumber(1f)));
			em.addEmployee(empl[0]);
			em.addEmployee(empl[1]);
			em.addEmployee(empl[2]);
			em.addEmployee(empl[3]);
			jDialogEditEmployee dialog = new jDialogEditEmployee();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Este metodo repinta todos los paneles jlist con
	 * las evaluaciones del empleado que se ha seleccionado
	 * (y que se pasa como parametro).
	 * 
	 * @param e
	 */
	private void repintarPanel(Employee e){
		for(int i=0; i<cf.getNumCompetences(); i++){
			if(e==null){
				listas[i].setModel(new DefaultListModel());
				textMeans[i].setText("");
				showEvalButtons[i].setVisible(false);
			}else
			if(!e.getEvaluations(i).isEmpty()){
				DefaultListModel model = new DefaultListModel();
				for(int j=0; j<e.getEvaluations(i).size(); j++){
					String str = e.getEvaluations(i).get(j).toString();
					if(e.getEvaluations(i).get(j).existFuzzyImprecision()){
						str += " (" + e.getEvaluations(i).get(j).getStrValue() 
							+ ")";
					}
					model.add(j, str);
				}
				listas[i].setModel(model);
				textMeans[i].setText(e.getEvaluation(i).toString().split(": ")[2]);
				if(e.getEvaluation(i).existFuzzyImprecision()){
					final Evaluation evToShow = e.getEvaluation(i);
					final String emplName = e.getName();
					/* Borro los action listeners previos que pudiera haber 
					para actualizarlo con el nuevo */
					for(int j=0; j<showEvalButtons[i].getActionListeners().length;j++){
						showEvalButtons[i].removeActionListener(showEvalButtons[i].getActionListeners()[j]);
					}
					showEvalButtons[i].addActionListener(new ActionListener() {
					    public void actionPerformed(ActionEvent evt) {
					    	jDialogShowEvaluation dialogShowEval = new jDialogShowEvaluation(evToShow, emplName);
					    	dialogShowEval.setModal(true);
					    	dialogShowEval.setVisible(true);
					    }
					});
					showEvalButtons[i].setVisible(true);
				}else{
					showEvalButtons[i].setVisible(false);					
				}
			}else{
				listas[i].setModel(new DefaultListModel());
				textMeans[i].setText("");
				showEvalButtons[i].setVisible(false);
			}
		}
	}
	
	/**
	 * Create the dialog.
	 */
	public jDialogEditEmployee() {
		this.setTitle("Edit employees");
		cf = CompetenceFactory.getInstance();
		setBounds(100, 100, 800, 600);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.rowWeights = new double[]{0.0, 1.0};
		gbl_contentPanel.columnWeights = new double[]{1.0, 0.0};
		this.setLocationRelativeTo(null);
		contentPanel.setLayout(gbl_contentPanel);
		{
			JPanel panelSeleccion = new JPanel(new GridBagLayout());
			panelSeleccion.setLayout(new FlowLayout(FlowLayout.CENTER));
			final JComboBox comboEmpleado = new JComboBox();
			//Relleno el combobox de los empleados
			for(int i=0; i<em.getNumEmployees(); i++){
				comboEmpleado.addItem(em.getEmployee(i).getName());
			}
			comboEmpleado.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent event) {
	            	Object selected = comboEmpleado.getSelectedItem();
	            	Employee e=null;
	            	for(int i=0; i<em.getNumEmployees(); i++){
	            		if(em.getEmployee(i).getName().compareTo((String) selected)==0){
	            			e=em.getEmployee(i);
	            		}
	            	}
	            	repintarPanel(e);
	            }
			});
			/* Boton añadir nuevo empleado. Despliega un popup para
			recoger su nombre. */
			JButton botonAdd = new JButton("Add");
			botonAdd.addActionListener(new ActionListener(){
	            public void actionPerformed(ActionEvent e){
	            	//Con caja de texto
	            	String name = JOptionPane.showInputDialog(
	            		contentPanel,
	            	   "Employee's name");  // el icono sera un iterrogante
	            	if(name!=null){
		            	em.addEmployee(new Employee(name, cf.getNumCompetences()));
		            	comboEmpleado.addItem(name);
	            	}
	            }
	        });
			
			JButton botonDel = new JButton("Delete");
			botonDel.addActionListener(new ActionListener(){
	            public void actionPerformed(ActionEvent e){
	            	int index=comboEmpleado.getSelectedIndex();
	            	if(index>-1){
	            		em.removeEmployee(index);
	            		comboEmpleado.removeItemAt(index);
	            	}
	            }
	        });
			panelSeleccion.add(comboEmpleado);
			panelSeleccion.add(botonAdd);
			panelSeleccion.add(botonDel);
			GridBagConstraints constr1 = new GridBagConstraints();
			constr1.insets = new Insets(0, 0, 5, 0);
			constr1.gridx = 0;
			constr1.gridy = 0;
			constr1.gridwidth = 2;
			constr1.gridheight = 1;
			contentPanel.add(panelSeleccion, constr1);
			{
				JPanel panelCompetencias = new JPanel();
				GridBagConstraints gbc_panelCompetencias = new GridBagConstraints();
				gbc_panelCompetencias.insets = new Insets(0, 0, 0, 5);
				gbc_panelCompetencias.fill = GridBagConstraints.BOTH;
				gbc_panelCompetencias.gridx = 0;
				gbc_panelCompetencias.gridy = 1;
				panelCompetencias.setLayout(new BorderLayout());
				contentPanel.add(panelCompetencias, gbc_panelCompetencias);
				{
					JPanel panelPrincipal = new JPanel();
					JScrollPane scrollPane = new JScrollPane();
					//panelPrincipal.setPreferredSize(new Dimension(400,400));
					scrollPane.setViewportView(panelPrincipal);
					panelCompetencias.add(scrollPane);
					panelPrincipal.setLayout(new GridBagLayout());
					
					listas = new JList[cf.getNumCompetences()];
					textMeans = new JTextField[cf.getNumCompetences()];
					showEvalButtons = new JButton[cf.getNumCompetences()];
					for(int i=0; i<cf.getNumCompetences(); i++){
						
						GridBagConstraints constraints = new GridBagConstraints();
						JPanel panelFila = new JPanel(new GridBagLayout());
						constraints.gridx = 0;
						constraints.gridy = 0;
						constraints.weighty = 0.0;
						constraints.weightx = 0.0;
						//constraints.fill = GridBagConstraints.NONE;
						
						//JPanel panelEtiq = new JPanel(new BorderLayout());
						//panelEtiq.setPreferredSize(new Dimension(50,50));
						//panelEtiq.add(new JLabel(cf.getCompetence(i).getName() + " "), BorderLayout.LINE_END);
						JLabel etiqNombre = new JLabel(cf.getCompetence(i).getName() + " ");
						etiqNombre.setPreferredSize(new Dimension(100,100));
						panelFila.add(etiqNombre, constraints);
						
						listas[i] = new JList();
						DefaultListModel model = new DefaultListModel();
						if(em.getNumEmployees()>0){
							for(int j=0; j<em.getEmployee(0).getEvaluations(i).size(); j++){
								model.add(j, em.getEmployee(0).getEvaluations(i).get(j));
							}
						}
						listas[i].setModel(model);
						
						final int k=i;
						listas[i].addMouseListener(new MouseAdapter(){
							  
							  public void mouseReleased(MouseEvent Me){
								  final JPopupMenu Pmenu = new JPopupMenu();
								  if(comboEmpleado.getSelectedItem()==null){
									  JOptionPane.showMessageDialog(contentPanel,
											    "There is no employee selected",
											    "Error",
											    JOptionPane.ERROR_MESSAGE);
								  }
								  else if(Me.getButton()==MouseEvent.BUTTON3){
									  JMenuItem menuItemNew = new JMenuItem("New evaluation");
									  Pmenu.add(menuItemNew);
									  menuItemNew.addActionListener(new ActionListener(){
										  public void actionPerformed(ActionEvent ae){
											  int selected=comboEmpleado.getSelectedIndex();
											  jDialogAddEvaluation dialogNewEval = new jDialogAddEvaluation(k,selected);
											  dialogNewEval.setModal(true);
											  dialogNewEval.setVisible(true);
											  if(dialogNewEval.getEvaluation()!=null){
												  Employee e = em.getEmployee(comboEmpleado.getSelectedIndex());
												  e.addEvaluation(dialogNewEval.getEvaluation());
												  repintarPanel(e);
											  }
								          }
									  });
									  if(listas[k].getSelectedIndex()>-1){
										  JMenuItem menuItemDel = new JMenuItem("Delete evaluation");
										  Pmenu.add(menuItemDel);
										  menuItemDel.addActionListener(new ActionListener(){
											  public void actionPerformed(ActionEvent ae){
												  Employee e = em.getEmployee(comboEmpleado.getSelectedIndex());
												  e.removeEvaluation(k, listas[k].getSelectedIndex());
												  DefaultListModel model = (DefaultListModel) listas[k].getModel();
												  model.remove(listas[k].getSelectedIndex());
												  repintarPanel(e); //Porque la media ha cambiado
											  }
										  });
									  }
									  Pmenu.show(Me.getComponent(), Me.getX(), Me.getY());
								  }
							  }
						});
						
						JPanel p = new JPanel();
						JScrollPane scrollPanel=new JScrollPane(listas[i]);
						p.add(scrollPanel);
						scrollPanel.setPreferredSize(new Dimension(300,100));
						constraints.gridx = 1;
						constraints.gridy = 0;
						panelFila.add(p, constraints);
						
						JPanel panelMean = new JPanel(new GridBagLayout());
						GridBagConstraints c2 = new GridBagConstraints();
						c2.gridx = 0; 
						c2.gridy = 0;
						c2.insets = new Insets(5,5,5,5);

						c2.weighty = 1.0;
						c2.weightx = 1.0;
						textMeans[i] = new JTextField(25);
						textMeans[i].setEditable(false);
						
						panelMean.add(new JLabel("Mean eval: "), c2);
						c2.gridy = 1;
						panelMean.add(textMeans[i], c2);
						c2.gridy = 3;
						showEvalButtons[i] = new JButton("Show eval");
						panelMean.add(showEvalButtons[i], c2);
						constraints.gridx = 2;
						constraints.gridy = 0;
						panelFila.add(panelMean, constraints);
						
						GridBagConstraints constraintsTotal = new GridBagConstraints();
						constraintsTotal.gridx = 0;
						constraintsTotal.gridy = i;
						constraintsTotal.weighty = 0.0;
						constraintsTotal.weightx = 0.0;
						panelPrincipal.add(panelFila, constraintsTotal);
					}
				}
			}
			if(em.getNumEmployees()>0){
				repintarPanel(em.getEmployee(0));
			}
			
		}
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

}
