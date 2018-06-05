package sang.com.virtuallocation.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import sang.com.commonlibrary.xadapter.XAdapter;
import sang.com.commonlibrary.xadapter.holder.BaseHolder;
import sang.com.minitools.utlis.JLog;
import sang.com.minitools.utlis.ViewUtils;
import sang.com.virtuallocation.R;
import sang.com.virtuallocation.entity.LocationBean;

/**
 * 作者： ${PING} on 2018/6/5.
 */

public class CollectionAdapter extends XAdapter<CollectionAdapter.CollectionBean> {


    public CollectionAdapter(Context context, List<CollectionBean> list) {
        super(context, list);
    }


    private boolean showSelect;
    private   OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setShowSelect(boolean showSelect) {
        this.showSelect = showSelect;
        notifyDataSetChanged();
    }

    public boolean isShowSelect() {
        return showSelect;
    }

    public interface OnItemClickListener{
        void itemClick(CollectionAdapter.CollectionBean bean,int position);
        void itemLongClick(CollectionAdapter.CollectionBean bean,int position);
    }

    /**
     * 初始化ViewHolder,{@link XAdapter#onCreateViewHolder(ViewGroup, int)}处,用于在非头布局\脚布局\刷新时候
     * 调用
     *
     * @param parent   父View,即为RecycleView
     * @param viewType holder类型,在{@link XAdapter#getItemViewType(int)}处使用
     * @return BaseHolder或者其父类
     */
    @Override
    protected BaseHolder<CollectionBean> initHolder(ViewGroup parent, int viewType) {
        return new BaseHolder<CollectionBean>(context, parent, R.layout.location_item_collection) {
            @Override
            public void initView(View itemView, final int position, final CollectionBean data) {
                super.initView(itemView, position, data);
                TextView tvTitle = itemView.findViewById(R.id.tv_title);
                TextView tvContemt = itemView.findViewById(R.id.tv_content);
                CheckBox cbCollect = itemView.findViewById(R.id.cb_collect);
                cbCollect.setChecked(data.isSelect());

                if (data.isSelect()){
                    JLog.i("初始化:"+data.getLocationBean().getCityName()+data.getLocationBean().getName());

                }

                cbCollect.setVisibility(showSelect?View.VISIBLE:View.GONE);

                cbCollect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        data.setSelect(buttonView.isChecked());
                        JLog.i(data.getLocationBean().getCityName()+data.getLocationBean().getName()+":"+isChecked);
                    }
                });

                ViewUtils.setText(tvTitle,data.getLocationBean().getCityName());
                ViewUtils.setText(tvContemt,data.getLocationBean().getName());

                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (!showSelect){
                            setShowSelect(true);
                            if (listener!=null){
                                listener.itemLongClick(data,position);
                            }
                        }
                        return false;
                    }
                });

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener!=null){
                            listener.itemClick(data,position);
                        }
                    }
                });
            }
        };
    }


    public static class CollectionBean {
        private LocationBean locationBean;
        /**
         * 是否选中
         */
        private boolean isSelect;

        public LocationBean getLocationBean() {
            return locationBean;
        }

        public void setLocationBean(LocationBean locationBean) {
            this.locationBean = locationBean;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }
    }

}
