package com.demo02;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

@SuppressWarnings("serial")
public class TargetTextField extends JTextField implements ActionListener {
	public TargetTextField() {
		super();
	}
	public TargetTextField(int length) {
		super(length);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JTextField txtField = (JTextField) e.getSource();
		this.setText(txtField.getText());

	}
}
