package com.example.uidemo.mark.Entity;

public class ReturnMarkPic {
    private String background;
    private String sporttype;
    private int sporttime;
    private String impression;

    public ReturnMarkPic() {
        super();
    }

    public ReturnMarkPic(String background, String sporttype, int sporttime, String impression) {
        super();
        this.background = background;
        this.sporttype = sporttype;
        this.sporttime = sporttime;
        this.impression = impression;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getSporttype() {
        return sporttype;
    }

    public void setSporttype(String sporttype) {
        this.sporttype = sporttype;
    }

    public int getSporttime() {
        return sporttime;
    }

    public void setSporttime(int sporttime) {
        this.sporttime = sporttime;
    }

    public String getImpression() {
        return impression;
    }

    public void setImpression(String impression) {
        this.impression = impression;
    }
}
