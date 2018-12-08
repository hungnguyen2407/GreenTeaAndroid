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
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.greentea.R;
import com.greentea.dao.CustomConnection;
import com.greentea.model.MemoryName;
import com.greentea.model.NameOfResources;

public class GroupJoinActivity extends AppCompatActivity {
    private Activity groupJoinActivity;
    private ConstraintLayout joinGroupForm;
    private ProgressBar progressBar;
    private SharedPreferences sp;
    private String userID, groupID;
    private EditText etGroupID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_join);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        groupJoinActivity = this;
        ImageButton returnBtn = findViewById(R.id.group_join_back_btn);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Button submitBtn = findViewById(R.id.group_join_submit_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog joinGroupDialog = new AlertDialog.Builder(groupJoinActivity)
                        .setTitle(getResources().getText(R.string.alert_group_join))
                        .setMessage(getResources().getText(R.string.alert_group_join_detail))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(getResources().getText(R.string.dialog_answer_yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                closeKeyBoard(v);
                                joinGroup();
                            }
                        })
                        .setNegativeButton(getResources().getText(R.string.dialog_answer_no), null).show();
                joinGroupDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(Color.GRAY);
                joinGroupDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        });


        joinGroupForm = findViewById(R.id.group_join_form);
        progressBar = findViewById(R.id.group_join_progress_bar);
        sp = getSharedPreferences(MemoryName.TEMP_DATA.toString(), Context.MODE_PRIVATE);
        etGroupID = findViewById(R.id.group_join_et_group_id);
    }

    private void joinGroup() {
        showProgress(true);
        userID = sp.getString(NameOfResources.USER_ID.toString(),
                getResources().getText(R.string.default_resource).toString());
        groupID = etGroupID.getText().toString();
        new JoinGroupTask().execute();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            joinGroupForm.setVisibility(show ? View.GONE : View.VISIBLE);
            joinGroupForm.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    joinGroupForm.setVisibility(show ? View.GONE : View.VISIBLE);
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
            joinGroupForm.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void closeKeyBoard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private class JoinGroupTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            CustomConnection.makePOSTConnectionWithParameter(groupJoinActivity,
                    CustomConnection.URLSuffix.POST_JOIN_GROUP,
                    NameOfResources.GROUP_JOIN_MESSAGE,
                    groupID, userID);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d("resource", sp.getString(NameOfResources.GROUP_JOIN_MESSAGE.toString(),
                    getResources().getText(R.string.default_resource).toString()));
            if (TextUtils.isEmpty(sp.getString(NameOfResources.GROUP_JOIN_MESSAGE.toString(),
                    getResources().getText(R.string.default_resource).toString())))
                return false;
            else return Boolean.valueOf(sp.getString(NameOfResources.GROUP_JOIN_MESSAGE.toString(),
                    getResources().getText(R.string.default_resource).toString()));
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            showProgress(false);
            if (aBoolean)
                Toast.makeText(groupJoinActivity.getApplicationContext(), getResources().getText(R.string.group_join_success_message), Toast.LENGTH_LONG).show();
            else
                Toast.makeText(groupJoinActivity.getApplicationContext(), getResources().getText(R.string.group_join_fail_message), Toast.LENGTH_LONG).show();
        }
    }
}
