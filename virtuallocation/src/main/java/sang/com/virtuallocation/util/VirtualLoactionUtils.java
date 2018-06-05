package sang.com.virtuallocation.util;


import com.amap.api.maps2d.CoordinateConverter;
import com.amap.api.maps2d.model.LatLng;
import com.lody.virtual.client.ipc.VirtualLocationManager;
import com.lody.virtual.remote.vloc.VCell;
import com.lody.virtual.remote.vloc.VLocation;
import com.lody.virtual.remote.vloc.VWifi;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
import sang.com.commonlibrary.entity.AppInfor;
import sang.com.commonlibrary.utils.rx.RxUtils;
import sang.com.virtuallocation.entity.CellInfo;
import sang.com.virtuallocation.entity.LocationBean;
import sang.com.virtuallocation.entity.WifiInfo;
import sang.com.virtuallocation.net.VirtualHttpFactory;

/**
 * 作者： ${PING} on 2018/6/1.
 */

public class VirtualLoactionUtils {




    /**
     * 子线程请求，主线程接收
     *
     * @return
     */
    public static ObservableTransformer<LocationBean, LocationBean> changWifiInfor(final AppInfor appInfor) {
        return new ObservableTransformer<LocationBean, LocationBean>() {

            @Override
            public ObservableSource<LocationBean> apply(Observable<LocationBean> upstream) {

                return upstream.map(new Function<LocationBean, LocationBean>() {
                    @Override
                    public LocationBean apply(LocationBean data) throws Exception {
                        List<CellInfo> cellList = data.getCellInfoList();
                        List<WifiInfo> wifiList = data.getWifiInfoList();

                        List<VCell> vCellList = new ArrayList<>();
                        List<VWifi> vWifiList = new ArrayList<>();


                        String packageName = appInfor.getPackageName();

                        for (int i = 0; i < cellList.size(); i++) {
                            vCellList.add(transferCell(cellList.get(i)));
                        }

                        for (int i = 0; i < wifiList.size(); i++) {
                            vWifiList.add(transferWifi(wifiList.get(i)));
                        }

                        if (vCellList.size() > 0) {
                            VirtualLocationManager.get().setCell(appInfor.getUserId(),
                                    appInfor.getPackageName(), vCellList.get(0));
                            VirtualLocationManager.get().setAllCell(appInfor.getUserId(),
                                    appInfor.getPackageName(), vCellList);
                        }

                        VirtualLocationManager.get().setAllWifi(appInfor.getUserId(),
                                packageName, vWifiList);

                        VirtualLocationManager.get().setLocation(appInfor.getUserId(),
                                packageName, transferLocation(data));
                        return data;
                    }
                });


            }


        };
    }

    public static Observable<LocationBean> changeLoactionByCollection(LocationBean resultLoaction, AppInfor appInfor) {
        return Observable
                .just(resultLoaction)
                .compose(VirtualLoactionUtils.<LocationBean>changWifiInfor(appInfor))
                .compose(RxUtils.<LocationBean>applySchedulers());

    }


    /**
     * 根据坐标信息虚拟位置
     *  @param locationBean
     *
     */
    public static Observable<LocationBean> changeLoaction(final LocationBean locationBean, final AppInfor appInfor) {
        Observable<List<CellInfo>> cell = VirtualHttpFactory.reCell(locationBean.getLon(), locationBean.getLat());
        Observable<List<WifiInfo>> wifi = VirtualHttpFactory.reWifi(locationBean.getLon(), locationBean.getLat());
        return Observable
                .zip(cell, wifi, Observable.just(locationBean),new Function3<List<CellInfo>, List<WifiInfo>, LocationBean, LocationBean>() {

                    @Override
                    public LocationBean apply(List<CellInfo> cellInfos, List<WifiInfo> wifiInfos,LocationBean locationBean) throws Exception {
                        locationBean.setCellInfoList(cellInfos);
                        locationBean.setWifiInfoList(wifiInfos);

                        return locationBean;
                    }
                })
                .compose(changWifiInfor(appInfor))
                .compose(RxUtils.<LocationBean>applySchedulers())
                ;


    }

    private static VLocation transferLocation(LocationBean locInfo) {
        if (locInfo == null)
            return null;
        VLocation vLocation = new VLocation();
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.ALIYUN);
        LatLng ll = new LatLng(locInfo.getLon(), locInfo.getLat());
        converter.coord(ll);
        ll = converter.convert();

        vLocation.latitude = ll.latitude;
        vLocation.longitude = ll.longitude;
        vLocation.address = locInfo.getName();
        vLocation.city = locInfo.getCityName();

        return vLocation;
    }

    private static VCell transferCell(CellInfo cellInfo) {
        if (cellInfo == null)
            return null;

        VCell obj = new VCell();
        obj.mcc = 460;
        obj.mnc = cellInfo.getMnc();
        obj.cid = cellInfo.getCi();
        obj.lac = cellInfo.getLac();
        obj.psc = -1;

        return obj;
    }

    private static VWifi transferWifi(WifiInfo wifiInfo) {
        if (wifiInfo == null)
            return null;

        VWifi obj = new VWifi();
        obj.bssid = wifiInfo.getMac();
        obj.level = wifiInfo.getAcc();

        return obj;
    }
}
