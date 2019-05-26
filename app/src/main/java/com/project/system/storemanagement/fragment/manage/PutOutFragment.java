package com.project.system.storemanagement.fragment.manage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.project.system.storemanagement.R;
import com.project.system.storemanagement.adapter.PutOutAdapter;
import com.project.system.storemanagement.base.BaseBean;
import com.project.system.storemanagement.base.MySupportFragment;
import com.project.system.storemanagement.bean.GoodsDataBean;
import com.project.system.storemanagement.event.AddOutEvent;
import com.project.system.storemanagement.event.ChangeOutEvent;
import com.project.system.storemanagement.fragment.MainFragment;
import com.project.system.storemanagement.fragment.ManageFragment;
import com.project.system.storemanagement.http.ApiServiceResult;
import com.project.system.storemanagement.http.Client;
import com.project.system.storemanagement.http.DialogTransformer;
import com.project.system.storemanagement.http.RxsRxSchedulers;
import com.project.system.storemanagement.utils.DialogUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PutOutFragment extends MySupportFragment {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recycler)
    RecyclerView recycler;

    private PutOutAdapter adapter;

    private AlertDialog dialog;

    private String searchStr;

    public static PutOutFragment newInstance() {
        PutOutFragment fragment = new PutOutFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_store, container, false);
            unbinder = ButterKnife.bind(this, mRootView);
            initView();
        } else {
            unbinder = ButterKnife.bind(this, mRootView);
        }

        return mRootView;
    }

    private void initView() {
        setRefreshLayout();

        recycler.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new PutOutAdapter();

        View emptyView = View.inflate(_mActivity, R.layout.empty_order, null);
        adapter.setEmptyView(emptyView);
        recycler.setAdapter(adapter);

        adapter.setOnItemClickListener((adapter1, view, position) -> {
            ((MainFragment) getParentFragment().getParentFragment()).startBrotherFragment(ChangeOutFragment.newInstance(position, adapter.getData().get(position)));
        });

        adapter.setOnItemLongClickListener((adapter1, view, position) -> {
            showDialog(position);
            return false;
        });

        getData();
    }

    private void showDialog(int position) {
        View view = View.inflate(_mActivity, R.layout.layout_dialog, null);
        TextView des = view.findViewById(R.id.tv_description);
        des.setText(R.string.deleteOut);
        view.findViewById(R.id.tv_cancle).setOnClickListener(v -> {
            dialog.dismiss();
            dialog = null;
        });

        view.findViewById(R.id.tv_confirm).setOnClickListener(v -> {
            deleteGoods(position, adapter.getData().get(position).getId());
            dialog.dismiss();
            dialog = null;
        });
        dialog = new DialogUtils().showDialog(_mActivity, view, false);
    }

    /**
     * 删除
     *
     * @param position
     * @param id
     */
    private void deleteGoods(int position, String id) {
        Map<String, String> map = new HashMap();
        map.put("id", id);
        dialogTransformer = new DialogTransformer(_mActivity);
        Client.getApiService().delete("/store/out/" + id)
                .compose(RxsRxSchedulers.<BaseBean>io_main())
                .compose(dialogTransformer.transformer())
                .subscribe(new ApiServiceResult(getComposite()) {

                    @Override
                    public void onFinish(BaseBean bean) {
                        adapter.remove(position);
                    }
                });
    }

    private void getData() {
        Map<String, Object> map = new HashMap();
        map.put("current", String.valueOf(page));
        map.put("size", "10");
        dialogTransformer = new DialogTransformer(_mActivity);

        searchStr = ((ManageFragment) getParentFragment()).getSearchStr();
        if (!TextUtils.isEmpty(searchStr)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", searchStr);
            map.put("params", jsonObject);
        }
        adapter.setSearchStr(searchStr);
        Client.getApiService().storeOuts(map)
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


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addOutEvent(AddOutEvent event) {
        page = 1;
        getData();
        refreshLayout.setEnableLoadMore(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeOutEvent(ChangeOutEvent event) {
        adapter.getData().set(event.getPosition(), event.getGoodsBean());
        adapter.notifyItemChanged(event.getPosition());
    }

}
