package com.example.jamie.puzzler.Models;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v7.widget.OrientationHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.jamie.puzzler.Logic.Helpers;
import com.example.jamie.puzzler.Logic.PuzzleLogic;
import com.example.jamie.puzzler.R;

import java.util.List;
import java.util.Random;

import static com.example.jamie.puzzler.Activities.PuzzlesActivity.CurrentPuzzle;

/**
 * Created by Jamie on 23/02/2017.
 */

public class ImageAdapter extends BaseAdapter {
    private Activity activity;
    private PuzzleLogic puzzleLogic;
    int orientation;
    public Bitmap[] images;
    public ImageAdapter(Activity activityIn, List<Bitmap> imagesIn) {
        activity = activityIn;
        images = imagesIn.toArray(new Bitmap[imagesIn.size()]);
        puzzleLogic = new PuzzleLogic(activityIn);
        orientation = activity.getResources().getConfiguration().orientation;
    }

    public int getCount() {
        return images.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item.
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {

            imageView = new ImageView(activity);

            if(orientation == Configuration.ORIENTATION_PORTRAIT){
            switch(CurrentPuzzle.Columns) {
                case 3:
                    imageView.setLayoutParams(new GridView.LayoutParams(parent.getWidth() / CurrentPuzzle.Columns - 200, parent.getHeight() / CurrentPuzzle.Rows - 35));
                    break;
                case 4:
                    if(CurrentPuzzle.Rows > 4){
                        imageView.setLayoutParams(new GridView.LayoutParams(parent.getWidth() / CurrentPuzzle.Columns - 100, parent.getHeight() / CurrentPuzzle.Rows - 30));
                        break;
                    }
                    imageView.setLayoutParams(new GridView.LayoutParams(parent.getWidth() / CurrentPuzzle.Columns - 70, parent.getHeight() / CurrentPuzzle.Rows - 70));
                    break;
                case 6:
                    imageView.setLayoutParams(new GridView.LayoutParams(parent.getWidth() / CurrentPuzzle.Columns - 70, parent.getHeight() / CurrentPuzzle.Rows - 65));
                    break;
                default:
                    imageView.setLayoutParams(new GridView.LayoutParams(parent.getWidth() / CurrentPuzzle.Columns - 70, parent.getHeight() / CurrentPuzzle.Rows - 35));
                    break;
                }
            }
            else if(orientation == Configuration.ORIENTATION_LANDSCAPE){
                switch(CurrentPuzzle.Columns) {
                    case 3:
                        imageView.setLayoutParams(new GridView.LayoutParams(parent.getWidth() / CurrentPuzzle.Columns - 200, parent.getHeight() / CurrentPuzzle.Rows - 35));
                        break;
                    case 4:
                        if(CurrentPuzzle.Rows < 4){
                            imageView.setLayoutParams(new GridView.LayoutParams(parent.getWidth() / CurrentPuzzle.Columns - 90, parent.getHeight() / CurrentPuzzle.Rows - 30));
                            break;
                        }
                        else if(CurrentPuzzle.Rows > 4){
                            imageView.setLayoutParams(new GridView.LayoutParams(parent.getWidth() / CurrentPuzzle.Columns - 120, parent.getHeight() / CurrentPuzzle.Rows - 35));
                            break;}
                        imageView.setLayoutParams(new GridView.LayoutParams(parent.getWidth() / CurrentPuzzle.Columns - 110, parent.getHeight() / CurrentPuzzle.Rows - 35));
                        break;

                    default:
                        imageView.setLayoutParams(new GridView.LayoutParams(parent.getWidth() / CurrentPuzzle.Columns - 70, parent.getHeight() / CurrentPuzzle.Rows - 30));
                        break;
                }
            }


            imageView.setScaleType(ImageView.ScaleType.CENTER);
            imageView.setPadding(0, 0, 0, 0);
            if(CurrentPuzzle.PuzzleItems.size() > 0) {
                PuzzleItem item = CurrentPuzzle.PuzzleItems.get(position);

                    if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){

                    if (item.Type != Helpers.SpecialTypes.STANDARD) {
                        switch (item.Type) {
                            case TWOTIMES:
                                imageView.setBackgroundResource(R.drawable.ic_specialcard);
                                break;
                            case FOURTIMES:
                                imageView.setBackgroundResource(R.drawable.ic_specialcard2);
                                break;
                        }
                    } else {
                        Random rn = new Random();
                        int cardSkin = rn.nextInt(4) + 1;


                        switch (cardSkin) {
                            case 1:
                                imageView.setBackgroundResource(R.drawable.ic_firecard);
                                break;
                            case 2:
                                imageView.setBackgroundResource(R.drawable.ic_mooncard);
                                break;
                            case 3:
                                imageView.setBackgroundResource(R.drawable.ic_suncard);
                                break;
                            case 4:
                                imageView.setBackgroundResource(R.drawable.ic_starcard);
                                break;

                        }
                    }
                }
            else{
                    if (item.Type != Helpers.SpecialTypes.STANDARD) {
                        switch (item.Type) {
                            case TWOTIMES:
                                imageView.setBackgroundResource(R.drawable.cardset4);
                                break;
                            case FOURTIMES:
                                imageView.setBackgroundResource(R.drawable.cardset9);
                                break;
                        }
                    } else {
                        Random rn = new Random();
                        int cardSkin = rn.nextInt(4) + 1;


                        switch (cardSkin) {
                            case 1:
                                imageView.setBackgroundResource(R.drawable.cardset6);
                                break;
                            case 2:
                                imageView.setBackgroundResource(R.drawable.cardset2);
                                break;
                            case 3:
                                imageView.setBackgroundResource(R.drawable.cardset1);
                                break;
                            case 4:
                                imageView.setBackgroundResource(R.drawable.cardset5);
                                break;

                        }
                    }
                    }
            }

        } else {
            imageView = (ImageView) convertView;
        }

        int height = imageView.getLayoutParams().height;
        int width = imageView.getLayoutParams().width;
        if(height > 0 && width> 0){
         if(puzzleLogic.CheckIfCardRevealed(position)){
            imageView.setImageBitmap(Bitmap.createScaledBitmap(CurrentPuzzle.PuzzleItemPictures().get(position), (width - width/15) , height - height/15, false));
             imageView.setOnClickListener(null);
         }
        }
        return imageView;
    }

}
