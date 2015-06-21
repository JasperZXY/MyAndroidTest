package com.jasper.myandroidtest.listView.entity;

/**
 * Created by Jasper on 2015/6/21.
 */
public class ContentItem implements IItem {
    private String title;
    private String detail;

    public ContentItem() { }
    public ContentItem(String title, String detail) {
        this.title = title;
        this.detail = detail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
