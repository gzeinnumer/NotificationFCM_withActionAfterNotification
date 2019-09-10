package com.gzeinnumer.notificationfcm;

import com.google.gson.annotations.SerializedName;

public class ResponseInsertToken {

	@SerializedName("kode")
	private int kode;

	@SerializedName("result_insert")
	private String resultInsert;

	public void setKode(int kode){
		this.kode = kode;
	}

	public int getKode(){
		return kode;
	}

	public void setResultInsert(String resultInsert){
		this.resultInsert = resultInsert;
	}

	public String getResultInsert(){
		return resultInsert;
	}
}