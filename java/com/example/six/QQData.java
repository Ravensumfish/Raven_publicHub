package com.example.six;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class QQData {
    private String gName;
    private String uName;
    private String content;

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    private int icon;




    public QQData(String gName, String uName, String content,int icon) {
        this.gName = gName;
        this.uName = uName;
        this.content = content;
        this.icon = icon;
    }

    public String getgName() {
        return gName;
    }

    public void setgName(String gName) {
        this.gName = gName;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
