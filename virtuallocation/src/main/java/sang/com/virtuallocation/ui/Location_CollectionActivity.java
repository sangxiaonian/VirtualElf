package sang.com.virtuallocation.ui;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import sang.com.commonlibrary.base.BaseActivity;
import sang.com.commonlibrary.entity.AppInfor;
import sang.com.commonlibrary.utils.event.BusFactory;
import sang.com.commonlibrary.utils.rx.CustomObserver;
import sang.com.commonlibrary.utils.rx.RxUtils;
import sang.com.minitools.utlis.JLog;
import sang.com.virtuallocation.R;
import sang.com.virtuallocation.entity.CellInfo;
import sang.com.virtuallocation.entity.LocationBean;
import sang.com.virtuallocation.entity.WifiInfo;
import sang.com.virtuallocation.ui.adapter.CollectionAdapter;

/**
 * 收藏地址列表
 */
public class Location_CollectionActivity extends BaseActivity implements View.OnClickListener {

    RecyclerView rv;
    private List<CollectionAdapter.CollectionBean> datas;
    private CollectionAdapter adapter;
    private LinearLayout llCancle;
    private LinearLayout llDeleted;
    private LinearLayout llBow;
    private int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location__collection);
        setToolTitle(getString(R.string.location_my_collection));
        initView();
        initData();
        initListener();


    }

    @Override
    protected void initView() {
        super.initView();
        rv = findViewById(R.id.rv);
        llCancle = findViewById(R.id.ll_cancle);
        llDeleted = findViewById(R.id.ll_deleted);
        llBow = findViewById(R.id.ll_bow);
        llBow.post(new Runnable() {
            @Override
            public void run() {
                height = llBow.getHeight();
                showBow(false);
            }
        });

    }

    @Override
    protected void initData() {
        super.initData();
        datas = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(manager);
        adapter = new CollectionAdapter(mContext, datas);
        rv.setAdapter(adapter);
        queryAllData();

    }

    @Override
    protected void initListener() {
        super.initListener();
        llDeleted.setOnClickListener(this);
        llCancle.setOnClickListener(this);
        adapter.setListener(new CollectionAdapter.OnItemClickListener() {
            @Override
            public void itemClick(CollectionAdapter.CollectionBean bean, int position) {
                if (adapter.isShowSelect()) {
                    bean.setSelect(!bean.isSelect());
                    adapter.notifyItemChanged(position, bean);
                } else {
                    BusFactory.getBus().post(bean.getLocationBean());
                    finish();
                }
            }

            @Override
            public void itemLongClick(CollectionAdapter.CollectionBean bean, int position) {
                showBow(true);
            }
        });
    }

    ValueAnimator animator;

    private void showBow(final boolean show) {
        if (animator == null) {
            animator = ValueAnimator.ofInt(0, height);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    ViewGroup.LayoutParams params = llBow.getLayoutParams();
                    int animatedValue = (int) animation.getAnimatedValue();
                    params.height = animatedValue;
                    llBow.setLayoutParams(params);

                }
            });
        }

        if (show) {
            animator.setIntValues(llBow.getHeight(), height);
        } else {
            animator.setIntValues(llBow.getHeight(), 0);
        }
        animator.setDuration(200).start();
    }


    @Override
    public void onBackPressed() {
        if (adapter.isShowSelect()) {
            llCancle.performClick();
        } else {
            super.onBackPressed();
        }

    }

    public void queryAllData() {
        Observable.just("")
                .map(new Function<String, List<LocationBean>>() {
                    @Override
                    public List<LocationBean> apply(String s) throws Exception {
                        return DataSupport.findAll(LocationBean.class);
                    }
                })
                .map(new Function<List<LocationBean>, List<CollectionAdapter.CollectionBean>>() {
                    @Override
                    public List<CollectionAdapter.CollectionBean> apply(List<LocationBean> locationBeans) throws Exception {
                        List<CollectionAdapter.CollectionBean> beans = new ArrayList<>();
                        for (LocationBean locationBean : locationBeans) {
                            CollectionAdapter.CollectionBean bean = new CollectionAdapter.CollectionBean();
                            bean.setLocationBean(locationBean);

                            List<WifiInfo> wifiInfos = DataSupport.where("name=?",  locationBean.getName()).find(WifiInfo.class);
                            List<CellInfo> cellInfos = DataSupport.where("name=?",  locationBean.getName()).find(CellInfo.class);
                            locationBean.setCellInfoList(cellInfos);
                            locationBean.setWifiInfoList(wifiInfos);
                            JLog.i(locationBean.toString());


                            beans.add(bean);
                        }
                        return beans;
                    }
                })
                .compose(RxUtils.<List<CollectionAdapter.CollectionBean>>applySchedulers())
                .subscribe(new CustomObserver<List<CollectionAdapter.CollectionBean>>(this) {
                    @Override
                    public void onNext(List<CollectionAdapter.CollectionBean> collectionBeans) {
                        super.onNext(collectionBeans);
                        datas.addAll(collectionBeans);
                        adapter.notifyDataSetChanged();
                    }
                });
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ll_cancle) {
            adapter.setShowSelect(false);
            showBow(false);

        } else if (i == R.id.ll_deleted) {
            deleted();
            adapter.setShowSelect(false);
            showBow(false);
        }
    }


    private void deleted() {
        Observable.just(datas)
                .map(new Function<List<CollectionAdapter.CollectionBean>, List<CollectionAdapter.CollectionBean>>() {
                    @Override
                    public List<CollectionAdapter.CollectionBean> apply(List<CollectionAdapter.CollectionBean> collectionBeans) throws Exception {
                        List<CollectionAdapter.CollectionBean> deleted = new ArrayList<>();
                        for (CollectionAdapter.CollectionBean bean : collectionBeans) {
                            if (bean.isSelect()) {
                                bean.getLocationBean().delete();

                                deleted.add(bean);
                            }
                        }
                        return deleted;
                    }
                })
                .compose(RxUtils.<List<CollectionAdapter.CollectionBean>>applySchedulers())
                .subscribe(new CustomObserver<List<CollectionAdapter.CollectionBean>>(this){
                    @Override
                    public void onNext(List<CollectionAdapter.CollectionBean> collectionBeans) {
                        super.onNext(collectionBeans);
                        datas.removeAll(collectionBeans);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}
