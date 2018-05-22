package sang.com.minitools.utlis;

import android.content.Context;

/**
 * 作者： ${PING} on 2018/5/22.
 * 转化工具类，主要用来进行数据、单位之间的转化
 */

public class ConvertUtils {

    private static Context context;

    public static void init(Context cnt){
         context=cnt;
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px( float dpValue) {
        if (context==null){
            throw new RuntimeException("尚未调用 init（context）初始化，context ==null");
        }
        return dip2px(context,dpValue);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(  float pxValue) {
        if (context==null){
            throw new RuntimeException("尚未调用 init（context）初始化，context ==null");
        }
        return px2dip (context,pxValue);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
