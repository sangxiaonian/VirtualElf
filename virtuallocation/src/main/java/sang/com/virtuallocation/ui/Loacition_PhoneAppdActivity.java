package sang.com.virtuallocation.ui;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import sang.com.commonlibrary.base.BaseActivity;
import sang.com.commonlibrary.entity.AppInfor;
import sang.com.commonlibrary.utils.AppUtils;
import sang.com.commonlibrary.utils.ImageLoader;
import sang.com.commonlibrary.utils.rx.CustomObserver;
import sang.com.commonlibrary.utils.rx.RxUtils;
import sang.com.commonlibrary.xadapter.XAdapter;
import sang.com.commonlibrary.xadapter.holder.BaseHolder;
import sang.com.minitools.utlis.JLog;
import sang.com.minitools.utlis.ToastUtils;
import sang.com.minitools.utlis.ViewUtils;
import sang.com.virtuallocation.R;
import sang.com.virtuallocation.entity.SelectBean;

/**
 * 获取手机上的应用列表
 */
public class Loacition_PhoneAppdActivity extends BaseActivity {

    RecyclerView rv;

    private List<SelectBean<AppInfor>> infors;
    private XAdapter<SelectBean<AppInfor>> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loacition_phone_app);
        setToolTitle(getString(R.string.loaction_app));
        initView();
        initData();
        initListener();
    }

    @Override
    protected void initView() {
        super.initView();
        rv = findViewById(R.id.rv);
    }

    @Override
    protected void initListener() {
        super.initListener();
        setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<AppInfor> list =new ArrayList<>();
                for (SelectBean<AppInfor> infor : infors) {
                    if (infor.isCheck()&&infor.getT()!=null){
                        list.add(infor.getT());
                    }
                }
                ToastUtils.showTextToast(mContext,list.size()+"");
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        setRightText(getString(R.string.loaction_install));
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(manager);
        infors = new ArrayList<>();
        adapter = new XAdapter<SelectBean<AppInfor>>(mContext, infors) {
            @Override
            protected BaseHolder<SelectBean<AppInfor>> initHolder(ViewGroup parent, int viewType) {
                return new BaseHolder<SelectBean<AppInfor>>(mContext, parent, R.layout.loaction_item_installed) {
                    @Override
                    public void initView(View itemView, int position, final SelectBean<AppInfor> data) {
                        super.initView(itemView, position, data);
                        ImageView imgIcon = itemView.findViewById(R.id.img_icon);
                        TextView tvTitle = itemView.findViewById(R.id.tv_title);
                        CheckBox checkBox = itemView.findViewById(R.id.checkbox);
                        checkBox.setChecked(data.isCheck());
                        ViewUtils.setText(tvTitle, data.getT().appName);
                        ImageLoader.loadImage(mContext, data.getT().getAppIcon(), imgIcon);
                        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                data.setCheck(isChecked);
                            }
                        });
                    }
                };
            }
        };
        rv.setAdapter(adapter);

        Observable.just(getPackageManager())
                .map(new Function<PackageManager, List<AppInfor>>() {
                    @Override
                    public List<AppInfor> apply(PackageManager packageManager) throws Exception {
                        return AppUtils.scanLocalInstallAppList(packageManager, false);
                    }
                })
                .flatMap(new Function<List<AppInfor>, Observable<AppInfor>>() {

                    @Override
                    public Observable<AppInfor> apply(List<AppInfor> appInfors) throws Exception {
                        return Observable.fromIterable(appInfors);
                    }
                })
                .map(new Function<AppInfor, SelectBean<AppInfor>>() {
                    @Override
                    public SelectBean<AppInfor> apply(AppInfor appInfor) throws Exception {
                        SelectBean<AppInfor> selectBean = new SelectBean<>();
                        selectBean.setT(appInfor);
                        return selectBean;
                    }
                })
                .compose(RxUtils.<SelectBean<AppInfor>>applySchedulers())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        adapter.notifyDataSetChanged();
                    }
                })
                .subscribe(new CustomObserver<SelectBean<AppInfor>>(this) {
                    @Override
                    public void onNext(SelectBean<AppInfor> appInforSelectBean) {
                        super.onNext(appInforSelectBean);
                        infors.add(appInforSelectBean);
                    }

                });

    }
}
