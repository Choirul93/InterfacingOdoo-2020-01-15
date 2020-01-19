package com.ca.interfacingodoo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "salesorder";
    public static final String TABLE_PRODUCT_PRODUCT = "productproduct";

    SQLiteDatabase sDb;
    private static DbHelper dbHelperInstance;

    public static synchronized DbHelper getInstance(Context context){
        if(dbHelperInstance == null){
            dbHelperInstance = new DbHelper(context.getApplicationContext());
        }
        return dbHelperInstance;
    }



    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME+".db", null, 1);
        sDb = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("create table "+TABLE_PRODUCT_PRODUCT+"(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "image_medium TEXT, " +
                "lst_price REAL,"+
                "odoo_id INTEGER,"+
                "uom_id INTEGER,"+
                "uom_name TEXT)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sDb.execSQL("DROP TABLE IF EXISTS "+TABLE_PRODUCT_PRODUCT);

    }


    public long insert(String table_name, ContentValues contentValues){
        return sDb.insert(table_name,null,contentValues);
    }

    public Cursor readAll(String table_name){
        return sDb.rawQuery("SELECT * FROM "+TABLE_PRODUCT_PRODUCT,null);
    }

    public  void deleteAll(String table_name){
        sDb.execSQL("DELETE FROM "+TABLE_PRODUCT_PRODUCT);
    }
}
