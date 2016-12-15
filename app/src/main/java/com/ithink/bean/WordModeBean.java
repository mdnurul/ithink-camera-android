package com.ithink.bean;

public class WordModeBean {

	private String offLineModeStatus;// 设备离线模式（1：开启；0：关闭）
	private String definition;// 视频存储清晰度（1：流畅；2：标清；3：高清）
	
	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public String getOffLineModeStatus() {
		return offLineModeStatus;
	}

	public void setOffLineModeStatus(String offLineModeStatus) {
		this.offLineModeStatus = offLineModeStatus;
	}

}
