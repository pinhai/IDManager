package com.hai.idmanager;

import android.app.Activity;

public class BaseActivity extends Activity {
	@SuppressWarnings("unchecked")
	protected <T> T findView(int id){
        return (T) findViewById(id);
    }
	
}
