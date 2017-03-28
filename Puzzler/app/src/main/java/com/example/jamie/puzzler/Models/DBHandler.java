package com.example.jamie.puzzler.Models;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jamie on 02/03/2017.
 */

public class DBHandler extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "puzzlesManager";
    private static final String TABLE_PUZZLES = "puzzles";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_HIGHSCORE = "highscore";

    public DBHandler(Activity contextIn){
        super(contextIn, DATABASE_NAME, null, DATABASE_VERSION);
    };

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PUZZLE_TABLE = "CREATE TABLE " + TABLE_PUZZLES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_HIGHSCORE + " INTEGER )";
        db.execSQL(CREATE_PUZZLE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PUZZLES);

        onCreate(db);
    }

    public void addPuzzle(String puzzleName, int highscore) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, puzzleName);
        values.put(KEY_HIGHSCORE, highscore);

        db.insert(TABLE_PUZZLES, null, values);
        db.close(); // Closing database connection
    }

    public void updateHighscore(String puzzleName, int highscore){
        SQLiteDatabase db = this.getWritableDatabase();
        String strSQL = "UPDATE " + TABLE_PUZZLES + " SET " + KEY_HIGHSCORE + " = " + highscore + " WHERE "+ KEY_NAME + " = '" + puzzleName+"'";
        db.execSQL(strSQL);
    }

    public int getHighscore(String puzzleName){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PUZZLES, new String[]{KEY_HIGHSCORE}, KEY_NAME + "=?", new String[] { puzzleName }, null, null, null, null);
        if (cursor != null)
            if(cursor.moveToFirst()){
                return cursor.getInt(cursor.getColumnIndex("highscore"));
        }
        return -1;
    }

    public void DropTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PUZZLES);
        onCreate(db);
    }


    public List<String> getAllPuzzles() {
        List<String> puzzleList = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PUZZLES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                String puzzle = "";
                puzzle = (c.getString(1));
                // Adding contact to list
                puzzleList.add(puzzle);
            } while (c.moveToNext());
        }
        // return contact list
        return puzzleList;
    }


    // Getting single contact
    public List<String> getPuzzle(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> s = getAllPuzzles();
        Cursor cursor = db.query(TABLE_PUZZLES, new String[] { KEY_ID, KEY_NAME, KEY_HIGHSCORE}, KEY_NAME + "=?", new String[] { String.valueOf(name) }, null, null, null, null);
        if (cursor != null)
            if(cursor.moveToFirst());{

            if(cursor.getCount() > 0){
            List<String> puzzle = new ArrayList<>();
            puzzle.add(cursor.getString(cursor.getColumnIndex("name")));
            puzzle.add(cursor.getString(cursor.getColumnIndex("highscore")));
                return puzzle;
            }
        return null;
        }
    }


}
