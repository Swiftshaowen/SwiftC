package com.example.style;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hudong.liuyan.Liuyan;
import com.hudong.liuyan.LiuyanDataBean;
import com.hudong.liuyan.Tools;
import com.itheima.htmlviewer.R;
import com.sw.newsfragment.NewsActivity;
import com.sw.sqlitedao.MyDao;
import com.sw.sqlitedao.MyScBean;

/**
 * @author  ChenShaoWen
 * 2017年4月15日  上午11:18:23
 */
public class ShouCang extends Activity {
	/**
	 * 展示收藏的activity  通过listview去展示，同时有一个左滑删除的操作
	 */
	public ListView myListView = null;
	MyAdapter myadapter = null;
	private MyDao dao ;
	private List<MyScBean> list ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shoucang);
		myListView = (ListView) findViewById(R.id.sc_listv_id);
		dao =new MyDao(this);
		//查询所有的收藏
		list = dao.queryall();
		myListView.setAdapter(new MyAdapter());
		//Item的点击事件！！！！！！！！
		myListView.setOnItemClickListener(new MyItemClickLis());
		
	}
	//ListView  的适配
	class MyAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			return list.size();
		}
		@Override
		public Object getItem(int position) {
			return list.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View mview  = convertView!=null?convertView:View.inflate(getApplicationContext(), R.layout.shoucang_item, null);
            TextView title_txt = (TextView) mview.findViewById(R.id.sctitle_txtid);
			final MyScBean bean = list.get(position);
			title_txt.setText(bean.getTitle());
			//title_txt.setText(bean.getUrl());
			return mview;
		}
	}
	//item的点击事件
	class MyItemClickLis implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			//将url传递到另外一个界面通过webview去解析加载
			final  MyScBean dt = list.get(position);
			Intent intent = new Intent(ShouCang.this, ShouCangWeb.class);
			intent.putExtra("url", dt.getUrl());
			Log.i("收藏的url即将被传递到另外一个界面", dt.getUrl());
			startActivity(intent);
		//	Toast.makeText(ShouCang.this, "点击事件被触发", 0).show();
		}
	}
}
