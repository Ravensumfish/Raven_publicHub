/**
 * description: 渐变圆角矩形（废
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/2/11
 */

package notebook.UI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.biji.R;

public class RoundRectView extends View {
    private Paint paint;
    private RectF rectF;
    private int height;
    private float cornerRadius = 20;
    private int startColor;
    private int endColor;
    LinearGradient gradient;

    public RoundRectView(Context context) {
        super(context);
        init();
    }

    public RoundRectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public float getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
    }

    public int getStartColor() {
        return startColor;
    }

    public void setStartColor(int startColor) {
        this.startColor = startColor;
    }

    public int getEndColor() {
        return endColor;
    }

    public void setEndColor(int endColor) {
        this.endColor = endColor;
    }

    //绘制渐变
    private void init() {
        paint = new Paint();
        rectF = new RectF();
        //设置抗锯齿
        paint.setAntiAlias(true);
        //笔刷不透明度
        paint.setAlpha(200);
        startColor = ContextCompat.getColor(getContext(), R.color.blue_3);
        endColor = ContextCompat.getColor(getContext(), R.color.white);
    }
    private void gradient() {

        //线性渐变从上到下，只需要知道控件高度
        gradient = new LinearGradient(
                0, 0, 0, height,
                startColor, endColor,
                Shader.TileMode.CLAMP
        );
        paint.setShader(gradient);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRoundRect(rectF,cornerRadius,cornerRadius,paint);
    }

    //随大小变化
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rectF.set(0,0,w,h);
        height = h;
        gradient();
    }
}
