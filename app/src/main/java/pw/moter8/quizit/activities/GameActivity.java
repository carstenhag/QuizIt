package pw.moter8.quizit.activities;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nispok.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pw.moter8.quizit.MaterialDialogBuilder;
import pw.moter8.quizit.R;
import pw.moter8.quizit.gamemodes.SingleCategory;

import static pw.moter8.quizit.gamemodes.GameModeConstants.*;


public class GameActivity extends ActionBarActivity {

    protected String correctAnswer = "";
    private CountDownTimer mCountDownTimer;
    private ParseObject question;

    @InjectView(R.id.loadingLabel) TextView mLoadingLabel;
    @InjectView(R.id.questionLabel) TextView mQuestionLabel;
    @InjectView(R.id.button0) Button mButton0;
    @InjectView(R.id.button1) Button mButton1;
    @InjectView(R.id.button2) Button mButton2;
    @InjectView(R.id.button3) Button mButton3;
    @InjectView(R.id.buttonJoker) Button mButtonFiftyFity;
    @InjectView(R.id.progressBar) ProgressBar mProgressBar;
    @InjectView(R.id.textCountdown) TextView mTextCoundown;
    @InjectView(R.id.textStats) TextView mTextStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.inject(this);
        Prefs.initPrefs(this);

        final int questionCategory = 0;

        mProgressBar.setVisibility(View.VISIBLE);
        ParseQuery<ParseObject> questionQuery = new ParseQuery<ParseObject>(SingleCategory.CLASS_SINGLE_CATEGORY[questionCategory]);
        questionQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                mProgressBar.setVisibility(View.INVISIBLE);
                if (e == null) {
                    prepareJokerButtons(mButtonFiftyFity, JokerType.FIFTY_FITY);
                    mCountDownTimer.start();
                    mTextCoundown.setVisibility(View.VISIBLE);

                    // select a random question Object and use its stuff to feed stuff
                    Random randomGen = new Random();
                    int randomNumber = randomGen.nextInt(parseObjects.size());

                    question = parseObjects.get(randomNumber);

                    incrementTimesPlayed();

                    correctAnswer = question.getString(SingleCategory.KEY_ANSWER0);

                    String questionText = question.getString(SingleCategory.KEY_QUESTION);
                    String[] answers = {question.getString(SingleCategory.KEY_ANSWER0),
                            question.getString(SingleCategory.KEY_ANSWER1),
                            question.getString(SingleCategory.KEY_ANSWER2),
                            question.getString(SingleCategory.KEY_ANSWER3)};
                    Collections.shuffle(Arrays.asList(answers));

                    int percentage = Math.round(
                            (float)question.getInt(SingleCategory.KEY_TIMES_CORRECT)/
                            (float)question.getInt(SingleCategory.KEY_TIMES_PLAYED)*100);

                    updateViews(questionText, answers, percentage);

                } else {
                    MaterialDialogBuilder.build(GameActivity.this, MaterialDialogBuilder.DialogType.NETWORK_ERR);
                }
            }
        });

        newAnswerOnClickListener(mButton0);
        newAnswerOnClickListener(mButton1);
        newAnswerOnClickListener(mButton2);
        newAnswerOnClickListener(mButton3);

        newJokerOnClickListener(mButtonFiftyFity, JokerType.FIFTY_FITY);

        mCountDownTimer = new CountDownTimer(10300, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTextCoundown.setText(Math.round(millisUntilFinished / 1000) + " seconds");
            }

            @Override
            public void onFinish() {
                mTextCoundown.setText(0 + " seconds");
                Snackbar.with(getApplicationContext())
                        .text("You were too slow!")
                        .show(GameActivity.this);
            }
        };
    }

    private void incrementTimesPlayed() {
        question.increment(SingleCategory.KEY_TIMES_PLAYED);
        question.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                //noinspection StatementWithEmptyBody
                if (e == null) {
                }
            }
        });
    }

    private void incrementTimesCorrect() {
        question.increment(SingleCategory.KEY_TIMES_CORRECT);
        question.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                //noinspection StatementWithEmptyBody
                if (e == null) {
                }
            }
        });
    }

    private void prepareJokerButtons(final Button button, JokerType jokerType) {
        Prefs.putInt("fifty-fifty", 1);
        if (Prefs.getInt("fifty-fifty", 0) == 0) {
            mButtonFiftyFity.setEnabled(false);
        } else mButtonFiftyFity.setEnabled(true);
    }


    private void newAnswerOnClickListener(final Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button.getText().toString().equals(correctAnswer)) {
                    Snackbar.with(getApplicationContext())
                            .text("Answer is Correct!")
                            .actionLabel("Yay")
                            .actionColor(Color.parseColor("#FFC107"))
                            .show(GameActivity.this);
                    incrementTimesCorrect();
                    button.setEnabled(false);
                } else {
                    Snackbar.with(getApplicationContext())
                            .text("Answer is not Correct!")
                            .show(GameActivity.this);
                    mButtonFiftyFity.setEnabled(false);
                }
                mCountDownTimer.cancel();
                mTextCoundown.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void newJokerOnClickListener (final Button button, final JokerType jokerType) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (jokerType) {
                    case FIFTY_FITY: {

                        int n = 0;

                        Button[] buttons = {mButton0, mButton1, mButton2, mButton3 };
                        Collections.shuffle(Arrays.asList(buttons));

                        for (int i = 0; i < 2 || n < 2; i++) {
                            if (!buttons[i].getText().toString().equals(correctAnswer)) {
                                buttons[i].setVisibility(View.INVISIBLE);
                                n++;
                            }
                        }
                        button.setEnabled(false);
                        break;
                        }
                    case BONUS_TIME:{
                        break;
                    }
                    case BONUS_POINTS:{
                        break;
                    }
                }
            }
        });
    }

    private void updateViews(String questionText, String[] answers, int percentage) {

        // String[] answers is supposed to be shuffled already.
        mQuestionLabel.setText(questionText);
        mButton0.setText(answers[0]);
        mButton1.setText(answers[1]);
        mButton2.setText(answers[2]);
        mButton3.setText(answers[3]);
        mTextStats.setText("" + percentage + "% of the players got this right!");

        mLoadingLabel.setVisibility(View.INVISIBLE);
        mQuestionLabel.setVisibility(View.VISIBLE);
        mButton0.setVisibility(View.VISIBLE);
        mButton1.setVisibility(View.VISIBLE);
        mButton2.setVisibility(View.VISIBLE);
        mButton3.setVisibility(View.VISIBLE);
        mTextStats.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings: {
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
