package com.moni.medianizer.app.view;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.moni.medianizer.app.Constants;

/**
 * Panel mit Medium, Titel und Interpret
 */
public class SelectPanel extends JPanel {
	private static final long serialVersionUID = -2732850112048175347L;
	
	private JComboBox<String> jcbTypes = new JComboBox<>(Constants.SA_TYPES);
	private JLabel jlTitle = new JLabel(Constants.S_TITLE);
	private JTextField jtfTitle = new JTextField();
	private JLabel jlInterpret = new JLabel(Constants.S_INTERPRET);
	private JTextField jtfInterpret = new JTextField();
	
	public SelectPanel() {
		init();
	}
	
	private void init() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
		
		jtfTitle.setPreferredSize(new Dimension(100,25));
		jtfInterpret.setPreferredSize(new Dimension(100,25));
		
		this.add(jcbTypes);
		this.add(jlTitle);
		this.add(jtfTitle);
		this.add(jlInterpret);
		this.add(jtfInterpret);
	}
	
	public String getType() {
		return (String) jcbTypes.getSelectedItem();
	}
	
	public String getTitle() {
		return jtfTitle.getText();
	}
	
	public String getInterpret() {
		return jtfInterpret.getText();
	}
}
