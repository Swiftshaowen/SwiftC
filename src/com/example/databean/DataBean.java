package com.example.databean;



/**
 * @author  ChenShaoWen
 * 2017年3月14日  上午9:09:12
 */
public class DataBean {

	private String mImgurl; //图片
	private  String mTime;//时间
	private String mTitle;//标题
	
	private int mID;//我的id;
	private String  mChannelname;//频道的栏目名称
	
	private String mDocurl;//正文内容
	
	public DataBean(String mImgurl, String mTime, String mTitle, int mID,
			String mChannelname, String mDocurl) {
		super();
		this.mImgurl = mImgurl;
		this.mTime = mTime;
		this.mTitle = mTitle;
		this.mID = mID;
		this.mChannelname = mChannelname;
		this.mDocurl = mDocurl;
	}
	
	public DataBean(String mImgurl, String mTime, String mTitle, int mID,
			String mChannelname) {
		super();
		this.mImgurl = mImgurl;
		this.mTime = mTime;
		this.mTitle = mTitle;
		this.mID = mID;
		this.mChannelname = mChannelname;
	}
	public DataBean() {
		super();
	}
	
	
	public String getmDocurl() {
		return mDocurl;
	}

	public void setmDocurl(String mDocurl) {
		this.mDocurl = mDocurl;
	}

	public String getmImgurl() {
		return mImgurl;
	}
	public void setmImgurl(String mImgurl) {
		this.mImgurl = mImgurl;
	}
	
	public String getmTime() {
		return mTime;
	}
	public void setmTime(String mTime) {
		this.mTime = mTime;
	}
	
	public String getmTitle() {
		return mTitle;
	}
	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}
	
	public int getmID() {
		return mID;
	}
	public void setmID(int mID) {
		this.mID = mID;
	}
	
	public String getmChannelname() {
		return mChannelname;
	}
	public void setmChannelname(String mChannelname) {
		this.mChannelname = mChannelname;
	}
	
	
	
	

}
