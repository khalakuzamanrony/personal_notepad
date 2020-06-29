package com.example.mininotes.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.mininotes.Model.Model;
import com.example.mininotes.Model.TrashModel;

import java.util.ArrayList;

public class TrashNotesDataBase extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "TrassNotes.db";
    private static final String TABLE_NAME = "Trash_Notes";
    private static final int VERSION_NUMBER = 1;
    //columns
    private static final String ID = "Id";
    private static final String TITLE = "Title";
    private static final String NOTE = "Notes";
    private static final String TIME = "Time";


    //Important Terms
    private static final String CREATE_TABLE = "CREATE  TABLE " + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE," + TITLE + " TEXT NOT NULL, " + NOTE + " TEXT  NOT NULL , " + TIME + " TEXT )";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static final String SELECT_ALL_DATA = "SELECT * FROM " + TABLE_NAME;

    public TrashNotesDataBase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUMBER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            // Toast.makeText(context, "On Create  is Called ", Toast.LENGTH_SHORT).show();
            db.execSQL(CREATE_TABLE);
        } catch (Exception e) {
            //Toast.makeText(context, "Exception from onCreate " + e, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            //   Toast.makeText(context, "On Update  is Called ", Toast.LENGTH_SHORT).show();
            db.execSQL(DROP_TABLE);
            onCreate(db);
        } catch (Exception e) {
            //  Toast.makeText(context, "Exception from onUpdate " + e, Toast.LENGTH_SHORT).show();
        }
    }

    //Store All Data
    public long AddTrashData(TrashModel model) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, model.getIndex());
        contentValues.put(TITLE, model.getTitle());
        contentValues.put(NOTE, model.getNote());
        contentValues.put(TIME, model.getTime());

        long rownumber = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();
        return rownumber;
    }

    //Load All Data
    public ArrayList<TrashModel> LoadAllTrashData() {
        ArrayList<TrashModel> arrayList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(SELECT_ALL_DATA, null);
        if (cursor.moveToLast()) {
            do {
                TrashModel trashModel = new TrashModel();
                trashModel.setIndex(cursor.getString(0));
                trashModel.setTitle(cursor.getString(1));
                trashModel.setNote(cursor.getString(2));
                trashModel.setTime(cursor.getString(3));

                arrayList.add(trashModel);
            } while (cursor.moveToPrevious());
        }

        sqLiteDatabase.close();
        return arrayList;
    }

  /*  //Update Data
    public Boolean UpdateData(Model model) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE, model.getTitle());
        contentValues.put(NOTE, model.getNote());
        contentValues.put(ID, model.getIndex());
        sqLiteDatabase.update(TABLE_NAME, contentValues, ID + "=?", new String[]{model.getIndex()});
        return true;
    }*/

    //Delete Data
    public Boolean DeleteTrashData(TrashModel trashModel) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, trashModel.getIndex());
        sqLiteDatabase.delete(TABLE_NAME, ID + "=?", new String[]{trashModel.getIndex()});
        return true;
    }

}
