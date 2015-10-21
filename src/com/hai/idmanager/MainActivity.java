package com.hai.idmanager;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.hai.idmanager.PageRequest.DoRequest;
import com.hai.idmanager.adapter.IdListAdapter;
import com.hai.idmanager.comm.FormResponse;
import com.hai.idmanager.comm.HomePageApi;
import com.hai.idmanager.comm.respentity.IdModel;
import com.hai.idmanager.comm.respentity.PageModel;
import com.hai.idmanager.custom.LoadMoreView;
import com.hai.idmanager.file.FileBackup;
import com.hai.idmanager.file.FileBackup.OnImportIdInfoListener;
import com.hai.idmanager.ui.AddIdView;
import com.hai.idmanager.ui.AddIdView.OnAddIdListener;
import com.hai.idmanager.ui.DelIdView;
import com.hai.idmanager.ui.DelIdView.OnDelIdListener;
import com.hai.idmanager.ui.EditIdActivity;
import com.hai.idmanager.ui.SearchIdInfoView;
import com.hai.idmanager.ui.SearchIdInfoView.OnClickSearchingItemListener;
import com.hai.sqlite.DbHelper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class MainActivity extends BaseActivity implements OnClickListener{
	private Button btn_addId;
	private PullToRefreshListView ptrlv_idInfo;
	private ListView lv_idInfo;
	private LoadMoreView loadMoreView ;
	
	private List<IdModel> mIdModels;
	private IdListAdapter mIdListAdapter;
	
	private PageRequest<IdModel> mIdItemPage;
	
	private AddIdView addIdView;
	private DelIdView delIdView;
	private SearchIdInfoView searchIdInfoView;
	private DbHelper dbHelper;
	
	private static long firstBackTimeMills = 0;
	
	private ProgressDialog dialog;
	
	private static int itemEdited = -1;	//记录被编辑的账号item行数
	
	private LinearLayout linear_top;
	private static int searchBarHeight = -1;
	
	private LinearLayout linear_search;
	
	private static final int IMPORT_ID_CODE = 112;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initData();
		initView();
	}

	private void initData() {
		dbHelper = new DbHelper(MainActivity.this);
		
		mIdItemPage = new PageRequest<IdModel>(idItemReq, idItemResp);
		
		loadMoreView = new LoadMoreView(this);
		
		mIdModels = new ArrayList<>();
		mIdListAdapter = new IdListAdapter(this, mIdModels);
		
	}

	private void initView() {
		btn_addId = findView(R.id.btn_addId);
		btn_addId.setOnClickListener(this);
		ptrlv_idInfo = findView(R.id.ptrlv_idInfo);
		lv_idInfo = ptrlv_idInfo.getRefreshableView();
		lv_idInfo.addFooterView(loadMoreView.getView());
		lv_idInfo.setAdapter(mIdListAdapter);
		lv_idInfo.setOnItemClickListener(mIdInfoItemClick);
		lv_idInfo.setOnItemLongClickListener(mIdItemLongClick);
		ptrlv_idInfo.setOnLastItemVisibleListener(mLastItem);
		ptrlv_idInfo.setOnRefreshListener(onRefreshListener);
		
		dialog = new ProgressDialog(this);
		
		showWaitingDialog();
		mIdItemPage.init();
		
		linear_top = findView(R.id.linear_top);
		linear_top.post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				searchBarHeight = linear_top.getMeasuredHeight();
			}
		});
		
		linear_search = findView(R.id.linear_search);
		linear_search.setOnClickListener(this);
		
		addIdView = new AddIdView(this, LayoutInflater.from(this).inflate(R.layout.view_addid, null), onAddIdListener);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.item_export) {
			if(FileBackup.exportIdInfo(dbHelper.queryIdInfoByPage(0))){
				Toast.makeText(this, "导出成功", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(this, "导出失败，请重试", Toast.LENGTH_SHORT).show();
			}
		}else if(id == R.id.item_import){
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("file/");
			try{
				startActivityForResult(intent, IMPORT_ID_CODE);
			}catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(this, "未安装文件管理器", Toast.LENGTH_SHORT).show();
			}
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if(searchIdInfoView.isShowing()){
				searchIdInfoView.dismiss();
			}else{
				long currentTimeMills = System.currentTimeMillis();
				if(currentTimeMills-firstBackTimeMills < 3000){
					System.exit(0);
				}else{
					Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
					firstBackTimeMills = currentTimeMills;
					return true;
				}
			}
			break;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_addId:
			showAddIdWindow();
			break;
		case R.id.linear_search:
			showSearchIdWindow();
			break;
		default:
			break;
		}
	}
	
	private void showSearchIdWindow() {
		searchIdInfoView = new SearchIdInfoView(this, dbHelper, new OnClickSearchingItemListener() {
			@Override
			public void onClickSearchingItem(int id) {
				searchIdInfoView.dismiss();
				IdModel idModel = dbHelper.queryIdInfoById(id);
				startEditIdActivityForResult(idModel);
			}
		});
		searchIdInfoView.clearEditText();
		searchIdInfoView.showAtLocation(getView(), Gravity.BOTTOM, 0, 0);
	}

	private void showAddIdWindow() {
		addIdView.clearEditText();
		addIdView.showAtLocation(getView(), Gravity.BOTTOM, 0, 0);
	}
	
	private View getView() {
		return LayoutInflater.from(this).inflate(R.layout.activity_main, null);
	}
	
	private void showWaitingDialog(){
		dialog.setTitle("提示");
		dialog.setMessage("请稍等...");
		dialog.setCancelable(false);
		dialog.show();
	}
	
	private void startEditIdActivityForResult(IdModel itemModel) {
		Intent intent = new Intent(MainActivity.this, EditIdActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("itemModel", itemModel);
		intent.putExtra("data", bundle);
		startActivityForResult(intent, 110);
	}
	
	OnAddIdListener onAddIdListener = new OnAddIdListener() {
		
		@Override
		public void onAddId(String idName, String idInfo) {
			dialog.dismiss();
			if(dbHelper.addIdInfo(new IdModel(idName, idInfo))){
				addIdView.dismiss();
				Toast.makeText(MainActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
				mIdModels.add(new IdModel(idName, idInfo));
				mIdListAdapter.notifyDataSetChanged();
//				mIdItemPage.init();
			}
		}

		@Override
		public void onAdding() {
			showWaitingDialog();
		}
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == 110 && resultCode == 110 && itemEdited >= 0 && data != null){
			IdModel idModelEdited = (IdModel) data.getBundleExtra("data").getSerializable("itemModel");
			mIdModels.set(itemEdited-1, idModelEdited);
			mIdListAdapter.notifyDataSetChanged();
			Toast.makeText(this, "账号修改成功", Toast.LENGTH_SHORT).show();
		}else if(requestCode == IMPORT_ID_CODE && data != null){
			Uri uri = data.getData();
			FileBackup.importIdInfo(this, uri, onImportIdInfoListener);
		}
	}
	
	private DoRequest<IdModel> idItemReq = new DoRequest<IdModel>() {
		@Override
		public void doRequest(int page,
				FormResponse<PageModel<IdModel>> response) {
			if(page == 1){
				mIdModels.clear();
			}
			HomePageApi homePageApi = new HomePageApi(MainActivity.this);
			homePageApi.getIdList(page, response);
		}
	};
	
	private FormResponse<PageModel<IdModel>> idItemResp = new FormResponse<PageModel<IdModel>>(){

		@Override
		public void onResponse(PageModel<IdModel> response) {
			if(mIdItemPage.isEnd()){
				loadMoreView.end();
			}else{
				loadMoreView.finish();
			}
			mIdModels.addAll(response.getDatas());
			mIdListAdapter.notifyDataSetChanged();
			dialog.dismiss();
			ptrlv_idInfo.onRefreshComplete();
		}

		@Override
		public void onErrorResponse(int error) {
			
		}
		
	};

	private OnItemClickListener mIdInfoItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Object tem = parent.getAdapter().getItem(position);
			if(tem instanceof IdModel){
				itemEdited = position;
				IdModel itemModel = (IdModel) tem;
				startEditIdActivityForResult(itemModel);
			}
		}

	};
	
	private OnItemLongClickListener mIdItemLongClick = new OnItemLongClickListener() {

		@SuppressLint("NewApi")
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			if((position-1) == mIdModels.size()){	//长按了页尾
				return false;
			}
			
			Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(50);
			
			final int p = position - 1;
			float offsetY = 0;
			if(searchBarHeight > 0){
				offsetY = view.getY() + searchBarHeight/2;
			}else{
				offsetY = view.getY() + 30;
			}
			
			delIdView = new DelIdView(MainActivity.this, mIdModels.get(p).getId(), new OnDelIdListener() {
				@Override
				public void onDelId(int id) {
					if(dbHelper.delIdInfo(id)){
						mIdModels.remove(p);
						mIdListAdapter.notifyDataSetChanged();
						delIdView.dismiss();
						Toast.makeText(MainActivity.this, "删除账号成功", Toast.LENGTH_SHORT).show();
					}
				}
			});
			delIdView.showAtLocation(getView(), Gravity.CENTER_HORIZONTAL|Gravity.TOP, 0, (int) offsetY);
			
			return false;
		}
		
	};
	
	private OnLastItemVisibleListener mLastItem = new OnLastItemVisibleListener() {

		@Override
		public void onLastItemVisible() {
			if(!mIdItemPage.isEnd()){
				loadMoreView.loadData();
				mIdItemPage.next();
			}
		}
	};
	
	private OnRefreshListener<ListView> onRefreshListener = new OnRefreshListener<ListView>() {

		@Override
		public void onRefresh(PullToRefreshBase<ListView> refreshView) {
			mIdItemPage.init();
		}
	};
	
	/*private OnRefreshListener2<ListView> onRefreshListener = new OnRefreshListener2<ListView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			mIdItemPage.init();
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		}
	};*/
	
	private OnImportIdInfoListener onImportIdInfoListener = new OnImportIdInfoListener() {
		
		@Override
		public boolean onImport(List<IdModel> idModels) {
			for(IdModel idModel : idModels){
				if(!dbHelper.addIdInfo(idModel)){
					return false;
				}
			}
			mIdModels.addAll(idModels);
			mIdListAdapter.notifyDataSetChanged();
			return true;
		}
	};

}
