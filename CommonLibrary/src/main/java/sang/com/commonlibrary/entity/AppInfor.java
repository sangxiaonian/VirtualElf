package sang.com.commonlibrary.entity;

import android.graphics.drawable.Drawable;

/**
 * 作者： ${PING} on 2018/5/22.
 * App 信息
 */

public class AppInfor {

    /**
     * App 包名
     */
    public String packageName;

    /**
     * App 名称
     */
    public String appName;

    /**
     * App 图标
     */
    public Drawable appIcon;


    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }
}
