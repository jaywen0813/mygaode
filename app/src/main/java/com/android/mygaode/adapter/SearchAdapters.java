package com.android.mygaode.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.PoiItem;
import com.android.mygaode.R;
import com.android.mygaode.weight.DistanceUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchAdapters extends BaseAdapter {



    private Context context;
    private List<PoiItem>  list;
    private LatLng myLaylng;

    private float distance;//距离
    LatLng latlngB;

    public SearchAdapters(Context context, List<PoiItem>  list, LatLng myLatlng) {
        this.context = context;
        this.list = list;
        this.myLaylng=myLatlng;
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
        if (!(item.getSnippet()==null||item.getSnippet().equals(""))){
            viewHolder.tvAddress.setText(item.getSnippet());
        }
        //距离

        latlngB=new LatLng(list.get(position).getLatLonPoint().getLatitude(),list.get(position).getLatLonPoint().getLongitude());

        distance = AMapUtils.calculateLineDistance(myLaylng, latlngB);//计算距离的方法

        viewHolder.tvDistance.setText(DistanceUtil.formateDistance(distance)+"");




        return convertView;
    }

    private static class ViewHolder {
        TextView tvName;
        TextView tvDistance;
        TextView tvAddress;
        ImageView imgDaohang2;









    }
}
