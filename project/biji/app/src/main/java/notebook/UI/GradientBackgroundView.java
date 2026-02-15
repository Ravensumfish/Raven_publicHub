/**
 * description: 主界面背景（废稿
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/2/11
 */

package notebook.UI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.biji.R;

public class GradientBackgroundView extends View {
    private Paint paint;
    private int width, height;
    private int startColor = ContextCompat.getColor(getContext(), R.color.light_blue);
    private int endColor = ContextCompat.getColor(getContext(), R.color.blue_3);

    public GradientBackgroundView(Context context) {
        super(context);
    }

    public GradientBackgroundView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    //绘制渐变
    private void gradient() {
        paint = new Paint();
        //设置抗锯齿
        paint.setAntiAlias(true);
        LinearGradient gradient = new LinearGradient(
                0, 0, 0, height,
                startColor, endColor,
                Shader.TileMode.CLAMP
        );
        paint.setShader(gradient);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth();
        height = getHeight();
        gradient();
        canvas.drawRect(0,0,width,height,paint);
    }
}
