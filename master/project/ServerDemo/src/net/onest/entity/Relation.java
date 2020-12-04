package net.onest.entity;

public class Relation {
	private int userid;
	private int childid;
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public int getChildid() {
		return childid;
	}
	public void setChildid(int childid) {
		this.childid = childid;
	}
	@Override
	public String toString() {
		return "Relation [userid=" + userid + ", childid=" + childid + "]";
	}
	public Relation(int userid, int childid) {
		super();
		this.userid = userid;
		this.childid = childid;
	}
	
}
