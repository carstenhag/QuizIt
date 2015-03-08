package pw.moter8.quizit.activities;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pw.moter8.quizit.MaterialDialogBuilder;
import pw.moter8.quizit.AppHelper;
import pw.moter8.quizit.R;
import pw.moter8.quizit.gamemodes.SingleCategory;

public class PostQuestionActivity extends ActionBarActivity {

    @InjectView(R.id.buttonPost) Button mButtonPost;
    @InjectView(R.id.question) MaterialEditText mQuestionText;
    @InjectView(R.id.answer0) MaterialEditText mAnswer0;
    @InjectView(R.id.answer1) MaterialEditText mAnswer1;
    @InjectView(R.id.answer2) MaterialEditText mAnswer2;
    @InjectView(R.id.answer3) MaterialEditText mAnswer3;
    @InjectView(R.id.progressBar) ProgressBar mProgressBar;

    protected ParseUser mCurrentUser;
    private ParseObject mQuestions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_question);
        ButterKnife.inject(this);

        mCurrentUser = ParseUser.getCurrentUser();


        mButtonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verifyInput()) {

                    char[] questionChars = getButtonString(mQuestionText).toCharArray(); // converts text to char array
                    if (getButtonString(mQuestionText).toCharArray()[questionChars.length - 1] != '?') { // checks if last char is ?
                        mQuestionText.setText(String.valueOf(questionChars) + "?"); // appends ? to array and sets new value
                    }
                    postQuestion(0);

                } else {
                    MaterialDialogBuilder.build(PostQuestionActivity.this, MaterialDialogBuilder.DialogType.INPUT_ERR);
                }
            }
        });

    }

    private void postQuestion(int questionCategory) {
        mProgressBar.setVisibility(View.VISIBLE);
        mButtonPost.setEnabled(false);

        mQuestions = new ParseObject(SingleCategory.CLASS_SINGLE_CATEGORY[questionCategory]);

        mQuestions.put(SingleCategory.KEY_USERNAME, mCurrentUser.getUsername());
        mQuestions.put(SingleCategory.KEY_QUESTION, getButtonString(mQuestionText));
        mQuestions.put(SingleCategory.KEY_ANSWER0, getButtonString(mAnswer0));
        mQuestions.put(SingleCategory.KEY_ANSWER1, getButtonString(mAnswer1));
        mQuestions.put(SingleCategory.KEY_ANSWER2, getButtonString(mAnswer2));
        mQuestions.put(SingleCategory.KEY_ANSWER3, getButtonString(mAnswer3));
        mQuestions.put(SingleCategory.KEY_TIMES_PLAYED, 0);
        mQuestions.put(SingleCategory.KEY_TIMES_CORRECT, 0);

        mQuestions.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                mProgressBar.setVisibility(View.INVISIBLE);
                mButtonPost.setEnabled(true);
                if (e == null) {
                    Snackbar.with(getApplicationContext())
                            .text("The Question has been submitted")
                            .actionColor(Color.parseColor("#FFC107"))
                            .actionLabel("Undo")
                            .actionListener(new ActionClickListener() {
                                @Override
                                public void onActionClicked(Snackbar snackbar) {
                                    mProgressBar.setVisibility(View.VISIBLE);
                                    mQuestions.deleteInBackground(new DeleteCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            mProgressBar.setVisibility(View.INVISIBLE);
                                            if (e == null) {
                                                Snackbar.with(getApplicationContext())
                                                        .text("The Question has been deleted")
                                                        .show(PostQuestionActivity.this);
                                            }
                                        }
                                    });
                                }
                            })
                            .show(PostQuestionActivity.this);

                } else {
                    MaterialDialogBuilder.build(PostQuestionActivity.this, MaterialDialogBuilder.DialogType.NETWORK_ERR);
                }
            }
        });
    }

    public static String getButtonString(MaterialEditText materialEditText) {

        char[] charArray = materialEditText.getText().toString().trim().toCharArray(); // trims and converts to char array
        charArray[0] = Character.toUpperCase(charArray[0]); // uppercases the first char

        return String.valueOf(charArray);
    }

    private boolean verifyInput() {


        String[] questionWords = getButtonString(mQuestionText).split(" ");

         return (getButtonString(mQuestionText).length() > 8 &&
                 getButtonString(mAnswer0).length() >= 4 &&
                 getButtonString(mAnswer1).length() >= 4 &&
                 getButtonString(mAnswer2).length() >= 4 &&
                 getButtonString(mAnswer3).length() >= 4 &&
                 questionWords.length >= 3);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post_question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
