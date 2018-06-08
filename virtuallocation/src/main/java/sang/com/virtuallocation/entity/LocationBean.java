package sang.com.virtuallocation.entity;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： ${PING} on 2018/6/1.
 */

public class LocationBean extends DataSupport {

    public int id;
    public String name;
    public double lon;
    public double lat;
    public String cityName;
    public List<CellInfo> cellInfoList = new ArrayList<>();

    private List<WifiInfo> wifiInfoList = new ArrayList<>();


    @Override
    public synchronized int delete() {

        for (WifiInfo wifiInfo : wifiInfoList) {
            wifiInfo.delete();
        }
        for (CellInfo cellInfo : cellInfoList) {
            cellInfo.delete();
        }
        return super.delete();
    }

    @Override
    public synchronized boolean saveOrUpdate(String... conditions) {

        if (!cellInfoList.isEmpty()) {
            for (CellInfo cellInfo : cellInfoList) {
                cellInfo.setName(name);
                cellInfo.save();
            }
        }
        if (!wifiInfoList.isEmpty()) {
            for (WifiInfo cellInfo : wifiInfoList) {
                cellInfo.setName(name);
                cellInfo.save();
            }
        }


        return super.saveOrUpdate(conditions);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "LocationBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lon=" + lon +
                ", lat=" + lat +
                ", cityName='" + cityName + '\'' +
                ", cellInfoList=" + cellInfoList +
                ", wifiInfoList=" + wifiInfoList +
                '}';
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

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
}
