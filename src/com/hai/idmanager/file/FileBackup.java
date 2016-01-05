package com.hai.idmanager.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hai.idmanager.comm.respentity.IdModel;

public class FileBackup {
	private static String sdCardPath;
	private final static String appDirectory = "/IdManager";
	private final static String fileName = "backup.id";
	
	/**
	 * 以json格式导出账号信息到文本
	 * @param idModels
	 * @return
	 */
	public static boolean exportIdInfo(List<IdModel> idModels){
		if(!Environment.isExternalStorageRemovable()){
			sdCardPath = Environment.getExternalStorageDirectory().toString();
			File file = new File(sdCardPath+appDirectory+"/"+fileName);
			File filePath = new File(sdCardPath+appDirectory);
			BufferedWriter bw = null;
			if(!file.exists()){
				if(!filePath.exists()){
					filePath.mkdirs();
				}
				try {
					file.createNewFile();
					Gson gson = new Gson();
					String idInfoStr = gson.toJson(idModels);
					bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
					bw.write(idInfoStr);
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}finally{
					try {
						bw.flush();
						bw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}else{
				try {
					file.delete();
					file.createNewFile();
					Gson gson = new Gson();
					String idInfoStr = gson.toJson(idModels);
					bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
					bw.write(idInfoStr);
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}finally{
					try {
						bw.flush();
						bw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return true;
		
	}
	
	/**
	 * 导入信息
	 * @param context
	 * @param uri
	 * @param onImportIdInfoListener
	 * @return
	 */
	@SuppressLint("SdCardPath")
	@SuppressWarnings({ })
	public static boolean importIdInfo(Context context, Uri uri, OnImportIdInfoListener onImportIdInfoListener){
//		sdCardPath = Environment.getExternalStorageDirectory().toString();
//		File file = new File(sdCardPath+appDirectory+exportFileName);
//		File filePath = new File(sdCardPath+appDirectory);
//		String uriStr = uri.toString();
//		uriStr = uriStr.substring(uriStr.indexOf("/sdcard/"), uriStr.length()-1);
		File file = new File(uri.getPath());
		if(!file.getName().equals(fileName)){
			Toast.makeText(context, "不支持的文件类型！", Toast.LENGTH_SHORT).show();
			return false;
		}
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			Gson gson = new Gson();
			List<IdModel> mIdModels = gson.fromJson(br, new TypeToken<List<IdModel>>(){}.getType());
			if(mIdModels.size() > 0){
				if(!onImportIdInfoListener.onImport(mIdModels)){
					Toast.makeText(context, "导入失败", Toast.LENGTH_SHORT).show();
					return false;
				}else{
					Toast.makeText(context, "导入成功", Toast.LENGTH_SHORT).show();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context, "文件解析错误，可能已损坏", Toast.LENGTH_SHORT).show();
			return false;
		}finally{
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return true;
	}
	
	public interface OnImportIdInfoListener{
		boolean onImport(List<IdModel> idModels);
	}
	
}
