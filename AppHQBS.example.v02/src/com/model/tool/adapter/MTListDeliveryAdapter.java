package com.model.tool.adapter;

import java.util.ArrayList;
import java.util.Map;

import com.hqbs.app.R;
import com.model.tool.view.SwipeLayout;
import com.view.VDiagActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MTListDeliveryAdapter extends BaseAdapter{
	private Context				context;
	private ArrayList<Map<String, String>> 	list;
	private int 				size;
	private int 				index;
	private ListView			listview;

	public MTListDeliveryAdapter(Context context,ArrayList<Map<String, String>> list,int index,ListView listview) {
		this.context	= context;
		this.list		= list;
		this.size		= this.list.size();
		this.index		= index;
		this.listview	= listview;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		Map<String, String> map = list.get(position);
		if (convertView == null) {
			viewHolder 			= new ViewHolder();
			convertView  		= LayoutInflater.from(context).inflate(R.layout.act_item_node_n, null);
			//	滑动块;
			viewHolder.swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipelayout);
			//	文本内容;
			viewHolder.tvnumber = (TextView) convertView.findViewById(R.id.tvnumber);
			viewHolder.tvid 	= (TextView) convertView.findViewById(R.id.tvid);
			viewHolder.tvname 	= (TextView) convertView.findViewById(R.id.tvname);
			viewHolder.ivstate 	= (TextView) convertView.findViewById(R.id.ivstate);
			viewHolder.tvstate 	= (TextView) convertView.findViewById(R.id.tvstate);
			//	删除按钮;
			viewHolder.txt_delete = (TextView) convertView.findViewById(R.id.text_delete);
			viewHolder.linear = (LinearLayout) convertView.findViewById(R.id.linear);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		final String id		= map.get("id");
		final String deadline	= map.get("deadline");
		final String information= map.get("information");
		final String goal		= map.get("goal");
		final String name		= map.get("name");
		int    order	= position+1;
		//	显示;
		viewHolder.ivstate.setBackgroundResource(R.drawable.icon_inv_ok);
		viewHolder.tvstate.setText("审批通过");
		viewHolder.tvstate.setTextColor(Color.GREEN);
		//	
		viewHolder.tvnumber.setText(String.valueOf(order));
		//设置内容
		viewHolder.tvid.setText(id);
		viewHolder.tvname.setText(name);

		//	进行标签的颜色交换;
		if(position%2==0)
			viewHolder.tvnumber.setBackgroundResource(R.drawable.shape_order_n);
		else viewHolder.tvnumber.setBackgroundResource(R.drawable.shape_order1_n);
		
		//	滑块布局的内容;
		viewHolder.linear.setVisibility(View.GONE);
		
		
		 //删除按钮
        viewHolder.txt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        		list.remove(position);
        		notifyDataSetChanged();
            }
        });
		//设置自定义监听
		viewHolder.swipeLayout.setOnSlide(new SwipeLayout.onSlideListener() {
			//侧滑完了之后调用 true已经侧滑，false还未侧滑
			@Override
			public void onSlided(boolean isSlide) {
				if (isSlide) {//是否滑动成功（包括侧滑之后的返回滑动）
                    if (index != -1) {
                    	//当第一个已经侧滑了，在侧滑第二个的时候，就把第一个还原
                        if (listview.getChildAt(index - listview.getFirstVisiblePosition()) != null) {
                            SwipeLayout swipeLayout = (SwipeLayout) listview.getChildAt(index - listview.getFirstVisiblePosition()).findViewById(R.id.swipelayout);
                            swipeLayout.revert();
                        }
                    }
                    index = position;
                }
			}
			//未侧滑状态下的默认显示整体的点击事件
			@Override
			public void onClick() {
				Intent intent=new Intent(context, VDiagActivity.class);
				Bundle bundle=new Bundle();
				bundle.putString("id",id);
				
				String string		 =
				"回收员        "+name+"\r\n\r\n" +
				"电池信息    \r\n"+information+"\r\n\r\n" +
				"地址           "+goal+"\r\n\r\n" +
				"日期           "+deadline;
				
				bundle.putString("content",string);
				intent.putExtras(bundle);
				context.startActivity(intent);
			}
		});
		
		return convertView;
	}
	//	对象类的内容;
	private class ViewHolder {
		/** 滑动父控件 */
		private SwipeLayout swipeLayout;
		/** 序号文本 */
		private TextView tvnumber;
		private TextView tvid;
		private TextView tvname;
		/**	状态标记**/
		private TextView ivstate;
		private TextView tvstate;
		/** 删除按钮 */
		private TextView txt_delete;
		/** 右边试图*/
		private LinearLayout linear;
	}
}
