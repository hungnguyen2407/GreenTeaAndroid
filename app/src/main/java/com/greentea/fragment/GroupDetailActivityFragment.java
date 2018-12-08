package com.greentea.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.greentea.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class GroupDetailActivityFragment extends Fragment {

    public GroupDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_group_detail, container, false);
    }
}
