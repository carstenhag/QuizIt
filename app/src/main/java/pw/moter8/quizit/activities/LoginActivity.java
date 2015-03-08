package pw.moter8.quizit.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pw.moter8.quizit.AppHelper;
import pw.moter8.quizit.MaterialDialogBuilder;
import pw.moter8.quizit.R;


public class LoginActivity extends ActionBarActivity {

    public static final int MIN_PASSWORD_LENGTH = 7;  // Define a constant length requirement

    @InjectView(R.id.usernameField) MaterialEditText mUsernameField;
    @InjectView(R.id.passwordField) MaterialEditText mPasswordField;
    @InjectView(R.id.loginButton) Button mLoginButton;
    @InjectView(R.id.signupButton) Button mSignupButton;
    @InjectView(R.id.progressBar) ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        if (ParseUser.getCurrentUser() != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        final InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        // Set its minimum password length to a constant
        mPasswordField.setMinCharacters(MIN_PASSWORD_LENGTH);

        // Logs in on button press
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn(inputManager);
            }
        });

        // Starts the Signup activity with the current username
        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                intent.putExtra("username", mUsernameField.getText().toString().trim());
                startActivity(intent);
            }
        });

        // Logs in on tapping Done
        mPasswordField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    logIn(inputManager);
                    return true;
                }
                return false;
            }
        });


    }

    private void logIn(InputMethodManager inputManager) {
        try {
            inputManager.hideSoftInputFromWindow(mLoginButton.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            Log.e("KeyBoardUtil", e.toString(), e);
        }

        String username = mUsernameField.getText().toString().trim();
        String password = mPasswordField.getText().toString().trim();

        if (AppHelper.isNetworkAvail(LoginActivity.this)) {

            if (username.length() >= 4 && password.length() >= MIN_PASSWORD_LENGTH) {
                mProgressBar.setVisibility(View.VISIBLE);
                ParseUser.logInInBackground(username, password, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        if (e == null) {
                            Crashlytics.setString("userStatus", "Logged in successfully");
                            //new SnackBar.Builder(LoginActivity.this)
                            //        .withMessageId(R.string.toast_login_successful)
                            //        .show();  // does not survive after starting another activity, so it's basically useless

                            Toast.makeText(LoginActivity.this, getString(R.string.toast_login_successful), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Crashlytics.setString("lastError", "ParseException, did not log in");
                            MaterialDialogBuilder.build(LoginActivity.this, MaterialDialogBuilder.DialogType.LOGIN_ERR);
                        }
                    }
                });

            } else if (password.length() < MIN_PASSWORD_LENGTH) {
                MaterialDialogBuilder.build(LoginActivity.this, MaterialDialogBuilder.DialogType.INPUT_PW_ERR);
            } else if (username.length() < 4) {
                MaterialDialogBuilder.build(LoginActivity.this, MaterialDialogBuilder.DialogType.INPUT_USERNAME_ERR);
            }
        } else {
            MaterialDialogBuilder.build(LoginActivity.this, MaterialDialogBuilder.DialogType.NETWORK_UNAVAIL_ERR);
        }
    }

}
