package sang.com.virtuallocation.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.os.VUserInfo;
import com.lody.virtual.os.VUserManager;
import com.lody.virtual.remote.InstallResult;
import com.lody.virtual.remote.InstalledAppInfo;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;
import org.reactivestreams.Subscriber;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import sang.com.commonlibrary.base.BaseActivity;
import sang.com.commonlibrary.entity.AppInfor;
import sang.com.commonlibrary.utils.ImageLoader;
import sang.com.commonlibrary.utils.rx.CustomObserver;
import sang.com.commonlibrary.utils.rx.RxUtils;
import sang.com.commonlibrary.xadapter.XAdapter;
import sang.com.commonlibrary.xadapter.holder.BaseHolder;
import sang.com.commonlibrary.xadapter.holder.PeakHolder;
import sang.com.minitools.utlis.ToastUtils;
import sang.com.minitools.utlis.ViewUtils;
import sang.com.virtuallocation.R;
import sang.com.virtuallocation.virtual.VirtualSDKUtils;

/**
 * 已安装的APP列表
 */
public class Loaction_InstallAppActivity extends BaseActivity {

    RecyclerView rv;
    List<AppInfor> datas;
    private XAdapter<AppInfor> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loaction__install_app);
        setToolTitle("已安装应用列表");
        initView();
        initData();
    }

    @Override
    protected void initView() {
        super.initView();
        rv = findViewById(R.id.rv);

    }

    @Override
    protected void initData() {
        super.initData();
        datas = new ArrayList<>();
        GridLayoutManager manager = new GridLayoutManager(mContext, 3);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(manager);
        adapter = new XAdapter<AppInfor>(mContext, datas) {
            @Override
            protected BaseHolder<AppInfor> initHolder(ViewGroup parent, int viewType) {

                return new BaseHolder<AppInfor>(context, parent, R.layout.loaction_item_install_app) {
                    @Override
                    public void initView(View itemView, int position, final AppInfor data) {
                        super.initView(itemView, position, data);
                        ImageView imgIcon = itemView.findViewById(R.id.img_icon);
                        TextView tv_title = itemView.findViewById(R.id.tv_title);
                        ViewUtils.setText(tv_title, data.appName);
                        ImageLoader.loadImage(mContext, data.getAppIcon(), imgIcon);

                        itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                VirtualSDKUtils.getInstance().launch(data, new VirtualCore.UiCallback() {
                                    @Override
                                    public void onAppOpened(String packageName, int userId) throws RemoteException {
                                        ToastUtils.showTextToast(mContext, "启动成功：" + packageName);
                                    }
                                });
                            }
                        });

                    }
                };
            }
        };
        adapter.addFoot(new PeakHolder(mContext, rv, R.layout.loaction_item_install_app) {
            @Override
            public void initView(View itemView, int position) {
                super.initView(itemView, position);
                ImageView imgIcon = itemView.findViewById(R.id.img_icon);
                TextView tv_title = itemView.findViewById(R.id.tv_title);
                ViewUtils.setText(tv_title, "添加新应用");
                ImageLoader.loadImage(mContext, R.drawable.ic_add_pic, imgIcon);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(mContext, Loacition_PhoneAppdActivity.class));
                    }
                });
            }
        });
        rv.setAdapter(adapter);
        Observable.just("")
                .map(new Function<String, List<AppInfor>>() {
                    @Override
                    public List<AppInfor> apply(String s) throws Exception {
                        List<AppInfor> all = DataSupport.findAll(AppInfor.class);
                        return all;
                    }
                })
                .compose(RxUtils.<List<AppInfor>>applySchedulers())
                .subscribe(new CustomObserver<List<AppInfor>>(this) {
                    @Override
                    public void onNext(List<AppInfor> appInfors) {
                        super.onNext(appInfors);
                        datas.addAll(appInfors);
                        adapter.notifyDataSetChanged();
                    }
                });


    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(List<AppInfor> event) {
        if (event != null && !event.isEmpty()) {

            installAppList(event);

        }
    }


    public void installAppList(final List<AppInfor> list) {
        Observable.just(list)

                .map(new Function<List<AppInfor>, List<AppInfor>>() {

                    @Override
                    public List<AppInfor> apply(List<AppInfor> list) throws Exception {

                        List<AppInfor> successList = new ArrayList<>();


                        for (AppInfor appInfor : list) {
                            VirtualSDKUtils.getInstance().installApk(appInfor);
                            successList.add(appInfor);
                            List<AppInfor> dbList = DataSupport.where("packageName = ?", appInfor.getPackageName()).find(AppInfor.class);
                            if (dbList == null || dbList.isEmpty()) {
                                appInfor.save();
                            } else {
                                appInfor.updateAll("packageName = ?", appInfor.getPackageName());
                            }

                        }

                        return successList;
                    }
                })

                .compose(RxUtils.<List<AppInfor>>applySchedulers())

                .subscribe(new CustomObserver<List<AppInfor>>(this){
                    @Override
                    public void onNext(List<AppInfor> appInfors) {
                        super.onNext(appInfors);
                        datas.addAll(appInfors);
                        adapter.notifyDataSetChanged();
                    }
                });
    }


}
