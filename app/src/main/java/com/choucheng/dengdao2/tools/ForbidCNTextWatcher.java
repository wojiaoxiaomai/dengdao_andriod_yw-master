package com.choucheng.dengdao2.tools;
/**
 * 禁止输入中文
 */
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.choucheng.dengdao2.R;


public class ForbidCNTextWatcher implements TextWatcher {

	private EditText editText;
	private Context context;
	private String temp,s1,s2;
	
	public ForbidCNTextWatcher(Context context, EditText editText) {
		this.editText  = editText;
		this.context=context;
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
      s2=s.toString();
	  if(count>0&&before==0){
		  temp=s2.substring(start,s2.length());
	  }else{
		  temp="";
	  }

	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        s1=s.toString();
	}
	
	@Override
	public void afterTextChanged(Editable s) {
      String content=s.toString();
	  if(!content.equals("")&&!temp.equals("")){
		  if(HelperUtil.isCN(temp)){
			  Toast.makeText(context, context.getString(R.string.CN_canNotBeenPwd),Toast.LENGTH_SHORT).show();
			  s.delete(s.length()-temp.length(),s.length());
		  }
	  }
      temp="";
	}
	
}
