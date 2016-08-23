package com.hai.idmanager.ui;

import java.util.ArrayList;
import java.util.List;

import com.hai.idmanager.MainActivity;
import com.hai.idmanager.R;
import com.hai.idmanager.adapter.IdListAdapter;
import com.hai.idmanager.comm.respentity.IdModel;
import com.hai.idmanager.util.DimensionUtil;
import com.hai.sqlite.DbHelper;
import com.handmark.pulltorefresh.library.swipemenu.SwipeMenu;
import com.handmark.pulltorefresh.library.swipemenu.SwipeMenuCreator;
import com.handmark.pulltorefresh.library.swipemenu.SwipeMenuItem;
import com.handmark.pulltorefresh.library.swipemenu.SwipeMenuListView;
import com.handmark.pulltorefresh.library.swipemenu.SwipeMenuListView.OnMenuItemClickListener;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.Toast;

public class SearchIdInfoView extends PopupWindow implements OnClickListener{
	private String TAG = "SearchIdInfoView";
	
	private Context mContext;
	private OnItemListener mOnItemListener;
	private DbHelper mDbHelper;
	private View view;
	
	private EditText et_search;
	private Button btn_cancel;
	private ImageView iv_envelop;
	private SwipeMenuListView lv_searchingId;
	private IdListAdapter idListAdapter;
	private List<IdModel> idModels;	//存放搜索到的ID
	private List<IdModel> idModelsTotal;	//储存全部ID
	
	public SearchIdInfoView(Context context){
		mContext = context;
	}
	
	public SearchIdInfoView(Context context, DbHelper dbHelper, OnItemListener onItemListener){
		mContext = context;
		mDbHelper = dbHelper;
		mOnItemListener = onItemListener;
		
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
		lv_searchingId = (SwipeMenuListView) view.findViewById(R.id.lv_searchingId);
		
		et_search.addTextChangedListener(textWatcher);
		et_search.setFocusable(true);
		InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		btn_cancel.setOnClickListener(this);
		iv_envelop.setOnClickListener(this);
		
		idModelsTotal = new ArrayList<>();
		idModelsTotal.addAll(mDbHelper.queryIdInfoByPage(0));
		idModels = new ArrayList<>();
		idListAdapter = new IdListAdapter(mContext, idModels);
		lv_searchingId.setAdapter(idListAdapter);
		lv_searchingId.setMenuCreator(creator);
		lv_searchingId.setOnMenuItemClickListener(onMenuItemClickListener);
		
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
		int viewHeight = DimensionUtil.getScreenHeight(mContext) - DimensionUtil.getStatusHeight((Activity) mContext);
		
		return viewHeight;
	}
	
	private void deleteItem(int position){
		if(mDbHelper.delIdInfo(idModels.get(position).getId())){
			idModels.remove(position);
			idListAdapter.notifyDataSetChanged();
			Toast.makeText(mContext, "删除账号成功", Toast.LENGTH_SHORT).show();
			if(idModels.size() == 0){
				lv_searchingId.setVisibility(View.GONE);
				iv_envelop.setVisibility(View.VISIBLE);
			}
		}else{
			Toast.makeText(mContext, "删除账号失败", Toast.LENGTH_SHORT).show();
		}
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
	
	private SwipeMenuCreator creator = new SwipeMenuCreator() {
		@Override
		public void create(SwipeMenu menu) {
			// create "delete" item
			SwipeMenuItem deleteItem = new SwipeMenuItem(mContext);
			// set item background
			deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
			// set item width
			deleteItem.setWidth(DimensionUtil.dp2px(mContext, 90));
			// set a icon
			deleteItem.setIcon(R.drawable.ic_delete);
			// add to menu
			menu.addMenuItem(deleteItem);
		}
	};
	
	private OnMenuItemClickListener onMenuItemClickListener = new OnMenuItemClickListener() {
		@Override
		public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
			switch (index) {
			case 0:
				deleteItem(position);
				break;

			default:
				break;
			}
			return false;
		}
	};
	
	OnItemClickListener itemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Object tem = parent.getAdapter().getItem(position);
			if(tem instanceof IdModel){
				mOnItemListener.onClick(((IdModel)tem).getId());
			}
		}
	};
	
	public interface OnItemListener{
		void onClick(int id);
	}
}
