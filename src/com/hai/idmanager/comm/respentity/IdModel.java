package com.hai.idmanager.comm.respentity;

import java.io.Serializable;

public class IdModel implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int id = 0;
	private String idName;
	private String idInfo;
	
	public IdModel(){
	}
	
	public IdModel(String idName, String idInfo){
		this.idName = idName;
		this.idInfo = idInfo;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIdName() {
		return idName;
	}
	public void setIdName(String idName) {
		this.idName = idName;
	}
	public String getIdInfo() {
		return idInfo;
	}
	public void setIdInfo(String idInfo) {
		this.idInfo = idInfo;
	}
}
