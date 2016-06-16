package com.choucheng.dengdao2.tools;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


/**
 *
 *@description  操作sharedpreference的工具
 *
 */
public class SharedUtil {

    /**
     *
     * @描述  保存指定名称的值到指定名称的sharedpreference
     *
     * @param share  要保存到的sharedpreference名称
     * @param name   保存在shared文件中的key
     * @param value  保存在shared中的value
     * */

    public static void commitInfo(SharedPreferences share,String name, String value){
        Editor editor = share.edit();
        editor.putString(name, value);
        editor.commit();
    }
    /**
     * 保存指定名称的值到指定名的缓存中
     * @param share 
     * @param name
     * @param value
     */
    public static void commitInfo(SharedPreferences share,String name, Object value){
        Editor editor = share.edit();
        if(value instanceof  String)
            editor.putString(name,(String)value);
        if(value instanceof Boolean)
            editor.putBoolean(name, (boolean) value);
        if(value instanceof Integer)
            editor.putInt(name, (int) value);
        if(value instanceof Long){
            editor.putLong(name, (long) value);
        }
        editor.commit();
    }

    /**
     * 提交加密文件
     * @param share 缓冲的目的对象
     * @param name 缓冲名
     * @param value 需要缓冲的，待加密的信息
     */
    public static void commitAESInfo(SharedPreferences share,String name,String value){
        Editor editor = share.edit();
        try {
			value=AESInfo.encrypt(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
        editor.putString(name, value);
        editor.commit();
    }

    /**
     * 提取已经加密信息
     * @param share
     * @param name
     */
    public static String getAESInfo(SharedPreferences share,String name){
    	String cache="";
    	if(share.contains(name)){
    		cache=share.getString(name, "");
    		if(!cache.equals("")&&!cache.equals("null")){
    			try {
					cache=AESInfo.decrypt(cache);
				} catch (Exception e) {
					e.printStackTrace();
				}
    		}
    	}
    	return cache;
    	
    }
    
    
    /**
     * @描述   清除掉sharedpreference中所有的键值对
     * */

    public static void clearInfo(SharedPreferences share){
        Editor editor = share.edit();
        editor.clear();
        editor.commit();

    }

    /**
     * @描述   清除掉sharedpreference中指定的键值对
     * */

    public static void clearInfo(SharedPreferences share,String key){

        Editor editor = share.edit();
        editor.remove(key);
        editor.commit();

    }




}
