package com.hai.idmanager.ui;

import com.hai.idmanager.R;

import android.R.color;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

public class DelIdView extends PopupWindow implements OnClickListener{
	private Context mContext;
	private OnDelIdListener mDelIdListener;
	private int mId;
	private View view;
	private int windowHeight;
	private int windowWidth;
	
	private Button btn_delId;
	
	public DelIdView(Context context, int id, OnDelIdListener delIdListener){
		mContext = context;
		mDelIdListener = delIdListener;
		mId = id;
		
		setPopupAttribute();
		initView();
		
	}
	
	private void setPopupAttribute(){
		view = LayoutInflater.from(mContext).inflate(R.layout.view_delid, null);
		setContentView(view);
		
//		windowHeight = view.findViewById(R.id.linear_container).getMeasuredHeight();
//		windowWidth = view.findViewById(R.id.linear_container).getMeasuredWidth();
		setHeight(LayoutParams.WRAP_CONTENT);
		setWidth(LayoutParams.WRAP_CONTENT);
		setBackgroundDrawable(mContext.getResources().getDrawable(color.holo_blue_dark));
		setFocusable(true);
		setOutsideTouchable(true);
	}

	private void initView(){
		btn_delId = (Button) view.findViewById(R.id.btn_delId);
		btn_delId.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if(v == btn_delId){
			mDelIdListener.onDelId(mId);
		}
	}
	
	/**
	 * 删除账号信息回调接口
	 * @author admin
	 *
	 */
	public interface OnDelIdListener{
		void onDelId(int id);
	}
	
}
