package com.carikoskaka.carikos;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HouseAdapter extends BaseAdapter {
    // params
    ArrayList<HouseModel> houseList;
    Activity activity;

    public HouseAdapter(Activity activity, ArrayList<HouseModel> houseList){
        this.activity = activity;
        this.houseList = houseList;
    }

    //method ini akan menentukan seberapa banyak data yang akan ditampilkan didalam ListView
    //houseList.size() == jumlah data dalam object List yang ada
    @Override
    public int getCount() {
        return houseList.size();
    }

    //method ini untuk mengakses per-item objek dalam list
    @Override
    public Object getItem(int position) {
        return houseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    //method ini yang akan menampilkan baris per baris dari item yang ada di ListView
    //dengan menggunakan pattern ViewHolder untuk optimasi performa dari ListView
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder = null;

        if (view == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_list, null);
            holder.txtHarga = (TextView)view.findViewById(R.id.txt_item_harga);
            holder.txtLokasi = (TextView)view.findViewById(R.id.txt_item_lokasi);
            holder.txtLokasi.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_location_blacksmall, 0, 0, 0);


            holder.txtTitle = (TextView)view.findViewById(R.id.txt_item_title);
            holder.imgItem = (ImageView)view.findViewById(R.id.img_item);
            view.setTag(holder);
        }else{
            holder = (ViewHolder)view.getTag();
        }

        HouseModel house = (HouseModel)getItem(position);
        holder.txtTitle.setText(house.getTitle());
        holder.txtHarga.setText(house.getHarga());
        holder.txtLokasi.setText(house.getLokasi());

        Picasso.with(activity).load(house.getImage()).into(holder.imgItem);

        return view;
    }

    static class ViewHolder{
        ImageView imgItem;
        TextView txtTitle, txtHarga, txtLokasi;
    }
}