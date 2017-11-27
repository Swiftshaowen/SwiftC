package com.sw.newsfragment;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.example.databean.DataBean;
import com.itheima.htmlviewer.R;
import com.loopj.android.image.SmartImageView;/*
import com.sw.newsfragment.SportFragment.MyAdapter;
import com.sw.newsfragment.TechFragment.MyItemClickLis;
import com.sw.newsfragment.TechFragment.MyAdapter.ViewHolder;*/
import com.utils.MyTools;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author  ChenShaoWen
 * 2017年3月9日  下午12:44:49
 */
public class EduFragment extends Fragment {
	//通过不同的位置进行显示不同的标题
		public int mPosition;
		static int M_PAGE=2;
		//网络操作的相关声明
		List<DataBean> mListData  =  new ArrayList<DataBean>();

		Context context = null;
		private ListView mListView=null;
		LinearLayout mLinearLayout = null ; //数据正在加载的提示
		public View mView;
		MyAdapter myAdapter = null;
		
		//刷新控件
		ScrollView scr;
		Button edu_shauxinbtn;
		
		
		
		//通过Handler去更新主线程的UI
	     Handler handler   =new Handler(){
	    	//接收异步最后返回来的信息并将其更新
			@SuppressWarnings("unchecked")
			@Override
			public void handleMessage(Message msg) {
				if(msg.what==1)
				{
					//将数据加载提示隐藏
					mLinearLayout.setVisibility(View.GONE);
					
					mListView.setVisibility(View.VISIBLE);
					
					Log.i("如果访问网络成功", "11111111111111111111111111");
					myAdapter.notifyDataSetChanged();

				}
				else
					//网络访问失败的时候
					if(msg.what==0)
					{
						Log.i("访问网络出错", "1234");
						mLinearLayout.setVisibility(View.GONE);
						mListView.setVisibility(View.VISIBLE);
						Toast.makeText(getActivity(), "网络异常，请稍后重试呀~", Toast.LENGTH_LONG).show();
					}
					else
						if(msg.what==3)
						{
							//刷新成功
							//mListView.setAdapter(null);
							Toast.makeText(getActivity(), "成功加载！", Toast.LENGTH_LONG).show();
							myAdapter.notifyDataSetChanged();
						}
						else
							if(msg.what==4)
							{
								//刷新失败
								Toast.makeText(getActivity(), "网络异常，加载失败！", Toast.LENGTH_LONG).show();
								
							}
				if(msg.what==7)
				{
					Toast.makeText(getActivity(), "没有更多数据！", Toast.LENGTH_LONG).show();

				}
				
			}
	    };
		//构造函数
		public EduFragment(int mPosition) {
			this.mPosition = mPosition;
		}
		
		public EduFragment() {
			context =getActivity();
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			//启动异步任务去访问网络
			getDataFromNet();
			
			
			View mView = inflater.inflate(R.layout.edu_listview, container, false);
			
			mLinearLayout  = (LinearLayout) mView.findViewById(R.id.fragment_layout);
			
			mListView = (ListView)mView.findViewById(R.id.edu_listid);
			//适配ListView 
			myAdapter  = new MyAdapter(getActivity(), mListData);
			mListView.setAdapter(myAdapter);
			//这里由于要查看每一个Item ,到时候再添加每一个Item的监听事件就好。
			mListView.setOnItemClickListener(new MyItemClickLis());
			scr = (ScrollView) mView.findViewById(R.id.edu_shuaxinscr_id);
			
			edu_shauxinbtn = (Button) mView.findViewById(R.id.edu_shuaxinbtn_id);
			//刷新的点击事件 这里好像如果设置onclick属性是无效的！！！
			edu_shauxinbtn.setOnClickListener(new Button.OnClickListener() {
				//final String path = "http://wangyi.butterfly.mopaasapp.com/news/api?type=tech&page=2&limit=10";
				@Override
				public void onClick(View v) {
					Thread t = new Thread()
					{
						@Override
						public void run() {
							try {
								URL url = new URL(geturl());
								HttpURLConnection conn = (HttpURLConnection) url.openConnection(); 
								conn.setRequestMethod("GET");
								conn.setReadTimeout(2000);
								//访问网络成功
								if(conn.getResponseCode()==200)
								{
									InputStream is =  conn.getInputStream();
									sxforjson(is);

								}
								else
								{
									//刷新不成功
									Message msg = new Message();
									msg.what =4;
									handler.sendMessage(msg);
								}
								
							} catch (Exception e) {
								e.printStackTrace();
							}
							
						}
						
					};
					t.start();
				}
			});
			return mView;
		}
		//刷新的时候进行的json解析
		  public  List<DataBean> sxforjson(InputStream is) throws Exception
		  {
			  byte[] data  = MyTools.readStream(is);
		      //把字符串组转换成字符串
		      String json = new String(data);
		      JSONObject jsonobj  = JSONObject.fromObject(json);//转换成一个object
			  JSONArray jsonarray = jsonobj.getJSONArray("list");//list是要解析的那个数组对象
			 // DataBean mydata = new DataBean();
			 // mListData = new ArrayList<DataBean>();
				 mListData.clear();
			 for(Object obj:jsonarray)
			 {
				 
				 //!!!!!!!!由于这里把new放在了外面，导致每次界面加载的都是最后一条数据循环加载
				 //解决方法就是把这个new 放在里面，因为每次循环都是同一个对象！！！！！Perfect!
				  DataBean mydata = new DataBean();
				 //得到每一个对象
				 JSONObject jsonitem  = (JSONObject)obj;
				 int id = jsonitem.getInt("id");
				 String title = jsonitem.getString("title");
				 Log.i("**********", title);
				 String time = jsonitem.getString("time");
				 Log.i("**********", time);
				 String channelname = jsonitem.getString("channelname");
				 Log.i("**********", channelname);
				 //图片
				 String imgurl  = jsonitem.getString("imgurl");
				 //正文  应该不是这么直接的访问
				 String docurl = jsonitem.getString("docurl");
				 Log.i("我是正文呀", docurl);
				 

				 mydata.setmID(id);
				 mydata.setmChannelname(channelname);
				 mydata.setmImgurl(imgurl);
				 mydata.setmTime(time);
				 mydata.setmTitle(title);
				 mydata.setmDocurl(docurl);
				 mListData.add(mydata);
			 }
			 if(mListData.size()==0)
			 {
				 Message msg = new Message();
				 msg.what=7;
				 handler.sendMessage(msg);//刷新成功并且解析son数据成功的标识

			 }
			 else
			 {
			 Message msg = new Message();
			 msg.what=3;
			 handler.sendMessage(msg);//刷新成功并且解析son数据成功的标识
			 }
		   return mListData;
		  }
		
		/**
		 * 访问网络的异步方法
		 */
		private void getDataFromNet() {
			
			final String path = "http://wangyi.butterfly.mopaasapp.com/news/api?type=edu&page=1&limit=8";
	    
			Thread t = new Thread()
			{
				@Override
				public void run() {
					try {
						URL url = new URL(path);
						HttpURLConnection conn = (HttpURLConnection) url.openConnection(); 
						conn.setRequestMethod("GET");
					
						conn.setReadTimeout(2000);
						
						//如果访问网络成功的话
						if(conn.getResponseCode()==200)
						{
							InputStream is =  conn.getInputStream();
							getdatafrom_json(is);

						}
						else
						{
							//访问网络不成功
							Message msg = new Message();
							msg.what =0;
							handler.sendMessage(msg);
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
				
			};
			
			t.start();

		
		}
		//解析json格式的数据
	  public List<DataBean> getdatafrom_json(InputStream is) throws Exception
	  {
		  //把输入流转换成字符串组
	      byte[] data  = MyTools.readStream(is);
	      //把字符串组转换成字符串
	      String json = new String(data);
	      JSONObject jsonobj  = JSONObject.fromObject(json);//转换成一个object
		  JSONArray jsonarray = jsonobj.getJSONArray("list");//list是要解析的那个数组对象
		 // DataBean mydata = new DataBean();
		 // mListData = new ArrayList<DataBean>();
		 for(Object obj:jsonarray)
		 {
			 
			 //!!!!!!!!由于这里把new放在了外面，导致每次界面加载的都是最后一条数据循环加载
			 //解决方法就是把这个new 放在里面，因为每次循环都是同一个对象！！！！！Perfect!
			  DataBean mydata = new DataBean();
			 //得到每一个对象
			 JSONObject jsonitem  = (JSONObject)obj;
			 int id = jsonitem.getInt("id");
			 String title = jsonitem.getString("title");
			 Log.i("**********", title);
			 String time = jsonitem.getString("time");
			 Log.i("**********", time);
			 String channelname = jsonitem.getString("channelname");
			 Log.i("**********", channelname);
			 //图片
			 String imgurl  = jsonitem.getString("imgurl");
			 //正文  应该不是这么直接的访问
			 String docurl = jsonitem.getString("docurl");
			 Log.i("我是正文呀", docurl);
			 
			 mydata.setmID(id);
			 mydata.setmChannelname(channelname);
			 mydata.setmImgurl(imgurl);
			 mydata.setmTime(time);
			 mydata.setmTitle(title);
			 mydata.setmDocurl(docurl);
			 mListData.add(mydata);
		 }
		 
		 Message msg = new Message();
		 msg.what=1;
		 handler.sendMessage(msg);//访问网络并且解析son数据成功的标识
	   return mListData;
	  }
	  
	  
	  //********************************!!!!!!!!!!***********************************

		//适配listView的内部类
		class MyAdapter extends BaseAdapter
		{
			
			
			Context context = null;
			LayoutInflater  layoutinflate;
			List<DataBean> mListData =  new ArrayList<DataBean>();
			
			public MyAdapter(Context context,
					List<DataBean> mListData) {
				this.context = context;
				this.layoutinflate = LayoutInflater.from(context);
				this.mListData = mListData;
			}

			//对ListView进行优化 ViewHolder类
			 class ViewHolder
			{
				public SmartImageView  mImage = null;
				// public ImageView mImage =null;
				 public  TextView mTitle=null;
	              public TextView mTime =null ;
			}
			@Override
			public int getCount() {
				return mListData.size();
			}
			@Override
			public Object getItem(int position) {
				return null;//mListData.get(position)
			}
			@Override
			public long getItemId(int position) {
				return 0;//position
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
			
				//DataBean data =mListData.get(position);
				View mView = null;
				ViewHolder mViewHolder = null;
				//如果convertview为空的话就find 并且缓存起来
				if(convertView==null)
				{
					mView = View.inflate(getActivity(), R.layout.all_list_item, null);
					mViewHolder = new ViewHolder();
					mViewHolder.mTitle =  (TextView) mView.findViewById(R.id.mTxt_id);
					mViewHolder.mImage  = (SmartImageView) mView.findViewById(R.id.mImage_id);
				//	mViewHolder.mImage = (ImageView) mView.findViewById(R.id.mImage_id);
					mViewHolder.mTime = (TextView) mView.findViewById(R.id.mTime_id);
					mView.setTag(mViewHolder);
				}
				else
				{
					//否则就直接从Holder缓存机制里面取值
					mView = convertView;
					mViewHolder = (ViewHolder) mView.getTag();
				}
				
			  //  mViewHolder.mImage.setImageBitmap(getbmp(position));
				mViewHolder.mImage.setImageUrl(mListData.get(position).getmImgurl());
				mViewHolder.mTitle.setText(mListData.get(position).getmTitle());
				mViewHolder.mTime.setText(mListData.get(position).getmTime());
				/*mViewHolder.mImage.setBackgroundResource(icons[position]);
				mViewHolder.mTitle.setText(title[position]);
				mViewHolder.mTime.setText(time[position]);*/
				
				/*//填充每一个item的内容 第一种实现view的方式效率比较低。
				View view  = View.inflate(getActivity(), R.layout.all_list_item, null);
				//初始化每一个Item里面的控件
				ImageView mImage  = (ImageView) view.findViewById(R.id.mImage_id);
				TextView mTitle = (TextView) view.findViewById(R.id.mTxt_id);
				TextView mTime = (TextView) view.findViewById(R.id.mTime_id);
				mImage.setBackgroundResource(icons[position]);
				mTitle.setText(title[position]);
				mTime.setText(time[position]);*/
				return mView;
			}
			
		}
		
		
	/*	public Bitmap getbmp(int position)
		{
			try {
				BitmapFactory.Options opts = new BitmapFactory.Options(); 
				InputStream stream;
				stream = new URL(mListData.get(position).getmImgurl()).openStream();
				byte[] bytes = MyTools.readStream(stream); 

				//这3句是处理图片溢出的begin( 如果不需要处理溢出直接 opts.inSampleSize=1;) 
				opts.inJustDecodeBounds = true; 
				BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts); 
				opts.inSampleSize =10; 
				//end 
				opts.inJustDecodeBounds = false; 
			  Bitmap   bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts); 
				return bmp;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}*/
		//监听listIem的内部类
		class MyItemClickLis implements OnItemClickListener
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) 
					
			{
				DataBean data =mListData.get(position);
				Intent intent = new Intent(getActivity(), NewsActivity.class);
				intent.putExtra("id", data.getmID());
				intent.putExtra("title", data.getmTitle());
				intent.putExtra("image", data.getmImgurl());
				intent.putExtra("time", data.getmTime());
				intent.putExtra("docurl", data.getmDocurl());
				startActivity(intent);
			}
		}
		
		public String geturl()
		{
			//"http://wangyi.butterfly.mopaasapp.com/news/api?type=edu&page=1&limit=10";
			M_PAGE++;
			String a  ="http://wangyi.butterfly.mopaasapp.com/news/api?type=edu&page=";
			String b = "&limit=8";
			String page =String.valueOf(M_PAGE);
			String total= a+page+b;
			return total;
		}

		//Performing stop of activity that is not resumed:
		@Override
		public void onResume() {
			super.onResume();
			handler.sendEmptyMessageDelayed(1, 1000);
		}
		
		
}
