package com.hudong.liuyan;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.commons.logging.Log;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.itheima.htmlviewer.R;

/**
 * @author ChenShaoWen 2017年3月21日 下午12:10:06
 */
public class Regesite extends Activity {
	
	//实质就是通过网络向服务器发送一对name+password;插入到数据库中。
	
	Button rgbtn;
	
	EditText rgzh;
	EditText rgmm;
	Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			
			switch(msg.what)
			{case 1:
				String cg = new String("注册成功");
				String sb = new String("注册失败");
				
				if(msg.obj.toString().equals(cg))
				{
					android.util.Log.i("这是已经注册成功的标志", msg.obj.toString());
				Toast.makeText(Regesite.this, "注册成功,自动为您跳转回登录界面！", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(Regesite.this, Login.class);
					startActivity(intent);
					finish();
				}
				else{
					//这里是因为局域网的原因，，，所以每次都不会出现注册失败的情况。
					android.util.Log.i("这里被执行呀", "又网络但是注册失败");
					Toast.makeText(Regesite.this, "注册失败，请检查您的网络", Toast.LENGTH_SHORT).show();
				}
				break;
				
			case 0:
				
				android.util.Log.i("这里被执行呀", "没有网络但是注册失败");
				Toast.makeText(Regesite.this, "注册失败，请检查您的网络", Toast.LENGTH_SHORT).show();
				break;
				default :break;
			}
			
		}
	};
	//注册的加载页面
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.regesite);
		
		init();
	}
	/**
	 * 
	 */
	private void init() {
		
		rgbtn =(Button) findViewById(R.id.rg_id);
		rgzh =(EditText) findViewById(R.id.rg_zh_edit_id);
		rgmm =(EditText) findViewById(R.id.rg_password_id);
	}
	
	//注册按钮
	public void rgclick(View v)
	{
		//通过判断是否插入数据库成功 ，是的话提示注册成功，否则注册失败！
	final String  path = "http://120.25.239.107:8080/MyService/servlet/Login"	;
	
	final String name =  rgzh.getText().toString();
	final String password = rgmm.getText().toString();
	
	
	//禁止传进空值！！！所以在这里做个判断
	if(name.length()!=0&&password.length()!=0)
	{
	Thread  t = new Thread(new Runnable() {
		@Override
		public void run() {
			try {
				URL url = new URL(path);
				HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("POST");//请求方式
				conn.setReadTimeout(5000); //请求超时的时间限制
				conn.setConnectTimeout(5000);//设置连接超时
				
				//添加post请求头中自动添加的属性
				//流里数据的mimetype
				conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				String content = "&name="+name+"&pass="+password;
				
				//打开连接对象的输出流
				conn.setDoOutput(true);
				//获取连接对象的输出流
				OutputStream os = conn.getOutputStream();
				//把数据写入输出流中
				
				os.write(content.getBytes());
				
				//如果连接成功的话
				if(conn.getResponseCode()==200)
				{
					InputStream is = conn.getInputStream();
					String text = Tools.getTextFromStream(is);
					Message msg = handler.obtainMessage();
					msg.obj = text;
					msg.what=1;
					handler.sendMessage(msg);
				}
				else
				{
					Message msg = new Message();
					msg.what=0;
					handler.sendMessage(msg);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	});
	t.start();
	}
	
	else
	{
		Toast.makeText(Regesite.this, "账号和密码不能为空", 0).show();
	}
}
}
