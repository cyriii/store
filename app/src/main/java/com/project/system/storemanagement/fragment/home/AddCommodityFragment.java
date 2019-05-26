package com.project.system.storemanagement.fragment.home;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.project.system.storemanagement.R;
import com.project.system.storemanagement.base.BaseBackFragment;
import com.project.system.storemanagement.base.BaseBean;
import com.project.system.storemanagement.event.AddGoosEvent;
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
 * 添加商品
 */
public class AddCommodityFragment extends BaseBackFragment {
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

    public static AddCommodityFragment newInstance() {
        AddCommodityFragment fragment = new AddCommodityFragment();
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
        title.setText(getString(R.string.add_commodity));
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
        map.put("name", name);
        map.put("unit", unit);
        map.put("remark", remark);
        dialogTransformer = new DialogTransformer(_mActivity);
        Client.getApiService().good(map)
                .compose(RxsRxSchedulers.<BaseBean>io_main())
                .compose(dialogTransformer.transformer())
                .subscribe(new ApiServiceResult(getComposite()) {

                    @Override
                    public void onFinish(BaseBean bean) {
                        EventBus.getDefault().post(new AddGoosEvent());
                        ToastUtils.showShortToast(R.string.add_success);
                        _mActivity.onBackPressed();
                    }
                });
    }


}
