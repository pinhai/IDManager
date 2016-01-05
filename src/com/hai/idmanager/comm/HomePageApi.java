package com.hai.idmanager.comm;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.hai.idmanager.comm.respentity.IdModel;
import com.hai.idmanager.comm.respentity.PageModel;
import com.hai.sqlite.DbHelper;

public class HomePageApi {
	private static final int countPerPage = 10;	//每页显示条数
	private DbHelper dbHelper;
	
	public HomePageApi(Context context){
		dbHelper = new DbHelper(context);
	}
		
	/**
	 * 获取账号列表
	 * @param page
	 * @param response
	 */
	public void getIdList(int page, FormResponse<PageModel<IdModel>> response){
		try {
			List<IdModel> idListModel = dbHelper.queryIdInfoByPage(0);
			int pageTatol = 0;
			if(idListModel.size()%countPerPage == 0){
				pageTatol = idListModel.size()/countPerPage;
			}else{
				pageTatol = idListModel.size()/countPerPage + 1;
			}
			PageModel<IdModel> pageModel = new PageModel<>();
			pageModel.setTotalCount(idListModel.size());
			pageModel.setTotalPage(pageTatol);
			List<IdModel> data = new ArrayList<>();
			for(int i=countPerPage*page-10; i<countPerPage*page && i<idListModel.size(); i++){
				data.add(idListModel.get(i));
			}
			pageModel.setDatas(data);
			
			response.onResponse(pageModel);
		} catch (Exception e) {
			response.onErrorResponse(110);
		}
		
	}
}
