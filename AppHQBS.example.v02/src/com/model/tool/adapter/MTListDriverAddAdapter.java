package com.model.tool.adapter;

import java.util.ArrayList;
import java.util.Map;

import com.hqbs.app.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MTListDriverAddAdapter extends BaseAdapter{
	
	private Context context;
	private ArrayList<Map<String, String>> list;
	private int		size;
	
	public MTListDriverAddAdapter(Context context,ArrayList<Map<String, String>> list) {
		this.context=context;
		this.list	=list;
		this.size	=this.list.size();
	}
	@Override
	public int getCount() {
		return size;
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		Map<String, String> map=list.get(position);
		String tel		 = map.get("id");
		String car_number= map.get("car_number");
		String name		 = map.get("name");										
		String state	 = map.get("state");
		
		if (convertView == null) {
			viewHolder 	 = new ViewHolder();
			convertView  = LayoutInflater.from(context).inflate(R.layout.act_item_add_n, null);
			//	滑动块;
			viewHolder.name  = (TextView) convertView.findViewById(R.id.name);
			viewHolder.number= (TextView) convertView.findViewById(R.id.number);
			viewHolder.tel	 = (TextView) convertView.findViewById(R.id.tel);
			viewHolder.ivstate	 = (TextView) convertView.findViewById(R.id.ivstate);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
			
		}
		//	显示的控件;
		viewHolder.name.setText(name);
		viewHolder.number.setText(car_number);
		viewHolder.tel.setText(tel);
		//	状态;
		int nstate=Integer.parseInt(state);
		
		if(nstate==1)
			viewHolder.ivstate.setBackgroundResource(R.drawable.icon_person_sele);	
		else
			viewHolder.ivstate.setBackgroundResource(R.drawable.icon_person_unsele);
		
		return convertView;
	}
	//	对象类的内容;
	private class ViewHolder {
		/** 序号文本 */
		private TextView name;
		private TextView number;
		private TextView tel;

		/**	状态标记**/
		private TextView 		ivstate;
		
	}
}
