package com.project.system.storemanagement.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.project.system.storemanagement.R;
import com.project.system.storemanagement.adapter.InfoAdapter;
import com.project.system.storemanagement.base.BaseBean;
import com.project.system.storemanagement.base.BaseMainFragment;
import com.project.system.storemanagement.bean.GoodsDataBean;
import com.project.system.storemanagement.http.ApiServiceResult;
import com.project.system.storemanagement.http.Client;
import com.project.system.storemanagement.http.DialogTransformer;
import com.project.system.storemanagement.http.RxsRxSchedulers;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 信息查询
 */
public class InfoFragment extends BaseMainFragment {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.et_search)
    EditText etSearch;

    private int page = 1;

    private InfoAdapter adapter;

    private String searchStr;

    public static InfoFragment newInstance() {
        InfoFragment fragment = new InfoFragment();
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_info;
    }

    @Override
    protected View setToolBarView() {
        return toolbar;
    }

    @Override
    protected int statusBarColor() {
        return R.color.transparent;
    }

    @Override
    protected void initView() {
        super.initView();

        toolbar.setTitle("");
        title.setText(getString(R.string.info_des));

        setRefreshLayout();

        recycler.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new InfoAdapter();

        View emptyView = View.inflate(_mActivity, R.layout.empty_order, null);
        adapter.setEmptyView(emptyView);
        recycler.setAdapter(adapter);

        getData();
    }

    @OnClick(R.id.tv_search)
    public void onViewClicked() {
        search();
    }

    public void search() {
        page = 1;
        getData();
        refreshLayout.setEnableLoadMore(true);
    }

    private void setRefreshLayout() {
        refreshLayout.setEnableLoadMore(true);
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setOnRefreshListener(refreshLayout1 -> {
            page = 1;
            getData();
            refreshLayout.setEnableLoadMore(true);
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout1 -> {
            getData();
        });
    }

    private void getData() {
        Map<String, Object> map = new HashMap();
        map.put("current", String.valueOf(page));
        map.put("size", "10");
        dialogTransformer = new DialogTransformer(_mActivity);
        searchStr = etSearch.getText().toString().trim();
        if (!TextUtils.isEmpty(searchStr)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", searchStr);
            map.put("params", jsonObject);
        }
        adapter.setSearchStr(searchStr);

        Client.getApiService().stores(map)
                .compose(RxsRxSchedulers.<BaseBean>io_main())
                .compose(dialogTransformer.transformer())
                .subscribe(new ApiServiceResult<GoodsDataBean>(getComposite()) {
                    @Override
                    public void onFinish(BaseBean<GoodsDataBean> bean) {
                        if (bean != null && bean.getData() != null) {
                            if (bean.getData().getRecords() != null && !bean.getData().getRecords().isEmpty()) {
                                if (page == 1) {
                                    adapter.setNewData(bean.getData().getRecords());
                                } else
                                    adapter.addData(bean.getData().getRecords());
                            } else {
                                if (page == 1) {
                                    adapter.setNewData(null);
                                }
                            }

                            if (page < bean.getData().getPages()) {
                                page++;
                            } else {
                                refreshLayout.setEnableLoadMore(false);
                            }
                        } else {
                            if (page == 1) {
                                adapter.setNewData(null);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        if (refreshLayout != null) {
                            refreshLayout.finishLoadMore();
                            refreshLayout.finishRefresh();
                        }
                    }

                    @Override
                    public void onNext(BaseBean<GoodsDataBean> bean) {
                        super.onNext(bean);
                        if (refreshLayout != null) {
                            refreshLayout.finishLoadMore();
                            refreshLayout.finishRefresh();
                        }
                    }
                });
    }

}
