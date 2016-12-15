package com.ithink.bean;

public class AlarmSettingsBean {

	private String alarmStatus;// 红外报警状态（1:开启；0：关闭）
	private String alarmStartTime;// 报警开启的时间
	private String alarmEndTime;// 报警关闭的时间
	private String alarmSoundStatus;// 红外报警声音状态（1:开启；0：关闭）
	
	public String getAlarmStatus() {
		return alarmStatus;
	}

	public void setAlarmStatus(String alarmStatus) {
		this.alarmStatus = alarmStatus;
	}
	
	public String getAlarmSoundStatus() {
		return alarmSoundStatus;
	}

	public void setAlarmSoundStatus(String alarmSoundStatus) {
		this.alarmSoundStatus = alarmSoundStatus;
	}
	
	public String getAlarmStartTime() {
		return alarmStartTime;
	}

	public void setAlarmStartTime(String alarmStartTime) {
		this.alarmStartTime = alarmStartTime;
	}

	public String getAlarmEndTime() {
		return alarmEndTime;
	}

	public void setAlarmEndTime(String alarmEndTime) {
		this.alarmEndTime = alarmEndTime;
	}
}
