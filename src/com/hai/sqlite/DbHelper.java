package com.hai.sqlite;

import java.util.ArrayList;
import java.util.List;

import com.hai.idmanager.comm.respentity.IdModel;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper{
	private final static String DATABASE_NAME = "id_db";
	private final static int VERSION = 1;
	
	public final static String TABLE_ID_INFO = "id_info";		//id信息表
	
	private final static int itemPreItem = 8;	//每一页加载的item数
	
	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		StringBuffer id_infoBuffer = new StringBuffer();
		id_infoBuffer.append("create table ").append(TABLE_ID_INFO)
			.append("(id integer primary key autoincrement,")
			.append("idname text,")
			.append("idinfo text);");
		db.execSQL(id_infoBuffer.toString());
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 添加id信息
	 * @param idInfo
	 * @return
	 */
	public boolean addIdInfo(IdModel idInfo){
		SQLiteDatabase db = this.getWritableDatabase();
		String addIdInfo = "insert into " + TABLE_ID_INFO + "(idname,idinfo) values('"+ idInfo.getIdName() +"','" + idInfo.getIdInfo() + "')";
		try {
			db.execSQL(addIdInfo);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
			db.close();
			return false;
		}
		
		return true;
	}
	
	/**
	 * 删除id信息
	 * @param id
	 * @return
	 */
	public boolean delIdInfo(int id){
		SQLiteDatabase db = this.getWritableDatabase();
		String delIdInfo = "delete from "+ TABLE_ID_INFO +" where id="+id;
		try {
			db.execSQL(delIdInfo);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
			db.close();
			return false;
		}
		
		return true;
	}
	
	/**
	 * 修改id信息
	 * @param idInfo
	 * @return
	 */
	public boolean modifyIdInfo(IdModel idInfo){
		SQLiteDatabase db = this.getWritableDatabase();
		String modifyIdInfo = "update "+ TABLE_ID_INFO +" set idname='"+ idInfo.getIdName() +"',idinfo='"+ idInfo.getIdInfo() +"'" +
				" where id="+idInfo.getId()+"";
		try {
			db.execSQL(modifyIdInfo);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
			db.close();
			return false;
		}
		
		return true;
	}
	
	/**
	 * 按条件查询id信息
	 * @return
	 */
	public List<IdModel> queryIdInfo(IdModel idInfo){
		SQLiteDatabase db = this.getReadableDatabase();
		List<IdModel> idInfos = new ArrayList<>();
		String idNameStr = idInfo.getIdName();
		String idInfoStr = idInfo.getIdInfo();
		Cursor cursor = null;
		if(idNameStr != null && idInfoStr == null){
			cursor = db.query(TABLE_ID_INFO,null,"idname=?",new String[]{idInfo.getIdName()},null,null,"id desc");
		}else if(idNameStr == null && idInfoStr != null){
			cursor = db.query(TABLE_ID_INFO,null,"idinfo=?",new String[]{idInfo.getIdInfo()},null,null,"id desc");
		}else if(idNameStr != null && idInfoStr != null){
			cursor = db.query(TABLE_ID_INFO,null,"idname=? or idinfo=?",new String[]{idInfo.getIdName(),idInfo.getIdInfo()},null,null,"id desc");
		}else {
			cursor = db.query(TABLE_ID_INFO,null,null,null,null,null,"id desc");
		}
		int count = cursor.getCount();
		for(int i=0; i<count; i++){
			IdModel id = new IdModel();
			cursor.moveToPosition(i);
			id.setId(cursor.getInt(0));
			id.setIdName(cursor.getString(1));
			id.setIdInfo(cursor.getString(2));
			idInfos.add(id);
		}
		db.close();
		
		return idInfos;
	}
	
	/**
	 * 按id查询id信息
	 * @return
	 */
	public IdModel queryIdInfoById(int id){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_ID_INFO,null,"id = ?",new String[]{String.valueOf(id)},null,null,"id desc");
		IdModel idModel = new IdModel();
		cursor.moveToPosition(0);
		idModel.setId(cursor.getInt(0));
		idModel.setIdName(cursor.getString(1));
		idModel.setIdInfo(cursor.getString(2));
		db.close();
		
		return idModel;
	}
	
	/**
	 * 按页数查询，小于等于0则查询全部
	 * @param page
	 * @return
	 */
	public List<IdModel> queryIdInfoByPage(int page){
		SQLiteDatabase db = this.getReadableDatabase();
		List<IdModel> idInfos = new ArrayList<IdModel>();
		Cursor cursor = db.query(TABLE_ID_INFO,null,null,null,null,null,"id desc");
		int count = cursor.getCount();
		if(page <= 0){
			for(int i=0; i<count; i++){
				IdModel id = new IdModel();
				cursor.moveToPosition(i);
				id.setId(cursor.getInt(0));
				id.setIdName(cursor.getString(1));
				id.setIdInfo(cursor.getString(2));
				idInfos.add(id);
			}
		}else{
			int loadedItems = itemPreItem * (page - 1);
			if(count > loadedItems){
				for(int i=loadedItems; i<count && i<loadedItems+itemPreItem; i++){
					IdModel id = new IdModel();
					cursor.moveToPosition(i);
					id.setId(cursor.getInt(0));
					id.setIdName(cursor.getString(1));
					id.setIdInfo(cursor.getString(2));
					idInfos.add(id);
				}
			}
		}
		db.close();
		
		return idInfos;
	}
	
}
