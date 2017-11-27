package com.sw.sqlitedao;

/**
 * @author  ChenShaoWen
 * 2017年4月28日  下午11:19:33
 */
public class MyScBean {
	private  String title;//标题
	private String url;//正文的url
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public MyScBean(String title, String url) {
		super();
		this.title = title;
		this.url = url;
	}
	public MyScBean() {
		super();
	}
	
}
