package com.moni.medianizer.app.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.moni.medianizer.app.Constants;
import com.moni.medianizer.app.controller.listener.InsertButtonListener;
import com.moni.medianizer.app.controller.listener.MediaListener;

public class SecondGUI {
	
	private String sType;
	private String sTitle;
	private String sInterpret;
	private JFrame frame = new JFrame(Constants.S_APP_NAME);
	private SelectPanel sPanel = new SelectPanel();
	private AmountPane amountPane = new AmountPane();
	private JPanel jpInsert = new JPanel();
	private JPanel jpFullPane = new JPanel(new BorderLayout(20,20));
	private JButton jbInsert = new JButton(Constants.S_INSERT);
	
	public SecondGUI(String type, String title, String interpret) {
		this.sType = type;
		this.sTitle = title;
		this.sInterpret = interpret;
		createGUI();
	}
	
	public SecondGUI(String type, String title) {
		this.sType = type;
		this.sTitle = title;
		createGUI();
	}
	
	/**
	 * Erstellung Benutzerschnittstelle 2
	 */
	private void createGUI() {
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		
		sPanel.setTitle(sTitle);
		sPanel.setType(sType);
		sPanel.setInterpret(sInterpret);
		
		jbInsert.addActionListener(new InsertButtonListener(sPanel, amountPane));
		
		jpInsert.setPreferredSize(new Dimension(100, 25));
		jpInsert.add(jbInsert);
		
		jpFullPane.add(sPanel, BorderLayout.WEST);
		jpFullPane.add(amountPane, BorderLayout.CENTER);
		jpFullPane.add(jpInsert, BorderLayout.EAST);
		
		frame.add(jpFullPane);
		frame.pack();
		frame.setMinimumSize(frame.getSize());
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}	
}
