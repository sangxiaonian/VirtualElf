package sang.com.minitools.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;

import sang.com.minitools.R;
import sang.com.minitools.widget.OWLoadingView;


/**
 * 作者： ${PING} on 2017/10/18.
 */

public class LoadDialog extends Dialog {
    OWLoadingView loading;
    public LoadDialog(@NonNull Context context) {
        this(context, R.style.loadDialog);
    }

    public LoadDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        initView(context);
    }


    private void initView(Context context) {
        setContentView(R.layout.layout_loading);
        loading = findViewById(R.id.owloading);
        loading.setColor(Color.parseColor("#907860"));
        setCanceledOnTouchOutside(false);
        getWindow().setGravity(Gravity.CENTER);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        loading.startAnim();

    }

    @Override
    public void onDetachedFromWindow() {
        loading.stopAnim();

        super.onDetachedFromWindow();
    }
}
