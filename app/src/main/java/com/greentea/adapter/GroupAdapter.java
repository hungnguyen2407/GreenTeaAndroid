package com.greentea.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import com.greentea.R;
import com.greentea.model.Group;

public class GroupAdapter extends BaseAdapter {
    private List<Group> listGroups;
    private LayoutInflater inflater;
    private Activity activity;

    public GroupAdapter(Activity activity, Context context, int resource, List<Group> listGroups) {
        this.activity = activity;
        this.listGroups = listGroups;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listGroups.size();
    }

    @Override
    public Group getItem(int position) {
        return listGroups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null)
            view = inflater.inflate(R.layout.group_row, null);
        TextView tvGroupName = view.findViewById(R.id.group_tv_name);
        final Group group = listGroups.get(position);

        tvGroupName.setText(group.getGroupName());
        return view;
    }
}
