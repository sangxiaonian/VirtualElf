package sang.com.commonlibrary.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import io.reactivex.disposables.Disposable;
import sang.com.commonlibrary.utils.event.BusFactory;
import sang.com.commonlibrary.utils.rx.BaseControl;

/**
 * 作者： ${PING} on 2018/5/22.
 */

public class BaseActivity extends AppCompatActivity implements BaseControl.TaskListener<String>{

    protected Context mContext;
    protected Disposable d;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        if (useEventBus()){
            BusFactory.getBus().register(this);
        }

    }

    protected void initView(){};
    protected void initData(){};
    protected void initListener(){};

    public boolean useEventBus(){
        return false;
    }


    @Override
    public void taskStart(Disposable d) {
        this.d=d;
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
        if (d!=null){
            d.dispose();
        }

        if (useEventBus()){
            BusFactory.getBus().unregister(this);
        }

    }
}
