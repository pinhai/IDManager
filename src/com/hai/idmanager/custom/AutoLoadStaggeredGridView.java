package com.hai.idmanager.custom;

import android.content.Context;
import android.util.AttributeSet;

import com.etsy.android.grid.StaggeredGridView;
import com.handmark.pulltorefresh.library.PullToRefreshStaggeredGridView;

public class AutoLoadStaggeredGridView extends PullToRefreshStaggeredGridView {
	private OnRefreshListener2<StaggeredGridView> mOnRefreshListener2;
	private boolean mIsLoading = false;
	public AutoLoadStaggeredGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public AutoLoadStaggeredGridView(
			Context context,
			com.handmark.pulltorefresh.library.PullToRefreshBase.Mode mode,
			com.handmark.pulltorefresh.library.PullToRefreshBase.AnimationStyle animStyle) {
		super(context, mode, animStyle);
		init();
	}

	public AutoLoadStaggeredGridView(Context context,
			com.handmark.pulltorefresh.library.PullToRefreshBase.Mode mode) {
		super(context, mode);
		init();
	}
	

	public AutoLoadStaggeredGridView(Context context) {
		super(context);
		init();
	}
	
	private void init(){
		setOnLastItemVisibleListener(mLastItemVisibleListener);
	}
	
	@Override
	public void setOnRefreshListener(OnRefreshListener2<StaggeredGridView> onRefreshListener2){
		super.setOnRefreshListener(onRefreshListener2);
		mOnRefreshListener2 = onRefreshListener2;
	}

	@Override
	public void onRefreshComplete() {
		super.onRefreshComplete();
		mIsLoading = false;
	}
	
	private OnLastItemVisibleListener mLastItemVisibleListener = new OnLastItemVisibleListener() {

		@Override
		public void onLastItemVisible() {
			if(mOnRefreshListener2 != null && !mIsLoading){
				mOnRefreshListener2.onPullUpToRefresh(AutoLoadStaggeredGridView.this);
				mIsLoading = true;
			}
		}
	};
}
