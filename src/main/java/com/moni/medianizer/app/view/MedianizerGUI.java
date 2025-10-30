package com.moni.medianizer.app.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.moni.medianizer.app.Constants;
import com.moni.medianizer.app.controller.listener.MediaListener;
import com.moni.medianizer.app.controller.listener.OkButtonListener;

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
		jbOK.addActionListener(new OkButtonListener(sPanel));
		jbOK.setEnabled(false);
		MediaListener mediaListener = new MediaListener(sPanel, selected -> {
			jbOK.setEnabled(selected != Constants.S_DEFAULT);
		});
		sPanel.addTypeChangeListener(mediaListener);
		
		jpSelect.add(sPanel, BorderLayout.CENTER);
		
		jpSelect.add(jpOK, BorderLayout.EAST);
		jpOK.add(jbOK);
	
		frame.add(jpSelect);
		frame.pack();
		frame.setMinimumSize(frame.getSize());
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}	
}
