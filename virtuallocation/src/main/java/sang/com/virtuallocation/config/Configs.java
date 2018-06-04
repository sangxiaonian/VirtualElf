package sang.com.virtuallocation.config;

/**
 * 作者： ${PING} on 2018/6/1.
 */

public class Configs {

    /**
     * 服务器万能的转接接口，但是目前只适配了json
     */
    public static final String baseUrl="http://apis.ascwh.com/";



    /**
     * LBS官方接口，用来获取到相应的基站和WiFi数据，目前禁止手机直接访问，因此增加转接功能
     * http://www.cellocation.com/interfac/
     */
    public static final String LBS="http://api.cellocation.com:81/";
    public static int appUserId=0;
}
