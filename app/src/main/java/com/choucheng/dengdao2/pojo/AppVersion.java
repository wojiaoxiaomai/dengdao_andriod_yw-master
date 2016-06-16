package com.choucheng.dengdao2.pojo;


import java.io.Serializable;

public class AppVersion implements Serializable{
	private int hasNewVersion;//":0,
	private String currVersion;//":"",
	private String url;//":"",
	private String description;//":"","
	private int updateType;//":0  更新类型  0-正常（用户可以暂不升级）,1—警告升级（用户可以暂不升级），2—强制升级，用户不升级退出软件
	
	public int getHasNewVersion() {
		return hasNewVersion;
	}
	public void setHasNewVersion(int hasNewVersion) {
		this.hasNewVersion = hasNewVersion;
	}
	public String getCurrVersion() {
		return currVersion;
	}
	public void setCurrVersion(String currVersion) {
		this.currVersion = currVersion;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getUpdateType() {
		return updateType;
	}
	public void setUpdateType(int updateType) {
		this.updateType = updateType;
	}
	
	
    
    
    
}
