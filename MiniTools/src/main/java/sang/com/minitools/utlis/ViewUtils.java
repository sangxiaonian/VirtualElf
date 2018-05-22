package sang.com.minitools.utlis;

import android.widget.TextView;

/**
 * 作者： ${PING} on 2018/5/22.
 */

public class ViewUtils {

    public static void setText(TextView view,String text ){
        setText(view,text,"");
    }

    public static void setText(TextView view,String text,String defaultValue){
        if (text==null){
            text=defaultValue;
        }
        view.setText(text);
    }

}
