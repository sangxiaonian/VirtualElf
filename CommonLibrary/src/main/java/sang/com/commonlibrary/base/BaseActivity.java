package sang.com.commonlibrary.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;
import sang.com.commonlibrary.R;
import sang.com.commonlibrary.utils.event.BusFactory;
import sang.com.commonlibrary.utils.rx.BaseControl;
import sang.com.minitools.utlis.ToastUtils;

/**
 * 作者： ${PING} on 2018/5/22.
 */

public class BaseActivity extends AppCompatActivity implements BaseControl.TaskListener<String> {

    protected Context mContext;
    protected Disposable d;

    ImageView imgLeft;
    LinearLayout left;
    TextView toolTitle;
    ImageView imgRight;
    TextView tvRight;
    RelativeLayout right;
    RelativeLayout tool;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        if (useEventBus()) {
            BusFactory.getBus().register(this);
        }


    }


    protected void setLeftOnClickListener(View.OnClickListener leftOnClickListener) {
        if (left != null) {
            left.setOnClickListener(leftOnClickListener);
        }
    }

    protected void setRightOnClickListener(View.OnClickListener rightOnClickListener) {
        if (right != null) {
            right.setOnClickListener(rightOnClickListener);
        }
    }

    protected void setRightText(String text) {
        if (tvRight != null) {
            tvRight.setText(text == null ? "" : text);
        }
    }

    protected void setRightImg(int img) {
        if (imgRight != null) {
            imgRight.setImageResource(img);
        }
    }
    protected void setLefttImg(int img) {
        if (imgLeft != null) {
            imgLeft.setImageResource(img);
        }
    }
    protected void setToolTitle(String title) {
        if (toolTitle != null) {
            toolTitle.setText(title == null ? "" : title);
        }
    }


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        imgLeft = findViewById(R.id.img_left);
        left = findViewById(R.id.left);
        toolTitle = findViewById(R.id.tool_title);
        imgRight = findViewById(R.id.img_right);
        tvRight = findViewById(R.id.tv_right);
        tool = findViewById(R.id.tool);
        right = findViewById(R.id.right);
        setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }



    protected void initView() {
    }

    ;

    protected void initData() {
    }

    ;

    protected void initListener() {
    }

    ;

    public boolean useEventBus() {
        return false;
    }


    @Override
    public void taskStart(Disposable d) {
        this.d = d;
    }

    @Override
    public void taskSuccessed() {

    }

    @Override
    public void taskFaile(String errorCode, String errorMessage) {

    }

    /**
     * 开始任务进度详情
     *
     * @param infor
     */
    @Override
    public void taskDetail(String infor) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (d != null) {
            d.dispose();
        }

        if (useEventBus()) {
            BusFactory.getBus().unregister(this);
        }

    }
}
