package com.greentea.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.greentea.R;
import com.greentea.model.Announce;

public class AnnounceAdapter extends BaseAdapter {

    private ArrayList<Announce> listAnnounces;
    private LayoutInflater inflater;
    public AnnounceAdapter(Context context, int resource, List<Announce> listAnnounce) {
        this.listAnnounces = (ArrayList<Announce>) listAnnounce;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listAnnounces.size();
    }

    @Override
    public Announce getItem(int position) {
        return listAnnounces.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null)
            view = inflater.inflate(R.layout.announce_row, null);
        TextView tvHeader = view.findViewById(R.id.announce_tv_header);
        TextView tvGroup = view.findViewById(R.id.announce_tv_group);
        TextView tvDate = view.findViewById(R.id.announce_tv_date);
        Announce announce = listAnnounces.get(position);


        tvHeader.setText(announce.getHeader());
        tvGroup.setText(announce.getGroup());
        tvDate.setText(announce.getDate());
        return view;
    }
}
