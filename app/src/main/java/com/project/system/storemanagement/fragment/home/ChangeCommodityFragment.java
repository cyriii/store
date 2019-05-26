package com.project.system.storemanagement.fragment.home;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.project.system.storemanagement.R;
import com.project.system.storemanagement.base.BaseBackFragment;
import com.project.system.storemanagement.base.BaseBean;
import com.project.system.storemanagement.bean.GoodsBean;
import com.project.system.storemanagement.event.ChangeGoosEvent;
import com.project.system.storemanagement.http.ApiServiceResult;
import com.project.system.storemanagement.http.Client;
import com.project.system.storemanagement.http.DialogTransformer;
import com.project.system.storemanagement.http.RxsRxSchedulers;
import com.project.system.storemanagement.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.project.system.storemanagement.R.string.input_commodity_name;

/**
 * 修改商品
 */
public class ChangeCommodityFragment extends BaseBackFragment {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_commodity_name)
    EditText etCommodityName;
    @BindView(R.id.et_commodity_unit)
    EditText etCommodityUnit;
    @BindView(R.id.et_commodity_remark)
    EditText etCommodityRemark;

    private int position;
    private GoodsBean goodsBean;

    public static ChangeCommodityFragment newInstance(int position, GoodsBean goodsBean) {
        ChangeCommodityFragment fragment = new ChangeCommodityFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("goodsBean", goodsBean);
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_add_commodity;
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
        initToolbarNav(toolbar);

        toolbar.setTitle("");
        title.setText(getString(R.string.change_commodity));

        position = getArguments().getInt("position");
        goodsBean = (GoodsBean) getArguments().getSerializable("goodsBean");

        etCommodityName.setText(goodsBean.getName());
        etCommodityUnit.setText(goodsBean.getUnit());
        etCommodityRemark.setText(goodsBean.getRemark());
    }

    @OnClick(R.id.btn_commit)
    public void onViewClicked() {
        String name = etCommodityName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            ToastUtils.showShortToast(getResources().getString(input_commodity_name));
            return;
        }
        String unit = etCommodityUnit.getText().toString().trim();
        if (TextUtils.isEmpty(unit)) {
            ToastUtils.showShortToast(getResources().getString(R.string.input_commodity_unit));
            return;
        }
        String remark = etCommodityRemark.getText().toString().trim();
        if (TextUtils.isEmpty(remark)) {
            ToastUtils.showShortToast(getResources().getString(R.string.input_commodity_remark));
            return;
        }
        Map<String, String> map = new HashMap();
        map.put("id", goodsBean.getId());
        map.put("name", name);
        map.put("unit", unit);
        map.put("remark", remark);
        dialogTransformer = new DialogTransformer(_mActivity);
        Client.getApiService().changeGoods(map)
                .compose(RxsRxSchedulers.<BaseBean>io_main())
                .compose(dialogTransformer.transformer())
                .subscribe(new ApiServiceResult(getComposite()) {

                    @Override
                    public void onFinish(BaseBean bean) {
                        goodsBean.setName(name);
                        goodsBean.setUnit(unit);
                        goodsBean.setRemark(remark);
                        EventBus.getDefault().post(new ChangeGoosEvent(position, goodsBean));
                        ToastUtils.showShortToast(R.string.change_success);
                        _mActivity.onBackPressed();
                    }
                });
    }


}
