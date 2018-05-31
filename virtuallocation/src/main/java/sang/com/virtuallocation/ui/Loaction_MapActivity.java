package sang.com.virtuallocation.ui;

import android.content.Context;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import java.util.List;

import sang.com.commonlibrary.base.BaseActivity;
import sang.com.minitools.utlis.JLog;
import sang.com.virtuallocation.R;
import sang.com.virtuallocation.map.MapUtils;

public class Loaction_MapActivity extends BaseActivity implements MapUtils.OnLoactionChangeListener, GeocodeSearch.OnGeocodeSearchListener, PoiSearch.OnPoiSearchListener {

    private MapView mMapView;
    private GeocodeSearch geocodeSearch;
    TextView tvLoaction;
    private EditText edtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loaction__map);
        setToolTitle("选择位置");
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);

        tvLoaction = findViewById(R.id.tv_location);
        edtSearch = findViewById(R.id.edt_search);

        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String trim = edtSearch.getText().toString().trim();
                    if (!TextUtils.isEmpty(trim)) {
                        startSearch(trim);
                    }
                    // 当按了搜索之后关闭软键盘
                    ((InputMethodManager) getSystemService(
                            Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;
                }
                return false;
            }
        });


        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
//初始化地图控制器对象
        geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(this);
        MapUtils.getInstance().initLoaction(this, mMapView).setLoactionLisetner(this);

    }

    private void startSearch(String trim) {
        int currentPage = 0;
// 第一个参数表示搜索字符串，第二个参数表示POI搜索类型，二选其一
// 第三个参数表示POI搜索区域的编码，必设
        PoiSearch.Query query = new PoiSearch.Query(trim, "", "");
// 设置每页最多返回多少条poiitem
        query.setPageSize(1);
// 设置查第一页
        query.setPageNum(currentPage);
        PoiSearch poiSearch = new PoiSearch(this, query);
//设置搜索完成后的回调
        poiSearch.setOnPoiSearchListener(this);
//进行异步查询
        poiSearch.searchPOIAsyn();
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
        geocodeSearch.getFromLocationAsyn(query);
    }


    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
        String formatAddress = regeocodeAddress.getFormatAddress();
        String simpleAddress = formatAddress.substring(9);

        tvLoaction.setText("查询经纬度对应详细地址：\n" + simpleAddress);
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    @Override
    public void onPoiSearched(PoiResult result, int i) {
        if (result != null && result.getQuery() != null) {
            List<PoiItem> poiItems = result.getPois();
            if (poiItems != null && poiItems.size() > 0) {
                // 清理之前的图标，将查询结果显示在地图上
                AMap aMap = mMapView.getMap();

                LatLonPoint cameraPosition = poiItems.get(0).getLatLonPoint();
                LatLng target =new LatLng( cameraPosition .getLatitude(),cameraPosition.getLongitude());
                List<Marker> markers = aMap.getMapScreenMarkers();
                if (markers.size()>0){
                    Marker marker = markers.get(0);
                    marker.setPosition(target);
                }
                aMap.setLoadOfflineData(true);
                aMap.moveCamera(CameraUpdateFactory.newLatLng(target));
            }
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {
    }
}
