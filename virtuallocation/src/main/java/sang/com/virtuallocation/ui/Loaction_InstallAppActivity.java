package sang.com.virtuallocation.ui;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import sang.com.commonlibrary.base.BaseActivity;
import sang.com.commonlibrary.entity.AppInfor;
import sang.com.commonlibrary.utils.ImageLoader;
import sang.com.commonlibrary.utils.event.BusFactory;
import sang.com.commonlibrary.utils.rx.CustomObserver;
import sang.com.commonlibrary.utils.rx.RxUtils;
import sang.com.commonlibrary.xadapter.XAdapter;
import sang.com.commonlibrary.xadapter.holder.BaseHolder;
import sang.com.commonlibrary.xadapter.holder.PeakHolder;
import sang.com.minitools.utlis.JLog;
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
        GridLayoutManager manager = new GridLayoutManager(mContext, 1);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(manager);
        adapter = new XAdapter<AppInfor>(mContext, datas) {
            @Override
            protected BaseHolder<AppInfor> initHolder(ViewGroup parent, int viewType) {

                return new BaseHolder<AppInfor>(context, parent, R.layout.loaction_item_install_app) {
                    @Override
                    public void initView(final View itemView, final int position, final AppInfor data) {
                        super.initView(itemView, position, data);
                        final ImageView imgIcon = itemView.findViewById(R.id.img_icon);
                        final TextView tv_title = itemView.findViewById(R.id.tv_title);
                        final View llItem = itemView.findViewById(R.id.ll_item);
                        ViewUtils.setText(tv_title, data.appName);
                        ImageLoader.loadImage(mContext, data.getAppIcon(), imgIcon);
                        itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                BusFactory.getBus().postSticky(data);
                                Intent intent = new Intent(mContext, Location_AppDetailActivity.class);


                                Pair<View, String> imgPair = new Pair<>((View) imgIcon, getString(R.string.location_share_img_name));
                                Pair<View, String> titlePair = new Pair<>((View) tv_title, getString(R.string.location_share_bt_name));

                                if ( Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                                    ActivityOptions more = null;
                                        more = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext, imgPair, titlePair);

                                    ActivityOptions single = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext,
                                            imgIcon, getString(R.string.location_share_img_name));
                                    startActivity(
                                            intent, more.toBundle()
                                    );
                                } else {
                                    startActivity(intent);
                                }

                            }
                        });


                        itemView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                builder.setMessage("确定要删除" + data.appName + "?");
                                builder
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(final DialogInterface dialog, int which) {
                                                Observable.just(data)
                                                        .filter(new Predicate<AppInfor>() {
                                                            @Override
                                                            public boolean test(AppInfor appInfor) throws Exception {
                                                                return VirtualSDKUtils.getInstance().unInstall(data);
                                                            }
                                                        })
                                                        .map(new Function<AppInfor, AppInfor>() {
                                                            @Override
                                                            public AppInfor apply(AppInfor appInfor) throws Exception {
                                                                DataSupport.deleteAll(AppInfor.class, "packageName = ?", data.getPackageName());
                                                                return appInfor;
                                                            }
                                                        })
                                                        .compose(RxUtils.<AppInfor>applySchedulers())
                                                        .subscribe(new CustomObserver<AppInfor>(Loaction_InstallAppActivity.this) {
                                                            @Override
                                                            public void onSubscribe(Disposable d) {
                                                                super.onSubscribe(d);
                                                                dialog.dismiss();

                                                            }

                                                            @Override
                                                            public void onNext(AppInfor aBoolean) {
                                                                super.onNext(aBoolean);
                                                                datas.remove(aBoolean);
                                                                adapter.notifyItemDeleted(position);
                                                            }
                                                        });

                                            }
                                        });
                                builder.create().show();
                                return false;
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
        getData(false);
    }

    private void getData(final Boolean clear) {
        Observable.just(clear)
                .map(new Function<Boolean, List<AppInfor>>() {
                    @Override
                    public List<AppInfor> apply(Boolean s) throws Exception {
                        List<AppInfor> all = DataSupport.findAll(AppInfor.class);
                        if (clear) {
                            datas.clear();
                        }
                        return all;
                    }
                })
                .compose(RxUtils.<List<AppInfor>>applySchedulers())
                .subscribe(new CustomObserver<List<AppInfor>>(this) {
                    @Override
                    public void onNext(List<AppInfor> appInfors) {
                        super.onNext(appInfors);
                        for (AppInfor appInfor : appInfors) {
                            JLog.i(appInfor.toString());
                        }
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
        JLog.i("获取到数据了" + event.size());
        if (!event.isEmpty()) {
            installAppList(event);
        }
    }


    public void installAppList(final List<AppInfor> list) {
        Observable.fromIterable(list)
                .map(new Function<AppInfor, AppInfor>() {

                    @Override
                    public AppInfor apply(AppInfor appInfor) throws Exception {
                        return VirtualSDKUtils.getInstance().installApk(appInfor);
                    }
                })
                .filter(new Predicate<AppInfor>() {
                    @Override
                    public boolean test(AppInfor appInfor) throws Exception {
                        return appInfor != null;
                    }
                })
                .map(new Function<AppInfor, AppInfor>() {
                    @Override
                    public AppInfor apply(AppInfor appInfor) throws Exception {
                        List<AppInfor> dbList = DataSupport.where("packageName = ?", appInfor.getPackageName()).find(AppInfor.class);
                        if (dbList == null || dbList.isEmpty()) {
                            appInfor.save();
                        } else {
                            appInfor.updateAll("packageName = ?", appInfor.getPackageName());
                        }
                        return appInfor;
                    }
                })

                .compose(RxUtils.<AppInfor>applySchedulers())

                .subscribe(new CustomObserver<AppInfor>(this) {
                    @Override
                    public void onNext(AppInfor appInfors) {
                        super.onNext(appInfors);
                        datas.add(appInfors);
                        adapter.notifyItemAdd(datas.size());
                    }
                });

    }


}
