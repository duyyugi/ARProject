package com.example.arproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.arproject.R;
import com.example.arproject.model.BaiGiang;

import java.util.List;

public class BaiGiangAdapter extends BaseAdapter {
    Context myContext;
    int myLayout;
    List<BaiGiang> arrayBaiGiang;
    public BaiGiangAdapter(Context context, int layout, List<BaiGiang> baiGiangList){
        myContext = context;
        myLayout = layout;
        arrayBaiGiang = baiGiangList;
    }
    @Override
    public int getCount() {
        return arrayBaiGiang.size();
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
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(myLayout, null);
        TextView txtTenBaiGiang = (TextView) convertView.findViewById(R.id.textViewTenBaiGiang);
        txtTenBaiGiang.setText(arrayBaiGiang.get(position).Ten);
        TextView txtTenGiaoVien = (TextView) convertView.findViewById(R.id.textViewTenGiaoVien);
        txtTenGiaoVien.setText(String.valueOf(arrayBaiGiang.get(position).TenGiaoVien));
        return convertView;
    }
}
