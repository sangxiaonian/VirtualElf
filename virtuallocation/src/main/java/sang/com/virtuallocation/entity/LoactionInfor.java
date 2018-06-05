package sang.com.virtuallocation.entity;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * 位置信息，请求下来之后获取到的位置相关信息
 */
public class LoactionInfor extends DataSupport {

    private List<CellInfo> cellInfoList;

    private List<WifiInfo> wifiInfoList;

    private LocationBean locationInfo;

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

    public LocationBean getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(LocationBean locationInfo) {
        this.locationInfo = locationInfo;
    }
}