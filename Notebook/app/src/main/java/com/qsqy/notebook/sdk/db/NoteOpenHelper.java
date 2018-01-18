package com.qsqy.notebook.sdk.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.qsqy.notebook.sdk.toast.Toast;

/**
 * Created by CK on 2018/1/5.
 */

public class NoteOpenHelper extends SQLiteOpenHelper {
    private Context context;

    private static final String CREATE_NOTE = "create table Note("
            +"note_id integer primary key autoincrement,"
            +"note_title text,"
            +"note_message text,"
            +"note_password text,"
            +"note_time text)";

    private static final String CREATE_PASSWORD = "create table Password("
            +"password_id integer primary key autoincrement,"
            +"password_title text,"
            +"password_text text,"
            +"password_tip text)";

    public NoteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_NOTE);
        sqLiteDatabase.execSQL(CREATE_PASSWORD);
        Log.d("api","创建成功");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    }
}
