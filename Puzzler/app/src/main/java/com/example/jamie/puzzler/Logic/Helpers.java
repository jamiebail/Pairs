package com.example.jamie.puzzler.Logic;

import android.content.res.Configuration;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Jamie on 23/02/2017.
 */

public class Helpers {

    public static Boolean ToBeSaved = false;

    public static List<String> ConvertJSONArrayToStringList(JSONArray array) {
        List<String> strings = new ArrayList<String>();
        try {
            for (int i = 0; i < array.length(); i++) {
                strings.add(array.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return strings;

    }    public static List<Integer> ConvertJSONArrayToIntList(JSONArray array) {
        List<Integer> strings = new ArrayList<Integer>();
        try {
            for (int i = 0; i < array.length(); i++) {
                strings.add(array.getInt(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return strings;
    }

    public enum SpecialTypes{
        STANDARD, TWOTIMES, FOURTIMES

    }

    public enum PuzzleSize{
        SMALL, MEDIUM, LARGE;
    }


}
