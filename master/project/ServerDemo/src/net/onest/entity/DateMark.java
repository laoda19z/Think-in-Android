package net.onest.entity;

public class DateMark {
	//打卡日期的属性
	private String markdate;
	public DateMark() {
		super();
	}

	public DateMark(String markdate) {
		super();
		this.markdate = markdate;
	}

	public String getMarkdate() {
		return markdate;
	}

	public void setMarkdate(String markdate) {
		this.markdate = markdate;
	}

	@Override
	public String toString() {
		return "DateMark [markdate=" + markdate + "]";
	}
	
}
