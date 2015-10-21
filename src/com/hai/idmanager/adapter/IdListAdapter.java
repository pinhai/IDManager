package com.hai.idmanager.adapter;

import java.util.List;

import com.hai.idmanager.R;
import com.hai.idmanager.comm.respentity.IdModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class IdListAdapter extends BaseAdapter {
	private List<IdModel> mIdModels;
	private LayoutInflater inflater;
	
	public IdListAdapter(Context context, List<IdModel> idModels){
		mIdModels = idModels;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mIdModels.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mIdModels.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.item_idinfo, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_idName = (TextView) convertView.findViewById(R.id.tv_idName);
			viewHolder.tv_idInfo = (TextView) convertView.findViewById(R.id.tv_idInfo);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		setViewItem(viewHolder,mIdModels.get(position));
		
		return convertView;
	}
	
	private void setViewItem(ViewHolder viewHolder,IdModel idInfo) {
	    if(null != idInfo){
	        viewHolder.tv_idName.setText(idInfo.getIdName());
	        viewHolder.tv_idInfo.setText(idInfo.getIdInfo());
	    }
	}

	static class ViewHolder{
		TextView tv_idName;
		TextView tv_idInfo;
	}

}
