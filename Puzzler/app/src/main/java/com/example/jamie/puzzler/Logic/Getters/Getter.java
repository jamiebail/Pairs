package com.example.jamie.puzzler.Logic.Getters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.jamie.puzzler.Activities.MainActivity;
import com.example.jamie.puzzler.Fragments.AllDownloadedFragment;
import com.example.jamie.puzzler.Fragments.NoConnectionFragment;
import com.example.jamie.puzzler.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.example.jamie.puzzler.Activities.PuzzlesActivity.CurrentGameManager;

/**
 * Created by Jamie on 23/02/2017.
 */

public class Getter {

    public Activity activity;

    public Getter(Activity activityIn) throws IOException {
        activity = activityIn;
    }

    public JSONObject Get(IGetRequests request){
        return request.doInBackground();
    }

    public JSONObject GetJSON(String urlAppend){

        BufferedReader reader;
        String result = "";
        JSONObject obj = null;
        try {
            final URL url;

            url  = new URL(activity.getString(R.string.base_url) + urlAppend);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(1000);
            connection.setReadTimeout(1000);

            connection.connect();
            MainActivity.isOnline = true;
            InputStream streamIn = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(streamIn));

            StringBuilder string = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null){
                string.append(line);
            }
            result = string.toString();
    }
    catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException e) {
            MainActivity.isOnline = false;
        e.printStackTrace();
    }
        try {
            obj = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }


    public Bitmap GetPicture(String imageAppend){
        try {
            final URL url;
            url  = new URL(activity.getString(R.string.base_url) + activity.getString(R.string.pictureURLAppend) + imageAppend);
            Bitmap image = null;
            InputStream streamIn = url.openStream();
            image = BitmapFactory.decodeStream(streamIn);
            return image;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
