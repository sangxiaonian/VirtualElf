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

    public String getMnc() {
        return mnc;
    }

    public void setMnc(String mnc) {
        this.mnc = mnc;
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
