package sang.com.virtuallocation.entity;

import sang.com.virtuallocation.config.Configs;

/**
 * 作者： ${PING} on 2018/6/1.
 */

public class RequestBean {


    /**
     * url地址
     */
    private String __url= Configs.LBS;
     /**
     * 请求方法
     */
    private String __method;
    private String mcc;
    private String mnc;
    private String lac;
    private String ci;
    private String output="json";

}
