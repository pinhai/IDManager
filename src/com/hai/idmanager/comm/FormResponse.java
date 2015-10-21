package com.hai.idmanager.comm;


public interface FormResponse<T> {
	public void onResponse(T response);
	
	public void onErrorResponse(int error);
}
