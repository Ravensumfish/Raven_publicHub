/**
 * description: 封装一些工具
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/2/2
 */

package notebook.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import notebook.entity.Note;
import notebook.entity.NotePreview;
import notebook.sql.NoteDB;

public class AppUtils {
    //跳转
    public static void startActivity(Context context, Class<? extends Activity> target) {
        if (context == null || target == null) {
            return;
        }
        Intent intent = new Intent(context, target);
        context.startActivity(intent);
    }

    //用户名之后还会用，所以写个带数据的活动跳转
    public static void startActivity(Context context, Class<? extends Activity> target, String username) {
        if (context == null || target == null) {
            return;
        }
        Intent intent = new Intent(context, target);
        intent.putExtra("username", username);
        Log.d("TAG", "(NoteDB_username:)-->>" + username);
        context.startActivity(intent);
    }

    //带笔记实体的跳转
    public static void startActivity(Context context, Class<? extends Activity> target, Note note) {
        if (context == null || target == null) {
            return;
        }
        Intent intent = new Intent(context, target);
        intent.putExtra("note", note);
        context.startActivity(intent);
    }

    //获取当前时间
    public static String getCurrentTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date date = new Date();
        return simpleDateFormat.format(date);
    }

    //检查不为空
    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    //用于计算字数，不算空格
    public static String wordCount(String s) {
        s = s.replaceAll("\\s+", "");
        return String.valueOf(s.length());
    }

    public static boolean isChanged(String pre, String aft) {
        if (pre.equals(aft)) {
            return false;
        }
        return true;
    }

    //提前测量目标view的高度
    public static int getHeight(View v) {
        v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        return v.getMeasuredHeight();
    }


}
