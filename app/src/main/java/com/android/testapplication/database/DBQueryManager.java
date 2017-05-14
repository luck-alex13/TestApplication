package com.android.testapplication.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.android.testapplication.dataModels.MyDataModel;


import java.util.ArrayList;
import java.util.Random;


public class DBQueryManager {
    private final String LOG_TAG = "LOG_TAG " + DBQueryManager.class.getSimpleName();

    private SQLiteDatabase database;
    private int existingId;

    public DBQueryManager(SQLiteDatabase database) {
        this.database = database;
    }


    public int deleteCity(int id) {
        String where = DBHelper.ID + " = ?";
        String[] whereArgs = new String[]{String.valueOf(id)};
        return this.database.delete(DBHelper.TABLE_GALLERY, where, whereArgs);
    }


    private ContentValues objectToContentValues(MyDataModel object) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBHelper.ID, object.getId());
        contentValues.put(DBHelper.WIDTH, object.getWidth());
        contentValues.put(DBHelper.HEIGHT, object.getHeight());
        contentValues.put(DBHelper.FILE_NAME, object.getFilename());
        contentValues.put(DBHelper.FORMAT, object.getFormat());
        contentValues.put(DBHelper.AUTHOR, object.getAuthor());
        contentValues.put(DBHelper.AUTHOR_URL, object.getAuthorUrl());
        contentValues.put(DBHelper.POST_URL, object.getPostUrl());
        return contentValues;
    }

    public void saveGalleryItem(MyDataModel dataModel) {
        ContentValues newValues = objectToContentValues(dataModel);
        if (!isItemExistInDB(DBHelper.TABLE_GALLERY, DBHelper.ID, String.valueOf(dataModel.getId()))) {
            this.database.insert(DBHelper.TABLE_GALLERY, null, newValues);
            Log.d(LOG_TAG, "saveCity() " + newValues.toString());
        } else {
            int affected = this.database.update(DBHelper.TABLE_GALLERY, newValues, DBHelper.ID + " = ?", new String[]{String.valueOf(dataModel.getId())});

            // Log.d(LOG_TAG, "saveCity() affected rows " + affected + " update " + newValues.toString());
        }
    }

    public ArrayList<MyDataModel> readGalleryItems() {
        ArrayList<MyDataModel> list = new ArrayList<>();
        Random random = new Random();
        long size = DatabaseUtils.queryNumEntries(this.database, DBHelper.TABLE_GALLERY);

        Cursor cursor = this.database.query(DBHelper.TABLE_GALLERY, null, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int i = 0;
            do {
                cursor.moveToPosition(random.nextInt((int) size));
                MyDataModel myDataModel = new MyDataModel(
                        cursor.getInt(cursor.getColumnIndex(DBHelper.ID)),
                        cursor.getString(cursor.getColumnIndex(DBHelper.FILE_NAME)),
                        cursor.getString(cursor.getColumnIndex(DBHelper.FORMAT)),
                        cursor.getInt(cursor.getColumnIndex(DBHelper.WIDTH)),
                        cursor.getInt(cursor.getColumnIndex(DBHelper.HEIGHT)),
                        cursor.getString(cursor.getColumnIndex(DBHelper.AUTHOR)),
                        cursor.getString(cursor.getColumnIndex(DBHelper.AUTHOR_URL)),
                        cursor.getString(cursor.getColumnIndex(DBHelper.POST_URL))
                );

                list.add(myDataModel);

                i++;
                //Log.d(LOG_TAG, "readGalleryItems() " + MyDataModel.getName());
            } while (i < 10);
            Log.d(LOG_TAG, "readGalleryItems() " + DBHelper.TABLE_GALLERY + " rows->" + list.size());
        } else {
            cursor.close();
            Log.d(LOG_TAG, "readGalleryItems() " + DBHelper.TABLE_GALLERY + " NO rows");
            //return null;
        }

        cursor.close();
        return list;
    }


    public boolean isItemExistInDB(String dbName, String field, String value) {
        //String[] columns = new String[]{COLUMN_ID};
        String selection = field + " = ?";
        String[] selectionArgs = new String[]{value};
        Cursor cursor;
        try {
            cursor = this.database.query(dbName, null, selection, selectionArgs, null, null, null);
            if (cursor.moveToFirst()) {
                Log.d(LOG_TAG, "ItemExistInDB " + dbName + "; Data " + cursor.getString(0) + ": " + cursor.getString(1));
                existingId = cursor.getInt(0);
                cursor.close();
                return true;
            } else {
                cursor.close();
                return false;
            }
        } catch (SQLiteException ex) {
            ex.printStackTrace();
            Log.d(LOG_TAG, "SQLiteException " + dbName);
            Log.d(LOG_TAG, ex.getMessage());
            return false;
        }
    }

    public boolean isOpen() {
        return database.isOpen();
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }

    public void close() {
        database.close();
        Log.d(LOG_TAG, "database closed ");
    }
}
