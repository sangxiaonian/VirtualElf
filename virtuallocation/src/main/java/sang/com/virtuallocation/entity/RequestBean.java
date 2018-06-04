package sang.com.virtuallocation.entity;

import sang.com.virtuallocation.config.Configs;

/**
 * 作者： ${PING} on 2018/6/1.
 */

public class RequestBean {


    /**
     * url地址
     */
    private String __url = Configs.LBS;
    /**
     * 请求方法
     */
    private String __method;
    private String mcc;
    private String lac;
    private String ci;
    private String output = "json";

    /**
     * 纬度
     */
    private float lat;
    /**
     * 纬度
     */
    private float lon;
    /**
     * 否	返回基站个数
     */
    private int n;
    /**
     * 否	选择运营商，-1:全部（默认），0:移动，1:联通，3:电信
     */
    private int mnc;
    /**
     * 否	返回半径r公里范围内的基站，默认值1
     */
    private String r;

    /**
     * 否	输入坐标类型(wgs84/gcj02/bd09)，默认wgs84
     */
    private String incoord;

    /**
     * 否	输出坐标类型(wgs84/gcj02/bd09)，默认wgs84
     */
    private String coord;


    public String get__url() {
        return __url;
    }

    public void set__url(String __url) {
        this.__url = __url;
    }

    public String get__method() {
        return __method;
    }

    public void set__method(String __method) {
        this.__method = __method;
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getMnc() {
        return mnc;
    }

    public void setMnc(int mnc) {
        this.mnc = mnc;
    }

    public String getR() {
        return r;
    }

    public void setR(String r) {
        this.r = r;
    }

    public String getIncoord() {
        return incoord;
    }

    public void setIncoord(String incoord) {
        this.incoord = incoord;
    }

    public String getCoord() {
        return coord;
    }

    public void setCoord(String coord) {
        this.coord = coord;
    }

    public String getLac() {
        return lac;
    }

    public void setLac(String lac) {
        this.lac = lac;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }
}
