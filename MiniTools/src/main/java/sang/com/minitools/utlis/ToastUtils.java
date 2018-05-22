package sang.com.minitools.utlis;

import android.content.Context;
import android.widget.Toast;


/**
 * Toast 的简单工具类，防止段时间内多次弹出 Toast 引起Toast长时间存在，不能消失
 */
public class ToastUtils {
    private static Toast toast = null;
    private static Context context;

    public static void init(Context cnt) {
        context = cnt;
    }

    public static void showTextToast(String msg) {
        if (context==null){
            throw new RuntimeException("尚未调用 init（context）初始化，context ==null");
        }
        showTextToast(context, msg);
    }

    public static void showTextToast(int stringId) {
        if (context==null){
            throw new RuntimeException("尚未调用 init（context）初始化，context ==null");
        }
        showTextToast(context, context.getString(stringId));
    }


    public static void showTextToast(Context context, String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    public static void showTextToast(Context context, int msg) {
        showTextToast(context, context.getString(msg));

    }


}