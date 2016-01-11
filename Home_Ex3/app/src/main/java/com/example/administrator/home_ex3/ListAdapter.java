package com.example.administrator.home_ex3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by AviramAlkobi on 10/01/2016.
 */


public class ListAdapter extends BaseAdapter {

    // object like struct
    static class ViewHolder {
        TextView date;
        TextView time;
        TextView temperature;
        TextView info ;
    }

    private ArrayList<Item> listData;
    private LayoutInflater layoutInflater;

    public ListAdapter(Context aContext, ArrayList<Item> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    public ArrayList<Item> getListData(){
        return this.listData;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int location) {
        return listData.get(location);
    }

    @Override
    public long getItemId(int location) {
        return location;
    }


    // implement from BaseAdapter
    public View getView(int location, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row, null);
            holder = new ViewHolder();
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.time = (TextView) convertView.findViewById(R.id.cTime);
            holder.temperature = (TextView) convertView.findViewById(R.id.temp);
            holder.info = (TextView)convertView.findViewById(R.id.infoText);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.date.setText(listData.get(location).getDate());
        holder.time.setText(listData.get(location).getTime());
        holder.temperature.setText(listData.get(location).getTemperature());
        holder.info.setText(listData.get(location).getInfo());

        return convertView;
    }

}
