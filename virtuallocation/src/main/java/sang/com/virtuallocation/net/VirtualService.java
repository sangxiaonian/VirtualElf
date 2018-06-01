package sang.com.virtuallocation.net;


import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;
import sang.com.virtuallocation.entity.CellInfo;
import sang.com.virtuallocation.entity.RequestBean;
import sang.com.virtuallocation.entity.WifiInfo;

/**
 * 作者： ${PING} on 2018/6/1.
 */

public interface VirtualService {


    /**
     * 根据经纬度反查所在位置附近的基站编号
     *
     * @param lat     纬度
     * @param lon     经度
     * @param number  返回基站个数，范围1-10
     * @param mnc     选择运营商，-1:全部（默认），0:移动，1:联通，3:电信
     * @param radius  返回半径r公里范围内的基站，默认值1
     * @param incoord 输入坐标类型(wgs84/gcj02/bd09)，默认wgs84
     * @param coord   输入坐标类型(wgs84/gcj02/bd09)，默认wgs84
     * @return
     */
    @GET("recell")
    Observable<List<CellInfo>> recell(@Url String url,
                                      @Query("lat") double lat,
                                      @Query("lon") double lon,
                                      @Query("n") int number,
                                      @Query("mnc") int mnc,
                                      @Query("r") int radius,
                                      @Query("incoord") String incoord,
                                      @Query("coord") String coord);

    /**
     * 根据经纬度反查所在位置附近的WIFI热点MAC地址
     *
     * @param lat     纬度
     * @param lon     经度
     * @param number  返回基站个数，范围1-10
     * @param incoord 输入坐标类型(wgs84/gcj02/bd09)，默认wgs84
     * @param coord   输入坐标类型(wgs84/gcj02/bd09)，默认wgs84
     * @return
     */
    @GET("rewifi")
    Observable<List<WifiInfo>> rewifi(@Url String url,
                                      @Query("lat") double lat,
                                      @Query("lon") double lon,
                                      @Query("n") int number,
                                      @Query("incoord") String incoord,
                                      @Query("coord") String coord);

    /**
     * 根据经纬度反查所在位置附近的基站编号 通过服务器进行转接
     *
     * @return
     */
    @GET("recell")
    Observable<List<CellInfo>> recell(@Body RequestBean bean);

    /**
     * 根据经纬度反查所在位置附近的WIFI热点MAC地址通过服务器进行转接
     *
     * @return
     */
    @GET("rewifi")
    Observable<List<WifiInfo>> rewifi(@Body RequestBean bean);

}
