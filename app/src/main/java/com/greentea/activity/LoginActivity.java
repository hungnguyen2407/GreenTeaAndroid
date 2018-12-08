package com.greentea.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.greentea.R;
import com.greentea.dao.CustomConnection;
import com.greentea.model.MemoryName;
import com.greentea.model.NameOfResources;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {
    private SharedPreferences sp;
    private static final int REQUEST_READ_CONTACTS = 0;
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText etID;
    private EditText etPass;
    private View mProgressView;
    private View mLoginFormView;
    private Activity loginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginActivity = this;
        setContentView(R.layout.activity_login);
        // Set up the login form.
        etID = findViewById(R.id.login_et_id);
        etID.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        sp = getSharedPreferences(MemoryName.TEMP_DATA.toString(), Context.MODE_PRIVATE);
        etPass = findViewById(R.id.login_et_pass);
        etPass.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        etPass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button loginSubmitBtn = findViewById(R.id.login_submit_btn);
        loginSubmitBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        Button forgetPassBtn = findViewById(R.id.login_forget_pass_btn);
        forgetPassBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(loginActivity, ForgetPassActivity.class));
            }
        });
    }

    private void closeKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	        assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(etID, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }
        closeKeyBoard();
        // Reset errors.
        etID.setError(null);
        etPass.setError(null);
        // Store values at the time of the login attempt.
        String id = etID.getText().toString();
        String password = etPass.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            etPass.setError(getString(R.string.error_field_required));
            focusView = etPass;
            cancel = true;
        }

        if (TextUtils.isEmpty(id)) {
            etID.setError(getString(R.string.error_field_required));
            focusView = etID;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(id, password);
            mAuthTask.execute((Void) null);
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
	    int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

	    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
	    mLoginFormView.animate().setDuration(shortAnimTime).alpha(
			    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
		    @Override
		    public void onAnimationEnd(Animator animation) {
			    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		    }
	    });

	    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
	    mProgressView.animate().setDuration(shortAnimTime).alpha(
			    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
		    @Override
		    public void onAnimationEnd(Animator animation) {
			    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
		    }
	    });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    @SuppressLint("StaticFieldLeak")
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String id;
        private final String password;

        UserLoginTask(String id, String password) {
            this.id = id;
            this.password = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            if (CustomConnection.makeGETConnectionWithParameter(loginActivity,
                    CustomConnection.URLSuffix.GET_USER_LOGIN,
                    NameOfResources.LOGIN_SUCCESS,
                    id, password)) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (Boolean.valueOf(sp.getString(NameOfResources.LOGIN_SUCCESS.toString(), "false"))) {
                    CustomConnection.makeGETConnectionWithParameter(loginActivity,
                            CustomConnection.URLSuffix.GET_USER_INFO,
                            NameOfResources.USER_INFO, id);
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    CustomConnection.makeGETConnectionWithParameter(loginActivity,
                            CustomConnection.URLSuffix.GET_ANNOUNCE_GET_BY_USER_ID,
                            NameOfResources.ANNOUNCE_DATA, id);
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;
                } else
                    return false;
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (Boolean.valueOf(sp.getString(NameOfResources.LOGIN_SUCCESS.toString(), "false"))) {
                Intent mainActivityIntent = new Intent(loginActivity, MainActivity.class);
                mainActivityIntent.putExtra(NameOfResources.USER_INFO.toString(), sp.getString(NameOfResources.USER_INFO.toString(), ""));
                mainActivityIntent.putExtra(NameOfResources.ANNOUNCE_DATA.toString(), sp.getString(NameOfResources.ANNOUNCE_DATA.toString(), ""));
                showProgress(false);
                startActivity(mainActivityIntent);
            } else {
                Toast.makeText(LoginActivity.this, "Sai thông tin đăng nhập", Toast.LENGTH_SHORT).show();
                mAuthTask = null;
                showProgress(false);
            }

//            if (success) {
//                finish();
//            } else {
//                Toast.makeText(LoginActivity.this, "Sai thông tin đăng nhập", Toast.LENGTH_SHORT).show();
//                mAuthTask = null;
//                showProgress(false);
//            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    @Override
    public void onBackPressed() {

    }
}

