package com.hudong.liuyan;

import java.io.InputStream;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.databean.DataBean;
import com.itheima.htmlviewer.R;
import com.loopj.android.image.SmartImageView;

/**
 * @author  ChenShaoWen
 * 2017年3月22日  下午12:18:40
 */
public class Liuyan extends Activity {

   public 	EditText editxt ; 
	public Button btn;
	public Button refbtn;
	
	public ListView myListView = null;
	ScrollView mScro;
	
	TextView mAddtxt;//+给我留言  不需要传递到数据库中去

	static String usernmae = null;
	//适配数据的集合
	List<LiuyanDataBean> mListData = new ArrayList<LiuyanDataBean>();
	MyAdapter myadapter = null;
	
	Handler handler = new Handler()
	{

		@Override
		public void handleMessage(Message msg) {
			
			//Log.i("这是访问网络之后一开始返回的", (String)msg.obj);
			//更新listview还没实现这个功能
			//myadapter.notifyDataSetChanged();

			switch(msg.what)
			{
			case 4:
	    		Toast.makeText(Liuyan.this, "不能提交匿名或空留言！", 0).show();

				//匿名用户如果有输入的操作，清空。
				editxt.setText("");
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);    
		        imm.hideSoftInputFromWindow(editxt.getWindowToken(), 0) ; 
				break;
			
			case 3:
				android.util.Log.i("刷新失败", "刷新失败");
				Toast.makeText(Liuyan.this, "刷新失败", 0).show();
				break;
			case 2:
				android.util.Log.i("刷新成功", "但是更新界面失败");

				//这是留言成功的时候执行的  将数据加载到ListView中去
				Toast.makeText(Liuyan.this, "刷新成功", 0).show();
				
		        
				myListView = (ListView) findViewById(R.id.liuyanlistxt_id);

				myadapter = new MyAdapter(Liuyan.this, mListData);
				myListView.setAdapter(myadapter);
				myadapter.notifyDataSetChanged();
				break;
				
			case 1:
				String cg = new String("留言成功");
				String sb = new String("留言失败");
				if(msg.obj.toString().equals(cg))
				{
					//留言成功的话，执行清除掉edittext里面的内容 同时收起软键盘
					editxt.setText("");
					//收起软键盘
					/*InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);   
					if(imm != null) { 
						imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),    
			                       0);   
					}*/
					InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);    
			        imm1.hideSoftInputFromWindow(editxt.getWindowToken(), 0) ;  
			        
					android.util.Log.i("这是已经留言成功的标志", msg.obj.toString());
					Toast.makeText(Liuyan.this, "For Swiftc :感谢您的留言！", Toast.LENGTH_SHORT).show();

				}
				else{
					android.util.Log.i("这里被执行呀", "有网络但是留言失败");
					Toast.makeText(Liuyan.this, "留言失败，服务器未知异常", Toast.LENGTH_SHORT).show();
				}
				break;
			case 0:
				android.util.Log.i("这里被执行呀", "没有网络但是留言失败");
				Toast.makeText(Liuyan.this, "留言失败，请检查您的网络", Toast.LENGTH_SHORT).show();
				break;
				
				
				default :break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.liuyan);
		
		Intent intent = getIntent();
        usernmae = intent.getStringExtra("name");//获取上一个界面传递过来的值
        Log.i("上一个界面传递过来的用户名", usernmae);
        editxt = (EditText) findViewById(R.id.liuyan_id);
		btn = (Button) findViewById(R.id.liuyanbtn_id);
		//mAddtxt = (TextView) findViewById(R.id.dmsmadlsamdls);
		refbtn =  (Button) findViewById(R.id.refreshbtn_id);
		
	mScro = (ScrollView) findViewById(R.id.liuyanscr);

	}

    //留言的点击事件
    public void liuyanclick(View v)
    {
    	//需要写入数据库的有留言内容 ，系统时间，用户名
    	final String contentxt = editxt.getText().toString();
    	//time
    	SimpleDateFormat sf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Date date = new Date();
    	
    	final String time  = sf.format(date);
    	
    	Log.i("系统的时间", time);
    	//usernmae
    	//final String path= "http://192.168.191.1:8080/MyService/servlet/LiuYan"	;
    	final String path = "http://120.25.239.107:8080/MyService/servlet/LiuYan";
    	if(usernmae.length()!=0&&contentxt.length()!=0)//到时候如果是匿名登录的话就不给她权限评论
    	{
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
        				String content = "&username="+usernmae+"&context1="+contentxt+"&time="+time;
        				//打开连接对象的输出流
        				conn.setDoOutput(true);
        				//获取连接对象的输出流
        				OutputStream os = conn.getOutputStream();
        				//把数据写入输出流中
        				os.write(content.getBytes());
                        //访问网络成功
                        if(conn.getResponseCode()==200)
                        {
                        	//向数据库写入三个值
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
                        	//留言失败
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
    	else{
    	//	Toast.makeText(this, "不能提交匿名或空留言！", 0).show();
    		Message msg = new Message();
			msg.what=4;
			handler.sendMessage(msg);
    	}
    }
    
    //刷新的点击事件
    public void refreshbtn(View v)
    {
    	Log.i("刷新被点击", "刷新事件");
    	//从数据库中查询所有的留言，并且将数据适配到ListView中。
    	//final String path = "http://192.168.191.1:8080/MyService/servlet/Refresh";
    	final String path = "http://120.25.239.107:8080/MyService/servlet/Refresh";
		// final org.json.JSONArray jsonarray =null ;
    	
    	Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					URL url =  new URL(path);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(5000);
					conn.setReadTimeout(5000);
					
					Log.i("进入异步任务", "成功访问网络前");
					//如果访问网络成功的话
					if (conn.getResponseCode() == 200) {
						InputStream is = conn.getInputStream();
						String text = Tools.getTextFromStream(is);
						//这里的text实际上是Json 转换成的字符流  需要解析
						//已经是字符串输出流了，直接将它转换成json对象
						
						
					   org.json.JSONArray jsonarray = new org.json.JSONArray(text);
						mListData = new ArrayList<LiuyanDataBean>();
						for(int i = 0;i<jsonarray.length();i++)
						{
							LiuyanDataBean  ldb = new LiuyanDataBean();
							//获取一条数据的json
							 org.json.JSONObject json = (org.json.JSONObject) jsonarray.get(i);
							if(json.has("username")&&json.has("context")&&json.has("time"))
							{
								
						
							 String usern = json.optString("username");
							 String cont  = json.optString("context");	
							 String time = json.optString("time");
							 Log.i("这是解析出来的时间",time);
							 //然后将获取到的数据设置到bean中。
							 ldb.setUsernmae(usern);
							 ldb.setContentxt(cont);
							 ldb.setTime(time);
							 mListData.add(ldb);
							}
						}
						
						Message msg = handler.obtainMessage();
						msg.obj = mListData;
						msg.what=2;
						handler.sendMessage(msg);
					}
					else
					{
						Message msg =new Message();
						msg.what = 3;
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
    	t.start();
    }
    
    
    //ListView 的内部类实现
    class MyAdapter extends BaseAdapter
    {
    	Context context = null;
    	LayoutInflater layoutinflate;
    	
    	List<LiuyanDataBean> mListData = new ArrayList<LiuyanDataBean>();
    	//内部类的构造函数
    	
    	public MyAdapter(Context context,
				List<LiuyanDataBean> mListData) {
			this.context = context;
			this.layoutinflate = LayoutInflater.from(context);
			this.mListData = mListData;
		}
    	//Holder类
    	class ViewHolder
    	{
    		public TextView mUsertxt = null;//用户名的设置
    		public TextView mAddtxt;//+给我留言  不需要传递到数据库中去
    		public TextView mContetxt= null;//留言的正文内容 
    		public TextView mTime=null;//留言的时间  当发送留言内容的时候，同时获取时间将它存在数据库中
    	}
		@Override
		public int getCount() {
			return mListData.size();
		}
		@Override
		public Object getItem(int position) {
			return null;
		}
		@Override
		public long getItemId(int position) {
			return 0;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View mView  = null;
			ViewHolder mViewHolder = null;
			if(convertView == null)
			{
				mView = View.inflate(Liuyan.this, R.layout.liuyan_item, null);
				mViewHolder = new ViewHolder();
				mViewHolder.mUsertxt =  (TextView) mView.findViewById(R.id.liuyanuser_id);
				mViewHolder.mAddtxt = (TextView) mView.findViewById(R.id.dmsmadlsamdls);
				mViewHolder.mContetxt = (TextView) mView.findViewById(R.id.liuyan_content_id);
				mViewHolder.mTime = (TextView) mView.findViewById(R.id.liuyan_tima_id);
			    mView.setTag(mViewHolder);
			}
			else
			{
				//否则从缓存中读取
				mView = convertView;
				mViewHolder = (ViewHolder) mView.getTag();
			}
			
			mViewHolder.mUsertxt.setText(mListData.get(position).getUsernmae());
			mViewHolder.mAddtxt.setText("+给 我 留 言 :");
			mViewHolder.mContetxt.setText(mListData.get(position).getContentxt());
			mViewHolder.mTime.setText(mListData.get(position).getTime());
			return mView;
		}
    }
    
}
