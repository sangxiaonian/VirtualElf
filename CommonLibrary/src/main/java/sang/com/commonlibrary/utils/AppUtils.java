package sang.com.commonlibrary.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import java.io.File;
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
                myAppInfo.setVersionCode(packageInfo.versionCode);
                myAppInfo.setVersionName(packageInfo.versionName);
                myAppInfo.setApkFilePath(packageInfo.applicationInfo.sourceDir);
                if (packageInfo.applicationInfo.loadIcon(packageManager) == null) {
                    continue;
                }
                myAppInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(packageManager));

                myAppInfos.add(myAppInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JLog.e("===============获取应用包信息失败");
        }
        return myAppInfos;
    }


    /**
     * 只遍历第二层,找到手机中安装的APP,以及APK路径
     * @param context
     * @param rootDir
     * @return
     */
    public static List<AppInfor> findAndParseAPKs(Context context, String rootDir) {
        List<AppInfor> infoList = new ArrayList<>();
        if(rootDir == null)
            return infoList;

        final File rootFile = new File(rootDir);
        if(!rootFile.exists())
            return infoList;

        if(rootFile.isDirectory()){
            File[] dirFiles = rootFile.listFiles();
            if (dirFiles == null)
                return infoList;

            for (File f : dirFiles) {
                AppInfor appInfor = getAppInfor(context, f);
                if (appInfor!=null){
                    infoList.add(appInfor);
                }
            }
        }else{
            AppInfor appInfor = getAppInfor(context, rootFile);
            if (appInfor!=null){
                infoList.add(appInfor);
            }
        }

        return infoList;
    }

    private static AppInfor getAppInfor(Context context, File f){
        if (!f.getName().toLowerCase().endsWith(".apk"))
            return null;

        AppInfor appInfo = new AppInfor();
        try {
            PackageInfo pkgInfo = context.getPackageManager().getPackageArchiveInfo(f.getAbsolutePath(), 0);
            //pkgInfo.applicationInfo.sourceDir = f.getAbsolutePath();
            //pkgInfo.applicationInfo.publicSourceDir = f.getAbsolutePath();
            appInfo.setAppName(f.getName());

            appInfo.setPackageName(pkgInfo.applicationInfo.packageName);
            Drawable icon = pkgInfo.applicationInfo.loadIcon(context.getPackageManager());
            appInfo.setAppIcon(icon);
            appInfo.setApkFilePath(f.getAbsolutePath());

        } catch (Exception e) {
            // Ignore
        }
        return appInfo;
    }


    /**
     * 获取SD卡目录
     * @return
     */
    public static File getSDPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir;

    }







}
