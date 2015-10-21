package com.hai.idmanager;

import android.R.color;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;

public class BasePopupWindow extends PopupWindow{
	private Context mContext;
	
	public BasePopupWindow(Context context){
		super(context);
	}
	
	public BasePopupWindow(View contentView, int width, int height){
		super(contentView, width, height, false);
	}
	
	public BasePopupWindow(View contentView, int width, int height, boolean focusable){
		super(contentView, width, height, focusable);
	}
	
	public void setPopupAttribute(Context context, int height){
		mContext = context;
//		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
//		windowHeight = wm.getDefaultDisplay().getHeight()*2/5;
//		windowHeight = height;
//		windowWidth = wm.getDefaultDisplay().getWidth();
		
		setHeight(LayoutParams.WRAP_CONTENT);
		setWidth(LayoutParams.WRAP_CONTENT);
		setBackgroundDrawable(mContext.getResources().getDrawable(color.white));
		setFocusable(true);
		setOutsideTouchable(true);
		setAnimationStyle(R.style.PopupAnimation);
		setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
	}
	
}
