package com.jasper.myandroidtest.listView.entity;

/**
 * Created by Jasper on 2015/6/21.
 */
public class GroupItem implements IItem {
    private String name;

    public GroupItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
