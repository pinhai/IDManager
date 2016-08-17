package com.hai.idmanager.ui;

import java.util.ArrayList;
import java.util.List;

import com.hai.idmanager.R;
import com.hai.idmanager.adapter.IdListAdapter;
import com.hai.idmanager.comm.respentity.IdModel;
import com.hai.idmanager.util.HeightUtil;
import com.hai.sqlite.DbHelper;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;

public class SearchIdInfoView extends PopupWindow implements OnClickListener{
	private String TAG = "SearchIdInfoView";
	
	private Context mContext;
	private OnClickSearchingItemListener mOnClickSearchingItemListener;
	private DbHelper mDbHelper;
	private View view;
	
	private EditText et_search;
	private Button btn_cancel;
	private ImageView iv_envelop;
	private ListView lv_searchingId;
	private IdListAdapter idListAdapter;
	private List<IdModel> idModels;
	private List<IdModel> idModelsTotal;	//储存全部ID
	
	public SearchIdInfoView(Context context){
		mContext = context;
	}
	
	public SearchIdInfoView(Context context, DbHelper dbHelper, OnClickSearchingItemListener onClickSearchingItemListener){
		mContext = context;
		mDbHelper = dbHelper;
		mOnClickSearchingItemListener = onClickSearchingItemListener;
		
		setPopupAttribute();
		initView();
	}
	
	private void setPopupAttribute(){
		view = LayoutInflater.from(mContext).inflate(R.layout.view_searchid, null);
		setContentView(view);
		
		setHeight(getViewHeight());
		setWidth(LayoutParams.MATCH_PARENT);
		setBackgroundDrawable(mContext.getResources().getDrawable(android.R.color.transparent));
		setFocusable(true);
		setOutsideTouchable(true);
	}

	private void initView(){
		et_search = (EditText) view.findViewById(R.id.et_search);
		btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
		iv_envelop = (ImageView) view.findViewById(R.id.iv_envelop);
		lv_searchingId = (ListView) view.findViewById(R.id.lv_searchingId);
		
		et_search.addTextChangedListener(textWatcher);
		et_search.setFocusable(true);
		InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		btn_cancel.setOnClickListener(this);
		iv_envelop.setOnClickListener(this);
		
		lv_searchingId = (ListView) view.findViewById(R.id.lv_searchingId);
		idModelsTotal = new ArrayList<>();
		idModelsTotal.addAll(mDbHelper.queryIdInfoByPage(0));
		idModels = new ArrayList<>();
		idListAdapter = new IdListAdapter(mContext, idModels);
		lv_searchingId.setAdapter(idListAdapter);
		
	}
	
	@Override
	public void onClick(View v) {
		if(v == btn_cancel || v == iv_envelop){
			this.dismiss();
		}
	}
	
	public void clearEditText(){
		et_search.setText("");
	}
	
	private int getViewHeight(){
		int viewHeight = HeightUtil.getScreenHeight(mContext) - HeightUtil.getStatusHeight((Activity) mContext);
		
		return viewHeight;
	}
	
	TextWatcher textWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			Log.i(TAG, "onTextChanged");
			String str = s.toString();
			if(str.toString().equals("") || str == null){
				lv_searchingId.setVisibility(View.GONE);
				iv_envelop.setVisibility(View.VISIBLE);
				return;
			}
			idModels.clear();
			for(IdModel idModel : idModelsTotal){
				if(idModel.getIdName().contains(str) || idModel.getIdInfo().contains(str)){
					idModels.add(idModel);
				}
			}
			idListAdapter.notifyDataSetChanged();
			lv_searchingId.setVisibility(View.VISIBLE);
			iv_envelop.setVisibility(View.GONE);
			lv_searchingId.setOnItemClickListener(itemClickListener);
			
		}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			Log.i(TAG, "beforeTextChanged");
		}
		@Override
		public void afterTextChanged(Editable s) {
			Log.i(TAG, "afterTextChanged");
		}
	};
	
	OnItemClickListener itemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Object tem = parent.getAdapter().getItem(position);
			if(tem instanceof IdModel){
				int mId = ((IdModel) tem).getId();
				mOnClickSearchingItemListener.onClickSearchingItem(mId);
			}
		}
	};
	
	public interface OnClickSearchingItemListener{
		void onClickSearchingItem(int id);
	}
}
