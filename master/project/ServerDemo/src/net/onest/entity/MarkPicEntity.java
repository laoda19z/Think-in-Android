package net.onest.entity;

public class MarkPicEntity {
    private String username;
    private String date;
    private int child;

    public MarkPicEntity() {
    }

    

    public MarkPicEntity(String username, String date, int child) {
		super();
		this.username = username;
		this.date = date;
		this.child = child;
	}



	public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



	public int getChild() {
		return child;
	}



	public void setChild(int child) {
		this.child = child;
	}
    
}