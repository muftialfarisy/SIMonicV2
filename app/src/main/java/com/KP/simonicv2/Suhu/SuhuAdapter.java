package com.KP.simonicv2.Suhu;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.KP.simonicv2.R;

import java.util.ArrayList;

public class SuhuAdapter extends BaseAdapter {
    public ArrayList<Suhu> suhulist;
    Activity activity;

    public SuhuAdapter(Activity activity, ArrayList<Suhu> suhulist) {
        super();
        this.activity = activity;
        this.suhulist = suhulist;
    }
    @Override
    public int getCount() {
        return suhulist.size();
    }

    @Override
    public Object getItem(int position) {
        return suhulist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private class ViewHolder {

    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (view == null) {
            view = inflater.inflate(R.layout.listview_table_suhu, null);
            holder = new ViewHolder();

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        return view;
    }
}