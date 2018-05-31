package sang.com.virtuallocation.map;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorCreator;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.xiaosu.lib.permission.OnRequestPermissionsCallBack;
import com.xiaosu.lib.permission.PermissionCompat;

import java.util.List;

import mirror.android.app.Activity;
import sang.com.minitools.utlis.JLog;
import sang.com.virtuallocation.R;

/**
 * 作者： ${PING} on 2018/5/31.
 */

public class MapUtils {

    private static MapUtils mapUtils;
    private MyLocationStyle myLocationStyle;
    private OnLoactionChangeListener loactionLisetner;

    public static MapUtils getInstance() {
        if (mapUtils == null) {
            synchronized (MapUtils.class) {
                if (mapUtils == null) {
                    mapUtils = new MapUtils();
                }
            }
        }
        return mapUtils;
    }


    public MapUtils initLoaction(final Context activity, final MapView mMapView) {


        PermissionCompat.create(activity)
                .permissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE)

//                .permissions(Manifest.permission_group.LOCATION)
                .explain("请允许使用定位权限,以获取您当前位置信息")
                .retry(true)
                .callBack(new OnRequestPermissionsCallBack() {
                    @Override
                    public void onGrant() {

//初始化地图控制器对象

                        final AMap aMap = mMapView.getMap();
                        aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
                        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
                            @Override
                            public void onCameraChange(CameraPosition cameraPosition) {
                                LatLng target = cameraPosition.target;
                                List<Marker> markers = aMap.getMapScreenMarkers();
                                if (markers.size() > 0) {
                                    Marker marker = markers.get(0);
                                    marker.setPosition(target);
                                }


                            }

                            @Override
                            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                                if (loactionLisetner != null) {
                                    LatLng target = cameraPosition.target;
                                    loactionLisetner.onFinishChange(target);
                                }
                            }
                        });

                        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
                            @Override
                            public void onMyLocationChange(Location location) {
                                List<Marker> markers = aMap.getMapScreenMarkers();
                                if (markers.size() < 2) {
                                    MarkerOptions markerOptions = new MarkerOptions();
                                    markerOptions.position(new LatLng(location.getLatitude(), location.getLongitude())).title("标题").snippet("内容");
                                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                            .decodeResource(activity.getResources(), R.mipmap.ic_dest_loc)));//设置图标
                                }

                                if (loactionLisetner != null) {
                                    loactionLisetner.onLocationChange(location);
                                }
                            }
                        });

                        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。

                        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);//定位一次，且将视角移动到地图中心点。
                        myLocationStyle.strokeColor(Color.TRANSPARENT).radiusFillColor(Color.TRANSPARENT);
                        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
                        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
                        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
                        aMap.getUiSettings().setZoomControlsEnabled(false);


                    }

                    @Override
                    public void onDenied(String permission, boolean retry) {
                        if (!retry) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                            dialog.setTitle("注意")
                                    .setMessage("请手动开启定位权限,已完成相关定位功能")
                                    .setNegativeButton("", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .create()
                                    .show();
                        }
                    }
                })
                .build()
                .request();


        return this;
    }

    public MapUtils setLoactionLisetner(OnLoactionChangeListener loactionLisetner) {
        this.loactionLisetner = loactionLisetner;
        return this;
    }

    public interface OnLoactionChangeListener {
        void onLocationChange(Location location);

        /**
         * 坐标转换完成，获取到具体位置
         *
         * @param location
         */
        void onFinishChange(LatLng location);
    }

    /**
     * 设置定位蓝点图标的锚点方法
     *
     * @param u
     * @param v
     */
    public void moveLoaction(float u, float v) {
        if (myLocationStyle != null) {
            myLocationStyle.anchor(u, v);//设置定位蓝点图标的锚点方法。
        }
    }

}
