package com.moni.medianizer.app.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.moni.medianizer.app.Constants;

/**
 * Startfenster
 */
public class MedianizerGUI {
	private static MedianizerGUI instance;
	private JFrame frame = new JFrame(Constants.S_APP_NAME);
	private SelectPanel sPanel = new SelectPanel();
	private JPanel jpSelect = new JPanel(new BorderLayout(20, 20));
	private JPanel jpOK = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
	private JButton jbOK = new JButton(Constants.S_OK);
	
	private MedianizerGUI() {
		createGUI();
	}
	
	/**
	 * Singleton Startfenster
	 * @return instance
	 */
	public static MedianizerGUI getInstance() {
		if (instance == null) {
			instance = new MedianizerGUI();
		}
		return instance;
	}
	
	/**
	 * Erstellung Startfenster
	 */
	private void createGUI() {
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jpSelect.add(sPanel, BorderLayout.CENTER);
		jbOK.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println();
				
			}
		});
		jpSelect.add(jpOK, BorderLayout.EAST);
		jpOK.add(jbOK);
		jbOK.addActionListener(e -> {
			String sType = sPanel.getType();
			String sTitle = sPanel.getTitle();
			String sInterpret = sPanel.getInterpret();
			
			JOptionPane.showMessageDialog(frame, "Medium: " + sType + " Titel: " + sTitle + " Interpret: " + sInterpret, "Eingabe bestätigt", JOptionPane.INFORMATION_MESSAGE);
		});
	
		frame.add(jpSelect);
		frame.pack();
		frame.setMinimumSize(frame.getSize());
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
