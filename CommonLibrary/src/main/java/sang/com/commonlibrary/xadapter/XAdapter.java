package sang.com.commonlibrary.xadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.List;

import sang.com.commonlibrary.xadapter.holder.BaseHolder;
import sang.com.commonlibrary.xadapter.holder.PeakHolder;


/**
 * 作者： ${PING} on 2017/9/4.
 * 带看记录使用的ViewPager
 */

public abstract class XAdapter<T> extends RecyclerView.Adapter {

    public Context context;
    protected List<T> list;
    protected List<PeakHolder> heads;
    protected List<PeakHolder> foots;
    protected final int HEADTYPE = 100000;
    protected final int FOOTTYPE = 200000;

    public XAdapter(Context context, List<T> list) {
        this.context = context;
        this.list = list;
        heads = new ArrayList<>();
        foots = new ArrayList<>();
    }


    public void addHeard(PeakHolder heardHolder) {
        heads.add(heardHolder);
    }

    public void removeHeard(PeakHolder heardHolder) {
        heads.remove(heardHolder);
    }

    public void removeHeard(int index) {
        heads.remove(index);
    }

    public void addFoot(PeakHolder heardHolder) {
        foots.add(heardHolder);
    }

    public void addFoot(int index, PeakHolder heardHolder) {
        foots.add(index, heardHolder);
    }

    public List<PeakHolder> getFoots() {
        return foots;
    }

    public void removeFoot(int index) {
        foots.remove(index);
    }

    public void removeFoot(PeakHolder heardHolder) {

        if (foots.contains(heardHolder)) {
            foots.remove(heardHolder);
            notifyDataSetChanged();
        }
    }


    public void notifyItemAdd(int position) {
        position += heads.size();
        notifyItemInserted(position);
        notifyItemRangeChanged(position-1, getItemCount() - position);
    }

    public void notifyItemDeleted(int position) {
        position += heads.size();
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount() - position);
    }


    @Override
    public int getItemViewType(int position) {
        if (position < heads.size()) {
            return position + HEADTYPE;
        } else if (position >= heads.size() + list.size()) {
            return FOOTTYPE + position - heads.size() - list.size();
        } else {
            position -= heads.size();
        }

        return getViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        if (viewType >= HEADTYPE && viewType < FOOTTYPE) {
            return heads.get(viewType - HEADTYPE);
        } else if (viewType >= FOOTTYPE) {
            return foots.get(viewType - FOOTTYPE);

        } else {
            return initHolder(parent, viewType);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int viewType = getItemViewType(position);
        if (viewType >= HEADTYPE) {//脚布局
            PeakHolder holder1 = (PeakHolder) holder;
            holder1.initView(holder1.getItemView(),viewType - HEADTYPE);
        } else if (viewType >= FOOTTYPE) {//头布局
            PeakHolder holder1 = (PeakHolder) holder;
            holder1.initView(holder1.getItemView(), viewType - FOOTTYPE);
        } else {//一般布局
            position -= heads.size();
            BaseHolder holder1 = (BaseHolder) holder;
            holder1.initView(holder1.getItemView(), position, list.get(position));
        }


    }


    @Override
    public int getItemCount() {
        return list.size() + heads.size() + foots.size();
    }

    /**
     * 初始化ViewHolder,{@link XAdapter#onCreateViewHolder(ViewGroup, int)}处,用于在非头布局\脚布局\刷新时候
     * 调用
     *
     * @param parent   父View,即为RecycleView
     * @param viewType holder类型,在{@link XAdapter#getItemViewType(int)}处使用
     * @return BaseHolder或者其父类
     */
    protected abstract BaseHolder<T> initHolder(ViewGroup parent, final int viewType);

    /**
     * 初始化XAdapter 的viewType,且此处已经经过处理,去除Header等的影响,可以直接从0开始使用
     *
     * @param position 当前item的Position(从0开始)
     * @return
     */
    public int getViewType(int position) {
        return 0;
    }


    public T getItemData(int position) {
        return list.get(position);
    }


    public List<PeakHolder> getHeads() {
        return heads;
    }
}
