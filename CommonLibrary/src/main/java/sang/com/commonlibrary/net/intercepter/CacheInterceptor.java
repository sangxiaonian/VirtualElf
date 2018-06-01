package sang.com.commonlibrary.net.intercepter;

import android.content.Context;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import sang.com.minitools.MiniTools;
import sang.com.minitools.utlis.JLog;
import sang.com.minitools.utlis.NetWorkUtils;

/**
 * 作者： ${PING} on 2018/4/26.
 */

public class CacheInterceptor implements Interceptor {

    public static Cache getCache() {
        File cacheFile = new File(MiniTools.context.getCacheDir(), "cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 20); //100Mb
        return cache;
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!NetWorkUtils.isNetworkConnected(MiniTools.context)) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
            JLog.w("no network");
        }
        Response originalResponse = chain.proceed(request);
        if (NetWorkUtils.isNetworkConnected(MiniTools.context)) {
            //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
            String cacheControl = request.cacheControl().toString();
            return originalResponse.newBuilder()
                    .header("Cache-Control", cacheControl)
                    .removeHeader("Pragma")
                    .build();
        } else {
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=2419200")
                    .removeHeader("Pragma")
                    .build();
        }

    }
}
