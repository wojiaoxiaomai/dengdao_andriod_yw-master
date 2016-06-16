package com.choucheng.dengdao2.definewidget.dialog.wheeldialog;







import com.choucheng.dengdao2.definewidget.datapicker_other.WheelAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class WheelSpinnerAdapter implements WheelAdapter {
	private ArrayList<HashMap<String, String>> lstData;

	public WheelSpinnerAdapter(ArrayList<HashMap<String, String>> lstData) {
		this.lstData = lstData;
	}

	@Override
	public int getItemsCount() {

		return lstData.size();
	}

	@Override
	public String getItem(int index) {
		if (index >= 0 && index < getItemsCount()) {
			return lstData.get(index).get(LstDataKeys.TITLE);
		}
		return null;
	}

	@Override
	public int getMaximumLength() {

		return -1;
	}

	public class LstDataKeys {
		public final static String ID = "id";
		public final static String TITLE = "title";
	}
}
