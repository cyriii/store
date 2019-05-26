package com.project.system.storemanagement.fragment.home;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.project.system.storemanagement.R;
import com.project.system.storemanagement.base.BaseBackFragment;
import com.project.system.storemanagement.base.BaseBean;
import com.project.system.storemanagement.event.AddSupplierEvent;
import com.project.system.storemanagement.http.ApiServiceResult;
import com.project.system.storemanagement.http.Client;
import com.project.system.storemanagement.http.DialogTransformer;
import com.project.system.storemanagement.http.RxsRxSchedulers;
import com.project.system.storemanagement.utils.RegexUtils;
import com.project.system.storemanagement.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.project.system.storemanagement.R.string.input_supplier_name;

/**
 * 添加供货商
 */
public class AddSupplierFragment extends BaseBackFragment {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_commodity_name)
    EditText etCommodityName;
    @BindView(R.id.et_linkman)
    EditText etLinkman;
    @BindView(R.id.et_tel_number)
    EditText etTelNumber;
    @BindView(R.id.et_post_code)
    EditText etPostCode;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.et_remark)
    EditText etRemark;

    public static AddSupplierFragment newInstance() {
        AddSupplierFragment fragment = new AddSupplierFragment();
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_add_supplier;
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
        title.setText(getString(R.string.add_supplier));
    }

    @OnClick(R.id.btn_commit)
    public void onViewClicked() {
        String name = etCommodityName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            ToastUtils.showShortToast(getResources().getString(input_supplier_name));
            return;
        }
        String linkMan = etLinkman.getText().toString().trim();
        if (TextUtils.isEmpty(linkMan)) {
            ToastUtils.showShortToast(getResources().getString(R.string.input_linkman));
            return;
        }
        String telNumber = etTelNumber.getText().toString().trim();
        if (TextUtils.isEmpty(telNumber)) {
            ToastUtils.showShortToast(getResources().getString(R.string.input_tel_number));
            return;
        }
        if (!RegexUtils.isMobileSimple(telNumber)) {
            ToastUtils.showShortToast(getResources().getString(R.string.phone_error));
            return;
        }
        Map<String, String> map = new HashMap();
        map.put("name", name);
        map.put("linkMan", linkMan);
        map.put("telNumber", telNumber);

        String postCode = etPostCode.getText().toString().trim();
        if (!TextUtils.isEmpty(postCode)) {
            map.put("postCode", postCode);
        }
        String address = etAddress.getText().toString().trim();
        if (!TextUtils.isEmpty(address)) {
            map.put("address", address);
        }
        String remark = etRemark.getText().toString().trim();
        if (!TextUtils.isEmpty(remark)) {
            map.put("remark", remark);
        }
        dialogTransformer = new DialogTransformer(_mActivity);
        Client.getApiService().supplier(map)
                .compose(RxsRxSchedulers.<BaseBean>io_main())
                .compose(dialogTransformer.transformer())
                .subscribe(new ApiServiceResult(getComposite()) {

                    @Override
                    public void onFinish(BaseBean bean) {
                        EventBus.getDefault().post(new AddSupplierEvent());
                        ToastUtils.showShortToast(R.string.add_success);
                        _mActivity.onBackPressed();
                    }
                });
    }


}
