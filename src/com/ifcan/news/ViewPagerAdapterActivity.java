package com.ifcan.news;

import java.util.ArrayList;
import java.util.List;

import com.example.style.Global;
import com.example.style.SlideRightMActivity;
import com.itheima.htmlviewer.R;
import com.sw.newsfragment.EduFragment;
import com.sw.newsfragment.EntFragment;
import com.sw.newsfragment.GupiaoFragment;
import com.sw.newsfragment.LadyFragment;
import com.sw.newsfragment.MoneyFragment;
import com.sw.newsfragment.TechFragment;
import com.sw.newsfragment.SportFragment;
import com.sw.newsfragment.TravelFragment;
import com.sw.newsfragment.WarFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * @author  ChenShaoWen
 * 2017年3月8日  下午2:29:39
 */
public class ViewPagerAdapterActivity extends FragmentPagerAdapter{

    //新闻的标题
	public static String [] TITLES  = new String[]{"科技","体育","军事","娱乐","教育","财经","旅游","股票","健康"};
	
	//将没一个标题对应的Fragment装进List集合中去
//	public List<Fragment> mList  = new ArrayList<Fragment>();
	public ViewPagerAdapterActivity(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		
  List<Fragment> mList  = new ArrayList<Fragment>();
		//对已经定义的fragment进行一个创建实例对象！
		//给不同的Fragment传进去一个位置的参数，通过传进去的参数进行显示不同的Fragment!
	TechFragment mKeji  = new TechFragment();
	SportFragment mSport = new SportFragment();
	WarFragment mWar = new WarFragment();
	EntFragment mEnt = new EntFragment();
	EduFragment mEdu = new EduFragment();
	MoneyFragment mMoney = new MoneyFragment();
	TravelFragment mTravel = new TravelFragment();
	GupiaoFragment mGupiao = new GupiaoFragment();
	LadyFragment mLady = new LadyFragment();
  	//将对象添加到list集合中返回
	
	mList.add(mKeji);
	mList.add(mSport);
	mList.add(mWar);
	mList.add(mEnt);
	mList.add(mEdu);
	mList.add(mMoney);
	mList.add(mTravel);
	mList.add(mGupiao);
	mList.add(mLady);
	
	return mList.get(position);
	}
	
	@Override
	public int getCount() {
		return TITLES.length;
	}
	@Override
	public CharSequence getPageTitle(int position)
	
	{
		//每滑动一个title就会返回一页，即上边定义的GetItem.返回Tab标题！
		return TITLES[position];
	}
	
	/**
	 * 
	 * 
	 * 在FragmentPagerAdapter默认会保存的三个item也就是当前的一个，前一个和后一个。
	 * 滑动过程中适配器默认会把前一个之前的item destroy掉，所以当滑动回来时就依然会重新加载。
	 * 也就是还会执行一次onCreateView的方法。分析其原因就是适配器销毁了之前的item，自然解决办法就是不让他销毁。
                   具体方法就是重写FragmentPagerAdapter的destroyItem方法注释掉
        super.destroyItem(Container, position, object);就行了。
	 * 
	 */
	@Override  
    public void destroyItem(ViewGroup container, int position, Object object) {  
        //super.destroyItem(container, position, object);  
    }  

	
}
