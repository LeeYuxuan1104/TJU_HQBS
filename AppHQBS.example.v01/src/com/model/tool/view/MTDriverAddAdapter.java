package com.model.tool.view;
import java.util.ArrayList;
import java.util.Map;

import com.hqbs.app.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
public class MTDriverAddAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<Map<String, String>> 	listinfo;
	private int	 	size;
	
	public MTDriverAddAdapter(Context context,ArrayList<Map<String, String>> listinfo) {
		this.context	=context;
		this.listinfo	=listinfo;
		this.size		=this.listinfo.size();
	}
	
	public ArrayList<Map<String, String>> getListinfo() {
		return listinfo;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return size;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int id) {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		//	相应显示控件的内容;
		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder 	 = new ViewHolder();
			convertView  = LayoutInflater.from(context).inflate(R.layout.act_item03, null);
			
			viewHolder.tvnumber = (TextView) convertView.findViewById(R.id.number);
			viewHolder.tvcontent= (TextView) convertView.findViewById(R.id.content);
			viewHolder.cbcheck	= (CheckBox) convertView.findViewById(R.id.check);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		Map<String, String> map			=	listinfo.get(position);
		String 				id			=	map.get("id");
		String 				carnumber	=	map.get("carnumber");
		String 				name		=	map.get("name");
		String 				state		=	map.get("state");
		final int					nstate		=	Integer.parseInt(state);
		
		viewHolder.tvnumber.setText(id);
		viewHolder.tvcontent.setText(carnumber+"  "+name);
		switch (nstate) {
		case 0:
			viewHolder.cbcheck.setChecked(false);
			break;
		case 1:
			viewHolder.cbcheck.setChecked(true);
			break;

		default:
			break;
		}
		viewHolder.cbcheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean flag) {
				if(flag) listinfo.get(position).put("state","1");
				else listinfo.get(position).put("state","0");
			}
		});
		
		notifyDataSetChanged();
		return convertView;
	}
	private class ViewHolder {
		private TextView tvnumber,tvcontent;
		private CheckBox cbcheck;
	}
}
