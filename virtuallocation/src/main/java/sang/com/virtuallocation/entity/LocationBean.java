package sang.com.virtuallocation.entity;

/**
 * 作者： ${PING} on 2018/6/1.
 */

public class LocationBean {

    public String name;
    public double latitude;
    private double longitude;


    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
