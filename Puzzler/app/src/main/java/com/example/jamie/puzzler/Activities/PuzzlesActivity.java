package com.example.jamie.puzzler.Activities;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jamie.puzzler.Fragments.PuzzleInterfaceFragment;
import com.example.jamie.puzzler.Fragments.PuzzleListFragment;
import com.example.jamie.puzzler.Logic.Getters.GetPuzzle;
import com.example.jamie.puzzler.Logic.Getters.IGetter;
import com.example.jamie.puzzler.Logic.Getters.InternalFileGetter;
import com.example.jamie.puzzler.Logic.Helpers;
import com.example.jamie.puzzler.Logic.IOLogic;
import com.example.jamie.puzzler.Logic.UserInterfaceLogic;
import com.example.jamie.puzzler.Logic.PuzzleLogic;
import com.example.jamie.puzzler.Logic.Setters.ISetter;
import com.example.jamie.puzzler.Logic.Setters.InternalFileSetter;
import com.example.jamie.puzzler.Models.GameManager;
import com.example.jamie.puzzler.Models.IOResponse;
import com.example.jamie.puzzler.Models.Puzzle;
import com.example.jamie.puzzler.Models.PuzzleListViewModel;
import com.example.jamie.puzzler.Models.Stopwatch;
import com.example.jamie.puzzler.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class PuzzlesActivity extends AppCompatActivity {

    public static String puzzleName;
    public static List<Integer> puzzleSequence;
    PuzzleLogic _logic = new PuzzleLogic(this);
    public static Puzzle CurrentPuzzle;
    public static GameManager CurrentGameManager;
    public static boolean match;
    IGetter _getter = new InternalFileGetter(this);
    ISetter _setter = new InternalFileSetter(this);
    IOLogic IOLogic = new IOLogic(this, _setter, _getter);
    UserInterfaceLogic UILogic = new UserInterfaceLogic(this);
    public static TextView GameTextView;
    Activity activity = this;
    public static String GameText = "";
    public static String GameColour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UILogic.HideStatusBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puzzle_activity);
        CurrentPuzzle = new Puzzle();
        CurrentGameManager = new GameManager(this);

        PuzzleInterfaceFragment f = (PuzzleInterfaceFragment)UILogic.GetFragment("puzzleInterface");
        if(f != null)
            f.RefreshPuzzleGrid();

        Intent intent = getIntent();
        CurrentPuzzle.Name = intent.getExtras().getString("selectedPuzzle");
        JSONObject localPuzzle;
        CurrentPuzzle.IsLocal = false;


        try {
            IOResponse response = IOLogic.CheckIfSaved(CurrentPuzzle.Name);
            if(!response.Success) {
                // Set static flag that a later method will use to save the puzzle locally.
                Helpers.ToBeSaved = true;
                new GetPuzzle(this, new InternalFileSetter(this)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get(500, TimeUnit.MILLISECONDS);
            }
            else{
                CurrentPuzzle.IsLocal = true;
                localPuzzle = new JSONObject(response.object.toString());
                Log.d("GetPuzzle() ", CurrentPuzzle.Name + " loaded from Internal Storage");
                new GetPuzzle(this, new InternalFileSetter(this)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, localPuzzle);

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    @Override
    protected void onResume() {
        super.onResume();
        UILogic.InitialisePuzzleInterfaceFragment();
        if(getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE)
            UILogic.InitialisePuzzleListFragment();
        IOLogic.LoadPuzzles();
        if(CurrentGameManager.isPlaying) {
            if (CurrentGameManager.GameTime != 0) {
                CurrentGameManager.Stopwatch = new Stopwatch(activity);
                CurrentGameManager.Stopwatch.execute(CurrentGameManager.GameTime);
            }
            if (CurrentGameManager.Timer != null)
                CurrentGameManager.Timer.start();

        }
        else{
            if (CurrentGameManager.GameTime != 0) {
                CurrentGameManager.Stopwatch = new Stopwatch(activity);
                PuzzleInterfaceFragment fragment  =(PuzzleInterfaceFragment)UILogic.GetFragment("puzzleInterface");
                fragment.SetTimer(String.valueOf(CurrentGameManager.GameTime));
                CurrentGameManager.Stopwatch.cancel(true);}
        }

    }

    // onPostResume assures that the UI has been drawn, calling below code in onResume would return null references to UI elements.
    @Override
    protected void onPostResume() {
        super.onPostResume();

        LinearLayout layoutBar = (LinearLayout)findViewById(R.id.notifierBar);
        if(UILogic.GetOrientation() == Configuration.ORIENTATION_PORTRAIT){
            GameTextView = (TextView)findViewById(R.id.textView2);
            if(GameText != "") {
                if (GameColour != null)
                    if (CurrentGameManager.isPlaying) {
                        layoutBar.setBackgroundColor(Color.parseColor(GameColour));
                        GameTextView.setText(GameText);
                    }
            }

        }
        else{
            GameTextView = (TextView)findViewById(R.id.textView3);
            if(GameText != "")
                if(GameColour != null){
                    if (CurrentGameManager.isPlaying) {
                        GameTextView.setBackgroundColor(Color.parseColor(GameColour));
                        GameTextView.setText(GameText);
                    }
                }
        }


    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore state members from saved instance
        CurrentGameManager.isPlaying = savedInstanceState.getBoolean("isPlaying");
        CurrentGameManager.Score = savedInstanceState.getInt("GameScore");
        CurrentGameManager.GameTime = savedInstanceState.getInt("GameTime");
        ArrayList<Integer> completeMatches = savedInstanceState.getIntegerArrayList("CompleteMatches");
        if(completeMatches != null)
         CurrentGameManager.CompleteMatches = completeMatches;

        String powerupType = savedInstanceState.getString("Powerup");
        if(powerupType != "null")
            CurrentGameManager.PowerupType = Helpers.SpecialTypes.valueOf(powerupType);
        GameText  = savedInstanceState.getString("GameText");
        CurrentGameManager.PowerupTimeRemaining = savedInstanceState.getInt("PowerupTime");
        CurrentGameManager.Timer = _logic.InitialiseTimer(CurrentGameManager.PowerupTimeRemaining, 1);
        GameColour = savedInstanceState.getString("Color");
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("GameScore", CurrentGameManager.Score);
        outState.putInt("GameTime", CurrentGameManager.GameTime);
        outState.putString("Powerup", String.valueOf(CurrentGameManager.PowerupType));
        outState.putInt("PowerupTime", CurrentGameManager.PowerupTimeRemaining);
        outState.putIntegerArrayList("CompleteMatches",CurrentGameManager.CompleteMatches);
        outState.putBoolean("isPlaying", CurrentGameManager.isPlaying);
        TextView gameText = null;
         outState.putString("GameText", GameText);
        outState.putString("Color", GameColour);
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if(CurrentGameManager.Stopwatch != null)
            CurrentGameManager.Stopwatch.cancel(true);
        if(CurrentGameManager.Timer != null)
            CurrentGameManager.Timer.cancel();
    }
}







