package sang.com.commonlibrary.utils;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import sang.com.commonlibrary.entity.AppInfor;
import sang.com.minitools.utlis.JLog;

/**
 * 作者： ${PING} on 2018/5/22.
 */

public class AppUtils {

    /**
     * 获取本地已经安装的APP信息
     *
     * @param packageManager
     * @param addSysAPP      是否添加系统APP true 添加 ，false 不添加
     * @return
     */
    public static List<AppInfor> scanLocalInstallAppList(PackageManager packageManager, boolean addSysAPP) {
        List<AppInfor> myAppInfos = new ArrayList<AppInfor>();
        try {
            List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
            for (int i = 0; i < packageInfos.size(); i++) {
                PackageInfo packageInfo = packageInfos.get(i);
                //过滤掉系统app
                if ((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags) != 0 && !addSysAPP) {
                    continue;
                }

                AppInfor myAppInfo = new AppInfor();
                myAppInfo.setPackageName(packageInfo.packageName);
                myAppInfo.setAppName(packageInfo.applicationInfo.loadLabel(packageManager).toString());
                if (packageInfo.applicationInfo.loadIcon(packageManager) == null) {
                    continue;
                }
                myAppInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(packageManager));

                myAppInfos.add(myAppInfo);
            }
        } catch (Exception e) {
            JLog.e("===============获取应用包信息失败");
        }
        return myAppInfos;
    }


}
