package sang.com.virtuallocation.entity;

import org.litepal.crud.DataSupport;

/**
 * @author pengl
 */

public class WifiInfo extends DataSupport{

    /**
     * mac : bc:d1:77:16:21:84
     * acc : 100
     * location : {"lon":116.346825,"lat":39.527309}
     */

    private String mac;
    private int acc;
    private LocationBean location;

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getAcc() {
        return acc;
    }

    public void setAcc(int acc) {
        this.acc = acc;
    }

    public LocationBean getLocation() {
        return location;
    }

    public void setLocation(LocationBean location) {
        this.location = location;
    }

}
