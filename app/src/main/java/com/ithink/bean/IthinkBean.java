package com.ithink.bean;

import java.util.List;

public class IthinkBean {
	
	private List<DeviceBean> deviceList;
	private List<SubAccountInfoBean> userList;
	private String playBackList;
	private List<AlarmMsgBean> alarmList;

	
	
	
	public List<AlarmMsgBean> getAlarmList() {
		return alarmList;
	}

	public void setAlarmList(List<AlarmMsgBean> alarmList) {
		this.alarmList = alarmList;
	}

	public String getPlayBackList() {
		return playBackList;
	}

	public void setPlayBackList(String playBackList) {
		this.playBackList = playBackList;
	}

	public List<DeviceBean> getDeviceList() {
		return deviceList;
	}

	public void setDeviceList(List<DeviceBean> deviceList) {
		this.deviceList = deviceList;
	}

	public List<SubAccountInfoBean> getUserList() {
		return userList;
	}

	public void setUserList(List<SubAccountInfoBean> userList) {
		this.userList = userList;
	}
	
	
}
