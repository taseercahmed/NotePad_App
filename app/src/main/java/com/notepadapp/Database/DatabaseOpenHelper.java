package com.notepadapp.Database;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseOpenHelper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "database.db";
    private static final String DATABASE_NAME2 = "hisnul.sqlite3";

    private static final int DATABASE_VERSION = 1;

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME2, null, DATABASE_VERSION);
    }
}