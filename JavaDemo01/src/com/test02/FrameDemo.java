package com.test02;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

public class FrameDemo {
	private static void createAndShowGUI() {
		//	创建并设置窗体;
		JFrame frame = new JFrame("练习");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(200, 200));
		JPanel jPanel1=new JPanel();
		//按钮控件;
		JButton button=new JButton();
		button.setText("按钮");
		jPanel1.add(button);
		//复选框控件;
		JCheckBox jCheckBox=new JCheckBox("读书");
		jPanel1.add(jCheckBox);
		//单选按钮;
		JRadioButton jRadioButton=new JRadioButton();
		jPanel1.add(jRadioButton);
		//button group的使用;
		ButtonGroup buttonGroup=new ButtonGroup();
		JRadioButton jrb1=new JRadioButton("男");
		JRadioButton jrb2=new JRadioButton("女");
		buttonGroup.add(jrb1);
		buttonGroup.add(jrb2);
		jPanel1.add(jrb1);
		jPanel1.add(jrb2);
		// 密码框组件;
		JPasswordField jPasswordField=new JPasswordField(16);
		//	设置隐藏的值内容;
		jPasswordField.setEchoChar('·');
		jPanel1.add(jPasswordField);
		//	进行数据的设置;
		String [] petName={"小狗","小猫","兔子"};
		@SuppressWarnings({ "rawtypes", "unchecked" })
		JComboBox comboBox=new JComboBox(petName);
		comboBox.setSelectedIndex(0);
		jPanel1.add(comboBox);
		//	滑块控件;
		JSlider slider1=new JSlider();
		jPanel1.add(slider1);
		//	微调组制组件;
		JSpinner spinner1=new JSpinner();
		jPanel1.add(spinner1);
		//	菜单组件;
		JMenuBar menuBar=new JMenuBar();
		//	菜单内容;
		JMenu menu,submenu;
		menu=new JMenu("菜单A");
		menu.setMnemonic(KeyEvent.VK_A);
		menu.getAccessibleContext().setAccessibleDescription("只有这个菜单有菜单想");
		menuBar.add(menu);
		jPanel1.add(menuBar);
		//	进度条;
		JProgressBar jProgressBar=new JProgressBar(0, 100);
		jProgressBar.setValue(0);
		jProgressBar.setStringPainted(true);
		//	提示对话框;
		jProgressBar.setToolTipText("进度提示框");
		jPanel1.add(jProgressBar);
		//	调色板控件;
		JColorChooser colorChooser=new JColorChooser();
		jPanel1.add(colorChooser);
		//	调色板控件2;
		Color	selectedColor=colorChooser.getColor();
		JLabel label=new JLabel("颜色");
		label.setForeground(selectedColor);
		jPanel1.add(label);
		//	文件选择框;
//		JFileChooser fc=new JFileChooser();
//		int returnVal=fc.showOpenDialog(jPanel1);
//		jPanel1.add(fc);
		//	编辑窗格;
		JEditorPane editorPane=new JEditorPane();
		editorPane.setText("xxx");
		//	可以选择不可以编辑;
//		editorPane.setEditable(false);
		//	获得输入框的内容信息;
		String input=editorPane.getText().toString();
		System.out.println("input="+input);
		//	将输入框放置到下拉列表里;
		JScrollPane editorScrollPane=new JScrollPane(editorPane);
		jPanel1.add(editorScrollPane);
		JTextArea textArea=new JTextArea(3, 20);
		JScrollPane scrollPane=new JScrollPane(textArea);
		textArea.setEditable(false);
		jPanel1.add(scrollPane);
		//	分割面板
		
		
		//	放置布局;
		frame.add(jPanel1);
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
