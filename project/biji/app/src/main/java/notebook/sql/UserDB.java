/**
 * description: 用户数据库，用于存储用户名与密码，以实现登录功能和后续以用户为“主键”的功能
 * author:漆子君
 * email:3100776336@qq.com
 * date:2026/2/3
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

public class UserDB extends SQLiteOpenHelper {

    private static final String DB_NAME = "MYSQLiteDB";
    //后续相关参数名必须和表中设定一致
    private static final String createUsers = "create table users(username varchar(25) UNIQUE NOT NULL,password varchar(25) NOT NULL)";

    public UserDB(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    //整个app只执行一次，若想重新执行需卸载app
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createUsers);
    }

    //更新数据库版本用的，目前用不着
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //实现用户注册
    public long sign(User user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String name = user.getName();
        String password = user.getPassword();

        Log.d("TAG", "(name:" + name + ")-->>");
        Log.d("TAG", "(password:" + password + ")-->>");
    //用户名和密码不能为空
        if (name.isEmpty() || password.isEmpty()) {
            return -1;
        } else {
            contentValues.put("username", name);
            contentValues.put("password", password);
            long users = db.insert("users", null, contentValues);
            //用于检查
            if (users != -1) {
                Log.d("TAG", "(注册:成功写入UsersDB)-->>");
            } else {
                Log.d("TAG", "(注册:写入失败)-->>");
            }
            return users;
        }
    }
    //实现用户登录
    public boolean login(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        boolean result = false;
        //先在表中查找用户存在（按用户名查找）
        Cursor users = db.query("users", null, "username = ?", new String[]{username}, null, null, null);
        Log.d("TAG", "(users:" + users + ")-->>");
        //再检测账号密码是否匹配，依次遍历匹配
        while (users.moveToNext()) {
            //索引从0开始，密码列对应索引为1
            String pwd = users.getString(1);
            if (password.equals(pwd)) {
                result = true;
            }
        }
        Log.d("TAG", "(登录判断:" + result + ")-->>");
        users.close();
        return result;
    }
}
