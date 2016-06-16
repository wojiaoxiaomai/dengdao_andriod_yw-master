package com.choucheng.dengdao2.tools;

import android.graphics.Paint;
import android.graphics.Rect;
import android.text.InputFilter;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextPaint;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;
import java.util.UUID;


/**
 *
 *@description  操作字符串的工具
 * 
 * 
 */
public class TextUtils {
	
	public static String handleString(String str){
		return str.replace("[", "").replace("]", "").replace(" ", "");
	}
	
	
	/***
	 * @描述  将光标移动到字符末尾的方法
	 * */
	
	public static void moveToLast(EditText editText){
		String content=editText.getText()+"";
		editText.setSelection(content.length());
	}
	
	/***
	 * @描述  输入字母自动大写（另外的实现方式->继承TextWatcher的类,此方式会出现光标定位不准的问题）
	 * */
	
	
	public static void autoUpcase(EditText editText){
		InputFilter[] FilterArray = new InputFilter[1];
		FilterArray[0] = new InputFilter.AllCaps();
		editText.setFilters(FilterArray);
	}
	
	
	/***
	 * @描述  输入字母自动大写并且将光标移动到末尾
	 * */
	
	public static void autoUpcaseAndMoveToLast(EditText editText){
		InputFilter[] FilterArray = new InputFilter[1];
		FilterArray[0] = new InputFilter.AllCaps();
		editText.setFilters(FilterArray);
		CharSequence chars = editText.getText();
		if(chars instanceof Spannable){
			Spannable span = (Spannable) chars;
			Selection.setSelection(span, chars.length());
		}
	}
	
	
	/**
	 * 防止参数为null
	 * @param s
	 * @return 
	 */
	public static String setStringArgument(String s){
		String string="";
		 if(s!=null&&!s.equals("null")){
			 string=s;
		 }
		 string=string.trim();
		 return string;
	}

    // 计算出该TextView中文字的长度(像素)
    public static float getTextViewLength(TextView textView,String text){
        TextPaint paint = textView.getPaint();
        // 得到使用该paint写上text的时候,像素为多少
        float textLength = paint.measureText(text);
//        Log.d(TAG, "getTextViewLength textLength:" + textLength);
        return textLength;
    }
    // 计算出该TextView中文字的宽度(像素)
    public static float getTextViewWidth(TextView textView){
        Rect rect = new Rect();
        TextPaint paint = textView.getPaint();
        paint.getTextBounds((String)textView.getText(), 0, textView.getText().length(), rect);
//        Log.d(TAG, "getTextViewWidth height:" + rect.height() + "width:" + rect.width());
        return rect.width();
    }
    // 计算出该TextView中文字的宽度(像素)
    public static float getTextViewWidth(TextView textView,String str){
        Rect rect = new Rect();
        TextPaint paint = textView.getPaint();
        paint.getTextBounds(str, 0, str.length(), rect);
//        Log.d(TAG, "getTextViewWidth height:" + rect.height() + "width:" + rect.width());
        return rect.width();
    }

    public static float getTextWidth(String str,float textSize,int start,int end){
        Paint pFont = new Paint();
        pFont.setTextSize(textSize);
        Rect rect = new Rect();
        pFont.getTextBounds(str, start, end, rect);
//        Log.d(TAG, "getTextWidth height:" + rect.height() + "width:" + rect.width());
        return rect.width();
    }
    /**
     * 生成32位编码
     * @return string
     */
    public static String getUUID(){
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
        return uuid;
    }

    /**
     * 自定义规则生成32位编码
     * @return string
     */
    public static String getUUIDByRules(String rules)
    {

        String radStr = rules;
        int rpoint = 0;
        StringBuffer generateRandStr = new StringBuffer();
        Random rand = new Random();
        int length = 32;
        for(int i=0;i<length;i++)
        {
            if(rules!=null){
                rpoint = rules.length();
                int randNum = rand.nextInt(rpoint);
                generateRandStr.append(radStr.substring(randNum,randNum+1));
            }
        }
        return generateRandStr+"";
    }

    /**
     * 判断字符串是否为空
     * @param s
     * @return
     */
    public static boolean isEmpty(String s){
        if(s==null||s.equals("")){
            return true;
        }
        return  false;
    }

}
