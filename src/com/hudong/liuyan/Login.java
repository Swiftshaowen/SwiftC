package com.hudong.liuyan;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.itheima.htmlviewer.R;

/**
 * @author ChenShaoWen 2017年3月21日 上午11:54:41
 */
public class Login extends Activity {

	Button loginbtn;
	Button regesiterbtn;
	Button nimingbtn;

	EditText zh;
	EditText mm;
	CheckBox ckb;
	
	
    static String name=null;//需要传递用户名
    
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			
			switch(msg.what)
			{
			case 1:
				String cg = new String("登录成功");
				String sb = new String("密码错误");
				
				if(msg.obj.toString().equals(cg))
				{
					Log.i("成功登录呀", msg.obj.toString());
					Intent intent = new Intent(Login.this, Liuyan.class);
					intent.putExtra("name", name); //把用户名传递过去
					startActivity(intent);
					finish();
				}
				else
				{
					Log.i("输入出错", msg.obj.toString());
					
					Toast.makeText(Login.this,(String)msg.obj, 0).show();
				}
				break;
				
			case 0:
				Log.i("网络异常", "登录失败");
				Toast.makeText(Login.this, "网络异常，登录失败", 0).show();
				break;
				
				default: break;
			}

		}

	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		init();
		//记住用户的密码和账号
		innitfun();
		initlittene();
		
	}
	/**
	 * 
	 */
	private void initlittene() {
		//checkbox实现监听的时候一定要加上CompoundButton
		 ckb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {  
	            @Override  
	            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {  
	              
	            	SharedPreferences settings = getSharedPreferences("SETTING_Infos", 0);  
	                if (arg1 == true) {//勾选时，存入EditText中的用户名密码  
	                    settings.edit().putString("judgeText", "yes")  
	                            .putString("userNameText", zh.getText().toString())  
	                            .putString("passwordText", mm.getText().toString())  
	                            .commit();  
	                    Toast.makeText(Login.this, "记住用户名和密码", Toast.LENGTH_SHORT)  
	                            .show();  
	                } else {//不勾选，存入空String对象  
	                    settings.edit().putString("judgeText", "no")  
	                            .putString("UserNameText", "")  
	                            .putString("passwordText", "")  
	                            .commit();  
	                    Toast.makeText(Login.this, "不记住用户名和密码", Toast.LENGTH_SHORT)  
	                            .show();  
	                }  
	            }  
	        });  
	}
	/**
	 * 
	 */
	private void innitfun() {
		 //从配置文件中取用户名密码的键值对  
        //若第一运行，则取出的键值对为所设置的默认值  
        SharedPreferences settings = getSharedPreferences("SETTING_Infos", 0);  
        String strJudge = settings.getString("judgeText", "no");// 选中状态  
        String strUserName = settings.getString("userNameText", "");// 用户名  
        String strPassword = settings.getString("passwordText", "");// 密码  
        if (strJudge.equals("yes")) {  
            ckb.setChecked(true);  
            zh.setText(strUserName);  
            mm.setText(strPassword);  
        } else {  
            ckb.setChecked(false);  
            zh.setText("");  
            mm.setText("");  
        }  
		
	}

	/**
	 * 初始化控件
	 */
	private void init() {
		loginbtn = (Button) findViewById(R.id.login_id);
		regesiterbtn = (Button) findViewById(R.id.regesite_id);
		nimingbtn = (Button) findViewById(R.id.nimingbtn_id);
		zh = (EditText) findViewById(R.id.zh_edit_id);
		mm = (EditText) findViewById(R.id.password_id);
		ckb =  (CheckBox) findViewById(R.id.checkb_id);
		
	
	}
	
	

	// 监听按钮
	// 登录按钮 向数据库你查询账号密码是否是正确的 如果登录成功的话就跳转到留言界面
	public void loginclick(View v) {

	    name = zh.getText().toString();
		final String password = mm.getText().toString();
		
	
		
		
		
		//禁止不输入密码和账号就登录的行为
		if(name.length()!=0&&password.length()!=0)
		{
			
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				String path = "http://120.25.239.107:8080/MyService/servlet/Rq?name="+ name + 
						"&pass=" + password;
				try {
					URL url = new URL(path);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(5000);
					conn.setReadTimeout(5000);
				//	conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
					if (conn.getResponseCode() == 200) {
						InputStream is = conn.getInputStream();
						String text = Tools.getTextFromStream(is);
						Message msg = handler.obtainMessage();
						msg.obj = text;
						msg.what=1;
						handler.sendMessage(msg);
					}
					else
					{
						Message msg  = new Message();
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
			Toast.makeText(Login.this, "密码或账号不能为空", 0).show();
		}
	}

	public void regesitclick(View v) {
		Log.i("注册按钮", "被点击了");
		Intent intent = new Intent(this, Regesite.class);
		startActivity(intent);
		finish();
	}

	public void nimingclcik(View v) {
		Log.i("匿名按钮", "被点击了");
		Intent intent = new Intent(Login.this, Liuyan.class);
		intent.putExtra("name", "");
		startActivity(intent);
		finish();
	}

}
