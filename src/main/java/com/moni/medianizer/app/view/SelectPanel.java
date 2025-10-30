package com.moni.medianizer.app.view;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.moni.medianizer.app.Constants;
import com.moni.medianizer.app.controller.listener.MediaListener;

/**
 * Panel mit Medium, Titel und Interpret
 */
public class SelectPanel extends JPanel implements InputProvider {
	
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
		jtfTitle.setEnabled(false);
		jtfInterpret.setPreferredSize(new Dimension(100,25));
		jtfInterpret.setEnabled(false);
		
		this.add(jcbTypes);
		this.add(jlTitle);
		this.add(jtfTitle);
		this.add(jlInterpret);
		this.add(jtfInterpret);
	}
	
	/**
	 * Fügt der ComboBox für die Typ-Auswahl den Listener hinzu
	 * @param l
	 */
	public void addTypeChangeListener(MediaListener l) {
		jcbTypes.addItemListener(l);
	}

	@Override
	public String getType() {
		return (String) jcbTypes.getSelectedItem();
	}

	@Override
	public String getTitle() {
		return jtfTitle.getText().trim();
	}

	@Override
	public String getInterpret() {
		return jtfInterpret.getText().trim();
	}

	@Override
	public void setInterpretEnabled(boolean enabled) {
		jtfInterpret.setEnabled(enabled);	
	}

	@Override
	public void clearInterpret() {
		jtfInterpret.setText("");
		
	}

	@Override
	public void setTitleEnabled(boolean enabled) {
		jtfTitle.setEnabled(enabled);	
	}

	@Override
	public void clearTitle() {
		jtfTitle.setText("");
	}
	
}
