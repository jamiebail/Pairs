package com.example.jamie.puzzler.Models;

import android.graphics.Bitmap;

import com.example.jamie.puzzler.Logic.Helpers;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Jamie on 23/02/2017.
 */



public class Puzzle {
    public String Name;
    public int Id;
    public String PictureSet;
    public int Rows;
    public int Columns;
    public JSONArray Layout;
    public int Highscore;
    public List<PuzzleItem> PuzzleItems = new ArrayList<PuzzleItem>();
    public Boolean IsLocal;
    public Date startTime;
    public Date endTime;
    public Puzzle(String name){
        Name = name;
    }
    public Helpers.PuzzleSize Size;

    public Puzzle(){}

    public List<Bitmap> PuzzleItemPictures(){
        List<Bitmap> images = new ArrayList<Bitmap>();
        for(PuzzleItem item: PuzzleItems){
            images.add(item.Image);
        }
        return images;
    }
}
