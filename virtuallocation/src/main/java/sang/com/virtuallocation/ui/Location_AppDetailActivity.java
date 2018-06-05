package sang.com.virtuallocation.ui;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.VirtualLocationManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.util.List;

import sang.com.commonlibrary.base.BaseActivity;
import sang.com.commonlibrary.entity.AppInfor;
import sang.com.commonlibrary.utils.ImageLoader;
import sang.com.commonlibrary.utils.event.BusFactory;
import sang.com.commonlibrary.utils.rx.CustomObserver;
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

    /**
     * 模拟地理位置相关信息
     */
    public LocationBean resultLoaction;
    private View tvAddressTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location__app_detail);
        setToolTitle(getString(R.string.location_app_detail));
        initView();
        initData();
        initListener();
    }


    @Override
    protected void initView() {
        super.initView();
        ivIcon = findViewById(R.id.iv_icon);
        tvName = findViewById(R.id.tv_name);
        tvAddressTitle = findViewById(R.id.tv_address_title);
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
        setRightText(getString(R.string.location_my_collection));
        setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(mContext,Location_CollectionActivity.class));
            }

        });

        rlPosition.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                StringBuilder stringBuilder = new StringBuilder();
                String entry;
                String cancle;
                if (resultLoaction != null) {
                    stringBuilder.append(getString(R.string.location_entry_collection))
                            .append(resultLoaction.getName());
                    entry = getString(R.string.location_collection);
                    cancle = getString(R.string.location_cancle);
                } else {
                    stringBuilder.append(getString(R.string.location_no_select_location));
                    entry = getString(R.string.location_go);
                    cancle = getString(R.string.location_cancle_go);
                }

                builder.setMessage(stringBuilder.toString().trim());
                builder
                        .setNegativeButton(cancle, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(entry, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                dialog.dismiss();

                                if (resultLoaction != null) {
                                    List<LocationBean> beans = DataSupport.where("name = ?", resultLoaction.getName()).find(LocationBean.class);
                                    if (beans.isEmpty()){
                                        resultLoaction.save();
                                    }else {
                                        resultLoaction.updateAll("name = ?", resultLoaction.getName());
                                    }

                                    ToastUtils.showTextToast(getString(R.string.location_collect_success));
                                } else {
                                    startToMapActivity();
                                }

                            }
                        });
                builder.create().show();

                return true;
            }
        });

    }


    private void startToMapActivity() {
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeScaleUpAnimation(rlPosition,
                rlPosition.getWidth() / 2, rlPosition.getHeight() / 2, 0, 0);
        ActivityCompat.startActivity(this, new Intent(this, Loaction_MapActivity.class),
                compat.toBundle());
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
            cbCollect.setChecked(mode != 0);

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
    public void onLocationEvent(LocationBean event) {
        if (event != null && appInfor != null) {
            VirtualLoactionUtils
                    .changeLoaction(event, appInfor)
                    .subscribe(new CustomObserver<LocationBean>(this) {

                        @Override
                        public void onNext(LocationBean result) {
                            super.onNext(result);
                            resultLoaction = result;
                            ToastUtils.showTextToast(getString(R.string.location_change_gps));
                            ViewUtils.setText(tvAddress, result.getName());
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

          startToMapActivity();

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
        VirtualLocationManager.get().setMode(appInfor == null ? Configs.appUserId : appInfor.getUserId(), appInfor.getPackageName(), mode);
    }
}
