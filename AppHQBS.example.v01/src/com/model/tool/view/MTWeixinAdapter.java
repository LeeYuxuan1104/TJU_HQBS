package com.model.tool.view;
import java.util.List;
import java.util.Map;

import com.hqbs.app.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
public class MTWeixinAdapter extends BaseAdapter{
	private Context context;
	private List<Map<String, String>> 	listinfo;
	private int	 	size;
	
	public MTWeixinAdapter(Context context,List<Map<String, String>> listinfo) {
		this.context	=context;
		this.listinfo	=listinfo;
		this.size		=this.listinfo.size();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		//	相应显示控件的内容;
		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder 	 = new ViewHolder();
			convertView  = LayoutInflater.from(context).inflate(R.layout.act_weixin_item, null);
			
			viewHolder.tvleft 	= (TextView) convertView.findViewById(R.id.tvleft);
			viewHolder.tvright  = (TextView) convertView.findViewById(R.id.tvright);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		Map<String, String> map		=	listinfo.get(position);
		String 				cookie	=	map.get("cookie");
		int					nCookie	=	Integer.parseInt(cookie);
		String 				content	=	map.get("content");
		if(nCookie==0) viewHolder.tvleft.setText(content);
		else viewHolder.tvright.setText(content);
		
		return convertView;
	}
	private class ViewHolder {
		private TextView tvleft;
		private TextView tvright;
	}
}
