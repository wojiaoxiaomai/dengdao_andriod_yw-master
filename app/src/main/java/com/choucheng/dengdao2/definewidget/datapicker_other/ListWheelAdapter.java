/*
 *  Copyright 2010 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.choucheng.dengdao2.definewidget.datapicker_other;

import com.choucheng.dengdao2.pojo.City;

import java.util.List;

/**
 * The simple List wheel adapter
 * @param <T> the element type
 */
public class ListWheelAdapter<T> implements WheelAdapter {

	/** The default items length */
	public static final int DEFAULT_LENGTH = -1;

	// items
	private List<T> items;
	// length
	private int length;

	/**
	 * Constructor
	 * @param items the items
	 * @param length the max items length
	 */
	public ListWheelAdapter(List<T> items, int length) {
		this.items = items;
		this.length = length;
	}

	/**
	 * Contructor
	 * @param items the items
	 */
	public ListWheelAdapter(List<T> items) {
		this(items, DEFAULT_LENGTH);
	}

	@Override
	public String getItem(int index) {
		if (index >= 0 && index < items.size()) {
			Object object=items.get(index);
			if(object instanceof City){
				return ((City) object).getName();
			}
		}
		return null;
	}


	public void setList(List<T> changeitems){
		if(items!=null){
			items.clear();
			items.addAll(items);
		}
	}

	@Override
	public int getItemsCount() {
		return items.size();
	}

	@Override
	public int getMaximumLength() {
		return length;
	}

}
