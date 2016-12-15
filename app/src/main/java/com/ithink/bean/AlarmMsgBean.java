package com.ithink.bean;



public class AlarmMsgBean{
	
	private DeviceInfoBean devInfo;
	private String alarmTime;
	private String alarmID;
	private int isRead; //是否为未读消息 0 已读 1未读
	/**
	 * 该报警视频是否可以下载
	 */
	private int isDownload;

	
	public int getIsDownload() {
		return isDownload;
	}

	public void setIsDownload(int isDownload) {
		this.isDownload = isDownload;
	}

	public int getIsRead() {
		return isRead;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}

	public DeviceInfoBean getDevInfo() {
		return devInfo;
	}

	public void setDevInfo(DeviceInfoBean devInfo) {
		this.devInfo = devInfo;
	}

	public String getAlarmTime() {
		return alarmTime;
	}

	public void setAlarmTime(String alarmTime) {
		this.alarmTime = alarmTime;
	}

	public String getAlarmID() {
		return alarmID;
	}

	public void setAlarmID(String alarmID) {
		this.alarmID = alarmID;
	}
}
