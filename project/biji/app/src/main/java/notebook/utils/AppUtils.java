/**
 * description: 封装一些工具
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/2/2
 */

package notebook.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.biji.databinding.DialogEditIntroduceBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import notebook.activities.UserActivity;
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

    //写个带数据的活动跳转,用户跳转
    public static void startActivityWithUserId(Context context, Class<? extends Activity> target, int userId) {
        if (context == null || target == null) {
            return;
        }
        Intent intent = new Intent(context, target);
        intent.putExtra("user_id",userId);
        Log.d("TAG", "(UserIntent_userId:)-->>" + userId);
        context.startActivity(intent);
    }

    public static void startActivityWithNoteId(Context context, Class<? extends Activity> target, long noteId) {
        if (context == null || target == null) {
            return;
        }
        Intent intent = new Intent(context, target);
        intent.putExtra("note_id",noteId);
        Log.d("TAG", "(UserIntent_userId:)-->>" + noteId);
        context.startActivity(intent);
    }

    public static void startActivityWithGroupId(Context context, Class<? extends Activity> target, int groupId) {
        if (context == null || target == null) {
            return;
        }
        Intent intent = new Intent(context, target);
        intent.putExtra("group_id",groupId);
        Log.d("TAG", "(NoteGroupIntent_GroupId:)-->>" + groupId);
        context.startActivity(intent);
    }

    //带笔记实体的跳转
    public static void startActivityWithNote(Context context, Class<? extends Activity> target, Note note) {
        if (context == null || target == null) {
            return;
        }
        Intent intent = new Intent(context, target);
        intent.putExtra("note", note);
        Log.d("TAG", "(NoteIntent:note)-->>" + note);
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

    public static void splitTextToPages(String text, int singleLineMax, List<String> pages) {
        pages.clear();
        List<String> lines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();
        StringBuilder page = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            //换行符换行
            if (c == '\n') {
                lines.add(currentLine.toString());
                Log.d("TAG", "(toLine:currentLine/n)-->>" + currentLine);
                currentLine.setLength(0);
            } else if (currentLine.length() <= singleLineMax) {
                //无换行符且字数不满则追加
                currentLine.append(c);
            } else {
                //无换行符，但字数超了，成一行，加新行
                lines.add(currentLine.toString());
                Log.d("TAG", "(toLine:currentLine/max)-->>" + currentLine);
                currentLine.setLength(0);
                currentLine.append(c);
            }
        }

        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
            Log.d("TAG", "(toLine:currentLine/end)-->>" + currentLine);
        }


        for (int j = 0; j < lines.size(); j++) {
            page.append(lines.get(j)).append('\n');
            if ((j + 1) % 4 == 0 || j == lines.size() - 1) {
                pages.add(page.toString());
                Log.d("TAG", "(toLine:currentPage)-->>" + page);
                page.setLength(0);
            }
        }

    }

}


