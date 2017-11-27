package com.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author  ChenShaoWen
 * 2017年3月14日  上午9:38:21
 */
public class MyTools {
	//从输入流里面读出数据
	public static byte[] readStream(InputStream inputStream) throws Exception {    
	        ByteArrayOutputStream bout = new ByteArrayOutputStream();    
	        byte[] buffer = new byte[16*1024];    
	        int len = 0;    
	        while ((len = inputStream.read(buffer)) != -1) {    
	            bout.write(buffer, 0, len);    
	        }    
	        bout.close();    
	        inputStream.close();    
	        return bout.toByteArray();    
	    }    
}
