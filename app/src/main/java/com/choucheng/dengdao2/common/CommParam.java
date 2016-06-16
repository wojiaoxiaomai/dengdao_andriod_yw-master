package com.choucheng.dengdao2.common;


import com.choucheng.dengdao2.pojo.User;

public class CommParam {
   public static CommParam instance;;
   private String uid;
   private User user;

   
   private CommParam(){
   }
   
   
   
   public static CommParam getInstance(){
	   if(instance==null){
		   instance=new CommParam();
	   }
	   return instance;
   }



	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
