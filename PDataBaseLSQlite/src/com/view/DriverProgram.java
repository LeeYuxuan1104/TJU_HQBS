package com.view;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class DriverProgram {
	public static void main(String[] args) {
		JFrame f = new JFrame("我的小窗体！");
		Container c = f.getContentPane();
		c.setLayout(null);
		// JLable组件
		JLabel l = new JLabel("标签：");
		l.setFont(new Font("宋体", Font.BOLD, 20));// 设置字体大小格式
		l.setForeground(Color.BLUE);// 设置颜色
		l.setSize(300, 20);
		l.setLocation(10, 20);
		c.add(l);
		// JTextField 组件
		JTextField tx = new JTextField();
		tx.setForeground(Color.YELLOW);
		tx.setSize(300, 20);
		tx.setLocation(10, 200);
		c.add(tx);
		JPasswordField p = new JPasswordField("请输入密码：");
		p.setForeground(Color.GREEN);// 设置颜色
		p.setSize(300, 40);// 设置大小
		p.setLocation(10, 300);// 设置位置
		c.add(p);
		// JTextArea 组件
		JTextArea ta = new JTextArea();
		ta.setForeground(Color.ORANGE);
		ta.setSize(300, 80);
		ta.setLocation(10, 400);
		c.add(ta);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(300, 400);// 设置窗口大小
		f.setVisible(true);// 显示窗口
	}
}
