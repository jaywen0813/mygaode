package com.android.mygaode.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.android.mygaode.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchAdapters extends BaseAdapter {



    private Context context;
    private List<PoiItem>  list;

    public SearchAdapters(Context context, List<PoiItem>  list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_sousuo,
                    parent, false);
            viewHolder=new ViewHolder();
            viewHolder.tvName = convertView.findViewById(R.id.tv_name);
            viewHolder.tvDistance = convertView.findViewById(R.id.tv_distance);
            viewHolder.collectionView = convertView.findViewById(R.id.collection_view);
            viewHolder.tvAddress = convertView.findViewById(R.id.tv_address);
            viewHolder.imgDaohang2 = convertView.findViewById(R.id.img_daohang2);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final PoiItem item = list.get(position);

        //标题
        if (!(item.getTitle()==null||item.getTitle().equals(""))){
            viewHolder.tvName.setText(item.getTitle());
        }

        //地址





        return convertView;
    }

    private static class ViewHolder {
        TextView tvName;
        TextView tvDistance;
        View collectionView;
        TextView tvAddress;
        ImageView imgDaohang2;









    }
}
