package com.example.shoppy.bean;

/**
 * @Author: 薛志辉 
 * @Date: 2019/11/1 14:51
 * @Description:
 */
public class ChildBean {
    private String childName;
    private int price;
    private boolean checked;

    public ChildBean(String childName, int price, boolean checked) {
        this.childName = childName;
        this.price = price;
        this.checked = checked;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
