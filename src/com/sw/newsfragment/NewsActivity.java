package com.sw.newsfragment;

import java.io.BufferedInputStream;


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import weixinapi.WXEntryActivity;

import com.example.style.Global;
import com.example.style.SlideRightMActivity;
import com.example.style.Global.SkinType;
import com.hudong.liuyan.Tools;
import com.itheima.htmlviewer.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.loopj.android.image.BitmapImage;
import com.loopj.android.image.SmartImageView;
import com.sw.sqlitedao.MyDao;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.viewpagerindicator.TitlePageIndicator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.renderscript.Type;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ScaleXSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author ChenShaoWen 2017年3月13日 上午10:22:46
 */
public class NewsActivity extends Activity {

	WebView mWebView;//加载html页面  新闻正文内容
	private Handler mHandler;
	// 这里的话只有正文是通过网络进行实时访问的，其余的时间，标题，图片可以通过上一个跳转前的界面通过
	// intent进行传递，，，这样服务器可能响应的比较快。
	public TextView mTitle_Content;
	public SmartImageView mImage_Content;
	public TextView mTime_Content;
	public int id;
	public String imageurl;
	public String title;//标题
	public String time;
	public String docurl;//正文的URL
	// 即将通过网络访问的
	public TextView mTxt_Content;

	// 实现日夜间模式
	protected int skin;
	public Context mContext;
	SharedPreferences sp;
	
	//收藏按钮
	Button scbtn;
	//分享按钮
	Button sharebtn;
	
	//本地收藏按钮
	Button bdbtn;
	private MyDao dao;
	
	//微信初始化
	private IWXAPI mwxapi;
	
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			
			switch(msg.what)
			{
			case 1:
				Toast.makeText(NewsActivity.this, "收藏成功！", 0).show();
				break;
			case 0:
				Toast.makeText(NewsActivity.this, "网络异常，收藏失败！", 0).show();
				break;
				default :break;
			}
		}
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {

		setThemeMode(getSkinTypeValue());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_content);
		
		
		dao = new MyDao(this);//本地数据库的操作类
		bdbtn = (Button) findViewById(R.id.bdbtn_id);
		scbtn  =(Button) findViewById(R.id.scbtn_id);
		mWebView = (WebView) findViewById(R.id.mwebview_id);
		mTitle_Content = (TextView) findViewById(R.id.content_titleid);
	    mImage_Content = (SmartImageView) findViewById(R.id.content_imgid);
		mTime_Content = (TextView) findViewById(R.id.content_time_id);
		
		// 接收上一个界面传递过来的数据 标题 时间 图片
		Intent intent = getIntent();
		id = intent.getIntExtra("id", 0);
		
		title = intent.getStringExtra("title");//标题
		time = intent.getStringExtra("time");
		
		//这两个得到的还只是传递过来对应的URL，，，还需要通过网络进行解析
		docurl = intent.getStringExtra("docurl");//正文n
		
		imageurl = intent.getStringExtra("image");//图片
		mImage_Content.setImageUrl(imageurl);
		
		// 将得到的数据设置到view中去
		mTitle_Content.setText(title);//标题
		mTime_Content.setText(time);//时间
		
		
		//微信初始化*********
		mwxapi =WXAPIFactory.createWXAPI(this, "wx10c3e7c3a47dd0cb");
		mwxapi.registerApp("wx10c3e7c3a47dd0cb");
		
		
		
		//webview 放大缩小任意比例
		mWebView.getSettings().setUseWideViewPort(true);
		//设置内置的缩放控件。 这个取决于setSupportZoom(), 若setSupportZoom(false)，则该WebView不可缩放，这个不管设置什么都不能缩放。
		//mWebView.getSettings().setBuiltInZoomControls(true);
		
		//页面支持缩放
		WebSettings webSettings =   mWebView .getSettings();       
		webSettings.setJavaScriptEnabled(true);  
		webSettings.setBuiltInZoomControls(true);
		webSettings.setSupportZoom(true);
		
		//新增webview的设置
		webSettings.setDomStorageEnabled(true);//开启DOM storage API功能
		/*webSettings.setPluginState(WebSettings.PluginState.ON);
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setAppCacheEnabled(true);
		webSettings.setAllowFileAccessFromFileURLs(true);
		webSettings.setAllowFileAccess(true);
		webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
		webSettings.setSaveFormData(true);
		webSettings.setGeolocationEnabled(true);*/
		webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//缓存模式
		//隐藏缩放图标
		//webSettings.setDisplayZoomControls(false);
		
		//mWebView.setBackgroundColor(Color.BLUE);
		//WebView的设置
				mWebView.loadUrl(docurl);
				
				mWebView.setWebChromeClient(new WebChromeClient(){
					@Override
					public void onReceivedTitle(WebView view, String title) {
						super.onReceivedTitle(view, title);
					}
				});
				mWebView.setWebViewClient(new WebViewClient(){
					@Override
					public boolean shouldOverrideUrlLoading(WebView view, String url) {
						view.loadUrl(url);
						return super.shouldOverrideUrlLoading(view, url);
					}
				});
		
		loadhtml();
	}
	
	private void loadhtml() {
		  Thread t = new Thread()
		   {
			   public void run() {
					try {
						
						String path = docurl;
						Log.i("我是被传递进来的URL", path);
						URL httpUrl = new URL(path);
						HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
						conn.setReadTimeout(5000);
						conn.setRequestMethod("GET");
						conn.setDoInput(true);
						conn.setDoOutput(true);
					//���������ļ�
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
							mWebView.loadData(sb.toString(), "utf-8", null);;
							//imageview.setBitmap(bitmap);
						}
					});
					} catch (Exception e) {
					}
				}
			   
		   };
		   t.start();
		}
	
	//*************实现日夜间的模块*******************

	public void setThemeMode(SkinType skinType) {
		switch (skinType) {
		case Light:
			setTheme(R.style.AppTheme_Light);
			break;
		case Night:
			setTheme(R.style.AppTheme_Night);
			break;
		default:
			setTheme(R.style.AppTheme_Light);
			break;
		}
	}

	// 获取用户选取的模式
	public SkinType getSkinTypeValue() {
		if (sp == null) {
			sp = getSharedPreferences("AppSkinType", Context.MODE_PRIVATE);
		}
		int i = sp.getInt("AppSkinTypeValue", 0);
		switch (i) {
		case 0:
			return SkinType.Light;
		case 1:
			return SkinType.Night;
		default:
			break;
		}
		return SkinType.Light;
	}
	// 保存用户是选取的模式
	public void saveSkinValue(int skin) {
		if (sp == null) {
			sp = getSharedPreferences("AppSkinType", Context.MODE_PRIVATE);
		}
		Editor editor = sp.edit();
		editor.putInt("AppSkinTypeValue", skin);
		editor.commit();
	}
	//微信收藏按钮的点击事件
	public void scbtn(View v)
	{
		//Toast.makeText(this, "收藏成功！", 0).show();
		//将获取到到的标题和对应的url对应的存储到云服务器中
		//到时候再在侧滑模块中去 通过网络获取适配到listview中，实现一个可以删除的操作。
    	/*final String path = "http://192.168.191.130:8080/MyService/servlet/Resolve";
    	//向数据库插入数据 通过异步任务 子线程    网络
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					URL url = new URL(path);
                    HttpURLConnection conn  = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setReadTimeout(5000);
    				conn.setConnectTimeout(5000);//设置连接超时

                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    				String content = "&title="+title+"&url="+docurl;//将对应的标题和url发送到云服务器上
    				//打开连接对象的输出流
    				conn.setDoOutput(true);
    				//获取连接对象的输出流
    				OutputStream os = conn.getOutputStream();
    				//把数据写入输出流中
    				os.write(content.getBytes());
                    //访问网络成功 收藏成功
                    if(conn.getResponseCode()==200)
                    {
                    	//向数据库写入两个值
                    	InputStream is = conn.getInputStream();
    					String text = Tools.getTextFromStream(is);
    					Log.i("已经执行到这里", text.toString());
    					Message msg = handler.obtainMessage();
    					msg.obj = text;
    					msg.what=1;
    					handler.sendMessage(msg);
                    }
                    else
    				{
                    	//收藏失败
    					Message msg = new Message();
    					msg.what=0;
    					handler.sendMessage(msg);
    				}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		t.start();*/
		//微信收藏
		//初始化一个对象，填写url
				WXWebpageObject webpage = new WXWebpageObject();
				webpage.webpageUrl = docurl;
				//用对象初始化一个对象，填写标题，描述
				WXMediaMessage msg =new WXMediaMessage(webpage);
				msg.title=title;
				//msg.description=title;
				SendMessageToWX.Req req= new SendMessageToWX.Req();
				req.transaction = String.valueOf(System.currentTimeMillis());
				req.message =msg;
				//微信收藏只需要改动scen
				req.scene = SendMessageToWX.Req.WXSceneFavorite;
				mwxapi.sendReq(req);
				SendMessageToWX.Resp resp = new SendMessageToWX.Resp();
		
				Toast.makeText(this, "成功收藏至微信", 0).show();
	}

	//分享按钮的点击事件
	public void sharebtn(View v)
	{
	//Toast.makeText(this, "分享按钮被点击", 0).show();
		//分享title和url到微信上 去 即分享网页
		weixinshare();
	}

	/**
	 * 网页类型分享
	 */
	private void weixinshare() {
		
		//初始化一个对象，填写url
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = docurl;
		//用对象初始化一个对象，填写标题，描述
		WXMediaMessage msg =new WXMediaMessage(webpage);
		msg.title=title;
		//msg.description="网页描述"
		//压缩bitmap
		
	//	WXImageObject imageobj = new WXImageObject();
	//	msg.mediaObject=imageobj;
		// Bitmap bmp = BitmapFactory.decodeFile(imageurl);
		//Bitmap thumb = Bitmap.createScaledBitmap(getnetbitmap(imageurl), 150, 150, true);
		//msg.setThumbImage(thumb);
	//	bmp.recycle();
	//	msg.thumbData= bitmap2byte(thumb);
		
		SendMessageToWX.Req req= new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message =msg;
		req.scene = SendMessageToWX.Req.WXSceneTimeline;
		mwxapi.sendReq(req);
		SendMessageToWX.Resp resp = new SendMessageToWX.Resp();
		if(resp.errCode==BaseResp.ErrCode.ERR_OK)
		{
			Toast.makeText(this, "即将分享至微信", 0).show();
		}
		else
		{
			Toast.makeText(this, "未知异常，分享失败", 0).show();

		}
	}
	
	/*//把网络图片转换为bitmap对象
	public static Bitmap getnetbitmap(String url)
	{
		Bitmap bitmap =null;
		 InputStream in =null;
		BufferedOutputStream out =null;
		try {
			in = new BufferedInputStream(new URL(url).openStream(), 1024);
			final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
			out = new BufferedOutputStream(dataStream, 1024);
			copy(in,out);
			out.flush();
			byte[] data = dataStream.toByteArray();
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			data = null;
			return bitmap;
		} 
		 catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	*//**
	 * @param in
	 * @param out
	 *//*
	private static void copy(InputStream in, BufferedOutputStream out) throws IOException{
		
		byte[] b= new byte[1024];
		int read;
		while((read = in.read(b))!=-1)
		{
			out.write(b, 0, read);
		}
		
	}
	
	//将bitmap转换成byte数组
	public static byte[] bitmap2byte(Bitmap bitmap)
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		bitmap .compress(Bitmap.CompressFormat.PNG, 100, out);
		bitmap.recycle();
		byte[] result =out.toByteArray();
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	*/
	
	//本地收藏按钮的点击事件
	public void bdbtn(View v)
	{
		//将标题和正文的URl存储在本地数据库sqlite中
		dao.insert(title, docurl);
		Toast.makeText(this, "本地收藏成功", 0).show();
	}
}
