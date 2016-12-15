package com.ithink.bean;

import java.io.Serializable;

public class DeviceBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;// 设备名
	private String sid;// 设备IMEI
	private String img;// 设备显示图片
	private int status;// 设备状态 0 离线 1在线
	private int init;// 当前用户是否是管理员
	private String version;// 摄像头段APP版本号
	private String previewTime;// 生成预览时的时间
	private String remark;// 摄像头备注
	private String innerIP;// 内网IP
	private String macAddress;// MAC地址
	private String ledStatus;// LED指示灯状态
	private String cloudStatus;// 云存储状态（1：开启；0：关闭）
	// private CloudStorageBean storBean;//云存储信息实体
	private String soundLeadStatus;// 语音播报状态（1：开启；0关闭）
	private String time;// 当前时间
	private String lock;// 是否开启摄像头锁
	private String lockPsd;// 摄像头锁密码
	private String type;// 设备端类型：i1_1——一期第一版;i1_2——一期第二版;i2_1——
	
	
	private String storeStatus;// 设备端录像视频存储状态（1：开启；0：关闭）
	

	private String showAlarm;// 是否显示报警功能（1：显示；0：不显示）
	private String showOffLineModel;// 是否显示离线存储功能（1：显示；0：不显示）

	/**
	 * 是否显示LED灯功能（1：显示；0：不显示）
	 */
	private String showLED;
	/**
	 * 是否显示语音播报功能（1：显示；0：不显示）
	 */
	private String showSound;
	/**
	 * 是否显示为演示设备状态（1：显示为演示设备状态（只能看实时视频，不能看视频回放，不能进设置详情，不能对讲、截图、调清晰度）；0：正常显示）
	 */
	private String publicStatus ;

	/**
	 * //是否显示录像存储（1：显示；0：不显示）
	 */
	private String showLocalStore;
	
	/**
	 * 报警设置
	 */
	private AlarmSettingsBean alarmDic;
	
	/**
	 * 摄像机存储卡信息
	 */
	private MemCardBean memCardDic;
	
	/**
	 * 工作模式信息
	 */
	private WordModeBean workModeDic;
	
	
	
	public WordModeBean getWorkModeDic() {
		return workModeDic;
	}

	public void setWorkModeDic(WordModeBean workModeDic) {
		this.workModeDic = workModeDic;
	}

	public AlarmSettingsBean getAlarmDic() {
		return alarmDic;
	}

	public void setAlarmDic(AlarmSettingsBean alarmDic) {
		this.alarmDic = alarmDic;
	}

	public MemCardBean getMemCardDic() {
		return memCardDic;
	}

	public void setMemCardDic(MemCardBean memCardDic) {
		this.memCardDic = memCardDic;
	}

	public String getShowLocalStore() {
		return showLocalStore;
	}

	public void setShowLocalStore(String showLocalStore) {
		this.showLocalStore = showLocalStore;
	}

	public String getStoreStatus() {
		return storeStatus;
	}

	public void setStoreStatus(String storeStatus) {
		this.storeStatus = storeStatus;
	}

	public String getPublicStatus() {
		return publicStatus;
	}

	public void setPublicStatus(String publicStatus) {
		this.publicStatus = publicStatus;
	}

	public String getShowLED() {
		return showLED;
	}

	public void setShowLED(String showLED) {
		this.showLED = showLED;
	}

	public String getShowSound() {
		return showSound;
	}

	public void setShowSound(String showSound) {
		this.showSound = showSound;
	}

	public String getShowAlarm() {
		return showAlarm;
	}

	public void setShowAlarm(String showAlarm) {
		this.showAlarm = showAlarm;
	}

	public String getShowOffLineModel() {
		return showOffLineModel;
	}

	public void setShowOffLineModel(String showOffLineModel) {
		this.showOffLineModel = showOffLineModel;
	}

	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getInnerIP() {
		return innerIP;
	}

	public void setInnerIP(String innerIP) {
		this.innerIP = innerIP;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getCloudStatus() {
		return cloudStatus;
	}

	public void setCloudStatus(String cloudStatus) {
		this.cloudStatus = cloudStatus;
	}

	public String getLedStatus() {
		return ledStatus;
	}

	public void setLedStatus(String ledStatus) {
		this.ledStatus = ledStatus;
	}

	public String getSoundLeadStatus() {
		return soundLeadStatus;
	}

	public void setSoundLeadStatus(String soundLeadStatus) {
		this.soundLeadStatus = soundLeadStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getInit() {
		return init;
	}

	public void setInit(int init) {
		this.init = init;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getLockStatus() {
		return lock;
	}

	public void setLockStatus(String lock) {
		this.lock = lock;
	}

	public String getLockPsd() {
		return lockPsd;
	}

	public void setLockPsd(String lockPsd) {
		this.lockPsd = lockPsd;
	}

	// public void setLockErrorCount(int lockErrorCount){
	// this.lockErrorCount = lockErrorCount;
	// }
	//
	// public int getLockErrorCount(){
	// return lockErrorCount;
	// }
}
