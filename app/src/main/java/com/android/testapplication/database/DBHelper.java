package com.android.testapplication.database;

import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private final String LOG_TAG = "LOG_TAG_DBH";

    public static final String DATABASE_NAME = "local_database";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_GALLERY = " table_gallery ";
    public static final String ID = "_id";
    public static final String AUTHOR = "author";
    public static final String AUTHOR_URL = "author_url";
    public static final String FILE_NAME = "filename";
    public static final String FORMAT = "format";
    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";
    public static final String POST_URL = "post_url";



    public static final String SQL_SCRIPT_CREATE_TABLE_GALLERY = "CREATE TABLE IF NOT EXISTS" + TABLE_GALLERY +
            "(_id INTEGER PRIMARY KEY, " +
            WIDTH + " INTEGER NOT NULL, " +
            HEIGHT + " INTEGER NOT NULL, " +
            FILE_NAME + " TEXT NOT NULL, " +
            FORMAT + " TEXT NOT NULL, " +
            AUTHOR + " TEXT NOT NULL, " +
            AUTHOR_URL + " TEXT NOT NULL, " +
            POST_URL + " TEXT NOT NULL);";

    private DBQueryManager queryManager;
    //private DBUpdateManager updateManager = new DBUpdateManager(getWritableDatabase());

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //Log.d(LOG_TAG, "DBHelper()");
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_SCRIPT_CREATE_TABLE_GALLERY);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void clearCache(){
        Log.d(LOG_TAG, "DBHelper clearCache()");
        dropTable(getWritableDatabase(),TABLE_GALLERY);
        getWritableDatabase().execSQL(SQL_SCRIPT_CREATE_TABLE_GALLERY);
    }

    public void dropTable(SQLiteDatabase db, String tableName) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
    }

    public void deleteAllRows(String tableName) {
        getWritableDatabase().execSQL("delete from " + tableName);
    }

    public long getRowsNumber(String tname){
        return DatabaseUtils.queryNumEntries(getWritableDatabase(), tname);
    }


    public DBQueryManager query() {
        if (queryManager == null) {
            queryManager = new DBQueryManager(getWritableDatabase());
            return queryManager;
        } else {
            if (queryManager.isOpen())
                return queryManager;
            else {
                queryManager.setDatabase(getWritableDatabase());
                return queryManager;
            }
        }
    }

    public void closeDb() {
        if (queryManager != null) {
            if (queryManager.isOpen()) {
                queryManager.close();
            }
        }
    }


}
