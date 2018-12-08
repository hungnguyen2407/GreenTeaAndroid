package com.greentea.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.greentea.R;
import com.greentea.dao.CustomConnection;
import com.greentea.model.MemoryName;
import com.greentea.model.NameOfResources;

public class AnnounceDetailActivity extends AppCompatActivity {
    private SharedPreferences sp;
    private String userLevel;
    private Activity announceDetailActivity;
    private ScrollView scrollView3;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        announceDetailActivity = this;
        setContentView(R.layout.activity_announce_detail);
        Toolbar toolbar = findViewById(R.id.announce_detail_toolbar);
        sp = getSharedPreferences(MemoryName.TEMP_DATA.toString(), Context.MODE_PRIVATE);
        sp.getString(NameOfResources.USER_LEVEL.toString(), "");
        if (hasPermission())
            setSupportActionBar(toolbar);
        ImageButton btnBack = findViewById(R.id.announce_detail_back_btn);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        scrollView3 = findViewById(R.id.scrollView3);
        progressBar = findViewById(R.id.announce_detail_progress_bar);
        TextView tvHeader = findViewById(R.id.announce_detail_header);
        tvHeader.setText(sp.getString(NameOfResources.ANNOUNCE_HEADER.toString(), ""));
        TextView tvContent = findViewById(R.id.announce_detail_content);
        tvContent.setText(sp.getString(NameOfResources.ANNOUNCE_CONTENT.toString(), ""));
        TextView tvDate = findViewById(R.id.announce_detail_date);
        tvDate.setText(sp.getString(NameOfResources.ANNOUNCE_DATE.toString(), ""));
    }

    //User have permission to edit the announce
    private boolean hasPermission() {
        return Integer.valueOf(sp.getString(NameOfResources.USER_LEVEL.toString(), "")) < 4;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_announce_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_edit_announce:
                startActivity(new Intent(this, GroupJoinActivity.class));
                return true;
            case R.id.menu_action_delete_announce:
                AlertDialog confirmDialog = new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.delete_announce_confirm))
                        .setMessage(getString(R.string.delete_announce_confirm_quiz))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(getString(R.string.dialog_answer_yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                showProgress(true);

                                new DeleteAnnounceTask().execute();
                            }
                        })
                        .setNegativeButton(getString(R.string.dialog_answer_no), null).show();
                confirmDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(Color.GRAY);
                confirmDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            scrollView3.setVisibility(show ? View.GONE : View.VISIBLE);
            scrollView3.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    scrollView3.setVisibility(show ? View.GONE : View.VISIBLE);
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
            scrollView3.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private class DeleteAnnounceTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            CustomConnection.makeDELETEConnectionWithParameter(announceDetailActivity, CustomConnection.URLSuffix.DELETE_ANNOUNCE_DELETED, NameOfResources.DELETE_ANNOUNCE_MESSAGE, sp.getInt(NameOfResources.ANNOUNCE_ID.toString(), 0) + "");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            showProgress(false);
            if ("true".equalsIgnoreCase(sp.getString(NameOfResources.DELETE_ANNOUNCE_MESSAGE.toString(), ""))) {
                CustomConnection.makeGETConnectionWithParameter(announceDetailActivity,
                        CustomConnection.URLSuffix.GET_ANNOUNCE_GET_BY_USER_ID,
                        NameOfResources.ANNOUNCE_DATA, sp.getString(NameOfResources.USER_ID.toString(), ""));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Toast.makeText(announceDetailActivity.getApplicationContext(), announceDetailActivity.getText(R.string.announce_delete_success), Toast.LENGTH_LONG).show();
                Intent mainActivity = new Intent(announceDetailActivity, MainActivity.class);
                startActivity(mainActivity);
            } else
                Toast.makeText(announceDetailActivity.getApplicationContext(), announceDetailActivity.getText(R.string.announce_delete_fail), Toast.LENGTH_LONG).show();
        }
    }
}
