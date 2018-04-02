package com.model.tool.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.widget.EditText;
import android.widget.TextView;

import com.hqbs.app.R;

public class MTMenuBar2 {
	private String [] 	t_top={"任务单","完成单"};
	private int [] 	  	t_sel={R.drawable.tab_bar_icon_list_sel,R.drawable.tab_bar_icon_bat_sel};
	private int [] 	  	t_nar={R.drawable.tab_bar_icon_list_nar,R.drawable.tab_bar_icon_bat_nar};
	private TextView[]  t_view=new TextView[2];
	private ArrayList<Map<String, Object>> barlists=null;
	private EditText	et;
	//	右方按钮;
	private String []	t_tip={"请输入单号","请输入单号"};
	
	public MTMenuBar2(TextView view0,TextView view1,EditText et) {
		this.t_view[0]=view0;
		this.t_view[1]=view1;
		this.et=et;
		//	进行数据的添加;
		this.barlists=getBottomBarList();
	}

	//	获得容器;	
	private ArrayList<Map<String, Object>> getBottomBarList(){
		ArrayList<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		for(int i=0;i<2;i++){
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("view", t_view[i]);
			map.put("topic", t_top[i]);
			map.put("sel", t_sel[i]);
			map.put("nar", t_nar[i]);
			list.add(map);
		}
		return list;
	}
	//	设置布局;
	public void setBottomlayout(int location,TextView vtopic){
		for(int i=0;i<2;i++){
			TextView view=(TextView) barlists.get(i).get("view");
			
			if(i==location){
				view.setBackgroundResource(Integer.parseInt(barlists.get(i).get("sel").toString()));
			}else view.setBackgroundResource(Integer.parseInt(barlists.get(i).get("nar").toString()));
		}
		vtopic.setText(barlists.get(location).get("topic").toString());
	}
	//	设置搜索框内容;
	public void setSearchTip(int location){
		et.setHint(t_tip[location]);
	}
}
