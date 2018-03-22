package com.demo01;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class ButtonActionListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton button=(JButton) e.getSource();
		// TODO Auto-generated method stub
		String buttonName=e.getActionCommand();
		if(buttonName.equals("登录")){
			button.setText("退出登录");
			JOptionPane.showMessageDialog(null, "您已经成功登录!","消息",JOptionPane.INFORMATION_MESSAGE);
		}else{
			int answer=JOptionPane.showConfirmDialog(null, "您确定要退出吗?","消息",JOptionPane.YES_NO_OPTION);
			if(answer==0) button.setText("登录");
		}
	}

}
