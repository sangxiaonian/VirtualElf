package sang.com.virtuallocation.net;


import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;
import sang.com.commonlibrary.utils.rx.RxUtils;
import sang.com.virtuallocation.config.Configs;
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
                .rewifi(url,lat, lon, 10, "gcj02", "gcj02")
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
                .recell(url,lat, lon, 3, -1, 1, "gcj02", "gcj02")
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
        bean.set__method("get");
        bean.set__url(Configs.LBS+"rewifi/");
        bean.setLat((float) lat);
        bean.setLon((float) lon);
        bean.setN(10);
        bean.setIncoord("gcj02");
        bean.setCoord("gcj02");


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
        bean.set__method("get");
        bean.set__url(Configs.LBS+"recell/");
        bean.setLat((float) lat);
        bean.setLon((float) lon);
        bean.setN(3);
        bean.setR("1");
        bean.setMnc(-1);
        bean.setIncoord("gcj02");
        bean.setCoord("gcj02");
        return VirtualHttpClient.getClient().getService()
                .recell(bean)
                .compose(RxUtils.<List<CellInfo>>applySchedulers())
                ;
    }


}
