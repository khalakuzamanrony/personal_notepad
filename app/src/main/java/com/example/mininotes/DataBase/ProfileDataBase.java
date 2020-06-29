package com.example.mininotes.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.mininotes.Model.ProfileModel;

import java.security.PrivateKey;

public class ProfileDataBase extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "ProfileInfo.db";
    private static final String TABLE_NAME = "Profile";
    private static final int VERSION_NUMBER = 2;
    //columns
    private static final String ID = "Id";
    private static final String NAME = "Name";
    private static final String EMAIL = "Email";

    //Important Terms
    private static final String CREATE_TABLE = "CREATE  TABLE " + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE," + NAME + " TEXT NOT NULL, " + EMAIL + " TEXT  NOT NULL )";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static final String SELECT_ALL = "SELECT * FROM " + TABLE_NAME;


    public ProfileDataBase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUMBER);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE);
        } catch (Exception e) {

        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        try {
            db.execSQL(DROP_TABLE);
            onCreate(db);
        } catch (Exception e) {

        }
    }

    public  long AddProfileData(ProfileModel profileModel)
    {
        DeleteAll();
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(NAME,profileModel.getName());
        contentValues.put(EMAIL,profileModel.getEmail());

       long  rownum =sqLiteDatabase.insertOrThrow(TABLE_NAME,null,contentValues);
        return rownum;
    }
    //Load
    public Cursor LoadAllData()
    {
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(SELECT_ALL,null);
        return  cursor;
    }
    public  void DeleteAll()
    {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME,ID,null);
    }
}
