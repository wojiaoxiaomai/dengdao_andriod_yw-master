package com.choucheng.dengdao2.pojo;
/**
 * 广告
 * @author  zuoyan
 *
 */
public class AD {
	private int id;//":"1",
	private int type;//":"1",
	private String picture;//":"http:\/\/xbw.zgcom.cn\/Uploads\/Picture\/ad\/20141017\/b49c9fc9fb6c739db96a0d2f97822872.png",
	private String link;//":"",
	private int listorder;//":"0" 排序
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public int getListorder() {
		return listorder;
	}
	public void setListorder(int listorder) {
		this.listorder = listorder;
	}
	
	
}
