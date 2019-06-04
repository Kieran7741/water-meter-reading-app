package com.waterscheme.meterreading;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;


public class area_adaptor extends BaseAdapter {

    private Activity mActivity;
    private ArrayList<String> mItems;

    public area_adaptor(Activity activity, ArrayList<String> items){
        this.mActivity = activity;
        this.mItems = items;

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
