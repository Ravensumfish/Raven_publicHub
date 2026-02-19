package notebook.helper;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import notebook.utils.SoftInputUtils;

public class ImeToolBarHelper {
    public static void bind(View rootView, View toolBar) {
        //监听insets的变化
        ViewCompat.setOnApplyWindowInsetsListener(rootView, new OnApplyWindowInsetsListener() {
            @NonNull
            @Override
            public WindowInsetsCompat onApplyWindowInsets(@NonNull View v, @NonNull WindowInsetsCompat insets) {
                Insets imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime());
                int navigationBarHeight = SoftInputUtils.getNavigationBarHeight(v.getContext());
                //将toolbar向上平移键盘高度
                int height = Math.max(0,imeInsets.bottom);
                toolBar.setTranslationY(-height);
                return insets;
            }
        });

    }
}
