package com.choucheng.dengdao2.tools;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


public class MapUtil {

	public static MapUtil instance = null;


	public MapUtil() {
	}

	public static MapUtil getInstance() {
		if (instance == null) {
			instance = new MapUtil();
		}
		return instance;
	}

    public static String getString(HashMap<String, Object> map,String key){
        if(map==null){
            return  null;
        }
        Object obj=map.get(key);
        if(obj!=null){
            return  (String)obj;
        }else{
            return  null;
        }

    }
	public static Object getJsonMap(Map<String,Object> map) {
        StringBuffer buf = new StringBuffer();
        buf.append("{");
        Iterator<Entry<String, Object>> iter = map.entrySet().iterator();
        if (iter.hasNext()) {
            Entry ety = iter.next();
            keyToString(buf, ety.getKey());
            valueToString(buf, ety.getValue());
        }
        while (iter.hasNext()) {
            Entry ety = iter.next();
            buf.append(",");
            keyToString(buf, ety.getKey());
            valueToString(buf, ety.getValue());
        }
        buf.append("}");
        return buf;
    }
	
	
	private static void keyToString(StringBuffer buf, Object value) {
        buf.append("'");
        buf.append(getJsString(value));
        buf.append("'");
        buf.append(":");
    }
	private static String getJsString(Object obj) {
        if (obj == null) {
            obj = "";
        }
        return obj.toString().replaceAll("\\\\", "\\\\\\\\").replaceAll("'", "\\\\\'");
    }
	

	private static void valueToString(StringBuffer buf, Object objValue) {
        if (objValue instanceof Map) {
            buf.append(getJsonMap((Map<String, Object>) objValue));
        } else if (objValue instanceof Collection) {
            buf.append(getJsCollection((Collection) objValue));
        } else {
            buf.append("'");
            buf.append(getJsString(objValue));
            buf.append("'");
        }
    }
	
	

	
	private static Object getJsCollection(Collection list) {
        StringBuffer buf = new StringBuffer();
        buf.append("[");
        Iterator iter = list.iterator();
        if (iter.hasNext()) {
            valueToString(buf, iter.next());
        }
        while (iter.hasNext()) {
            buf.append(",");
            valueToString(buf, iter.next());
        }
        buf.append("]");
        return buf;
    }

	
	  public static String getinfo(String[] infos,String key){
	    	for(String s:infos){
	    		if(s.contains(key)){
	    			return s.split(":")[1];
	    		}
	    	}
	    	return "";
	    }
	    
    
    
}
