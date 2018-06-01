package sang.com.virtuallocation.util;


import android.content.Context;

import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.model.LatLng;
import com.lody.virtual.client.ipc.VirtualLocationManager;
import com.lody.virtual.remote.vloc.VCell;
import com.lody.virtual.remote.vloc.VLocation;
import com.lody.virtual.remote.vloc.VWifi;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import sang.com.commonlibrary.entity.AppInfor;
import sang.com.commonlibrary.utils.rx.BaseControl;
import sang.com.commonlibrary.utils.rx.RxUtils;
import sang.com.virtuallocation.config.Configs;
import sang.com.virtuallocation.entity.CellInfo;
import sang.com.virtuallocation.entity.LocationBean;
import sang.com.virtuallocation.entity.WifiInfo;
import sang.com.virtuallocation.net.VirtualHttpFactory;

/**
 * 作者： ${PING} on 2018/6/1.
 */

public class VirtualLoactionUtils {


    public static class ResultLoaction {

        private List<CellInfo> cellInfoList;

        private List<WifiInfo> wifiInfoList;

        private LocationBean locationInfo;

        public List<CellInfo> getCellInfoList() {
            return cellInfoList;
        }

        public void setCellInfoList(List<CellInfo> cellInfoList) {
            this.cellInfoList = cellInfoList;
        }

        public List<WifiInfo> getWifiInfoList() {
            return wifiInfoList;
        }

        public void setWifiInfoList(List<WifiInfo> wifiInfoList) {
            this.wifiInfoList = wifiInfoList;
        }

        public LocationBean getLocationInfo() {
            return locationInfo;
        }

        public void setLocationInfo(LocationBean locationInfo) {
            this.locationInfo = locationInfo;
        }
    }


    /**
     * 根据坐标信息虚拟位置
     *  @param locationBean
     * @param listener
     * @param context
     */
    public static   Observable<ResultLoaction> changeLoaction(final LocationBean locationBean, final AppInfor appInfor, final Context context) {
        Observable<List<CellInfo>> cell = VirtualHttpFactory.reCell(locationBean.getLatitude(), locationBean.getLongitude());
        Observable<List<WifiInfo>> wifi = VirtualHttpFactory.reWifi(locationBean.getLatitude(), locationBean.getLongitude());

       return Observable
                .zip(cell, wifi, new BiFunction<List<CellInfo>, List<WifiInfo>, ResultLoaction>() {

                    @Override
                    public ResultLoaction apply(List<CellInfo> cellInfos, List<WifiInfo> wifiInfos) throws Exception {
                        ResultLoaction loaction = new ResultLoaction();
                        loaction.setCellInfoList(cellInfos);
                        loaction.setWifiInfoList(wifiInfos);
                        loaction.setLocationInfo(locationBean);
                        return loaction;
                    }
                })
                .map(new Function<ResultLoaction, ResultLoaction>() {
                    @Override
                    public ResultLoaction apply(ResultLoaction data) throws Exception {
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
                            VirtualLocationManager.get().setCell(Configs.appUserId,
                                    appInfor.getPackageName(), vCellList.get(0));
                            VirtualLocationManager.get().setAllCell(Configs.appUserId,
                                    appInfor.getPackageName(), vCellList);
                        }

                        VirtualLocationManager.get().setAllWifi(Configs.appUserId,
                                packageName, vWifiList);

                        VirtualLocationManager.get().setLocation(Configs.appUserId,
                                packageName, transferLocation(data.getLocationInfo(), context));
                        return data;
                    }
                })
                .compose(RxUtils.<ResultLoaction>applySchedulers())
               ;


    }

    private static VLocation transferLocation(LocationBean locInfo, Context context) {
        if (locInfo == null)
            return null;

        VLocation vLocation = new VLocation();

        CoordinateConverter converter = new CoordinateConverter(context);
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
