package com.example.jamie.puzzler.Logic.Getters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;

/**
 * Created by Jamie on 01/03/2017.
 */

public class InternalFileGetter implements IGetter {

    public InternalFileGetter(Activity activityIn){
        activity = activityIn;
    }

    Activity activity;

    public Object Get(String filename){
        try {
            BufferedReader streamIn = new BufferedReader(new InputStreamReader(activity.openFileInput(filename)));
            StringBuilder contents = new StringBuilder("");
            String line;
            while( (line = streamIn.readLine()) != null){
                contents.append(line);
            }
            streamIn.close();
            return contents;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Bitmap GetImage(String filename){
        try {
            String path = activity.getFilesDir().getAbsolutePath();
            FileInputStream streamIn = new FileInputStream(path + "/" + filename);
            Bitmap image = BitmapFactory.decodeStream(streamIn);
            streamIn.close();
            return image;
        } catch (FileNotFoundException e) {
            Log.d("GetImage()", "Image not saved locally");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
