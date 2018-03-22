package com.model.tool.view;

import com.hqbs.app.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;

public class MTEditTextWithDel extends EditText {
	private final static String TAG = "EditTextWithDel";
	private Drawable imgInable;
	private Drawable imgAble;
	private Context mContext;
	//	下划线的内容;
	private Paint mPaint; 
	private int   kind;
	
	public MTEditTextWithDel(Context context) {
		super(context);
		mContext = context;
		init();
	}

	public MTEditTextWithDel(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init();
	}

	public MTEditTextWithDel(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}
	
	private void init() {
		imgInable = mContext.getResources().getDrawable(R.drawable.delete_gray);
		imgAble = mContext.getResources().getDrawable(R.drawable.delete);
		addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void afterTextChanged(Editable s) {
				setDrawable();
			}
		});
		setDrawable();
		kind=chooseKind();
		if(kind==1){
			//	画笔的内容;
			mPaint = new Paint();  
			mPaint.setStyle(Paint.Style.STROKE);  
			// 你可以根据自己的具体需要在此处对画笔做更多个性化设置  
			mPaint.setColor(Color.BLUE);			
		}
	}
	private int chooseKind(){
		String txt=getHint().toString();
		if(txt.equals("请输入您的账号")||txt.equals("请输入您的密码")){
			return 1;
		}else return 0;
	}
	
	
	
	@Override  
    public void onDraw(Canvas canvas) {  
        super.onDraw(canvas);  
        if(kind==1){        	
        	// 画底线  
        	canvas.drawLine(0, this.getHeight() - 1, this.getWidth() - 1, this.getHeight() - 1, mPaint);  
        }
    }  
	// 设置背景图片;
	private void setDrawable() {
		if(length() < 1)
			setCompoundDrawablesWithIntrinsicBounds(null, null, imgInable, null);
		else
			setCompoundDrawablesWithIntrinsicBounds(null, null, imgAble, null);
	}
	
	// 按下的标签内容;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (imgAble != null && event.getAction() == MotionEvent.ACTION_UP) {
            int eventX = (int) event.getRawX();
            int eventY = (int) event.getRawY();
            Log.e(TAG, "eventX = " + eventX + "; eventY = " + eventY);
            Rect rect = new Rect();
            getGlobalVisibleRect(rect);
            rect.left = rect.right - 50;
            if(rect.contains(eventX, eventY)) 
            	setText("");
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

}
