package sang.com.virtuallocation.virtual;

import com.lody.virtual.client.core.InstallStrategy;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.remote.InstallResult;

import java.io.IOException;

import sang.com.minitools.utlis.JLog;

/**
 * APP安装卸载监听
 */

public class VirAppRequestListener implements VirtualCore.AppRequestListener {

    //private final Context context;

    public VirAppRequestListener() {
        //this.context = context;
    }

    @Override
    public void onRequestInstall(String path) {
        //Toast.makeText(context, "Installing: " + path, Toast.LENGTH_SHORT).show();
        JLog.d("Installing: " + path);

        InstallResult res = VirtualCore.get().installPackage(path, InstallStrategy.UPDATE_IF_EXIST);
        if (res.isSuccess) {
            try {
                VirtualCore.get().preOpt(res.packageName);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (res.isUpdate) {
                //RxBus.getDefault().post(new InstallAppStatusEvent(res.packageName,InstallAppStatus.UPDATE_SUCCESS));
                JLog.d("Update: " + res.packageName + " success!");
                //Toast.makeText(context, "Update: " + res.packageName + " success!", Toast.LENGTH_SHORT).show();
            } else {
                JLog.d("Install: " + res.packageName + " success!");
                //RxBus.getDefault().post(new InstallAppStatusEvent(res.packageName,InstallAppStatus.INSTALL_SUCCESS));
                //Toast.makeText(context, "Install: " + res.packageName + " success!", Toast.LENGTH_SHORT).show();
            }
        } else {
            //Toast.makeText(context, "Install failed: " + res.error, Toast.LENGTH_SHORT).show();
            JLog.d("Install failed: " + res.error);
            //RxBus.getDefault().post(new InstallAppStatusEvent(res.packageName,InstallAppStatus.INSTALL_FAIL));
        }
    }

    @Override
    public void onRequestUninstall(String pkg) {
        //Toast.makeText(context, "Uninstall: " + pkg, Toast.LENGTH_SHORT).show();
        JLog.d("Uninstall: " + pkg);
        //RxBus.getDefault().post(new InstallAppStatusEvent(pkg,InstallAppStatus.UNINSTALL_SUCCESS));
    }
}
