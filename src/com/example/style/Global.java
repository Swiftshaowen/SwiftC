package com.example.style;

import android.app.Application;


/**
 * @author  ChenShaoWen
 * 2017年3月11日  下午4:03:14
 */
public class Global {
	
	//日夜模式的类型：日间，夜间。返回1 ，2 ， 3进行标识。
public static SkinType SkinType;
	
	
	public enum SkinType {
		Light(1), Night(2), Unkown(3);
		public int value;

		SkinType(int v) {
			value = v;
		}

		public int getTypeValue() {
			return value;
		}
	}


	public static SkinType SkinType(int skinTypeValue) {
		return SkinType(skinTypeValue);
	}
}
