package sang.com.minitools;

import android.content.Context;

import sang.com.minitools.utlis.ConvertUtils;
import sang.com.minitools.utlis.JLog;
import sang.com.minitools.utlis.ToastUtils;

/**
 * 作者： ${PING} on 2018/5/22.
 */

public class MiniTools {

    public static Context context;

    public static void init(Context cnt,boolean isDebug){
        context=cnt;
        ConvertUtils.init(cnt);
        ToastUtils.init(cnt);
    }

}
