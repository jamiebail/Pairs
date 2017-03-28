package com.example.jamie.puzzler.Models;

import android.app.Activity;
import android.os.AsyncTask;

import com.example.jamie.puzzler.Fragments.PuzzleInterfaceFragment;
import com.example.jamie.puzzler.Logic.UserInterfaceLogic;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.jamie.puzzler.Activities.PuzzlesActivity.CurrentGameManager;

/**
 * Created by Jamie on 09/03/2017.
 */

public class Stopwatch extends AsyncTask<Integer, Integer, Void > {

    private Activity activity;
    int savedTime;

    public Stopwatch(Activity activityIn){
        activity = activityIn;
        puzzleFragment = (PuzzleInterfaceFragment) new UserInterfaceLogic(activity).GetFragment("puzzleInterface");
    }

    int minute = 0;
     PuzzleInterfaceFragment puzzleFragment;

    boolean checkIfCancelled(){
        if(isCancelled()){
            return true;
        }
        return false;
    }
    @Override
    protected Void doInBackground(Integer... params) {
        Timer timer = new Timer();
        //Declare the timer
        final Timer myTimer = new Timer();
        if(params.length > 0) {
            savedTime = params[0];
        }


        //Set the schedule function and rate
        myTimer.scheduleAtFixedRate(new TimerTask() {

                                        Integer i = 0;
                                        @Override
                                        public void run() {
                                            if(checkIfCancelled()){
                                                myTimer.cancel();
                                                myTimer.purge();
                                                this.cancel();
                                                return;
                                            }
                                            if(savedTime != 0){
                                                i = savedTime;
                                                publishProgress(i);
                                                savedTime = 0;
                                            }
                                            else
                                                i++;

                                            publishProgress(i);
                                        }
                                    },
                //set the amount of time in milliseconds before first execution
                0,
                //Set the amount of time between each execution (in milliseconds)
                1000);

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        int seconds = values[0];
        CurrentGameManager.GameTime = seconds;
        String text = "";
        // If gametime has persisted from previous orientation (Time is in total seconds, much parse into minutes/secs)
        if(seconds > 60){
            minute = (seconds% 3600) / 60;
        }
        if(seconds % 60 == 0){
            minute++;
        }
        seconds -= minute * 60;
        if(seconds < 10)
            text =  minute + ":0" + seconds;
        else
        text =  minute + ":" + seconds;

        puzzleFragment.SetTimer(text);
    }

    @Override
    protected void onPostExecute(Void aBoolean) {
        super.onPostExecute(aBoolean);
    }
}
