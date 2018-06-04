package sang.com.virtuallocation.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.VirtualLocationManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Function;
import sang.com.commonlibrary.base.BaseActivity;
import sang.com.commonlibrary.entity.AppInfor;
import sang.com.commonlibrary.utils.ImageLoader;
import sang.com.commonlibrary.utils.event.BusFactory;
import sang.com.commonlibrary.utils.rx.CustomObserver;
import sang.com.minitools.utlis.JLog;
import sang.com.minitools.utlis.ToastUtils;
import sang.com.minitools.utlis.ViewUtils;
import sang.com.virtuallocation.R;
import sang.com.virtuallocation.config.Configs;
import sang.com.virtuallocation.entity.LocationBean;
import sang.com.virtuallocation.util.VirtualLoactionUtils;
import sang.com.virtuallocation.virtual.VirtualSDKUtils;

/**
 * App详情页面
 */
public class Location_AppDetailActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {


    ImageView ivIcon;
    TextView tvName;
    TextView tvTip;
    LinearLayout llItem;
    TextView tvAddress;
    CheckBox cbCollect;
    RelativeLayout rlPosition;
    private AppInfor appInfor;
    private LocationBean locationBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location__app_detail);
        setToolTitle("应用详情");
        initView();
        initData();
        initListener();
    }


    @Override
    protected void initView() {
        super.initView();
        ivIcon = findViewById(R.id.iv_icon);
        tvName = findViewById(R.id.tv_name);
        tvTip = findViewById(R.id.tv_tip);
        llItem = findViewById(R.id.ll_item);
        tvAddress = findViewById(R.id.tv_address);
        cbCollect = findViewById(R.id.cb_collect);
        rlPosition = findViewById(R.id.rl_position);
    }

    @Override
    protected void initListener() {
        super.initListener();
        llItem.setOnClickListener(this);
        rlPosition.setOnClickListener(this);
        setRightText("我的收藏");
        setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showTextToast("收藏地址列表");
                showLoad();
            }

        });


    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AppInfor event) {
        this.appInfor = event;
        if (event != null) {
            int mode = VirtualLocationManager.get().getMode(appInfor.getUserId(), appInfor.getPackageName());
            cbCollect.setOnCheckedChangeListener(this);
            cbCollect.setChecked( mode != 0);

            ViewUtils.setText(tvName, event.getAppName());
            ImageLoader.loadImage(mContext, event.getAppIcon(), ivIcon);
        }

        BusFactory.getBus().removeStickyEvent(event);
    }

    /**
     * 坐标位置
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLocationEvent(final LocationBean event) {
        if (event != null && appInfor != null) {
            this.locationBean = event;
            JLog.i("");
            VirtualLoactionUtils
                    .changeLoaction(locationBean, appInfor, this)
                    .subscribe(new CustomObserver<VirtualLoactionUtils.ResultLoaction>(this) {
                        @Override
                        public void onNext(VirtualLoactionUtils.ResultLoaction resultLoaction) {
                            super.onNext(resultLoaction);
                            ToastUtils.showTextToast("GPS更改成功");
                            ViewUtils.setText(tvAddress, event.getName());
                        }
                    });
            ;
        }
    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ll_item) {
            if (appInfor != null) {
                showLoad();
                VirtualSDKUtils.getInstance().launch(appInfor, new VirtualCore.UiCallback() {
                    @Override
                    public void onAppOpened(String packageName, int userId) throws RemoteException {
                        hideLoad();
                    }
                });
            }

        } else if (i == R.id.rl_position) {

            startActivity(new Intent(mContext, Loaction_MapActivity.class));

        }
    }

    /**
     * Called when the checked state of a compound button has changed.
     *
     * @param buttonView The compound button view whose state has changed.
     * @param isChecked  The new checked state of buttonView.
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int mode = isChecked ? VirtualLocationManager.MODE_USE_SELF : VirtualLocationManager.MODE_CLOSE;
        VirtualLocationManager.get().setMode(appInfor==null? Configs.appUserId:appInfor.getUserId(), appInfor.getPackageName(), mode);
    }
}
