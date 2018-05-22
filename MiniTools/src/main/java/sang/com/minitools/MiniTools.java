package sang.com.minitools;

import android.content.Context;

import sang.com.minitools.utlis.ConvertUtils;
import sang.com.minitools.utlis.JLog;
import sang.com.minitools.utlis.ToastUtils;

/**
 * 作者： ${PING} on 2018/5/22.
 */

public class MiniTools {

    public static void init(Context context,boolean isDebug){
        ConvertUtils.init(context);
        ToastUtils.init(context);
    }

}
