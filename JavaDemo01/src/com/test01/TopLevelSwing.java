package com.test01;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;

public class TopLevelSwing {
	private static void createAndShowGUI() {
		//	创建并设置窗体;
		JFrame frame = new JFrame("TopLevel Demo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JMenuBar greenMenuBar=new JMenuBar();
		greenMenuBar.setOpaque(true);
		greenMenuBar.setBackground(new Color(154, 165, 107));
		greenMenuBar.setPreferredSize(new Dimension(200,20));
		JLabel yellowLabel=new JLabel();
		yellowLabel.setOpaque(true);
		yellowLabel.setBackground(new Color(248,210,101));
		yellowLabel.setPreferredSize(new Dimension(200,180));
		frame.setJMenuBar(greenMenuBar);
		frame.getContentPane().add(yellowLabel, BorderLayout.CENTER);
		//	显示窗体;
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				createAndShowGUI();

			}
		});
	}
}
