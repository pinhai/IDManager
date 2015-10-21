package com.hai.idmanager.custom;

import com.hai.idmanager.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class LoadMoreView {
	private TextView mTxtLoadMore;
	private View mLoadView;
	private boolean mIsLoading = false;
	private boolean mInEnd = false;
//	private OnClickListener mClickListener;
//	private OnClickListener loadClick = new OnClickListener() {
//		
//		@Override
//		public void onClick(View v) {
//			loadData();
//		}
//	};
	
	public LoadMoreView(Context context){
		LayoutInflater inflater = LayoutInflater.from(context);
		mLoadView = inflater.inflate(R.layout.load_more, null);
		mTxtLoadMore = (TextView)mLoadView.findViewById(R.id.txt_load_more);
//		mTxtLoadMore.setOnClickListener(loadClick);
	}
	
	public View getView(){
		return mLoadView;
	}
	
//	public void setOnClickListener(OnClickListener onClickListener){
//		mClickListener = onClickListener;
//	}
	
	public void loadData(){
		if(!mIsLoading && !mInEnd){
			mIsLoading = true;
			mTxtLoadMore.setText("正在加载中");
//			if(mClickListener != null){
//				mClickListener.onClick(mTxtLoadMore);
//			}
		}
	}
	
	public void reset(){
		mIsLoading = false;
		mInEnd = false;
		mTxtLoadMore.setText("查看更多");
	}
	/**
	 * 数据加载结束
	 */
	public void finish(){
		mIsLoading = false;
		mInEnd = false;
		mTxtLoadMore.setText("查看更多");
	}
	/**
	 * 加载到了最后一页
	 */
	public void end(){
		mIsLoading = false;
		mInEnd = true;
		mTxtLoadMore.setText("已加载全部");
	}
}
