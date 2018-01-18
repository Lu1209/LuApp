package com.qsqy.notebook.sdk.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.qsqy.notebook.main.entity.NoteBook;
import com.qsqy.notebook.main.entity.Password;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CK on 2018/1/8.
 */

public class NoteDB {
    public static final String DB_NAME = "note";//数据库名
    public static final int VERSION = 1;//数据库版本号
    private static NoteDB noteDB;
    private NoteOpenHelper dbHelper;
    private SQLiteDatabase db;

    public NoteDB(Context context) {
        dbHelper = new NoteOpenHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getReadableDatabase();
    }

    /**
     * 获取KckpDB的实例。
     */
    public synchronized static NoteDB getInstance(Context context) {
        if (noteDB == null) {
            noteDB = new NoteDB(context);
        }
        return noteDB;
    }

    /**
     * 添加数据。
     */
    public void saveNote(String note_title, String note_message, String note_password, String note_time) {
//        UUID uuid = UUID.randomUUID();
//        String note_id = uuid.toString();//主键ID
        db.execSQL("insert into Note(note_title, note_message, note_password, note_time) values(?, ?, ?, ?)", new String[]{note_title, note_message, note_password, note_time});
    }

    public void savePassword(String password_title, String password_text) {
        db.execSQL("insert into Password(password_title, password_text) values(?, ?)", new String[]{password_title, password_text});
    }

    /**
     * 删除数据。
     */
    public void deleteNote(String note_id) {
        db.execSQL("delete from Note where note_id = ?", new String[]{note_id});
    }

    /**
     * 更新数据。
     */
    public void update(String note_id, String note_title, String note_message, String note_password, String note_time) {
        db.execSQL("update Note set note_title = ?, note_message = ?, note_password = ?, note_time = ? where note_id = ?", new String[]{note_title, note_message, note_password, note_time, note_id});
    }

    public void addPassword(String password_tip, String password_title) {
        db.execSQL("update Password set password_tip = ? where password_title = ?", new String[]{password_tip, password_title});
    }

    /**
     * 查询全部数据
     */
    public List<NoteBook> findAllData() {
        List<NoteBook> list = new ArrayList<NoteBook>();
        Cursor cursor = db.rawQuery("select * from Note", null);
        NoteBook noteBook = null;
        while (cursor.moveToNext()) {
            noteBook = new NoteBook();
            noteBook.setId(cursor.getString(cursor.getColumnIndex("note_id")));
            noteBook.setTitle(cursor.getString(cursor.getColumnIndex("note_title")));
            noteBook.setMessage(cursor.getString(cursor.getColumnIndex("note_message")));
            noteBook.setPassword(cursor.getString(cursor.getColumnIndex("note_password")));
            noteBook.setTime(cursor.getString(cursor.getColumnIndex("note_time")));
            list.add(noteBook);
        }
        cursor.close();
        return list;
    }

    /**
     * 查询密码数据
     */
    public List<Password> findPasswordData() {
        List<Password> list = new ArrayList<Password>();
        Cursor cursor = db.rawQuery("select * from Password", null);
        Password password = null;
        while (cursor.moveToNext()) {
            password = new Password();
            password.setId(cursor.getString(cursor.getColumnIndex("password_id")));
            password.setTitle(cursor.getString(cursor.getColumnIndex("password_title")));
            password.setText(cursor.getString(cursor.getColumnIndex("password_text")));
            password.setTip(cursor.getString(cursor.getColumnIndex("password_tip")));
            list.add(password);
        }
        cursor.close();
        return list;
    }

    /**
     * 查询数据
     * @param type 0:公开；1：私人
     * @return
     */
    public List<NoteBook> findData(int type) {
        List<NoteBook> list = new ArrayList<NoteBook>();
        Cursor cursor = db.rawQuery("select * from Note where note_password = " + type, null);
        NoteBook noteBook = null;
        while (cursor.moveToNext()) {
            noteBook = new NoteBook();
            noteBook.setId(cursor.getString(cursor.getColumnIndex("note_id")));
            noteBook.setTitle(cursor.getString(cursor.getColumnIndex("note_title")));
            noteBook.setMessage(cursor.getString(cursor.getColumnIndex("note_message")));
            noteBook.setPassword(cursor.getString(cursor.getColumnIndex("note_password")));
            noteBook.setTime(cursor.getString(cursor.getColumnIndex("note_time")));
            list.add(noteBook);
        }
        cursor.close();
        return list;
    }

    public void close() {
        dbHelper.close();
    }
}
