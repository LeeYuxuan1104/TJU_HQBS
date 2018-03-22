package com.demo02;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class TextFieldActionEventFrame extends JFrame{
	public TextFieldActionEventFrame() {
		super();
		setTitle("文本框动作事件事例");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JTextField sourceTextField=new JTextField(10);
		
		TargetTextField targetTextField=new TargetTextField(10);
		sourceTextField.addActionListener(targetTextField);
		JLabel label=new JLabel("=>");
		JPanel panel=new JPanel();
		panel.add(sourceTextField);
		panel.add(label);
		panel.add(targetTextField);
		
		this.getContentPane().add(panel,BorderLayout.CENTER);
		this.pack();
		this.setVisible(true);
	}
}
