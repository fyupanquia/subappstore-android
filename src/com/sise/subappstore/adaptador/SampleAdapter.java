package com.sise.subappstore.adaptador;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class SampleAdapter extends BaseAdapter {

    ArrayList<String> data;

    public SampleAdapter() {
        this.data = new ArrayList<String>();
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public void clearData() {
        // clear the data
        data.clear();
    }


	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		return null;
	}
}