/**description: 用于笔记存储
 * author:漆子君
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

import notebook.entity.User;

public class NoteDB extends SQLiteOpenHelper {

    private static final String DB_NAME = "NotesDB";
    private static final String createNotes = "create table notes(title,content,create_time,update_time,word_count)";

    public NoteDB(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    //整个app只执行一次，若想重新执行需卸载app
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createNotes);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
