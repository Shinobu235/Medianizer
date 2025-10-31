package com.moni.medianizer.app.view;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.moni.medianizer.app.Constants;

public class AmountPane extends JPanel implements InsertProvider {
	
	private static final long serialVersionUID = -3962658200932031069L;
	
	private JLabel jlAmount = new JLabel(Constants.S_AMOUNT);
	private JTextField jtfAmount = new JTextField();
	
	public AmountPane() {
		init();
	}
	
	private void init() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
		
		jtfAmount.setPreferredSize(new Dimension(100, 25));
		this.add(jlAmount);
		this.add(jtfAmount);
	}
	
	@Override
	public int getAmount() {
		return Integer.parseInt(this.jtfAmount.getText());
	}
}
