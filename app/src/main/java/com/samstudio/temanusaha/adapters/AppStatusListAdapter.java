package com.samstudio.temanusaha.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.samstudio.temanusaha.R;
import com.samstudio.temanusaha.entities.Partner;
import com.samstudio.temanusaha.util.TextConverter;
import com.samstudio.temanusaha.util.UniversalImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by satryaway on 10/17/2015.
 * adapter for application status
 */
public class AppStatusListAdapter extends BaseAdapter {
    private List<Partner> partnerList = new ArrayList<>();
    private Context context;
    private UniversalImageLoader imageLoader;

    public AppStatusListAdapter (Context context, List<Partner> partnerList) {
        this.context = context;
        this.partnerList = partnerList;
        imageLoader = new UniversalImageLoader(context);
        imageLoader.initImageLoader();
    }

    public void update(List<Partner> partnerList) {
        this.partnerList = partnerList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return partnerList.size();
    }

    @Override
    public Object getItem(int position) {
        return partnerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder;

        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.partner_status_lv_item_layout, parent, false);
            holder.partnerPicIV = (ImageView) convertView.findViewById(R.id.partner_pic_iv);
            holder.partnerNameTV = (TextView) convertView.findViewById(R.id.partner_name_tv);
            holder.ptNameTV = (TextView) convertView.findViewById(R.id.pt_name_tv);
            holder.statusTV = (TextView) convertView.findViewById(R.id.app_status_tv);
            holder.dateTV = (TextView) convertView.findViewById(R.id.date_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        imageLoader.display(holder.partnerPicIV, partnerList.get(position).getImgURL());
        holder.partnerNameTV.setText(partnerList.get(position).getName());
        holder.ptNameTV.setText(partnerList.get(position).getCompany());
        holder.statusTV.setText(TextConverter.convertStatsCodeToString(partnerList.get(position).getStatus()));
        holder.dateTV.setText(partnerList.get(position).getDate());

        return convertView;
    }

    class ViewHolder {
        ImageView partnerPicIV;
        TextView partnerNameTV;
        TextView ptNameTV;
        TextView statusTV;
        TextView dateTV;
    }
}
