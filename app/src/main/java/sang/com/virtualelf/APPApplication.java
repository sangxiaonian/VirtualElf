package sang.com.virtualelf;

import android.content.Context;

import org.litepal.LitePalApplication;

import sang.com.minitools.MiniTools;
import sang.com.virtuallocation.virtual.VirtualSDKUtils;

/**
 * 作者： ${PING} on 2018/5/23.
 */

public class APPApplication extends LitePalApplication {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        VirtualSDKUtils.getInstance().start(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        VirtualSDKUtils.getInstance().initVirtualSDK();
        MiniTools.init(this, BuildConfig.DEBUG);
    }


}
