package com.test.layout;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BorderLayoutDemo {
	public static void main(String[] args) {
		JFrame frame=new JFrame("布局");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(200, 200));
		
		JPanel jPanel1=new JPanel();
		JButton button=new JButton("按钮1 (PAGE_START)");
		jPanel1.add(button,BorderLayout.PAGE_START);
		
		button=new JButton("按钮2 (中间)");
		button.setPreferredSize(new Dimension(200, 100));
		jPanel1.add(button,BorderLayout.CENTER);
		
		button=new JButton("按钮3 (LINE_START)");
		jPanel1.add(button,BorderLayout.LINE_START);
		
		button=new JButton("按钮4 (PAGE_END)");
		jPanel1.add(button,BorderLayout.PAGE_END);
		
		button=new JButton("按钮5 (LINE_END)");
		jPanel1.add(button,BorderLayout.LINE_END);
		
		frame.add(jPanel1);
		frame.pack();
		frame.setVisible(true);
	}
}
