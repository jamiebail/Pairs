package com.example.jamie.puzzler.Logic.Setters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import com.example.jamie.puzzler.Models.PuzzleItem;
import com.example.jamie.puzzler.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static java.lang.System.out;

/**
 * Created by Jamie on 01/03/2017.
 */

public class InternalFileSetter implements ISetter {

    public InternalFileSetter(Activity activityIn){
        activity = activityIn;
    }

    Activity activity;

    public Boolean Set(String fileName, String Json){

        try {
            fileName = fileName + activity.getString(R.string.FileOutputType);
            FileOutputStream streamOut = activity.openFileOutput(fileName, Context.MODE_PRIVATE);
            streamOut.write(Json.getBytes());
            streamOut.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean SetPictureFile(String fileName, Bitmap image){

        FileOutputStream streamOut;
        try {
            streamOut = activity.openFileOutput(fileName, Context.MODE_PRIVATE);
            image.compress(Bitmap.CompressFormat.JPEG, 100, streamOut);
            streamOut.flush();
            streamOut.close();
            return true;
            }

         catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
