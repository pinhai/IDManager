package com.hai.idmanager;

import com.hai.idmanager.comm.FormResponse;
import com.hai.idmanager.comm.respentity.PageModel;

public class PageRequest<T> {
	private int mCurPage;
	private DoRequest<T> mDoRequest;
	private int mInitPage;
	private int mTotalPage = -1;
	private FormResponse<PageModel<T>> mResponse;
	
	private FormResponse<PageModel<T>> mDefaultResponse = new FormResponse<PageModel<T>>() {

		@Override
		public void onErrorResponse(int error) {
			if(mResponse != null){
				mResponse.onErrorResponse(error);
			}
		}

		@Override
		public void onResponse(PageModel<T> response) {
			mTotalPage = response.getTotalPage();
			if(mResponse != null){
				mResponse.onResponse(response);
			}
		}
	};

	public PageRequest(DoRequest<T> doRequest, FormResponse<PageModel<T>> response){
		this(doRequest, response, 1);
	}
	
	public PageRequest(DoRequest<T> doRequest, FormResponse<PageModel<T>> response, int initPage){
		mDoRequest = doRequest;
		mInitPage = initPage;
		mCurPage = mInitPage;
		mResponse = response;
	}
	
	public void setResponse(FormResponse<PageModel<T>> response){
		mResponse = response;
	}
	
	public void setInitPage(int page){
		mInitPage = page;
	}
	
	public int getCurPage(){
		return mCurPage;
	}
	
	public void init(){
		mCurPage = mInitPage;
		if(mDoRequest != null){
			mDoRequest.doRequest(mCurPage, mDefaultResponse);
		}
	}
	
	public boolean next(){
		if(mCurPage >= mTotalPage){
			return false;
		}
		mCurPage++;
		if(mDoRequest != null){
			mDoRequest.doRequest(mCurPage, mDefaultResponse);
		}
		return true;
	}
	
	public boolean isInited(){
		return mInitPage != -1;
	}
	
	public boolean isEnd(){
		return mCurPage >= mTotalPage;
	}
	
	public boolean previous(){
		if(mCurPage == 1){
			return false;
		}
		mCurPage--;
		if(mDoRequest != null){
			mDoRequest.doRequest(mCurPage, mDefaultResponse);
		}
		return true;
	}
	
	public interface DoRequest<T>{
		void doRequest(int page, FormResponse<PageModel<T>> response);
	}
}
