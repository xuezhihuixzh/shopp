package com.example.shoppy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.shoppy.bean.ChildBean;
import com.example.shoppy.bean.GroupBean;
import com.example.shoppy.bean.MCEvent;
import com.example.shoppy.bean.MsgEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
 
/**
 * @Author: 薛志辉 
 * @Date: 2019/11/1 14:50
 * @Description:
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * 全选
     */
    private CheckBox mCbAll;
    /**
     * 22
     */
    private TextView mTotalPrice;
    /**
     * 22
     */
    private TextView mTotalNum;
    private ExpandableListView mElv;

    private List<GroupBean> groupList = new ArrayList<>();
    private List<List<ChildBean>> childList = new ArrayList<>();
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        initView();
        initDate();
        mElv.setGroupIndicator(null);
        adapter = new MyAdapter(this, groupList, childList);
        mElv.setAdapter(adapter);
        for (int i = 0; i <groupList.size() ; i++) {
            mElv.expandGroup(i);
        }


    }

    @Subscribe
    public void moneyCount(MCEvent mcEvent){
        int money = mcEvent.getMoney();
        int count = mcEvent.getCount();
        mTotalNum.setText(count+"");
        mTotalPrice.setText(money+"");
    }
    @Subscribe
    public void messageEvent(MsgEvent msg) {
        mCbAll.setChecked(msg.isFlag());
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initDate() {
        for (int i = 0; i < 3; i++) {
            GroupBean groupBean = new GroupBean(false, "商家" + i);
            groupList.add(groupBean);
            List<ChildBean> list = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                ChildBean childBean = new ChildBean("商品" + i, 1 + i, false);
                list.add(childBean);
            }
            childList.add(list);
        }
    }

    private void initView() {
        mCbAll = (CheckBox) findViewById(R.id.cb_all);
        mCbAll.setOnClickListener(this);
        mTotalPrice = (TextView) findViewById(R.id.totalPrice);
        mTotalNum = (TextView) findViewById(R.id.totalNum);
        mElv = (ExpandableListView) findViewById(R.id.elv);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cb_all:
                adapter.allChecked(mCbAll.isChecked());
                break;
        }

}

}
