package com.example.jamie.puzzler.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jamie.puzzler.Activities.MainActivity;
import com.example.jamie.puzzler.Activities.PuzzlesActivity;
import com.example.jamie.puzzler.Logic.Helpers;
import com.example.jamie.puzzler.Logic.IOLogic;
import com.example.jamie.puzzler.R;

import org.w3c.dom.Text;

import static com.example.jamie.puzzler.Activities.PuzzlesActivity.CurrentGameManager;
import static com.example.jamie.puzzler.Activities.PuzzlesActivity.CurrentPuzzle;

/**
 * Created by Jamie on 06/03/2017.
 */

public class PuzzleInterfaceFragment extends Fragment{

    GridView puzzleGrid;

    LinearLayout backButton;
    LinearLayout reloadButton;

    TextView puzzleText;
    TextView timerText;
    TextView countdownText;
    TextView pointsText;
    TextView highscoreText;
    TextView highscoreHeader;

    ImageView starImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the maintextview for this fragment
        View view = inflater.inflate(R.layout.puzzle_fragment, container, false);
        puzzleGrid = (GridView)view.findViewById(R.id.gridview);
        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            puzzleText = (TextView)view.findViewById(R.id.textView2);
        else{
            puzzleText = (TextView)view.findViewById(R.id.textView3);
        }
        backButton = (LinearLayout) view.findViewById(R.id.backbutton);
        reloadButton = (LinearLayout)view.findViewById(R.id.reloadbutton);
        timerText = (TextView) view.findViewById(R.id.timertextview);
        countdownText = (TextView) view.findViewById(R.id.multiplierCountdown);
        pointsText = (TextView) view.findViewById(R.id.pointstextview);
        starImage = (ImageView) view.findViewById(R.id.multiplierStar);
        highscoreText = (TextView) view.findViewById(R.id.highscoretextview);


        backButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

            }
        });
        reloadButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                getActivity().finish();
                startActivity(getActivity().getIntent());

            }
        });

        return view;
    }

    public GridView GetPuzzleGrid(){
        return puzzleGrid;
    }

    public TextView GetPuzzleText(){
        return puzzleText;
    }

    public void RefreshPuzzleGrid(){
        if(puzzleGrid != null)
            puzzleGrid.setAdapter(null);
    }


    public void SetTimer(String time){
        timerText.setText(time);
    }

    public void SetCountdownTimer(String time){ countdownText.setText(time);}

    public void SetScoreText(String score){
        pointsText.setText(score);
    }

    public void SetHighscoreText(){
        if(CurrentPuzzle.Highscore > 0)
            highscoreText.setText(String.valueOf(CurrentPuzzle.Highscore));
        ;}

    public void ChangeMultiplier(){
        if(!CurrentGameManager.isPlaying){
            if(CurrentPuzzle.Layout != null) {
                int maxScore = CurrentPuzzle.Layout.length() * 200;
                if (CurrentGameManager.Score > maxScore * 0.7) {
                    starImage.setImageResource(R.drawable.first);
                    countdownText.setText(getActivity().getString(R.string.gold));
                } else if (CurrentGameManager.Score < maxScore * 0.7 && CurrentGameManager.Score > maxScore * 0.5) {
                    starImage.setImageResource(R.drawable.second);
                    countdownText.setText(getActivity().getString(R.string.silver));
                } else {
                    starImage.setImageResource(R.drawable.third);
                    countdownText.setText(getActivity().getString(R.string.bronze));
                }
            }
        }
        else if(CurrentGameManager.PowerupType != null){
        switch(CurrentGameManager.PowerupType){
            case STANDARD:starImage.setImageResource(R.drawable.starnew); break;
            case TWOTIMES:starImage.setImageResource(R.drawable.star2x); break;
            case FOURTIMES:starImage.setImageResource(R.drawable.star4x); break;
        }
        }
    }
}


