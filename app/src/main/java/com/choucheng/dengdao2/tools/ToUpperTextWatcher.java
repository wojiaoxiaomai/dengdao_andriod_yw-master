package com.choucheng.dengdao2.tools;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.widget.EditText;

public class ToUpperTextWatcher implements TextWatcher {

	private int changeCase = 0;
	private String srString = "";
	private int location = -1;
	private EditText editText;
	
	public ToUpperTextWatcher(EditText editText) {
		this.editText  = editText;
		
	}
	
	


	private int l=0;////////记录字符串被删除字符之前，字符串的长度
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	
	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
	int after) {
		l=s.length();
		location=editText.getSelectionEnd();
	}
	
	@Override
	public void afterTextChanged(Editable s) {
		if (changeCase == 0) {
			srString = s.toString().toUpperCase();
			if (s.toString().equals("")) {
				changeCase = 0;
			} else {
				changeCase = 1;
			}
			s.clear();
		} else if (changeCase == 1) {
			changeCase = 2;
			s.append(srString);
		} else {
			changeCase = 0;
		}
		Editable etable=editText.getText();
		Selection.setSelection(etable, location);
	
	}
	
}
