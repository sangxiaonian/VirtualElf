package sang.com.virtuallocation.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import java.util.ArrayList;
import java.util.List;

import sang.com.commonlibrary.base.BaseActivity;
import sang.com.commonlibrary.utils.event.BusFactory;
import sang.com.commonlibrary.xadapter.XAdapter;
import sang.com.commonlibrary.xadapter.holder.BaseHolder;
import sang.com.minitools.utlis.ViewUtils;
import sang.com.virtuallocation.R;

/**
 * 搜索界面
 */
public class Location_SearchActivity extends BaseActivity implements PoiSearch.OnPoiSearchListener {

    private EditText edtSearch;
    private RecyclerView rv;
    private ArrayList<PoiItem> datas;
    private XAdapter<PoiItem> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location__search);
        initView();
        initData();
        initListener();
    }

    @Override
    protected void initView() {
        super.initView();
        edtSearch = findViewById(R.id.edt_search);
        rv = findViewById(R.id.rv);
        findViewById(R.id.bt_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearch();
            }
        });

    }

    @Override
    protected void initData() {
        super.initData();
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(manager);
        datas = new ArrayList<>();

        adapter = new XAdapter<PoiItem>(mContext, datas) {
            @Override
            protected BaseHolder<PoiItem> initHolder(ViewGroup parent, int viewType) {
                return new BaseHolder<PoiItem>(mContext, parent, R.layout.location_item_piosearch) {
                    @Override
                    public void initView(View itemView, int position, final PoiItem data) {
                        super.initView(itemView, position, data);
                        TextView tvTitle = itemView.findViewById(R.id.tv_title);
                        TextView tvContent = itemView.findViewById(R.id.tv_content);
                        ViewUtils.setText(tvTitle, data.getTitle());
                        ViewUtils.setText(tvContent, data.getCityName()+"·"+data.getSnippet());
                        itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                BusFactory.getBus().post(data);
                                finish();
                            }
                        });

                    }
                };
            }
        };
        rv.setAdapter(adapter);


    }

    @Override
    protected void initListener() {
        super.initListener();
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    startSearch();
                    // 当按了搜索之后关闭软键盘
                    ((InputMethodManager) getSystemService(
                            Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;
                }
                return false;
            }
        });
    }

    private void startSearch() {
        String trim = edtSearch.getText().toString().trim();
        if (TextUtils.isEmpty(trim)) {
            datas.clear();
            adapter.notifyDataSetChanged();
           return;
        }
        showLoad();
        int currentPage = 0;
// 第一个参数表示搜索字符串，第二个参数表示POI搜索类型，二选其一
// 第三个参数表示POI搜索区域的编码，必设
        PoiSearch.Query query = new PoiSearch.Query(trim, "", "");
// 设置每页最多返回多少条poiitem
        query.setPageSize(15);
// 设置查第一页
        query.setPageNum(currentPage);
        PoiSearch poiSearch = new PoiSearch(this, query);
//设置搜索完成后的回调
        poiSearch.setOnPoiSearchListener(this);
//进行异步查询
        poiSearch.searchPOIAsyn();
    }

    @Override
    public void onPoiSearched(PoiResult result, int i) {
        hideLoad();
        if (result != null && result.getQuery() != null) {
            List<PoiItem> poiItems = result.getPois();

            datas.clear();
            if (poiItems != null && poiItems.size() > 0) {
                datas.addAll(poiItems);
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {
    }
}
