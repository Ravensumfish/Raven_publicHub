/**
 * description: 封装一些工具
 * author:漆子君
 * email:3100776336@qq.com
 * date:2026/2/2
 */

package notebook.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import java.text.SimpleDateFormat;
import java.util.Date;

import notebook.entity.Note;

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

}
