package view.GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class jDialogAbout extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			jDialogAbout dialog = new jDialogAbout();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public jDialogAbout() {
		this.setTitle("About EmployeesProfileViewer");
		setBounds(100, 100, 527, 338);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel labelLogoEpsig = new JLabel("");
		labelLogoEpsig.setBounds(12, 12, 100, 115);
		JLabel labelLogoUniovi = new JLabel("");
		labelLogoUniovi.setBounds(357, 12, 150, 115);
		this.setLocationRelativeTo(null);
		
		try {
			//Recognize file as image
			URL urlEpsig=this.getClass().getResource("/resources/img/epsig_logo.gif");
			URL urlUniovi=this.getClass().getResource("/resources/img/uniovi_logo.png");			
			//Show the image inside the label
			labelLogoEpsig.setIcon(new ImageIcon(urlEpsig));
			labelLogoUniovi.setIcon(new ImageIcon(urlUniovi));
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		contentPanel.add(labelLogoEpsig);
		contentPanel.add(labelLogoUniovi);
		
		JLabel lblUniversityOfOviedo = new JLabel("University of Oviedo");
		lblUniversityOfOviedo.setBounds(185, 109, 158, 15);
		contentPanel.add(lblUniversityOfOviedo);
		
		JLabel lblEscuelaPolitecnicaDe = new JLabel("Escuela Politécnica de Ingeniería Informática de Gijón");
		lblEscuelaPolitecnicaDe.setBounds(12, 166, 393, 15);
		contentPanel.add(lblEscuelaPolitecnicaDe);
		
		JLabel lblAuthorIvanObeso = new JLabel("Author: Iván Obeso Agüera");
		lblAuthorIvanObeso.setBounds(12, 192, 197, 15);
		contentPanel.add(lblAuthorIvanObeso);
		
		JLabel lblEmailIvanobesogmailcom = new JLabel("Email: ivan.obeso1@gmail.com");
		lblEmailIvanobesogmailcom.setBounds(12, 219, 222, 15);
		contentPanel.add(lblEmailIvanobesogmailcom);
		
		JLabel lblEmployeesprofileviewer = new JLabel("EmployeesProfileViewer");
		lblEmployeesprofileviewer.setFont(new Font("Dialog", Font.BOLD, 16));
		lblEmployeesprofileviewer.setBounds(143, 55, 222, 15);
		contentPanel.add(lblEmployeesprofileviewer);
		
		JLabel lblFinalMscProject = new JLabel("Final MSc project");
		lblFinalMscProject.setBounds(197, 82, 130, 15);
		contentPanel.add(lblFinalMscProject);
		
		JLabel lblJanuary = new JLabel("January, 2013");
		lblJanuary.setBounds(12, 246, 100, 15);
		contentPanel.add(lblJanuary);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(new ActionListener() {
				    public void actionPerformed(ActionEvent evt) {
				    	dispose();
				    }
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}
}
