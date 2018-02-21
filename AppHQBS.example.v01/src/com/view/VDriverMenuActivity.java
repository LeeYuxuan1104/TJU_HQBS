package com.view;

import com.hqbs.app.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class VDriverMenuActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_node_menu);
	}
	@Override
	public void onClick(View view) {
		int nVid=view.getId();
		switch (nVid) {
		case 1:
			
			break;
		
		default:
			break;
		}
	}

}
