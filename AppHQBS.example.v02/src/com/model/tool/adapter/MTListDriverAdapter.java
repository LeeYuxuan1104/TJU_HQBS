package com.model.tool.adapter;
import java.util.ArrayList;
import java.util.Map;

import com.hqbs.app.R;
import com.model.tool.view.SwipeLayout;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MTListDriverAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Map<String, String>> list;
	private int size;
	private int index;
	private ListView listview;

	public MTListDriverAdapter(Context context,
			ArrayList<Map<String, String>> list, int index, ListView listview) {
		this.context = context;
		this.list = list;
		this.size = this.list.size();
		this.index = index;
		this.listview = listview;
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
		Map<String, String> 	map	= list.get(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView  = LayoutInflater.from(context).inflate(R.layout.act_item_node_n, null);
			//	滑动块;
			viewHolder.swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipelayout);
			//	文本内容;
			viewHolder.tvnumber = (TextView) convertView.findViewById(R.id.tvnumber);
			viewHolder.tvid 	= (TextView) convertView.findViewById(R.id.tvid);
			viewHolder.tvname 	= (TextView) convertView.findViewById(R.id.tvname);
			viewHolder.tvtel 	= (TextView) convertView.findViewById(R.id.tvtel);
			//	删除按钮;
			viewHolder.txt_delete = (TextView) convertView.findViewById(R.id.text_delete);
			viewHolder.linear 	= (LinearLayout) convertView.findViewById(R.id.linear);
			viewHolder.laystate = (RelativeLayout) convertView.findViewById(R.id.laystate);
			viewHolder.tvnumber.setVisibility(View.GONE);
			viewHolder.laystate.setVisibility(View.GONE);
			viewHolder.tvtel.setVisibility(View.VISIBLE);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		String id  		 = map.get("id");
		String name		 = map.get("name");
		String car_number= map.get("car_number");

		viewHolder.tvtel.setText(id);
		viewHolder.tvid.setText(name);
		viewHolder.tvname.setText(car_number);

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
				
			}
		});
		
		return convertView;
	}

	// 对象类的内容;
	private class ViewHolder {
		/** 滑动父控件 */
		private SwipeLayout swipeLayout;
		/** 序号文本 */
		private TextView tvnumber;
		private TextView tvtel;
		private TextView tvid;
		private TextView tvname;

		/** 删除按钮 */
		private TextView txt_delete;
		/** 右边试图 */
		private LinearLayout linear;
		private RelativeLayout laystate;
	}
}
