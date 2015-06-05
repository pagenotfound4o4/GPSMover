package org.unlucky.gpsmover.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    private final static int DB_VERSION = 1;
    private final static String DB_NAME = "gpsmover.db";

    public DatabaseOpenHelper(Context context) {
        this(context, DB_NAME, null, DB_VERSION);
    }

    public DatabaseOpenHelper(Context context, String db_name, CursorFactory factory, int version) {
        super(context, db_name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create tb_favorites
        String sql = "create table " + FavoritesHelper.TABLE_NAME + " (";
        for (int i=0; i<FavoritesHelper.FIELD_NAME.length; i++) {
            if (i > 0) sql += ",";
            sql += FavoritesHelper.FIELD_NAME[i] + " " + FavoritesHelper.FIELD_TYPE[i];
        }
        sql += ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "drop table if exists " + FavoritesHelper.FIELD_NAME;
        onCreate(db);
    }
}
