package com.example.jamie.puzzler.Models;

import android.app.Activity;
import android.os.CountDownTimer;

import com.example.jamie.puzzler.Fragments.PuzzleInterfaceFragment;
import com.example.jamie.puzzler.Logic.Helpers;
import com.example.jamie.puzzler.Logic.UserInterfaceLogic;
import com.example.jamie.puzzler.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.jamie.puzzler.Logic.Helpers.SpecialTypes.FOURTIMES;
import static com.example.jamie.puzzler.Logic.Helpers.SpecialTypes.TWOTIMES;

/**
 * Created by Jamie on 10/03/2017.
 */

public class GameManager {
    public Date StartTime;
    public Date EndTime;
    public int PowerupTimeRemaining;
    public Helpers.SpecialTypes PowerupType;
    public int Score;
    public int GameTime;
    public Activity activity;
    public CountDownTimer Timer;
    public ArrayList<Integer> CompleteMatches = new ArrayList<>();
    public Stopwatch Stopwatch;
    public boolean isPlaying;
    public boolean isOffline;

    public GameManager(Activity activityIn){
        activity = activityIn;
    }

    public void IncreaseScore(int scoreIn){
        if(PowerupTimeRemaining > 0)
            switch(PowerupType){
                case TWOTIMES:
                    Score += scoreIn * 2;
                break;
                case FOURTIMES:
                    Score += scoreIn * 4;
                break;
                default:
                    Score += scoreIn;
            }
        else
            Score += scoreIn;

        PuzzleInterfaceFragment fragment = (PuzzleInterfaceFragment) new UserInterfaceLogic(activity).GetFragment("puzzleInterface");
        fragment.SetScoreText(String.valueOf(Score));
    }

    public void SetMultiplier(Helpers.SpecialTypes typeIn){
        PowerupType = typeIn;

    }

    public void Start(){
        StartTime = new Date();
    }

    public long End(){
        EndTime = new Date();
        long playTime = StartTime.getTime() - EndTime.getTime();
        return playTime;
    }

}
