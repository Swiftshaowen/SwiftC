package com.hudong.liuyan;

/**
 * @author  ChenShaoWen
 * 2017年3月24日  上午11:52:42
 */
public class LiuyanDataBean {

	private int id;
	private String usernmae; //用户
	private String addtxt; //"——+给我留言字样
	private String contentxt;//正文内容
	private String time; //留言时间
	
	public String getAddtxt() {
		return addtxt;
	}
	public void setAddtxt(String addtxt) {
		this.addtxt = addtxt;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsernmae() {
		return usernmae;
	}
	public void setUsernmae(String usernmae) {
		this.usernmae = usernmae;
	}
	public String getContentxt() {
		return contentxt;
	}
	public void setContentxt(String contentxt) {
		this.contentxt = contentxt;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public LiuyanDataBean(int id, String usernmae, String addtxt,
			String contentxt, String time) {
		super();
		this.id = id;
		this.usernmae = usernmae;
		this.addtxt = addtxt;
		this.contentxt = contentxt;
		this.time = time;
	}
	public LiuyanDataBean(String usernmae, String addtxt, String contentxt,
			String time) {
		super();
		this.usernmae = usernmae;
		this.addtxt = addtxt;
		this.contentxt = contentxt;
		this.time = time;
	}
	public LiuyanDataBean() {
		super();
	}
}
