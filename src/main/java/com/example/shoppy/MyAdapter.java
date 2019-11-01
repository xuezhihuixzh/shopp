package com.example.shoppy;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.shoppy.bean.ChildBean;
import com.example.shoppy.bean.GroupBean;
import com.example.shoppy.bean.MCEvent;
import com.example.shoppy.bean.MsgEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
/**
 * @Author: 薛志辉
 * @Date: 2019/11/1 14:50
 * @Description:
 */public class MyAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<GroupBean> groupList;
    private List<List<ChildBean>> childList;
    private int count;
    private int sumMoney;

    public MyAdapter(Context context, List<GroupBean> groupList, List<List<ChildBean>> childList) {
        this.context = context;
        this.groupList = groupList;
        this.childList = childList;
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder;
        if (convertView == null) {
            holder = new GroupViewHolder();
            convertView = View.inflate(context, R.layout.groupitem, null);
            holder.cb = convertView.findViewById(R.id.cb);
            holder.tvName = convertView.findViewById(R.id.tvName);
            convertView.setTag(holder);
        } else {

            holder = (GroupViewHolder) convertView.getTag();
        }
        //赋值
        GroupBean groupBean = groupList.get(groupPosition);
        holder.cb.setChecked(groupBean.isChecked());
        holder.tvName.setText(groupBean.getGroupName());
        //给group设置点击事件
        holder.cb.setOnClickListener(new GroupCbOnClickListener(groupPosition));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder;
        if (convertView == null) {
            holder = new ChildViewHolder();
            convertView = View.inflate(context, R.layout.childitem, null);
            holder.cb = convertView.findViewById(R.id.cb);
            holder.tvName = convertView.findViewById(R.id.tvName);
            holder.tvPrice = convertView.findViewById(R.id.tvPrice);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        //赋值
        ChildBean childBean = childList.get(groupPosition).get(childPosition);
        holder.cb.setChecked(childBean.isChecked());
        holder.tvName.setText(childBean.getChildName());
        holder.tvPrice.setText(childBean.getPrice() + "");
        //设置点击事件Child
        holder.cb.setOnClickListener(new ChildCbOnClickListener(groupPosition,childPosition));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupViewHolder {
        CheckBox cb;
        TextView tvName;
    }

    class ChildViewHolder {
        CheckBox cb;
        TextView tvName;
        TextView tvPrice;
    }

    class ChildCbOnClickListener implements View.OnClickListener {

        private int groupPosition;
        private int childPosition;

        public ChildCbOnClickListener(int groupPosition, int childPosition) {
            this.groupPosition = groupPosition;
            this.childPosition = childPosition;
        }

        @Override
        public void onClick(View v) {
            if (v instanceof CheckBox) {
                CheckBox cb = (CheckBox) v;
                List<ChildBean> childBeen = childList.get(groupPosition);
                ChildBean childBean = childBeen.get(childPosition);
                childBean.setChecked(cb.isChecked());
                //计算选中的商品数，并发送到主界面进行显示
                MCEvent mcEvent = new MCEvent();
                mcEvent.setCount(totalCount());
                mcEvent.setMoney(totalPrice());
                EventBus.getDefault().post(mcEvent);
                //判断商家所有的商品的checkbox是否选中
                if (isChildChecked(childBeen)) {
                    groupList.get(groupPosition).setChecked(true);
                    MsgEvent msgEvent = new MsgEvent();
                    msgEvent.setFlag(isGroupChecked());
                    EventBus.getDefault().post(msgEvent);
                    notifyDataSetChanged();

                } else {
                    groupList.get(groupPosition).setChecked(false);
                    MsgEvent msgEvent = new MsgEvent();
                    msgEvent.setFlag(false);
                    msgEvent.setFlag(isGroupChecked());
                    EventBus.getDefault().post(msgEvent);
                    notifyDataSetChanged();
                }
            }
        }

    }
    /**
     * 判断所有商家的所有商品的checkbox是否都选中
     *
     * @param childBean
     * @return
     */
    private boolean isChildChecked(List<ChildBean> childBean) {

        for (int i = 0; i < childBean.size(); i++) {
            ChildBean childBean1 = childBean.get(i);
            if (!childBean1.isChecked()) {
                return false;
            }
        }
        return true;
    }


    class GroupCbOnClickListener implements View.OnClickListener {

        private int groupPosition;

        public GroupCbOnClickListener(int groupPosition) {
            this.groupPosition = groupPosition;
        }

        @Override
        public void onClick(View v) {
            if (v instanceof CheckBox){
                CheckBox cb= (CheckBox) v;
                //根据cb.isChecked()是否选中，给一级列的checkbox改变状态
                groupList.get(groupPosition).setChecked(cb.isChecked());
                List<ChildBean> childBeenList = childList.get(groupPosition);
                for (ChildBean childBean : childBeenList){
                    childBean.setChecked(cb.isChecked());
                }
                //计算选中的商品数和金额，并发送到主界面进行显示
                MCEvent mcEvent = new MCEvent();
                mcEvent.setCount(totalCount());
                mcEvent.setMoney(totalPrice());
                EventBus.getDefault().post(mcEvent);

                MsgEvent msgEvent = new MsgEvent();
                msgEvent.setFlag(isGroupChecked());
                EventBus.getDefault().post(msgEvent);
                notifyDataSetChanged();
            }
        }
    }

    /**
     * 判断其他商家是否选中
     * @return
     */
    private boolean isGroupChecked() {
        for (GroupBean groupBean : groupList) {
            if (!groupBean.isChecked()){
                return false;
            }
        }
        return true;
    }

    //主界面全选框选中状态
    public void allChecked(boolean bool) {
        for (int i = 0; i < groupList.size(); i++) {
            groupList.get(i).setChecked(bool);
            for (int j = 0; j < childList.get(i).size(); j++) {
                childList.get(i).get(j).setChecked(bool);
            }
        }
        //计算选中的商品数，发送到主界面进行显示
        MCEvent mcEvent = new MCEvent();
        mcEvent.setCount(totalCount());
        mcEvent.setMoney(totalPrice());
        EventBus.getDefault().post(mcEvent);
        notifyDataSetChanged();

    }

    /**
     * 计算商品总价格
     *
     * @return
     */
    private int totalPrice() {
        sumMoney = 0;
        for (int i = 0; i < groupList.size(); i++) {
            for (int j = 0; j < childList.get(i).size(); j++) {
                if (childList.get(i).get(j).isChecked()) {
                    int price = childList.get(i).get(j).getPrice();
                    sumMoney += price;
                }
            }
        }
        return sumMoney;
    }

    /**
     * 计算商品的总数量
     *
     * @return
     */
    private int totalCount() {
        count = 0;
        for (int i = 0; i < groupList.size(); i++) {
            for (int j = 0; j < childList.get(i).size(); j++) {
                if (childList.get(i).get(j).isChecked()) {
                    //遍历所有商品，只要是选中状态的，就加1
                    count++;
                }
            }
        }
        return count;
    }


}
