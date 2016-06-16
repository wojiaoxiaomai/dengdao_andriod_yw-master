package com.choucheng.dengdao2.tools;

import android.app.Activity;
import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import com.choucheng.dengdao2.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Validator {

	/**
	 * 判断电话号码是否符合规则
	 *
	 *            电话号码
	 * @return boolean，true代表符号电话号码规则，false不符合电话规则
	 */
	
	
	public static boolean validate(String reg,String data){
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(data);
		return m.matches();
	}
	
	
	// if text is null, return true

	public static boolean checkIsNull(EditText text) {
		if (text.getText()!=null&&text.getText().toString().equals("")) {
			return true;
		}
		return false;
	}

	// if text is null, return true

	public static boolean checkIsNull(Context mContext,EditText text, String msg) {
		if (text.getText()!=null&&text.getText().toString().equals("")) {
            Toast.makeText(mContext, mContext.getString(R.string.pleaseinput)+ msg, Toast.LENGTH_SHORT).show();
			return true;
		}
		return false;
	}

	public static boolean checkIsCorrect(Context mContext,EditText text, String reg, String msg) {

		if (text.getText()!=null&&Validator.validate(reg, text.getText().toString().trim())) {
			return true;
		} else {
			Toast.makeText(mContext, mContext.getString(R.string.pleaseinputcorrect)+ msg,Toast.LENGTH_SHORT).show();
		}
		return false;
	}
	
	

	public static boolean checkIsNull(Activity  mContext,EditText text, String msg) {
		if (text.getText()!=null&&text.getText().toString().trim().equals("")) {
			Toast.makeText(mContext,mContext.getString(R.string.pleaseinput) + msg, Toast.LENGTH_SHORT).show();
			return true;
		}
		return false;
	}

	public static boolean checkIsCorrect(Activity mContext,EditText text, String reg, String msg) {

		if (Validator.validate(reg, (text.getText()+"").trim())) {
			return true;
		} else {
            String showinfo=mContext.getString(R.string.pleaseinputcorrect)+ msg;
			Toast.makeText(mContext,showinfo,Toast.LENGTH_SHORT).show();
		}
		return false;
	}

    /***
     * 检查字符串是否符合规则
     * @param text 需要检查的字符串
     * @param reg  验证的规则
     * @param msg  验证后提示的消息信息
     * @return  true 验证成功，false 验证失败
     * @time 2014.4.23
     */
    public static boolean checkIsCorrect(Activity mContext,String text, String reg, String msg) {

        if (Validator.validate(reg, text) ){
            return true;
        } else {
            String showinfo=mContext.getString(R.string.pleaseinputcorrect)+ msg;
			Toast.makeText(mContext,showinfo,Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public static boolean checkSepcialSFZMHM(Activity mContext,EditText text, String reg, String msg) {

        String sfzmhm=text.getText()+"";
        StringBuilder st=new StringBuilder(sfzmhm);
        if(st.length()==15){
            st.insert(6,"19");
            st.append("x");
        }
        sfzmhm=st.toString().trim();
        Logger.i("TAG","sfzmhm:"+sfzmhm);
        if (Validator.validate(reg, sfzmhm)) {
            return true;
        } else {
            String showinfo=mContext.getString(R.string.pleaseinputcorrect)+ msg;
			Toast.makeText(mContext,showinfo,Toast.LENGTH_SHORT).show();
        }
        return false;
    }
	
	
//==>2014.1.21
	public static boolean checkLength(Activity mContext,EditText text, int max, String msg){
		String content=text.getText().toString();
		if(content!=null&&!content.equals("")){
			if(content.length()>max){
				Toast.makeText(mContext, msg+mContext.getString(R.string.outofLength)+max,Toast.LENGTH_SHORT).show();
				return false;
			}else{
				return true;
			}
		}else{
			return false;
		}
	}

    /**
     * 根据byte来限制长度
     * @param mContext
     * @param text 需要检测的对象
     * @param max 现在的最大长度
     * @param msg 错误后，toast提示的内容
     * @return
     */
    public static boolean checkbyteLength(Activity mContext,EditText text, int max, String msg){
        if(text.getText()!=null){
            String content=text.getText().toString();
           if(content!=null&&!content.equals("")){
               byte[] bytes=content.getBytes();
                if(bytes.length>max){
					Toast.makeText(mContext, msg+mContext.getString(R.string.outofLength)+max+"byte",Toast.LENGTH_SHORT).show();
                    return false;
                }else{
                    return true;
                }
            }else{
                return false;
            }
        }
        return false;
    }
	

}
