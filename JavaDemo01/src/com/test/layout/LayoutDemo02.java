package com.test.layout;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LayoutDemo02 {
	public static void main(String[] args) {
		JFrame frame=new JFrame("练习");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(500,500));
		//
		JPanel	listJPanel=new JPanel();
		listJPanel.setLayout(new BoxLayout(listJPanel, BoxLayout.PAGE_AXIS));
		JLabel  label=new JLabel("标签:");
		
		listJPanel.add(label);
		listJPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		
		JPanel buttonPane=new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		//	添加一个可以自动增长的空白组件;
		buttonPane.add(Box.createHorizontalGlue());
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		
		listJPanel.add(buttonPane);
		frame.add(listJPanel);
		frame.pack();
		frame.setVisible(true);
	}
}
