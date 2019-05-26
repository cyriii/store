package com.project.system.storemanagement.fragment.manage;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.project.system.storemanagement.R;
import com.project.system.storemanagement.adapter.SupplierAdapter;
import com.project.system.storemanagement.base.BaseBackFragment;
import com.project.system.storemanagement.base.BaseBean;
import com.project.system.storemanagement.bean.GoodsDataBean;
import com.project.system.storemanagement.http.ApiServiceResult;
import com.project.system.storemanagement.http.Client;
import com.project.system.storemanagement.http.DialogTransformer;
import com.project.system.storemanagement.http.RxsRxSchedulers;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class CustomerListFragment extends BaseBackFragment {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recycler)
    RecyclerView recycler;

    private SupplierAdapter adapter;

    public static CustomerListFragment newInstance() {
        CustomerListFragment fragment = new CustomerListFragment();
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_select;
    }

    @Override
    protected View setToolBarView() {
        return toolbar;
    }

    @Override
    protected int statusBarColor() {
        return R.color.transparent;
    }

    protected void initView() {
        initToolbarNav(toolbar);

        toolbar.setTitle("");
        title.setText(getString(R.string.customerlist));

        setRefreshLayout();

        recycler.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new SupplierAdapter();

        View emptyView = View.inflate(_mActivity, R.layout.empty_order, null);
        adapter.setEmptyView(emptyView);
        recycler.setAdapter(adapter);

        adapter.setOnItemClickListener((adapter1, view, position) -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", adapter.getData().get(position));
            setFragmentResult(RESULT_OK, bundle);
            _mActivity.onBackPressed();
        });

        getData();
    }


    private void getData() {
        Map<String, Object> map = new HashMap();
        map.put("current", String.valueOf(page));
        map.put("size", "10");
        dialogTransformer = new DialogTransformer(_mActivity);

        Client.getApiService().customers(map)
                .compose(RxsRxSchedulers.<BaseBean>io_main())
                .compose(dialogTransformer.transformer())
                .subscribe(new ApiServiceResult<GoodsDataBean>(getComposite()) {
                    @Override
                    public void onFinish(BaseBean<GoodsDataBean> bean) {
                        if (bean != null && bean.getData() != null) {
                            if (bean.getData().getRecords() != null && !bean.getData().getRecords().isEmpty())
                                if (page == 1) {
                                    adapter.setNewData(bean.getData().getRecords());
                                } else
                                    adapter.addData(bean.getData().getRecords());

                            if (page < bean.getData().getPages()) {
                                page++;
                            } else {
                                refreshLayout.setEnableLoadMore(false);
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
}
