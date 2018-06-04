package sang.com.virtuallocation.virtual;

import android.content.Context;
import android.content.Intent;

import com.lody.virtual.client.core.InstallStrategy;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.os.VUserInfo;
import com.lody.virtual.os.VUserManager;
import com.lody.virtual.remote.InstallResult;
import com.lody.virtual.remote.InstalledAppInfo;

import java.io.IOException;

import sang.com.commonlibrary.entity.AppInfor;

/**
 * 作者： ${PING} on 2018/5/23.
 */

public class VirtualSDKUtils {

    private static VirtualSDKUtils utils;

    public static VirtualSDKUtils getInstance() {
        if (utils == null) {
            synchronized (VirtualSDKUtils.class) {
                if (utils == null) {
                    utils = new VirtualSDKUtils();
                }
            }
        }
        return utils;
    }

    private VirtualSDKUtils() {
    }

    public void start(Context context) {
        try {
            VirtualCore.get().startup(context);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void initVirtualSDK() {
        final VirtualCore virtualCore = VirtualCore.get();
        virtualCore.initialize(new VirtualCore.VirtualInitializer() {

            @Override
            public void onMainProcess() {
            }

            @Override
            public void onVirtualProcess() {
                //listener components
                //virtualCore.setComponentDelegate(new MyComponentDelegate());
                //fake phone imei,macAddress,BluetoothAddress
                //virtualCore.setPhoneInfoDelegate(new MyPhoneInfoDelegate());
                //fake task description's icon and title
                //virtualCore.setTaskDescriptionDelegate(new MyTaskDescriptionDelegate());
            }

            @Override
            public void onServerProcess() {
                virtualCore.setAppRequestListener(new VirAppRequestListener());
                virtualCore.addVisibleOutsidePackage("com.tencent.mobileqq");
                virtualCore.addVisibleOutsidePackage("com.tencent.mobileqqi");
                virtualCore.addVisibleOutsidePackage("com.tencent.minihd.qq");
                virtualCore.addVisibleOutsidePackage("com.tencent.qqlite");
                virtualCore.addVisibleOutsidePackage("com.facebook.katana");
                virtualCore.addVisibleOutsidePackage("com.whatsapp");
                virtualCore.addVisibleOutsidePackage("com.tencent.mm");
                virtualCore.addVisibleOutsidePackage("com.immomo.momo");
            }
        });
    }


    class AddResult {
        private AppInfor appData;
        private int userId;
        private boolean justEnableHidden;
    }

    /**
     * 安装App
     * @param appInfo
     * @return 安装失败返回null，否则返回appinfor
     */
    public AppInfor installApk(AppInfor appInfo) {

        AddResult addResult = new AddResult();
        String packageName = appInfo.getPackageName();

        InstalledAppInfo installedAppInfo = VirtualCore.get().getInstalledAppInfo(packageName, 0);
        addResult.justEnableHidden = installedAppInfo != null;
        if (addResult.justEnableHidden) {
            int[] userIds = installedAppInfo.getInstalledUsers();
            int nextUserId = userIds.length;
                            /*
                              Input : userIds = {0, 1, 3}
                              Output: nextUserId = 2
                             */
            for (int j = 0; j < userIds.length; j++) {
                if (userIds[j] != j) {
                    nextUserId = j;
                    break;
                }
            }
            addResult.userId = nextUserId;

            if (VUserManager.get().getUserInfo(nextUserId) == null) {
                // user not exist, create it automatically.
                String nextUserName = "Space " + (nextUserId + 1);
                VUserInfo newUserInfo = VUserManager.get().createUser(nextUserName, VUserInfo.FLAG_ADMIN);
                if (newUserInfo == null) {
                    throw new IllegalStateException();
                }
            }
            boolean success = VirtualCore.get().installPackageAsUser(nextUserId, packageName);
            if (!success) {
                return null;
            }
        } else {
            InstallResult res = addVirtualApp(appInfo);
            if (!res.isSuccess) {
                return null;
            }
        }

        boolean multipleVersion = addResult.justEnableHidden && addResult.userId != 0;
        if (!multipleVersion) {
            handleOptApp(packageName, true);
            appInfo.setFirstOpen(true);
        } else {
            appInfo.setUserId(addResult.userId);
            handleOptApp(packageName, false);
            appInfo.setFirstOpen(true);
        }

        return appInfo;

    }

    public InstallResult addVirtualApp(AppInfor info) {
        int flags = InstallStrategy.COMPARE_VERSION | InstallStrategy.SKIP_DEX_OPT;
        /*if (info.fastOpen) {
            flags |= InstallStrategy.DEPEND_SYSTEM_IF_EXIST;
        }*/
        return VirtualCore.get().installPackage(info.getApkFilePath(), flags);
    }

    private void handleOptApp(final String packageName, final boolean needOpt) {
        long time = System.currentTimeMillis();
        if (needOpt) {
            try {
                VirtualCore.get().preOpt(packageName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        time = System.currentTimeMillis() - time;
        if (time < 1500L) {
            try {
                Thread.sleep(1500L - time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * 启动APP
     *
     * @param appInfo
     * @param callback
     */
    public void launch(AppInfor appInfo, VirtualCore.UiCallback callback) {
        final int userId = appInfo.getUserId();

        final Intent intent = VirtualCore.get().getLaunchIntent(appInfo.getPackageName(), userId);
        if (intent == null)
            return;

        VirtualCore.get().setUiCallback(intent, callback);
        VActivityManager.get().startActivity(intent, userId);

    }

    /**
     * 卸载APP
     *
     * @param appInfor
     */
    public boolean unInstall(AppInfor appInfor) {
        return VirtualCore.get().uninstallPackage(appInfor.packageName);
    }
}
