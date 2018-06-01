package sang.com.virtuallocation.net;


import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Query;
import sang.com.commonlibrary.utils.rx.RxUtils;
import sang.com.virtuallocation.entity.CellInfo;
import sang.com.virtuallocation.entity.RequestBean;
import sang.com.virtuallocation.entity.WifiInfo;

/**
 * 作者： ${PING} on 2018/6/1.
 */

public class VirtualHttpFactory {


    /**
     * 根据经纬度反查所在位置附近的WIFI热点MAC地址
     *
     * @param lat
     * @param lon
     */
    public static Observable<List<WifiInfo>> reWifi(String url,double lat, double lon) {
        url=url+"/rewifi/";
        return VirtualHttpClient.getClient().getService()
                .rewifi(url,lat, lon, 10, "bd09", "bd09")
                .compose(RxUtils.<List<WifiInfo>>applySchedulers());

    }

    /**
     * 根据经纬度反查所在位置附近的基站编号
     *
     * @param lat
     * @param lon
     */
    public static Observable<List<CellInfo>> reCell(String url,double lat, double lon) {
        url=url+"/recell/";
        return VirtualHttpClient.getClient().getService()
                .recell(url,lat, lon, 3, -1, 1, "bd09", "bd09")
                .compose(RxUtils.<List<CellInfo>>applySchedulers())
                ;
    }

    /**
     * 根据经纬度反查所在位置附近的WIFI热点MAC地址
     *
     * @param lat
     * @param lon
     */
    public static Observable<List<WifiInfo>> reWifi(double lat, double lon) {

        RequestBean bean = new RequestBean();



        return VirtualHttpClient.getClient().getService()
                .rewifi(bean)
                .compose(RxUtils.<List<WifiInfo>>applySchedulers());

    }

    /**
     * 根据经纬度反查所在位置附近的基站编号
     *
     * @param lat
     * @param lon
     */
    public static Observable<List<CellInfo>> reCell(double lat, double lon) {
        RequestBean bean = new RequestBean();

        return VirtualHttpClient.getClient().getService()
                .recell(bean)
                .compose(RxUtils.<List<CellInfo>>applySchedulers())
                ;
    }


}
