package com.swift.my;

import com.itheima.htmlviewer.R;
import com.swift.my.GuideActivity;
import com.swift.my.MainActivity;





import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.ViewFlipper;

/**
 * @author  ChenShaoWen
 * 2017年3月6日  下午3:59:11
 */
public class GuideActivity extends Activity {
	/*
	 * 程序如果第一次启动的话就显示引导界面
	 */
	public ImageView mImageView_a;
	public ImageView mImageView_b;
	public ImageView mImageView_c;
	public ImageView mImageView_d;
	public ImageView mImageView_e;
	public ViewFlipper mViewFlipper;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide);
		
		
		mViewFlipper  =(ViewFlipper)findViewById(R.id.mViewFlipper_id);
        //设置自动播放图片
		mViewFlipper.startFlipping();
		//设置动画效果，从左边出现图片
	    mViewFlipper.setInAnimation(this, android.R.anim.slide_in_left);
		//监听事件：播放到最后一张的时候跳转回到MainActivity界面
	    mViewFlipper.getInAnimation().setAnimationListener(new Animation.AnimationListener() {
	      public void onAnimationStart(Animation animation) {}
			 public void onAnimationRepeat(Animation animation) {}
			    public void onAnimationEnd(Animation animation) {

			    int displayedChild = mViewFlipper.getDisplayedChild();
			    int childCount = mViewFlipper.getChildCount();

			    if (displayedChild == childCount - 1) {
			        	/*这里如果不添加休眠的话用户体验不太好，
			        	 * 因为当它跳转到最后一张的时候马上执行下边的程序
			        	 * 而不会再有延迟时间，看清最后一张图片的内容
			        	*/
			        	try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
			        	//停止播放图片
			        	mViewFlipper.stopFlipping();
                        //跳转回到主界面
						Intent intent = new Intent(GuideActivity.this, MainActivity.class);
						startActivity(intent);
						finish();
			        }
			    }
			});
	}
}
