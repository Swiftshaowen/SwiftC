package com.hudong.liuyan;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Tools {

	public static String getTextFromStream(InputStream is){
		byte[] b = new byte[3*1024];
		int len;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			while((len = is.read(b)) != -1){
				bos.write(b, 0, len);
			}
			String text =new String(bos.toByteArray());
			return text;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
}
