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

import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;
import java.util.List;

import notebook.entity.Note;
import notebook.entity.NoteGroup;
import notebook.utils.AppUtils;

public class NoteDB extends SQLiteOpenHelper {

    private static final String NOTE_DB_NAME = "NotesDB";
    private static final String NOTE_TABLE_NAME = "notes";
    private static final String NOTE_GROUP_TABLE_NAME = "note_group";
    private static final String createNotes = "create table " + NOTE_TABLE_NAME +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT," + //0
            "title," +                                 //1
            "content," +                               //2
            "create_time," +                           //3
            "update_time," +                           //4
            "word_count," +                            //5
            "user_id INTEGER NOT NULL," +              //6
            "group_id INTEGER)";              //7

    private static final String createNoteGroups = "create table " + NOTE_GROUP_TABLE_NAME +
            "(group_id INTEGER PRIMARY KEY AUTOINCREMENT," + //0
            "title," +                                       //1
            "create_time," +                                 //2
            "user_id INTEGER NOT NULL)";                     //3

    public NoteDB(@Nullable Context context) {
        super(context, NOTE_DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createNotes);
        db.execSQL(createNoteGroups);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //增
    public long insert(Note note, int userId) {

        Log.d("TAG", "(insert:noteDB)-->>" + userId);
        if (userId < 0) {

            return -1;
        }

        SQLiteDatabase db = getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put("title", note.getTitle());
        value.put("content", note.getContent());
        value.put("create_time", note.getCreateTime());
        value.put("update_time", note.getUpdateTime());
        value.put("word_count", note.getWordCount());
        value.put("user_id", userId);
        value.put("group_id", note.getGroupId());


        long rowId = db.insert(NOTE_TABLE_NAME, null, value);

        if (rowId != -1) {
            Log.d("TAG", "(数据库notes:成功插入)-->>");
        } else {
            Log.d("TAG", "(数据库notes:插入失败)-->>");
        }
        note.setId(rowId);
        return rowId;
    }

    public long insertGroup(NoteGroup group, int userId) {

        Log.d("TAG", "(insertGroup:noteDB)-->>" + userId);
        if (userId < 0) {

            return -1;
        }

        SQLiteDatabase db = getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put("title", group.getTitle());
        value.put("create_time", group.getCreateTime());
        value.put("user_id", userId);
        long rowId = db.insert(NOTE_GROUP_TABLE_NAME, null, value);

        if (rowId != -1) {
            Log.d("TAG", "(数据库noteGroup:成功插入)-->>");
        } else {
            Log.d("TAG", "(数据库noteGroup:插入失败)-->>");
        }
        group.setId((int) rowId);
        return rowId;
    }

    //删
    public long delete(Note note, int userId) {

        Log.d("TAG", "(delete:noteDB)-->>" + userId);
        if (userId < 0) {

            return -1;
        }

        SQLiteDatabase db = getWritableDatabase();
        Log.d("TAG", "(id:)-->>" + note.getId());
        int row = db.delete(NOTE_TABLE_NAME, "id = ? AND user_id = ?",
                new String[]{String.valueOf(note.getId()),
                        String.valueOf(userId)});
        if (row > 0) {
            Log.d("TAG", "(数据库notes:成功删除)-->>");
        } else {
            Log.d("TAG", "(数据库notes:删除失败)-->>");
        }

        return row;
    }

    public long deleteGroup(NoteGroup group, int userId) {

        Log.d("TAG", "(deleteGroup:noteDB)-->>" + userId);
        if (userId < 0) {

            return -1;
        }

        SQLiteDatabase db = getWritableDatabase();
        Log.d("TAG", "(groupId:)-->>" + group.getId());
        int row = db.delete(NOTE_GROUP_TABLE_NAME, "group_id = ? AND user_id = ?",
                new String[]{String.valueOf(group.getId()),
                        String.valueOf(userId)});
        if (row > 0) {
            Log.d("TAG", "(数据库noteGroup:成功删除)-->>");
        } else {
            Log.d("TAG", "(数据库noteGroup:删除失败)-->>");
        }

        return row;
    }

    //查
    //1.查所有
    public List<Note> queryAll(int userId) {

        Log.d("TAG", "(queryAll:noteDB)-->>" + userId);
        if (userId < 0) {

            return null;
        }

        SQLiteDatabase db = getReadableDatabase();
        List<Note> notes = new ArrayList<>();
        Cursor cursor = db.query(NOTE_TABLE_NAME, null, "user_id = ?", new String[]{String.valueOf(userId)}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(0);
                String title = cursor.getString(1);
                String content = cursor.getString(2);
                String createTime = cursor.getString(3);
                String updateTime = cursor.getString(4);
                String wordCount = cursor.getString(5);
                int groupId = cursor.getInt(7);
                Note note = new Note();
                note.setId(id);
                note.setTitle(title);
                note.setContent(content);
                note.setCreateTime(createTime);
                note.setUpdateTime(updateTime);
                note.setWordCount(wordCount);
                note.setGroupId(groupId);
                notes.add(note);
            }
        }

        cursor.close();
        return notes;
    }

    //2.按关键词查找某note
    public List<Note> query(String obj, int userId) {

        Log.d("TAG", "(query:noteDB)-->>" + userId);
        if (userId < 0) {

            return null;
        }

        if (AppUtils.isEmpty(obj)) {
            return queryAll(userId);
        }
        SQLiteDatabase db = getReadableDatabase();
        List<Note> notes = new ArrayList<>();
        Cursor cursor = db.query(NOTE_TABLE_NAME, null, "(title like ? OR content like ?) AND user_id = ?", new String[]{"%" + obj + "%", "%" + obj + "%", String.valueOf(userId)}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(0);
                String title = cursor.getString(1);
                String content = cursor.getString(2);
                String createTime = cursor.getString(3);
                String updateTime = cursor.getString(4);
                String wordCount = cursor.getString(5);
                int groupId = cursor.getInt(7);

                Note note = new Note();
                note.setId(id);
                note.setTitle(title);
                note.setContent(content);
                note.setCreateTime(createTime);
                note.setUpdateTime(updateTime);
                note.setWordCount(wordCount);
                note.setGroupId(groupId);
                notes.add(note);
            }
        }
        cursor.close();
        Log.d("TAG", "(查找到的notes:)-->>" + notes);
        return notes;
    }

    public Note queryNoteById(long id, int userId) {
        Log.d("TAG", "(query:noteDB)-->>" + userId);
        if (userId < 0) {

            return null;
        }

        SQLiteDatabase db = getReadableDatabase();
        Note note = new Note();
        Cursor cursor = db.query(NOTE_TABLE_NAME, null, "id = ? AND user_id = ?", new String[]{String.valueOf(id), String.valueOf(userId)}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String title = cursor.getString(1);
                String content = cursor.getString(2);
                String createTime = cursor.getString(3);
                String updateTime = cursor.getString(4);
                String wordCount = cursor.getString(5);
                int groupId = cursor.getInt(7);
                note.setId(id);
                note.setTitle(title);
                note.setContent(content);
                note.setCreateTime(createTime);
                note.setUpdateTime(updateTime);
                note.setWordCount(wordCount);
                note.setGroupId(groupId);
            }
        }
        cursor.close();
        Log.d("TAG", "(查找到的notes:)-->>" + note);
        return note;
    }

    //改
    public long update(Note note, int userId) {
        Log.d("TAG", "(update:noteDB)-->>" + userId);
        if (userId < 0) {
            return -1;
        }

        SQLiteDatabase db = getWritableDatabase();
        Log.d("TAG", "(id:)-->>" + note.getId());
        ContentValues value = new ContentValues();
        value.put("title", note.getTitle());
        value.put("content", note.getContent());
        value.put("update_time", note.getUpdateTime());
        value.put("word_count", note.getWordCount());
        value.put("group_id", note.getGroupId());

        long row = db.update(NOTE_TABLE_NAME, value, "id = ? AND user_id = ?", new String[]{String.valueOf(note.getId()), String.valueOf(userId)});

        if (row > 0) {
            Log.d("TAG", "(数据库notes:成功更新)-->>");
        } else {
            Log.d("TAG", "(数据库notes:更新失败)-->>");
        }
        return row;
    }


    public long updateGroup(NoteGroup group, int userId) {
        Log.d("TAG", "(updateGroup:noteDB)-->>" + userId);
        if (userId < 0) {

            return -1;
        }

        SQLiteDatabase db = getWritableDatabase();
        Log.d("TAG", "(id:)-->>" + group.getId());
        ContentValues value = new ContentValues();
        value.put("title", group.getTitle());
        long row = db.update(NOTE_GROUP_TABLE_NAME, value, "group_id = ? AND user_id = ?", new String[]{String.valueOf(group.getId()), String.valueOf(userId)});

        if (row > 0) {
            Log.d("TAG", "(数据库noteGroup:成功更新)-->>");
        } else {
            Log.d("TAG", "(数据库noteGroup:更新失败)-->>");
        }
        return row;
    }

    public List<NoteGroup> queryAllGroups(int userId) {

        Log.d("TAG", "(queryAllGroups:userId)-->>" + userId);
        if (userId < 0) {

            return null;
        }

        SQLiteDatabase db = getReadableDatabase();
        List<NoteGroup> groups = new ArrayList<>();
        Cursor cursor = db.query(NOTE_GROUP_TABLE_NAME, null, "user_id = ?", new String[]{String.valueOf(userId)}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String createTime = cursor.getString(2);
                int noteCount = queryCountFromNoteTable("group_id",String.valueOf(id));
                NoteGroup group = new NoteGroup();
                group.setId(id);
                group.setTitle(title);
                group.setCreateTime(createTime);
                group.setNoteCount(noteCount);
                groups.add(group);
            }
        }
        cursor.close();
        return groups;
    }

    public List<NoteGroup> queryGroup(String obj, int userId) {
        Log.d("TAG", "(query:noteDB)-->>" + userId);
        if (userId < 0) {

            return null;
        }

        if (AppUtils.isEmpty(obj)) {
            return queryAllGroups(userId);
        }
        SQLiteDatabase db = getReadableDatabase();
        List<NoteGroup> groups = new ArrayList<>();
        Cursor cursor = db.query(NOTE_GROUP_TABLE_NAME, null, "title like ? AND user_id = ?", new String[]{"%" + obj + "%", String.valueOf(userId)}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String createTime = cursor.getString(2);
                int noteCount = queryCountFromNoteTable("group_id",String.valueOf(id));
                NoteGroup group = new NoteGroup();
                group.setId(id);
                group.setTitle(title);
                group.setCreateTime(createTime);
                group.setNoteCount(noteCount);
                groups.add(group);
            }
        }
        cursor.close();
        Log.d("TAG", "(查找到的noteGroups:)-->>" + groups);
        return groups;
    }

    public NoteGroup queryGroupById(int groupId, int userId) {

        Log.d("TAG", "(query:noteDB)-->>" + userId);
        if (userId < 0 || groupId < 0) {

            return null;
        }


        SQLiteDatabase db = getReadableDatabase();
        NoteGroup group = new NoteGroup();
        Cursor cursor = db.query(NOTE_GROUP_TABLE_NAME, null, "group_id = ? AND user_id = ?", new String[]{String.valueOf(groupId), String.valueOf(userId)}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String createTime = cursor.getString(2);
                int noteCount = queryCountFromNoteTable("group_id",String.valueOf(id));
                group.setId(id);
                group.setTitle(title);
                group.setCreateTime(createTime);
                group.setNoteCount(noteCount);
            }
        }
        cursor.close();
        Log.d("TAG", "(查找到的noteGroups:)-->>" + group);
        return group;
    }

    public List<Note> queryGroupItemAll(int groupId, int userId) {

        Log.d("TAG", "(query:noteDB)-->>" + userId);
        if (userId < 0) {

            return null;
        }

        SQLiteDatabase db = getReadableDatabase();
        List<Note> notes = new ArrayList<>();
        Cursor cursor = db.query(NOTE_TABLE_NAME, null, "group_id = ? AND user_id = ?", new String[]{String.valueOf(groupId), String.valueOf(userId)}, null, null, null);
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
        Log.d("TAG", "(在group" + groupId + "中查找到的notes:)-->>" + notes);
        return notes;
    }

    public List<Note> queryGroupItem(String obj,int groupId, int userId) {
        Log.d("TAG", "(query:noteDB)-->>" + userId);
        if (userId < 0) {

            return null;
        }

        SQLiteDatabase db = getReadableDatabase();
        List<Note> notes = new ArrayList<>();
        Cursor cursor = db.query(NOTE_TABLE_NAME, null, "group_id = ? AND user_id = ? AND (title like ? OR content like ?)", new String[]{String.valueOf(groupId), String.valueOf(userId),"%" + obj + "%","%" + obj + "%"}, null, null, null);
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
        Log.d("TAG", "(在group" + groupId + "中查找到的notes:)-->>" + notes);
        return notes;
    }

    public int queryCountFromNoteTable(String key,String value) {
        SQLiteDatabase db = getReadableDatabase();
        String need = "SELECT COUNT(*) FROM " + NOTE_TABLE_NAME +" WHERE " + key + " = ?";
        Cursor cursor = db.rawQuery(need,new String[]{value});
        int count = 0;

            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
        cursor.close();
        return count;
    }

}
