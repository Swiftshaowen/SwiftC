package com.swift.my;



import android.app.Activity;
import android.content.Context;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.style.Global;
import com.example.style.SlideRightMActivity;
import com.ifcan.news.ViewPagerAdapterActivity;
import com.itheima.htmlviewer.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.viewpagerindicator.TabPageIndicator;

public class MainActivity extends SlidingFragmentActivity {

	//SlidingFragmentActivity
	public ViewPager mViewPager ;
		//它也是ViewPagerIndocator的其中一种！也就是为了实现指示器的滑动或点击事件而已！
	public TabPageIndicator mTabPagerIndicator ;
	public	 ViewPagerAdapterActivity mAdapter ;
	
	//实现日夜间模式
    protected int skain;
	public Context mContext;
	SharedPreferences sp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setThemeMode(getSkinTypeValue());
		super.onCreate(savedInstanceState);
		//将actionbar给隐藏掉
	//	requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_main);
		
//**********************1 引导模块*******************************//		
		//读写对象
		SharedPreferences  spf = getSharedPreferences("count",MODE_PRIVATE); 
		int count = spf.getInt("count", 0); 
		//如果是第一次启动程序，就跳转到引导界面
		if (count == 0) { 
			Intent intent = new Intent(); 
			intent.setClass(this,GuideActivity.class); 
			startActivity(intent); 
			finish();
			}
		//SharedPreferences只能通过Editor写入数据
		Editor editor = spf.edit(); 
		//存入数据 
		editor.putInt("count", ++count); 
		//提交修改 
		editor.commit();
//**********************引导模块*******************************//		
		//初始化侧滑
		initRightMenu();
		//初始化新闻指示器和Fragment模块
		initNewsContent();
		
		//设置日夜间模式
		setThemeMode(getSkinTypeValue());
	
	}

//***********************2 右侧滑模块*********************************//
	private void initRightMenu() {
		Fragment rightFragment = new SlideRightMActivity();
		setBehindContentView(R.layout.slide_right_frame);
		getSupportFragmentManager().beginTransaction()
	        .replace(R.id.slide_right_frame_id, rightFragment).commit();
		SlidingMenu menu = getSlidingMenu();
		//设置模式是右滑
		menu.setMode(SlidingMenu.RIGHT);
		// 设置滑动的屏幕范围，分为全屏滑动和边缘滑动，这是边缘滑动（防止和主Activity冲突）
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		// 划出时主页面显示的剩余宽度
		menu.setBehindOffsetRes(R.dimen.activity_horizontal_margin);
		// 设置渐入渐出效果的值
		menu.setFadeDegree(0.35f);
		menu.setBehindScrollScale(1.0f);
		
	}
	public void showLeftMenu(View view)
	{
		getSlidingMenu().showMenu();
	}
	
//***********************右侧滑模块*********************************//
	
//***********************3 新闻正文模块*********************************//

	private void initNewsContent() {
		//初始化ViewPager和 TabPagerIndicator
				mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
				mTabPagerIndicator = (TabPageIndicator) findViewById(R.id.id_indicator);
				mAdapter = new ViewPagerAdapterActivity(getSupportFragmentManager());
				mViewPager.setAdapter(mAdapter);
				//在github上也需要对Indicator组件进行一个set~
				mTabPagerIndicator.setViewPager(mViewPager, 0);
		
	}
	
//***********************新闻正文模块*********************************//
	
	//**********************4 实现日夜间模式*******************************//
	
	//设置日夜间模式
			public void setThemeMode(Global.SkinType skinType) {
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
			//获取用户选取的模式
			public Global.SkinType getSkinTypeValue() {
				if (sp == null) {
					sp = getSharedPreferences("AppSkinType", Context.MODE_PRIVATE);
				}
				int i = sp.getInt("AppSkinTypeValue", 0);
				switch (i) {
				case 0:
					return Global.SkinType.Light;
		        case 1:
					return Global.SkinType.Night;
				default:
					break;
				}
				return Global.SkinType.Light;
			}
			//保存用户是选取的模式  通过Shareprefrence去保存 日夜间模式的状态
			public void saveSkinValue(int skin) {
				if (sp == null) {
					sp = getSharedPreferences("AppSkinType", Context.MODE_PRIVATE);
				}
				Editor editor = sp.edit();
				editor.putInt("AppSkinTypeValue", skin);
				editor.commit();
			}
		//**********************4 实现日夜间模式*******************************//
	
			
			
			
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
