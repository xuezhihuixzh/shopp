package com.example.shoppy.bean;

/**
 * @Author: 薛志辉
 * @Date: 2019/11/1 14:51
 * @Description:
 */
public class GroupBean {
    private boolean checked;
    private String groupName;

    public GroupBean(boolean checked, String groupName) {
        this.checked = checked;
        this.groupName = groupName;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
