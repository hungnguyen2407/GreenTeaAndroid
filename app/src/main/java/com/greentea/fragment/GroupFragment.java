package com.greentea.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.greentea.R;
import com.greentea.activity.GroupDetailActivity;
import com.greentea.adapter.GroupAdapter;
import com.greentea.dao.CustomConnection;
import com.greentea.model.Group;
import com.greentea.model.JSONTags;
import com.greentea.model.MemoryName;
import com.greentea.model.NameOfResources;

public class GroupFragment extends Fragment {
    private Activity mainActivity;
    private View groupView;
    private ListView listView;
    private SharedPreferences sp;
    private SwipeRefreshLayout groupRefresher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainActivity = getActivity();
        groupView = inflater.inflate(R.layout.fragment_group, container, false);
        listView = groupView.findViewById(R.id.group_list_view);
        groupRefresher = groupView.findViewById(R.id.group_refresher);
        groupRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetGroupInfoTask().execute();
            }
        });
        listGroupHandler();
        return groupView;
    }

    private void listGroupHandler() {
        try {
            final ArrayList<Group> listGroups = new ArrayList<>();
            sp = mainActivity.getSharedPreferences(MemoryName.TEMP_DATA.toString(), Context.MODE_PRIVATE);

            JSONArray jsonArray = new JSONObject(sp.getString(NameOfResources.GROUP_LIST.toString(), "")).getJSONArray(JSONTags.GROUP_LIST.toString());
            JSONObject jsonObject = null;
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                listGroups.add(new Group(jsonObject));
            }
            GroupAdapter groupAdapter = new GroupAdapter(mainActivity, mainActivity, R.layout.group_row, listGroups);
            listView.setAdapter(groupAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    sendDataHandler(listGroups.get(position));
                    startActivity(new Intent(mainActivity, GroupDetailActivity.class));
                }
            });
            groupAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendDataHandler(Group group) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(NameOfResources.GROUP_ID.toString(), group.getGroupId());
        editor.putString(NameOfResources.GROUP_NAME.toString(), group.getGroupName());
        editor.putString(NameOfResources.GROUP_MEM_NUM.toString(), group.getMemNum() + "");
        editor.putString(NameOfResources.GROUP_FACULTY_ID.toString(), group.getFacultyId());
        editor.commit();
    }

    private class GetGroupInfoTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            CustomConnection.makeGETConnectionWithParameter(mainActivity,
                    CustomConnection.URLSuffix.GET_GROUP_LIST,
                    NameOfResources.GROUP_LIST, sp.getString(NameOfResources.USER_ID.toString(), ""));

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            groupRefresher.setRefreshing(false);
            listGroupHandler();
        }
    }
}
