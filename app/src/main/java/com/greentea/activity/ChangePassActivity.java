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
import android.text.TextUtils;
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

public class ChangePassActivity extends AppCompatActivity {
    private Activity changePassActivity;
    private String oldPass, newPass, newPassRepeat;
    private EditText etOldPass, etNewPass, etNewPassRepeat;
    private String userID;
    private ConstraintLayout changePassForm;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        changePassActivity = this;
        changePassForm = findViewById(R.id.change_pass_form);
        progressBar = findViewById(R.id.change_pass_progress_bar);
        SharedPreferences sp = getSharedPreferences(MemoryName.TEMP_DATA.toString(), Context.MODE_PRIVATE);
        userID = sp.getString(NameOfResources.USER_ID.toString(),
                getResources().getText(R.string.default_resource).toString());
        changPassAcitivityHander();
    }

    private void changPassAcitivityHander() {
        etOldPass = findViewById(R.id.change_pass_et_old_pass);
        etNewPass = findViewById(R.id.change_pass_et_new_pass);
        etNewPassRepeat = findViewById(R.id.change_pass_et_new_pass_repeat);
        oldPass = etOldPass.getText().toString();
        newPass = etNewPass.getText().toString();
        newPassRepeat = etNewPassRepeat.getText().toString();
        Button btnSubmit = findViewById(R.id.change_pass_submit_btn);
        ImageButton btnReturn = findViewById(R.id.change_pass_back_btn);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog logoutDialog = new AlertDialog.Builder(changePassActivity)
                        .setTitle(getResources().getText(R.string.alert_change_pass))
                        .setMessage(getResources().getText(R.string.alert_change_pass_detail))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(getResources().getText(R.string.dialog_answer_yes), new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                showProgress(true);
                                checkValid();
                            }
                        })
                        .setNegativeButton(getResources().getText(R.string.dialog_answer_no), null).show();
                logoutDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(Color.GRAY);
                logoutDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void checkValid() {
        closeKeyBoard();
        etOldPass.setError(null);
        etNewPass.setError(null);
        etNewPassRepeat.setError(null);
        oldPass = etOldPass.getText().toString();
        newPass = etNewPass.getText().toString();
        newPassRepeat = etNewPassRepeat.getText().toString();
        if (TextUtils.isEmpty(oldPass)) {
            etOldPass.setError(getResources().getString(R.string.error_field_required));
        }
        if (TextUtils.isEmpty(newPassRepeat)) {
            etNewPassRepeat.setError(getResources().getString(R.string.error_field_required));
        }

        if (TextUtils.isEmpty(newPass)) {
            etNewPass.setError(getResources().getString(R.string.error_field_required));
        } else if (newPass.length() <= 4) {
            etNewPass.setError(getResources().getString(R.string.error_invalid_password));
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_invalid_password), Toast.LENGTH_LONG).show();
        } else if (!newPass.equals(newPassRepeat)) {
            etNewPassRepeat.setError(getResources().getString(R.string.error_not_match_password));
        } else {
            new ChangePassTask().execute();
        }
    }

    private void closeKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            changePassForm.setVisibility(show ? View.GONE : View.VISIBLE);
            changePassForm.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    changePassForm.setVisibility(show ? View.GONE : View.VISIBLE);
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
            changePassForm.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private class ChangePassTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {

            SharedPreferences sp = getSharedPreferences(MemoryName.TEMP_DATA.toString(), Context.MODE_PRIVATE);
            CustomConnection.makePUTConnectionWithParameter(changePassActivity,
                    CustomConnection.URLSuffix.PUT_PASSWORD_CHANGE,
                    NameOfResources.CHANGE_PASS_MESSAGE,
                    userID,
                    oldPass, newPass);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return Boolean.valueOf(sp.getString(NameOfResources.CHANGE_PASS_MESSAGE.toString(), "false"));
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            showProgress(false);
            if (aBoolean)
                Toast.makeText(getApplicationContext(), getResources().getText(R.string.change_pass_success_message), Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(), getResources().getText(R.string.change_pass_fail_message), Toast.LENGTH_LONG).show();


        }
    }
}
