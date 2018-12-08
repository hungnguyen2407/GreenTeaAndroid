package com.greentea.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.greentea.R;
import com.greentea.activity.AnnounceDetailActivity;
import com.greentea.adapter.AnnounceAdapter;
import com.greentea.dao.CustomConnection;
import com.greentea.model.Announce;
import com.greentea.model.JSONTags;
import com.greentea.model.MemoryName;
import com.greentea.model.NameOfResources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/*
Xu ly hien thi cho trang thong bao
 */
public class AnnounceFragment extends Fragment {

    private View announceView;
    private Activity mainActivity;
    private ListView listView;
    private AnnounceAdapter adapter;
    private ArrayList<Announce> listAnnouces;
    private SharedPreferences sp;
    private LayoutInflater inflater;
    private ViewGroup container;
    private SwipeRefreshLayout announceRefresher;
    private int tabPosition;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        Lay activity
        mainActivity = getActivity();
        this.inflater = inflater;
        this.container = container;
//        Khoi tao view
        announceView = inflater.inflate(R.layout.fragment_announcement, container, false);
        sp = mainActivity.getSharedPreferences(MemoryName.TEMP_DATA.toString(), Context.MODE_PRIVATE);

//        Lay list view
        listView = announceView.findViewById(R.id.announce_list_view);
        announceRefresher = announceView.findViewById(R.id.announce_refresher);
        announceRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetAnnounceTask().execute();
            }
        });
        //        Xu ly hien thi tab
        tabLayoutHandler();
//        Xu ly hien thi thong bao
        announceListHandler();
//        Tra view cho activity hien thi
        return announceView;
    }

    @SuppressLint("NewApi")
    private void tabLayoutHandler() {
//        Tao tab view
        String[] tabsList = {getString(R.string.announce_tabs_all), getString(R.string.announce_tabs_faculty), getString(R.string.announce_tabs_class)};
//        String[] tabsList = {getString(R.string.announce_tabs_all)};
        TabLayout tabLayout = announceView.findViewById(R.id.announce_tablayout);
        tabLayout.setSelectedTabIndicatorColor(mainActivity.getColor(R.color.colorSecondary));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        tabPosition = 0;
                        announceListHandler();
                        break;
                    case 1:
                        tabPosition = 1;
                        filterByFaculty();
                        break;
                    case 2:
                        tabPosition = 2;
                        filterByGroup();
                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
//        Dong lap gan cac tab trong tablist vao tablayout
        for (int i = 0; i < tabsList.length; i++) {
            TextView tabCustom = (TextView) LayoutInflater.from(mainActivity).inflate(R.layout.custom_tab, null);
            tabCustom.setText(tabsList[i]);
            tabLayout.addTab(tabLayout.newTab().setCustomView(tabCustom));
        }
    }

    private void filterByGroup() {
        try {
            ArrayList<Announce> listAnnouncesByClass = new ArrayList<>();
            String data = sp.getString(NameOfResources.ANNOUNCE_DATA.toString(), "");
            String userClassID = sp.getString(NameOfResources.USER_CLASS_ID.toString(), "");
            Announce announce = null;
            JSONArray jsonArray = new JSONObject(data).getJSONArray(JSONTags.ANNOUNCE.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                announce = new Announce(jsonArray.getJSONObject(i));
                if (userClassID.equals(announce.getGroup()))
                    listAnnouncesByClass.add(announce);
            }
            listView = announceView.findViewById(R.id.announce_list_view);
            adapter = new AnnounceAdapter(mainActivity, R.layout.announce_row, listAnnouncesByClass);

//        Truyen adapter vao listview
            listView.setAdapter(adapter);

//        Set su kien khi bam vao 1 thong bao trong listview
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Announce announce = listAnnouces.get(position);
                    SharedPreferences sp = mainActivity.getSharedPreferences(MemoryName.TEMP_DATA.toString(), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(NameOfResources.ANNOUNCE_HEADER.toString(), announce.getHeader());
                    editor.putString(NameOfResources.ANNOUNCE_CONTENT.toString(), announce.getContent());
                    editor.putString(NameOfResources.ANNOUNCE_DATE.toString(), announce.getDate());
                    editor.apply();
                    startActivity(new Intent(mainActivity, AnnounceDetailActivity.class));
                }
            });
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void filterByFaculty() {
        try {
            ArrayList<Announce> listAnnouncesByFaculty = new ArrayList<>();
            String data = sp.getString(NameOfResources.ANNOUNCE_DATA.toString(), "");
            String userFacultyID = sp.getString(NameOfResources.USER_FACULTY_ID.toString(), "");
            Announce announce = null;
            JSONArray jsonArray = new JSONObject(data).getJSONArray(JSONTags.ANNOUNCE.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                announce = new Announce(jsonArray.getJSONObject(i));
                if (userFacultyID.equals(announce.getGroup()))
                    listAnnouncesByFaculty.add(announce);
            }
//        for (int i = 0; i < listAnnouces.size(); i++) {
//            if (sp.getString(NameOfResources.USER_FACULTY_ID.toString(), "").equals(listAnnouces.get(i).getGroup()))
//                listAnnouncesByFaculty.add(listAnnouces.get(i));
//        }
            adapter = new AnnounceAdapter(mainActivity, R.layout.announce_row, listAnnouncesByFaculty);

//        Truyen adapter vao listview
            listView.setAdapter(adapter);

//        Set su kien khi bam vao 1 thong bao trong listview
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Announce announce = listAnnouces.get(position);
                    SharedPreferences sp = mainActivity.getSharedPreferences(MemoryName.TEMP_DATA.toString(), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(NameOfResources.ANNOUNCE_HEADER.toString(), announce.getHeader());
                    editor.putString(NameOfResources.ANNOUNCE_CONTENT.toString(), announce.getContent());
                    editor.putString(NameOfResources.ANNOUNCE_DATE.toString(), announce.getDate());
                    editor.apply();
                    startActivity(new Intent(mainActivity, AnnounceDetailActivity.class));
                }
            });
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
    Gan danh sach cac item vao list view
     */
    private void announceListHandler() {
        try {
            listAnnouces = new ArrayList<>();
            String data = sp.getString(NameOfResources.ANNOUNCE_DATA.toString(), "");
            JSONArray jsonArray = new JSONObject(data).getJSONArray(JSONTags.ANNOUNCE.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                listAnnouces.add(new Announce(jsonArray.getJSONObject(i)));
            }
//        Tao adapter nhan vao danh sach thong bao
            adapter = new AnnounceAdapter(mainActivity, R.layout.announce_row, listAnnouces);

//        Truyen adapter vao listview
            listView.setAdapter(adapter);

//        Set su kien khi bam vao 1 thong bao trong listview
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Announce announce = listAnnouces.get(position);
                    SharedPreferences sp = mainActivity.getSharedPreferences(MemoryName.TEMP_DATA.toString(), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt(NameOfResources.ANNOUNCE_ID.toString(), announce.getId());
                    editor.putString(NameOfResources.ANNOUNCE_HEADER.toString(), announce.getHeader());
                    editor.putString(NameOfResources.ANNOUNCE_CONTENT.toString(), announce.getContent());
                    editor.putString(NameOfResources.ANNOUNCE_DATE.toString(), announce.getDate());
                    editor.apply();
                    startActivity(new Intent(mainActivity, AnnounceDetailActivity.class));
                }
            });
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void setAnnounceView(View announceView) {
        this.announceView = announceView;
    }

    private class GetAnnounceTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            CustomConnection.makeGETConnectionWithParameter(mainActivity,
                    CustomConnection.URLSuffix.GET_ANNOUNCE_GET_BY_USER_ID,
                    NameOfResources.ANNOUNCE_DATA, sp.getString(NameOfResources.USER_ID.toString(), ""));
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
            switch (tabPosition) {
                case 0:
                    announceListHandler();
                    break;
                case 1:
                    filterByFaculty();
                    break;
                case 2:
                    filterByGroup();
                    break;
                default:
                    announceListHandler();
                    break;
            }
            announceRefresher.setRefreshing(false);
        }
    }

}
