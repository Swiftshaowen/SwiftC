package com.example.style;
import android.content.Context;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.style.Global.SkinType;
import com.hudong.liuyan.Login;
import com.imooc.arcmenu.view.ArcMenu;
import com.itheima.htmlviewer.R;
import com.swift.my.MainActivity;
import com.utils.MyTools;

/**
 * @author  ChenShaoWen
 * 2017年3月7日  下午3:16:24
 */
public class SlideRightMActivity extends Fragment {

	/**
	 * 填充Fragment片段
	 */
	//private View mView;
	private CheckBox cbSetting;
	private TextView mTxt;
	private View llNight;
	protected int skin;
	public Context mContext;
	SharedPreferences sp;
	
	//留言按钮
	Button mLybtn;

	//自定义卫星菜单
	private ArcMenu mArcMenu;
	TextView txtv;
	
	//收藏按钮
	Button scbtn;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		setThemeMode(getSkinTypeValue());

		if(llNight==null)
		{
			llNight =inflater.inflate(R.layout.slide_right_menu, container, false);
		    
		}
		

		
		init();
		
		//自定义卫星菜单
		//保存checkedbox的记录状态
		sp = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
		boolean isProtecting = sp.getBoolean("isProtected", false);//每次进来的时候读取保存的数据
		if (isProtecting) {
			cbSetting.setChecked(true);
		}
		
		return llNight;
		}
	/**
	 * 
	 */
	//设置日夜间模式
	public void setThemeMode(SkinType skinType) {
		switch (skinType) {
		case Light:
			getActivity().setTheme(R.style.AppTheme_Light);
			break;
		case Night:
			getActivity().setTheme(R.style.AppTheme_Night);
			break;
		default:
			getActivity().setTheme(R.style.AppTheme_Light);
			break;
		}
	}
	//获取用户选取的模式
	public SkinType getSkinTypeValue() {
		if (sp == null) {
			sp = getActivity().getSharedPreferences("AppSkinType", Context.MODE_PRIVATE);
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
	//保存用户是选取的模式
	public void saveSkinValue(int skin) {
		if (sp == null) {
			sp = getActivity().getSharedPreferences("AppSkinType", Context.MODE_PRIVATE);
		}
		Editor editor = sp.edit();
		editor.putInt("AppSkinTypeValue", skin);
		editor.commit();
	}
	
	public void init() {
		mArcMenu = (ArcMenu) llNight.findViewById(R.id.id_menu);
		txtv = (TextView) llNight.findViewById(R.id.yl_id);

		scbtn  =  (Button) llNight.findViewById(R.id.mshoucangbtn_id);

		mLybtn  = (Button) llNight.findViewById(R.id.liuyanbtn_id);
		
		llNight = llNight.findViewById(R.id.ll_night);
		mTxt = (TextView) llNight.findViewById(R.id.sunandnight_modelid);
		cbSetting = (CheckBox) llNight.findViewById(R.id.checkbox_id);
		//checked的点击事件
		cbSetting.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					// 夜间模式
					//记住是否选中这个状态 即使应用已经结束掉了。
					Editor editor = sp.edit();
					editor.putBoolean("isProtected", true);
					editor.commit();//提交数据保存
					saveSkinValue(1);
					getActivity().setTheme(R.style.AppTheme_Night);//防止getTheme()取出来的值是创建时候得值
					initView();
					
				} else 
				{
					Editor editor = sp.edit();
					editor.putBoolean("isProtected", false);
					editor.commit();//提交数据保存
					// 白天模式
					saveSkinValue(0);
					getActivity().setTheme(R.style.AppTheme_Light);//防止getTheme()取出来的值是创建时候得值
					initView(); 
				}
			}
		});
		
		//留言按钮的监听事件，，，貌似把它写在外面监听失效。提示找不到这个监听方法
		mLybtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//跳转到登录留言互动的模块
				Intent intent = new Intent(getActivity(), Login.class);
				startActivity(intent);
			}
		});
		
		//卫星菜单的监听按钮
		mArcMenu.setOnMenuItemClickListener(new ArcMenu.OnMenuItemClickListener() {
			
			@Override
			public void onClick(View view, int pos) {
				switch(pos)
				{
				case 1: 
	                Toast.makeText(getActivity(), "新浪新闻", 0).show();					
					String path = "http://www.sina.com";
					Intent intent = new Intent(getActivity(), MWView.class);
					intent.putExtra("path", path);
					startActivity(intent);
					break;
				case 2:
	                  Toast.makeText(getActivity(), "百度新闻", 0).show();					

                    String path1 ="http://news.baidu.com";
                  Intent intent1 = new Intent(getActivity(), MWView.class);
                  intent1.putExtra("path", path1);

                  startActivity(intent1);

					break;
				case 3:
	                  Toast.makeText(getActivity(), "搜狐新闻", 0).show();					

					String path2 = "http://www.sohu.com";
                  Intent intent2 = new Intent(getActivity(), MWView.class);
                  intent2.putExtra("path", path2);

                  startActivity(intent2);

					break;
				case 4:
					String path3 = "http://www.qq.com";
                  Toast.makeText(getActivity(), "腾讯新闻", 0).show();					
                  Intent intent3 = new Intent(getActivity(), MWView.class);
                  intent3.putExtra("path", path3);
                  startActivity(intent3);
					break;
					default:break;
				}
				
			}
		});
		
		//收藏按钮的监听事件
		scbtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Toast.makeText(getActivity(), "收藏按钮被点击了", 0).show();
				//跳转到收藏的展示页
				Intent inten = new Intent(getActivity(), ShouCang.class);
				startActivity(inten);
			}
		});
		
		
	}
	
	//改变模式后手动修改UI,不然会出现闪屏  到时候解决头部问题的时候，可以试着把这段代码放在mainActivity里面试试
	@SuppressWarnings("deprecation")
	private void initView(){
		TypedValue typedValue = new TypedValue();
		Theme theme = getActivity().getTheme();
		theme.resolveAttribute(R.attr.gray_3_double, typedValue, true);   
		mTxt.setTextColor(getResources().getColor(typedValue.resourceId));
		theme.resolveAttribute(R.attr.source_bg, typedValue, true);   
		llNight.setBackgroundDrawable(getResources().getDrawable(typedValue.resourceId));  
	}
	
	
	
	
	
}

