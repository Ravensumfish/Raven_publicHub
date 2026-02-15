/**
 * description: 封装SharedPreference工具
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/2/14
 */

package notebook.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SPUtils {

    public static SharedPreferences getSpData(Context context) {
        return context.getSharedPreferences("spData", MODE_PRIVATE);
    }

    public static void edit(SharedPreferences sp,String key, String obj) {
        if (sp != null) {
            SharedPreferences.Editor edit = sp.edit();
            edit.putString(key, obj).apply();
        } else {
            Log.d("TAG","(SPUtils:edit)-->>" + "写入失败，sp为null");
        }
    }
}
