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
import sang.com.commonlibrary.entity.AppInfor;
import sang.com.commonlibrary.utils.rx.RxUtils;
import sang.com.virtuallocation.entity.CellInfo;
import sang.com.virtuallocation.entity.LoactionInfor;
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
    public static ObservableTransformer<LoactionInfor, LoactionInfor> changWifiInfor(final AppInfor appInfor) {
        return new ObservableTransformer<LoactionInfor, LoactionInfor>() {

            @Override
            public ObservableSource<LoactionInfor> apply(Observable<LoactionInfor> upstream) {

                return upstream.map(new Function<LoactionInfor, LoactionInfor>() {
                    @Override
                    public LoactionInfor apply(LoactionInfor data) throws Exception {
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
                                packageName, transferLocation(data.getLocationInfo()));
                        return data;
                    }
                });


            }


        };
    }

    public static Observable<LoactionInfor> changeLoaction(LoactionInfor resultLoaction, AppInfor appInfor) {
        return Observable
                .just(resultLoaction)
                .compose(VirtualLoactionUtils.<LoactionInfor>changWifiInfor(appInfor))
                .compose(RxUtils.<LoactionInfor>applySchedulers());

    }


    /**
     * 根据坐标信息虚拟位置
     *  @param locationBean
     *
     */
    public static Observable<LoactionInfor> changeLoaction(final LocationBean locationBean, final AppInfor appInfor) {
        Observable<List<CellInfo>> cell = VirtualHttpFactory.reCell(locationBean.getLatitude(), locationBean.getLongitude());
        Observable<List<WifiInfo>> wifi = VirtualHttpFactory.reWifi(locationBean.getLatitude(), locationBean.getLongitude());
        return Observable
                .zip(cell, wifi, new BiFunction<List<CellInfo>, List<WifiInfo>, LoactionInfor>() {

                    @Override
                    public LoactionInfor apply(List<CellInfo> cellInfos, List<WifiInfo> wifiInfos) throws Exception {
                        LoactionInfor loaction = new LoactionInfor();
                        loaction.setCellInfoList(cellInfos);
                        loaction.setWifiInfoList(wifiInfos);
                        loaction.setLocationInfo(locationBean);

                        return loaction;
                    }
                })
                .compose(changWifiInfor(appInfor))
                .compose(RxUtils.<LoactionInfor>applySchedulers())
                ;


    }

    private static VLocation transferLocation(LocationBean locInfo) {
        if (locInfo == null)
            return null;
        VLocation vLocation = new VLocation();
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.ALIYUN);
        LatLng ll = new LatLng(locInfo.getLatitude(), locInfo.getLongitude());
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
