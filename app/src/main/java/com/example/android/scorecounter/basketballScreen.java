package com.example.android.scorecounter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.concurrent.TimeUnit;

/*
    Basketball points counter with working countDown Timer
 */


public class basketballScreen extends Activity implements View.OnClickListener {
    private Button startCount, cancelButton;
    private ToggleButton togbtn;
    private TextView textViewTime;

    private boolean isStarted = false;
    private boolean isCanceled = false;
    private boolean isPaused = false;
    private long remainingTime = 600000;
    int partsPlayed = 0;
    String displayedTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basketballscreen);

        View Reset = findViewById(R.id.reset);
        Reset.setOnClickListener(this);
        View Back = findViewById(R.id.back);
        Back.setOnClickListener(this);
        textViewTime = (TextView) findViewById(R.id.time);
        startCount = (Button) findViewById(R.id.startButton);
        togbtn = (ToggleButton) findViewById(R.id.togBtn);
        cancelButton = (Button) findViewById(R.id.cancelBtn);

        cancelButton.setEnabled(false);
        togbtn.setEnabled(false);

        /*
        if clicked one of the countDown timer buttons
         */
        startCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStarted = true;
                startCount.setEnabled(false);
                cancelButton.setEnabled(true);
                togbtn.setEnabled(true);

                isPaused = false;
                isCanceled = false;
                Ticker();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCanceled = true;
                textViewTime.setText("10:00");
                startCount.setEnabled(true);
                togbtn.setEnabled(false);
                togbtn.setChecked(false);
                isPaused = isStarted = false;
                cancelButton.setEnabled(false);
                remainingTime = 600000;
            }
        });
        togbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (togbtn.isChecked()) {
                    isPaused = true;
                } else {
                    isPaused = false;
                    // starting onTick function displaying passing time of a match
                    Ticker();
                }
            }
        });
    }


    // called after countDown timer reaches 0
    private void checkIfEnd() {
        if (partsPlayed == 4) {
            displayWinnerWindow();
        }
        startCount.setEnabled(true);
        togbtn.setEnabled(false);
        togbtn.setChecked(false);
        cancelButton.setEnabled(false);
        showPartsPlayed();
    }

    // displaying parts played
    private void showPartsPlayed() {
        TextView t = (TextView) findViewById(R.id.partsPlayed);
        t.setText("Parts played: " + String.valueOf(partsPlayed));
    }

    // in case of rotating func saving scores and time played
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putInt("MyInt1", actualScore1);
        savedInstanceState.putInt("MyInt2", actualScore2);
        savedInstanceState.putBoolean("MyBoolean1", isCanceled);
        savedInstanceState.putBoolean("MyBoolean2", isPaused);
        savedInstanceState.putBoolean("MyBoolean3", isStarted);
        savedInstanceState.putLong("MyLong", remainingTime);
        savedInstanceState.putInt("MyInt3", partsPlayed);
        savedInstanceState.putString("MyString", displayedTime);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        actualScore1 = savedInstanceState.getInt("MyInt1");
        actualScore2 = savedInstanceState.getInt("MyInt2");
        isCanceled = savedInstanceState.getBoolean("MyBoolean1");
        isPaused = savedInstanceState.getBoolean("MyBoolean2");
        isStarted = savedInstanceState.getBoolean("MyBoolean3");
        remainingTime = savedInstanceState.getLong("MyLong");
        partsPlayed = savedInstanceState.getInt("MyInt3");
        displayedTime = savedInstanceState.getString("MyString");
        display1(actualScore1);
        display2(actualScore2);

        if (!isStarted) {
            togbtn.setEnabled(false);
            cancelButton.setEnabled(false);
            startCount.setEnabled(true);
            textViewTime.setText("10:00");
        } else {
            Ticker();
            startCount.setEnabled(false);
            togbtn.setEnabled(true);
            cancelButton.setEnabled(true);
            textViewTime.setText(displayedTime);
        }
        if (isPaused) {
            togbtn.isChecked();
        }
    }

    /*
    Adding points
     */
    int actualScore1 = 0;
    int actualScore2 = 0;

    public void Count1_1(View view) {
        actualScore1 += 1;
        display1(actualScore1);
    }

    public void Count2_1(View view) {
        actualScore1 += 2;
        display1(actualScore1);
    }

    public void Count3_1(View view) {
        actualScore1 += 3;
        display1(actualScore1);
    }

    public void Count1_2(View view) {
        actualScore2 += 1;
        display2(actualScore2);
    }

    public void Count2_2(View view) {
        actualScore2 += 2;
        display2(actualScore2);
    }

    public void Count3_2(View view) {
        actualScore2 += 3;
        display2(actualScore2);
    }

    // displaying points scored
    private void display1(int number) {
        TextView t = (TextView) findViewById(R.id.score_1);
        t.setText(String.valueOf(number));
    }

    private void display2(int number) {
        TextView t = (TextView) findViewById(R.id.score_2);
        t.setText(String.valueOf(number));
    }

    /*
    Lower buttons
     */
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset:
                display1(0);
                actualScore1 = 0;
                display2(0);
                actualScore2 = 0;
                partsPlayed=0;
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    // displaying window showing the winner at the end of 4th part
    private void displayWinnerWindow() {
        String Result = showWinner();
        Intent i = new Intent(this, WinnerWindow.class);
        i.putExtra("Results", Result);
        startActivity(i);
    }

    /*
    Finding winner
     */
    private String showWinner() {
        if (actualScore1 > actualScore2) {
            TextView editText = (EditText) findViewById(R.id.player_1);
            return editText.getText().toString() + " won";
        }
        if (actualScore1 == actualScore2) {
            return getString(R.string.show_Draw);
        }
        if (actualScore2 > actualScore1) {
            TextView editText = (EditText) findViewById(R.id.player_2);
            return editText.getText().toString() + " won";
        } else return "no answer";
    }

    // Ticker method counting passing time
    private void Ticker() {
        long millisInFuture;
        if (remainingTime != 0 || remainingTime != 600000) {
            millisInFuture = remainingTime;
        } else {
            millisInFuture = 600000;
        }
        long countDownInterval = 1000;
        new CountDownTimer(millisInFuture, countDownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (isPaused || isCanceled) {
                    cancel();
                } else {
                    displayedTime = String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                    textViewTime.setText(displayedTime);
                    remainingTime = millisUntilFinished;
                }
            }

            @Override
            public void onFinish() {
                textViewTime.setText("00:00");
                partsPlayed++;
                checkIfEnd();
            }
        }.start();
    }
}