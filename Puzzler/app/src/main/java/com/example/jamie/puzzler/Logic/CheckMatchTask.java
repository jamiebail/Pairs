package com.example.jamie.puzzler.Logic;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.Image;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jamie.puzzler.Activities.PuzzlesActivity;
import com.example.jamie.puzzler.Fragments.PuzzleInterfaceFragment;
import com.example.jamie.puzzler.Models.GameManager;
import com.example.jamie.puzzler.Models.PuzzleItem;
import com.example.jamie.puzzler.R;

import org.w3c.dom.Text;

import java.lang.ref.WeakReference;
import java.util.List;

import static com.example.jamie.puzzler.Activities.PuzzlesActivity.CurrentGameManager;
import static com.example.jamie.puzzler.Activities.PuzzlesActivity.CurrentPuzzle;
import static com.example.jamie.puzzler.Activities.PuzzlesActivity.GameColour;
import static com.example.jamie.puzzler.Activities.PuzzlesActivity.GameText;
import static com.example.jamie.puzzler.Activities.PuzzlesActivity.GameTextView;


/**
 * Created by Jamie on 22/02/2017.
 */

public class CheckMatchTask extends AsyncTask<Integer, Void, Boolean > {

    public List<Integer> puzzleSequence;

    Activity activity;
    public ImageView firstItem;
    public ImageView secondItem;
    public boolean IsSpecial;
    PuzzleLogic puzzleLogic;
    public CheckMatchTask(List<Integer> sequence, Activity activityIn){
        puzzleSequence = sequence;
        activity = activityIn;
        puzzleLogic = new PuzzleLogic(activity);
    }

    public CheckMatchTask(Activity activityIn){
        activity = activityIn;

    }

    @Override
    protected Boolean doInBackground(Integer... params) {


        if(params.length == 2){
           int first =  puzzleSequence.get(params[0]);
           int second = puzzleSequence.get(params[1]);


        if(first == second){
            // Items match
            Helpers.SpecialTypes firstCardPowerup = CurrentPuzzle.PuzzleItems.get(params[0]).Type;
            Helpers.SpecialTypes secondCardPowerup = CurrentPuzzle.PuzzleItems.get(params[1]).Type;

            long threadId = Thread.currentThread().getId();
            if(firstCardPowerup == Helpers.SpecialTypes.FOURTIMES || secondCardPowerup == Helpers.SpecialTypes.FOURTIMES){
               IsSpecial = true;
                CurrentGameManager.SetMultiplier(Helpers.SpecialTypes.FOURTIMES);

                Log.d("CheckMatchTask()", "CurrentGameManager set to FOURTIMES, line 63: thread : " + threadId);
            }
            else if(firstCardPowerup == Helpers.SpecialTypes.TWOTIMES || secondCardPowerup == Helpers.SpecialTypes.TWOTIMES){
                if(firstCardPowerup == Helpers.SpecialTypes.TWOTIMES && secondCardPowerup == Helpers.SpecialTypes.TWOTIMES){

                }
                IsSpecial = true;
                if(CurrentGameManager.PowerupType != Helpers.SpecialTypes.FOURTIMES){
                    CurrentGameManager.SetMultiplier(Helpers.SpecialTypes.TWOTIMES);
                    Log.d("CheckMatchTask()", "CurrentGameManager set to TWOTIMES, line 71: thread : " + threadId);
                }
            }
            else if(CurrentGameManager.PowerupType == null){
                CurrentGameManager.SetMultiplier(Helpers.SpecialTypes.STANDARD);
                 Log.d("CheckMatchTask()", "CurrentGameManager set to Standard, line 71: thread : " + threadId);
            }
            if(!CurrentGameManager.CompleteMatches.contains(params[0]) && !CurrentGameManager.CompleteMatches.contains(params[1])) {
                CurrentGameManager.CompleteMatches.add(params[0]);
                CurrentGameManager.CompleteMatches.add(params[1]);
            }

            // Game is complete, check if new score is highscore and save if so.
            if(puzzleLogic.CheckIfGameOver()){
                CurrentGameManager.isPlaying = false;
            }
            return true;
        }
        // Items do not match
        else{

           return false;
        }
    }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        PuzzleInterfaceFragment fragment = (PuzzleInterfaceFragment) new UserInterfaceLogic(activity).GetFragment("puzzleInterface");
        new UserInterfaceLogic(activity).ChangeMultiplierStar();
        GridView gridView = fragment.GetPuzzleGrid();
        TextView textView = (TextView)activity.findViewById(R.id.textView2);
        int orientation = new UserInterfaceLogic(activity).GetOrientation();
        View notifierBar;
        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            textView = (TextView) activity.findViewById(R.id.textView2);
            notifierBar =  activity.findViewById(R.id.notifierBar);
        }
        else{
            notifierBar = activity.findViewById(R.id.textView3);
            textView = (TextView) activity.findViewById(R.id.textView3);
        }
        if(success) {

            firstItem.setOnClickListener(null);
            secondItem.setOnClickListener(null);
            CurrentGameManager.PowerupTimeRemaining += 10;
            if(IsSpecial){
                if(CurrentGameManager.Timer != null){
                    CurrentGameManager.Timer.cancel();
                }
                CurrentGameManager.Timer = new PuzzleLogic(activity).InitialiseTimer(10,1);
                CurrentGameManager.Timer.start();
                IsSpecial = false;
            }

            CurrentGameManager.IncreaseScore(100);
                notifierBar.setBackgroundColor(Color.parseColor("#2ecc71"));
            GameColour = "#2ecc71";
            GameTextView.setText(activity.getString(R.string.match));
            GameText = activity.getString(R.string.match);

            if(!CurrentGameManager.isPlaying){
                CurrentGameManager.Timer.cancel();
                gridView.setOnItemClickListener(null);
                CurrentGameManager.Stopwatch.cancel(true);
                if(notifierBar != null)
                    notifierBar.setBackgroundColor(Color.parseColor("#1BBC9B"));
                else
                    textView.setBackgroundColor(Color.parseColor("#1BBC9B"));
                GameTextView.setText(activity.getString(R.string.puzzlecomplete));
                GameColour = "#1BBC9B";
                GameText = activity.getString(R.string.puzzlecomplete);
                new UserInterfaceLogic(activity).ChangeMultiplierStar();

                if(CurrentGameManager.Score > CurrentPuzzle.Highscore){
                    CurrentPuzzle.Highscore = CurrentGameManager.Score;
                    fragment.SetHighscoreText();
                    GameText = activity.getString(R.string.newhighscore);
                    GameTextView.setText(activity.getString(R.string.newhighscore));
                    new IOLogic(activity).SetCurrentPuzzleHighscore();
                }
            }
            puzzleLogic.DisplayCompleteImages();
        }
        else{
            if(notifierBar != null)
                notifierBar.setBackgroundColor(Color.parseColor("#e74c3c"));
            else
                textView.setBackgroundColor(Color.parseColor("#e74c3c"));
            GameColour = "#e74c3c";
            GameTextView.setText(activity.getString(R.string.failedmatch));
            GameText = activity.getString(R.string.failedmatch);
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            firstItem.setImageBitmap(null);
            secondItem.setImageBitmap(null);



        }

    }
}
