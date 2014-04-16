package org.unlucky.gpsmover.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

public class FavoritesHelper {
    public final static String TABLE_NAME = "tb_favorites";
    public final static String[] FIELD_NAME = {"id", "title", "latitude", "longitude", "zoomlevel"};
    public final static String[] FIELD_TYPE = {"integer primary key autoincrement",
            "text", "double", "double", "integer"};

    private static FavoritesHelper inst = null;

    private Context context;
    private SQLiteDatabase db;
    private DatabaseOpenHelper openHelper;

    public static FavoritesHelper getInstance(Context context) {
        if (inst == null) {
            inst = new FavoritesHelper(context);
        }
        return inst;
    }

    public FavoritesHelper(Context context) {
        this.context = context;
    }

    public FavoritesHelper open() throws SQLException {
        openHelper = new DatabaseOpenHelper(context);
        db = openHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        openHelper.close();
    }

    public void execSQL(String sql) throws SQLException {
        db.execSQL(sql);
    }

    public Cursor rawQuery(String sql, String [] selectionArgs) {
        return db.rawQuery(sql, selectionArgs);
    }

    public Cursor query(String table, String[] columns, String selection,
                        String[] selectionArgs, String groupBy, String having, String orderBy) {
        return db.query(table, columns, selection, selectionArgs,
                groupBy, having, orderBy);
    }

    public long insert(String table, ContentValues cv) {
        return db.insert(table, null, cv);
    }

    public int delete(String table, String whereClause, String[] whereArgs) {
        return db.delete(table, whereClause, whereArgs);
    }

    public int update(String table, ContentValues cv, String whereClause,
                      String[] whereArgs) {
        return db.update(table, cv, whereClause, whereArgs);
    }
}
