package com.ithink.bean;

public class MemCardBean {

	/**
	 * SD卡剩余空间
	 */
	private String sdcardAvailable;
	/**
	 *  SD卡整体空间
	 */
	private String sdcardTotal;
	/**
	 * SD状态 （是否插入SD卡 1 是 0 否）
	 */
	private String sdcardStatus;
	
	public String getSdcardAvailable() {
		return sdcardAvailable;
	}

	public void setSdcardAvailable(String sdcardAvailable) {
		this.sdcardAvailable = sdcardAvailable;
	}

	public String getSdcardTotal() {
		return sdcardTotal;
	}

	public void setSdcardTotal(String sdcardTotal) {
		this.sdcardTotal = sdcardTotal;
	}

	public String getSdcardStatus() {
		return sdcardStatus;
	}

	public void setSdcardStatus(String sdcardStatus) {
		this.sdcardStatus = sdcardStatus;
	}
}
