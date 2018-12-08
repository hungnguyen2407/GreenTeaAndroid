package com.greentea.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.greentea.R;
import com.greentea.activity.ChangePassActivity;

public class SettingFragment extends Fragment {

    private View settingView;
    private Activity mainActivity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        settingView = inflater.inflate(R.layout.fragment_setting, container, false);
        mainActivity = getActivity();
        settingViewHandler();
        return settingView;

    }

    private void settingViewHandler() {
        Button changePassBtn = settingView.findViewById(R.id.setting_change_pass_btn);
        changePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SharedPreferences sp = mainActivity.getSharedPreferences(MemoryName.TEMP_DATA.toString(), Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sp.edit();
//                editor.putString()
                startActivity(new Intent(mainActivity, ChangePassActivity.class));
            }
        });
    }
}
