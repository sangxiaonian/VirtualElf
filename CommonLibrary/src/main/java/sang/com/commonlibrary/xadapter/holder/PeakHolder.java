package sang.com.commonlibrary.xadapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 作者： ${PING} on 2017/8/29.
 */

public class PeakHolder<T> extends RecyclerView.ViewHolder {


    private   Context context;
    /**
     * holder 的根View
     */
    protected View itemView;

    public PeakHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
    }

    public PeakHolder(Context context, int layoutID) {
        this(context,null,layoutID);

    }

    public PeakHolder(Context context, ViewGroup parent, int layoutID) {
        this(LayoutInflater.from(context).inflate(layoutID, parent, false));
        this.context = context;
    }


    /**
     * 获取holer 的根View
     *
     * @return
     */
    public View getItemView() {
        return itemView;
    }

    /**
     * 初始化数据
     *
     * @param position 在所有HeadeView中的位置
     */
    public void initView(int position) {

    }
}
