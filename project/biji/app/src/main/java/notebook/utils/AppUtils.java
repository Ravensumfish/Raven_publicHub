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

import java.io.Serializable;

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

}
