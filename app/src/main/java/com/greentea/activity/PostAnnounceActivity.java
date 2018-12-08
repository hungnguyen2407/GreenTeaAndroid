package com.greentea.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.greentea.R;
import com.greentea.dao.CustomConnection;
import com.greentea.model.Group;
import com.greentea.model.JSONTags;
import com.greentea.model.MemoryName;
import com.greentea.model.NameOfResources;

public class PostAnnounceActivity extends AppCompatActivity {

    private Activity postAnnounceActivity;
    private EditText editTextHeader, editTextContent;
    private ConstraintLayout postAnnounceForm;
    private ProgressBar progressBar;
    private String groupID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_announce);
        Toolbar toolbar = findViewById(R.id.post_announce_toolbar);
        setSupportActionBar(toolbar);
        postAnnounceActivity = this;
        ImageButton btnBack = findViewById(R.id.post_announce_back_btn);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        postAnnounceHandler();
    }


    private void postAnnounceHandler() {
        editTextHeader = findViewById(R.id.post_announce_edit_text_header);
        editTextContent = findViewById(R.id.post_announce_edit_text_content);
        postAnnounceForm = findViewById(R.id.post_announce_form);
        progressBar = findViewById(R.id.post_announce_progress_bar);
        final Spinner spinner = findViewById(R.id.post_announce_spinner_group_id);
        final List<String> listGroupName = new ArrayList<>();
        final List<Group> listGroups = new ArrayList<>();
        SharedPreferences sp = getSharedPreferences(MemoryName.TEMP_DATA.toString(), Context.MODE_PRIVATE);

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONObject(sp.getString(NameOfResources.GROUP_LIST.toString(), "")).getJSONArray(JSONTags.GROUP_LIST.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = null;
        Group group = null;
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                jsonObject = jsonArray.getJSONObject(i);
                group = new Group(jsonObject);
                listGroups.add(group);
                listGroupName.add(group.getGroupName());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listGroupName);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                groupID = listGroups.get(position).getGroupId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Button btnSubmit = findViewById(R.id.post_announce_btn_submit);
        Button btnReset = findViewById(R.id.post_announce_btn_cancel);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog postAnnounceDialog = new AlertDialog.Builder(postAnnounceActivity)
                        .setTitle(getResources().getText(R.string.alert_post_announce))
                        .setMessage(getResources().getText(R.string.alert_post_announce_detail))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(getResources().getText(R.string.dialog_answer_yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                closeKeyBoard(v);
                                postAnnounce();
                            }
                        })
                        .setNegativeButton(getResources().getText(R.string.dialog_answer_no), null).show();
                postAnnounceDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(Color.GRAY);
                postAnnounceDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextHeader.setText(getResources().getText(R.string.default_resource), TextView.BufferType.EDITABLE);
                editTextContent.setText(getResources().getText(R.string.default_resource), TextView.BufferType.EDITABLE);
            }
        });
    }

    private void postAnnounce() {
        showProgress(true);

        new PostAnnounceTask().execute();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            postAnnounceForm.setVisibility(show ? View.GONE : View.VISIBLE);
            postAnnounceForm.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    postAnnounceForm.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            postAnnounceForm.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void closeKeyBoard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private class PostAnnounceTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            SharedPreferences sp = getSharedPreferences(MemoryName.TEMP_DATA.toString(), Context.MODE_PRIVATE);
            CustomConnection.makePOSTConnectionWithParameter(postAnnounceActivity,
                    CustomConnection.URLSuffix.POST_ANNOUNCE_POST,
                    NameOfResources.POST_ANNOUNCE_MESSAGE,
                    sp.getString(NameOfResources.USER_ID.toString(), ""),
                    editTextHeader.getText().toString(), editTextContent.getText().toString(),
                    groupID, null);

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (TextUtils.isEmpty(sp.getString(NameOfResources.POST_ANNOUNCE_MESSAGE.toString(), "")))
                return false;
            else
                return Boolean.valueOf(sp.getString(NameOfResources.POST_ANNOUNCE_MESSAGE.toString(), ""));
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            showProgress(false);
            if (aBoolean)
                Toast.makeText(postAnnounceActivity.getApplicationContext(),
                        getResources().getText(R.string.post_announce_success_message),
                        Toast.LENGTH_LONG).show();
            else
                Toast.makeText(postAnnounceActivity.getApplicationContext(),
                        getResources().getText(R.string.post_announce_fail_message),
                        Toast.LENGTH_LONG).show();
        }
    }
}
