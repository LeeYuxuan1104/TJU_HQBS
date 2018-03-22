package com.demo01;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class ActionEventFrame extends JFrame{
	public ActionEventFrame() {
		super();
		setTitle("动作事件示例");
		setBounds(100, 100, 300, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JButton OKButton=new JButton();
		OKButton.setText("登录");
		OKButton.addActionListener(new ButtonActionListener());
		
		getContentPane().add(OKButton,BorderLayout.SOUTH);
		this.setVisible(true);
	}
}
