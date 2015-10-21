package com.hai.idmanager.custom;

import android.content.Context;
import android.support.v4.util.SparseArrayCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class AutoSizeGridView extends GridView {
	private SparseArrayCompat<Boolean> arrayCompat = new SparseArrayCompat<Boolean>();
	public AutoSizeGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public AutoSizeGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public AutoSizeGridView(Context context) {
		super(context);
	}
	
	public void setChecked(int position, boolean isChecked){
		if(isChecked){
			arrayCompat.put(position, true);
		}else{
			arrayCompat.remove(position);
		}
		int count= getChildCount();
		if(position < count && position >= 0){
			((ViewGroup)getChildAt(position)).getChildAt(0).setSelected(isChecked);
		}
	}
	
	public void toggleChecked(int position){
		int count= getChildCount();
		if(position < count && position >= 0){
			View view = ((ViewGroup)getChildAt(position)).getChildAt(0);
			boolean isSelected = view.isSelected();
			setChecked(position, !isSelected);
		}
	}
	
	public int[] getCheckItems(){
		int size = arrayCompat.size();
		if(size == 0){
			return null;
		}
		int[] values = new int[size];
		int count = getChildCount();
		int curPos = 0;
		for(int i = 0; i < count && curPos < size; i++){
			if(arrayCompat.indexOfKey(i) >= 0){
				values[curPos] = i;
				curPos++;
			}
		}
		if(size == curPos){
			return values;
		}else{
			int[] tem = new int[curPos];
			for(int i = 0; i < curPos; i++){
				tem[i] = values[i];
			}
			return tem;
		}
	}
	
	public void clearChecked(){
		int count= getChildCount();
		for(int i = 0; i < count; i++){
			((ViewGroup)getChildAt(i)).getChildAt(0).setSelected(false);
		}
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if(changed){
			int count= getChildCount();
			if(count > 0){
				for(int i = 0; i < count; i++){
					if(arrayCompat.get(i, false)){
						((ViewGroup)getChildAt(i)).getChildAt(0).setSelected(true);
					}
				}
			}
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		int itemCount = getAdapter() == null ? 0 : getAdapter().getCount();
//		if(itemCount > 0){
//			int width = getMeasuredWidth();
//			int height = getMeasuredHeight();
//			height = height - getPaddingTop() - getPaddingBottom() - getVerticalFadingEdgeLength() * 2;
//			int rawCount = itemCount / 3;
//			rawCount = itemCount % 3 == 0 ? rawCount : rawCount + 1;
//			height = height * rawCount + UnitUtil.dipToPx(5, getResources()) * (rawCount - 1);
//			setMeasuredDimension(width, height);
//		}
	}
}
