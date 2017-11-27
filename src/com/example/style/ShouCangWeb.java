package com.example.style;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.itheima.htmlviewer.R;

/**
 * @author  ChenShaoWen
 * 2017年4月15日  下午12:06:47
 */
public class ShouCangWeb extends Activity {
	
	
	//展示收藏内容的webview
	private WebView mWeb;
	private Handler mHandler;
	private String url =null;
	// 退出时间  
    private long currentBackPressedTime = 0;  
    // 退出间隔  
    private static final int BACK_PRESSED_INTERVAL = 2000;  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scwebview);
		mWeb = (WebView) findViewById(R.id.scweb_id);
		Intent intent = getIntent();
         url = intent.getStringExtra("url");
		Log.i("被传递进来收藏的url", url);
		
		
		
		mWeb.loadUrl(url);
		
		//webview 放大缩小任意比例
		mWeb.getSettings().setUseWideViewPort(true);
				
				//页面支持缩放
				WebSettings webSettings =   mWeb .getSettings();       
				webSettings.setJavaScriptEnabled(true);  
				webSettings.setBuiltInZoomControls(true);
				webSettings.setSupportZoom(true);

				mWeb.setWebChromeClient(new WebChromeClient(){
			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
			}
		});
		
				mWeb.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return super.shouldOverrideUrlLoading(view, url);
			}
		});
		
	   Thread t = new Thread()
	   {
		   public void run() {
				try {
					
					String url1  = url;
					URL httpUrl = new URL(url1);
					HttpURLConnection conn = (HttpURLConnection) httpUrl
							.openConnection();
					conn.setReadTimeout(5000);
					conn.setRequestMethod("GET");
					conn.setDoInput(true);
					conn.setDoOutput(true);
				final StringBuffer sb= new StringBuffer();
				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String str;
				while((str=reader.readLine())!=null){
					sb.append(str);
					InputStream in = conn.getInputStream();
					FileOutputStream out = null;
					File downloadfile = null;
					String filename = String.valueOf(System.currentTimeMillis());
					if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
					{
						File parent = Environment.getExternalStorageDirectory();
						downloadfile = new File(parent, filename);
						out = new FileOutputStream(downloadfile);
					}
					byte []b = new byte[2*1024];
					int len;
					if(out!=null)
					{
						while((len=in.read(b))!=-1){
							out.write(b, 0, len);
						}
					}
					  final Bitmap bitmap = BitmapFactory.decodeFile(downloadfile.getAbsolutePath());
						
				}
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						mWeb.loadData(sb.toString(), "utf-8", null);;
					}
				});
				} catch (Exception e) {
				}
			}
		   
	   };
	   t.start();
	}
	@Override
	public void onBackPressed() {
		
	 if (mWeb.canGoBack()){ 
        if(mWeb.getUrl().equals(url)){ 
         super.onBackPressed(); 
                }else{ 
                	mWeb.goBack(); 
              } 
           }else{ 
         super.onBackPressed(); 
          } 
          
		 if (System.currentTimeMillis()- currentBackPressedTime > BACK_PRESSED_INTERVAL) {  
		        currentBackPressedTime = System.currentTimeMillis();  
		       Toast.makeText(this, "双击退出当前页面", Toast.LENGTH_SHORT).show();  
		    } else {  
		        // 退出  
		        finish();  
		    }  
	}
	
}
