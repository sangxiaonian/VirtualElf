package sang.com.virtuallocation.ui;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import sang.com.commonlibrary.base.BaseActivity;
import sang.com.commonlibrary.utils.event.BusFactory;
import sang.com.minitools.utlis.ToastUtils;
import sang.com.virtuallocation.R;
import sang.com.virtuallocation.entity.LocationBean;
import sang.com.virtuallocation.map.MapUtils;

/**
 * 地图界面
 */
public class Loaction_MapActivity extends BaseActivity implements MapUtils.OnLoactionChangeListener, GeocodeSearch.OnGeocodeSearchListener {

    private MapView mMapView;
    private GeocodeSearch geocodeSearch;
    TextView tvLoaction;
    private TextView edtSearch;
    private TextView btSelect;
    private LocationBean locationBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loaction__map);
        setToolTitle("选择位置");

        initView();
        initData();
        initListener();
        mMapView.onCreate(savedInstanceState);
        MapUtils.getInstance().initLoaction(this, mMapView).setLoactionLisetner(this);


    }


    @Override
    protected void initView() {
        super.initView();
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);

        tvLoaction = findViewById(R.id.tv_location);
        edtSearch = findViewById(R.id.edt_search);
        btSelect = findViewById(R.id.bt_select);

    }

    @Override
    protected void initData() {
        super.initData();
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
//初始化地图控制器对象
        geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(this);
    }

    @Override
    protected void initListener() {
        super.initListener();
        btSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationBean != null) {
                    BusFactory.getBus().post(locationBean);
                    finish();
                } else {
                    ToastUtils.showTextToast("坐标转换中，请稍后");
                }
            }
        });


        edtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, Location_SearchActivity.class));
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLocationChange(Location amapLocation) {

    }

    /**
     * 坐标转换完成，获取到具体位置
     */
    @Override
    public void onFinishChange(LatLng latLng) {

        LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 500f, GeocodeSearch.AMAP);
        //异步查询
        showLoad();
        geocodeSearch.getFromLocationAsyn(query);

    }


    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        hideLoad();
        RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
        String formatAddress = regeocodeAddress.getFormatAddress();
        tvLoaction.setText("地址：\n" + formatAddress);
        LatLonPoint point = regeocodeResult.getRegeocodeQuery().getPoint();
        if (locationBean == null) {
            locationBean = new LocationBean();
        }
        locationBean.setLatitude(point.getLatitude());
        locationBean.setLongitude(point.getLongitude());
        locationBean.setName(formatAddress);
        locationBean.setCityName(regeocodeAddress.getCity());


    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PoiItem event) {
        if (event != null) {
            AMap aMap = mMapView.getMap();
            LatLonPoint cameraPosition = event.getLatLonPoint();
            LatLng target = new LatLng(cameraPosition.getLatitude(), cameraPosition.getLongitude());
            List<Marker> markers = aMap.getMapScreenMarkers();
            if (markers.size() > 0) {
                Marker marker = markers.get(0);
                marker.setPosition(target);
            }
//            aMap.setLoadOfflineData(true);

            aMap.moveCamera(CameraUpdateFactory.newLatLng(target));

            if (locationBean == null) {
                locationBean = new LocationBean();
            }
            locationBean.setLatitude(cameraPosition.getLatitude());
            locationBean.setLongitude(cameraPosition.getLongitude());
            locationBean.setName(event.getTitle());
            locationBean.setCityName(event.getCityName());


        }
    }

}
