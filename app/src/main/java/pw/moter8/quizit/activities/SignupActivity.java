package pw.moter8.quizit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pw.moter8.quizit.AppHelper;
import pw.moter8.quizit.MaterialDialogBuilder;
import pw.moter8.quizit.R;


public class SignupActivity extends ActionBarActivity {

    public static final int MIN_PASSWORD_LENGTH = 7;

    @InjectView(R.id.usernameField) MaterialEditText mUsernameField;
    @InjectView(R.id.passwordField) MaterialEditText mPasswordField;
    @InjectView(R.id.emailField) MaterialEditText mEmailField;
    @InjectView(R.id.signupButton) Button mSignupButton;
    @InjectView(R.id.progressBar) ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this);

        //final InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        Intent intent = getIntent();
        String mUsername = intent.getStringExtra("username");

        mPasswordField.setMinCharacters(MIN_PASSWORD_LENGTH);
        if (mUsername.length() > 0) {
            mUsernameField.setText(mUsername);
        }

        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        mEmailField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    signUp();
                    return true;
                }
                return false;
            }
        });

    }

    // inputManager is not needed as one can't tap on the button anyway
    private void signUp() {
        String username = mUsernameField.getText().toString().trim();
        String password = mPasswordField.getText().toString().trim();
        String email = mEmailField.getText().toString().trim();

        if (AppHelper.isNetworkAvail(this)) {
            if (username.length() >= 4 && password.length() >= MIN_PASSWORD_LENGTH && email.length() >= 5) {
                mProgressBar.setVisibility(View.VISIBLE);

                ParseUser newUser = new ParseUser();
                newUser.setUsername(username);
                newUser.setPassword(password);
                newUser.setEmail(email);

                newUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            mProgressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(SignupActivity.this, getString(R.string.toast_signup_successful), Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            mProgressBar.setVisibility(View.INVISIBLE);
                            MaterialDialogBuilder.build(SignupActivity.this, MaterialDialogBuilder.DialogType.NETWORK_ERR);
                        }

                    }
                });

            } else if (password.length() < MIN_PASSWORD_LENGTH) {
                MaterialDialogBuilder.build(SignupActivity.this, MaterialDialogBuilder.DialogType.INPUT_PW_ERR);
            } else if (username.length() < 4) {
                MaterialDialogBuilder.build(SignupActivity.this, MaterialDialogBuilder.DialogType.INPUT_USERNAME_ERR);
            } else if (email.length() < 4) {
                MaterialDialogBuilder.build(SignupActivity.this, MaterialDialogBuilder.DialogType.INPUT_EMAIL_ERR);
            }
        } else {
            MaterialDialogBuilder.build(SignupActivity.this, MaterialDialogBuilder.DialogType.NETWORK_UNAVAIL_ERR);
        }
    }

}
