package sang.com.virtuallocation.virtual;

import android.content.Intent;
import android.os.RemoteException;

import com.lody.virtual.client.core.InstallStrategy;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.os.VUserInfo;
import com.lody.virtual.os.VUserManager;
import com.lody.virtual.remote.InstallResult;
import com.lody.virtual.remote.InstalledAppInfo;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import sang.com.commonlibrary.entity.AppInfor;
import sang.com.commonlibrary.utils.rx.BaseControl;
import sang.com.commonlibrary.utils.rx.RxUtils;
import sang.com.virtuallocation.config.Configs;

/**
 * 作者： ${PING} on 2018/6/4.
 */

public class VirtualInstallUtils {

    public static VirtualInstallUtils utils;
    public static VirtualInstallUtils getInstance(){
        if (utils==null) {
            synchronized (VirtualInstallUtils.class){
                if (utils==null) {
                    utils=new VirtualInstallUtils();
                }
            }
        }
        return utils;
    }

    private VirtualInstallUtils() {
    }

    class AddResult {
        private AppInfor appData;
        private int userId;
        private boolean justEnableHidden;
    }

    public Observable<List<AppInfor>> installAppList(final List<AppInfor> list){
      return  Observable.just("")
                //.observeOn(AndroidSchedulers.mainThread())
               /* .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        baseActivity.showProgressDialog("安装应用中...");
                    }
                })*/
                .map(new Function<String, List<AppInfor>>() {


                    @Override
                    public List<AppInfor> apply(String s) {
                        List<AppInfor> successList = new ArrayList<>();

                        for (int i = 0; i < list.size(); i++) {
                            //String path = list.get(i).getApkFilePath();

                            AddResult addResult = new AddResult();
                            AppInfor appInfor = list.get(i);
                            String packageName = appInfor.getPackageName();

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
                                    throw new IllegalStateException();
                                }
                            }else{
                                InstallResult res = addVirtualApp(list.get(i));
                                if (!res.isSuccess) {
                                    throw new IllegalStateException();
                                }
                            }

                            boolean multipleVersion = addResult.justEnableHidden && addResult.userId != 0;
                            if (!multipleVersion) {
                                handleOptApp(packageName, true);
                                list.get(i).setFirstOpen(true);
                            } else {
                                list.get(i).setUserId(addResult.userId);
                                handleOptApp(packageName, false);
                                list.get(i).setFirstOpen(true);
                            }
                            List<AppInfor> dbList = DataSupport.where("packageName = ?", list.get(i).getPackageName()).find(AppInfor.class);
                            if (dbList == null || dbList.isEmpty()) {
                                list.get(i).save();
                            } else {
                                appInfor.updateAll("packageName = ?", appInfor.getPackageName());
                            }
                            successList.add(appInfor);

                        }

                        return successList;
                    }
                })
                .compose(RxUtils.<List<AppInfor>>applySchedulers())
               ;
    }


    public void launch(AppInfor appInfo, final BaseControl.TaskListener listener) {
        if (listener!=null){
            listener.taskStart(null);
        }
        final int userId = appInfo.getUserId() != 0 ? appInfo.getUserId() : Configs.appUserId;

        final Intent intent = VirtualCore.get().getLaunchIntent(appInfo.getPackageName(), userId);
        if (intent == null) {
            if (listener != null) {
                listener.taskFaile("","");
            }
            return;
        }
        VirtualCore.get().setUiCallback(intent, new VirtualCore.UiCallback() {
            @Override
            public void onAppOpened(String packageName, int userId) throws RemoteException {
                if (listener!=null){
                    listener.taskSuccessed( );
                }
            }
        });

        VActivityManager.get().startActivity(intent, userId);

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
}
