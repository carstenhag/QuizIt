package pw.moter8.quizit.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.crashlytics.android.Crashlytics;
import com.parse.ParseUser;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.fabric.sdk.android.Fabric;
import pw.moter8.quizit.R;
import pw.moter8.quizit.gamemodes.GameModeConstants;


public class MainActivity extends ActionBarActivity {

    protected ParseUser mCurrentUser;

    @InjectView(R.id.buttonSingleCategory) Button mButtonSingleCategory;
    @InjectView(R.id.buttonPost) Button mButtonPostQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        mCurrentUser = ParseUser.getCurrentUser();

        if (mCurrentUser == null) {
            Crashlytics.setString("userStatus", "Not logged in");
            navigateToLogin();  // debug MainActivity
        } else {
            Crashlytics.setString("userStatus", "Logged in as " + mCurrentUser.getUsername());
        }

        mButtonSingleCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGameActivity(GameModeConstants.GameModes.SINGLE_CATEGORY);
            }
        });

        mButtonPostQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PostQuestionActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.action_logout: {
                ParseUser.logOut();
                navigateToLogin();
            }
            case R.id.action_settings: {
                return true;
            }

        }

        return super.onOptionsItemSelected(item);
    }

    private void startGameActivity(GameModeConstants.GameModes gameMode) {

        Intent intent;

        switch (gameMode) {
            default: {
                intent = new Intent();
            }
            case SINGLE_CATEGORY: {
                intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra("questionCategory", 0);
                break;
            }
            case MULTIPLE_CATEGORY: {
                intent = new Intent(MainActivity.this, LoginActivity.class);
                break;
            }
            case V_SINGLE_CATEGORY: {
                intent = new Intent(MainActivity.this, LoginActivity.class);
                break;
            }
            case V_MULTIPLE_CETAGORY: {
                intent = new Intent(MainActivity.this, LoginActivity.class);
                break;
            }
        }

        startActivity(intent);
    }

    private void navigateToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
