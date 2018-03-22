package com.test01;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;


import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class FrameDemo2 {
	public static void main(String[] args) {
		//	为事件分发线程预定一个工作:创建并显示本程序的GUI
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				createAndShowGUI();
			}
		});
	}
	public static void createAndShowGUI(){
		// 设置窗口设置
		JFrame.setDefaultLookAndFeelDecorated(true);
		// 创建并设置程序运行窗口;
		JFrame frame=new JFrame("FrameDemo");
		frame.setIconImage(getFDImage());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel emptyLabel=new JLabel("");
		emptyLabel.setPreferredSize(new Dimension(175, 100));
		frame.getContentPane().add(emptyLabel, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}
	
	protected static Image getFDImage(){
		java.net.URL imgURL=FrameDemo2.class.getResource("F:/image/ic_launcher.png");
		System.out.println("img="+imgURL);
		if(imgURL!=null){
			return new ImageIcon(imgURL).getImage();
		}else {
			return null;
		}
	}
}
