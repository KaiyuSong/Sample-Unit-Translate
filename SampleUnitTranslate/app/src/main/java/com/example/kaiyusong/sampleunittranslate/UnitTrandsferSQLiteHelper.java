package com.example.kaiyusong.sampleunittranslate;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class UnitTrandsferSQLiteHelper extends SQLiteOpenHelper {

    private static final String  DB_NAME = "UnitTransfer.db";
    private static final int DB_VERSION = 1;

    static final String TITLE = "UTransfer";

    static final String TYPE = "type";
    static final String FUNCTION = "FUNCTION";

    UnitTrandsferSQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TITLE
                + "( _id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TYPE + " TEXT,"
                + FUNCTION + " TEXT"
                + ")" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // TODO Auto-generated method stub
    }
}