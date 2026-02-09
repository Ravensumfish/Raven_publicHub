/**
 * description: 用于笔记存储
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/2/4
 */

package notebook.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import notebook.entity.Note;
import notebook.utils.AppUtils;

public class NoteDB extends SQLiteOpenHelper {

    private static final String NOTE_DB_NAME = "NotesDB";
    private static final String NOTE_TABLE_NAME = "notes";
    private static final String createNotes = "create table " + NOTE_TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT,title,content,create_time,update_time,word_count)";

    public NoteDB(@Nullable Context context) {
        super(context, NOTE_DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createNotes);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //增
    public long insert(Note note) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put("title", note.getTitle());
        value.put("content", note.getContent());
        value.put("create_time", note.getCreateTime());
        value.put("update_time", note.getUpdateTime());
        value.put("word_count", note.getWordCount());
        long rowId = db.insert(NOTE_TABLE_NAME, null, value);

        if (rowId != -1) {
            Log.d("TAG", "(数据库notes:成功插入)-->>");
        } else {
            Log.d("TAG", "(数据库notes:插入失败)-->>");
        }
        note.setId(rowId);
        return rowId;
    }

    //删
    public long delete(Note note) {
        SQLiteDatabase db = getWritableDatabase();
        Log.d("TAG", "(id:)-->>" + note.getId());
        int row = db.delete(NOTE_TABLE_NAME, "id = ?", new String[]{String.valueOf(note.getId())});
        if (row > 0) {
            Log.d("TAG", "(数据库notes:成功删除)-->>");
        } else {
            Log.d("TAG", "(数据库notes:删除失败)-->>");
        }

        return row;
    }

    //查
    //1.查所有
    public List<Note> queryAll() {
        SQLiteDatabase db = getReadableDatabase();
        List<Note> notes = new ArrayList<>();
        Cursor cursor = db.query(NOTE_TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(0);
                String title = cursor.getString(1);
                String content = cursor.getString(2);
                String createTime = cursor.getString(3);
                String updateTime = cursor.getString(4);
                String wordCount = cursor.getString(5);
                Note note = new Note();
                note.setId(id);
                note.setTitle(title);
                note.setContent(content);
                note.setCreateTime(createTime);
                note.setUpdateTime(updateTime);
                note.setWordCount(wordCount);
                notes.add(note);
            }
        }

        cursor.close();
        return notes;
    }

    //2.按关键词查找某note
    public List<Note> query(String obj) {
        if (AppUtils.isEmpty(obj)) {
            return queryAll();
        }
        SQLiteDatabase db = getReadableDatabase();
        List<Note> notes = new ArrayList<>();
        Cursor cursor = db.query(NOTE_TABLE_NAME, null, "title like ? OR content like ?", new String[]{"%" + obj + "%","%" + obj + "%"}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(0);
                String title = cursor.getString(1);
                String content = cursor.getString(2);
                String createTime = cursor.getString(3);
                String updateTime = cursor.getString(4);
                String wordCount = cursor.getString(5);
                Note note = new Note();
                note.setId(id);
                note.setTitle(title);
                note.setContent(content);
                note.setCreateTime(createTime);
                note.setUpdateTime(updateTime);
                note.setWordCount(wordCount);
                notes.add(note);
            }
        }
        cursor.close();
        Log.d("TAG","(查找到的notes:)-->>" + notes);
        return notes;
    }

    //改
    public long update(Note note) {
        SQLiteDatabase db = getWritableDatabase();
        Log.d("TAG", "(id:)-->>" + note.getId());
        ContentValues value = new ContentValues();
        value.put("title", note.getTitle());
        value.put("content", note.getContent());
        value.put("update_time", note.getUpdateTime());
        value.put("word_count", note.getWordCount());
        long row = db.update(NOTE_TABLE_NAME, value, "id = ?", new String[]{String.valueOf(note.getId())});

        if (row > 0) {
            Log.d("TAG", "(数据库notes:成功更新)-->>");
        } else {
            Log.d("TAG", "(数据库notes:更新失败)-->>");
        }
        return row;
    }

}
