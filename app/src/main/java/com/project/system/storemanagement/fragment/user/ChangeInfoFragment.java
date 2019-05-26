package com.project.system.storemanagement.fragment.user;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.project.system.storemanagement.AppConfig;
import com.project.system.storemanagement.R;
import com.project.system.storemanagement.base.BaseBackFragment;
import com.project.system.storemanagement.base.BaseBean;
import com.project.system.storemanagement.http.ApiServiceResult;
import com.project.system.storemanagement.http.Client;
import com.project.system.storemanagement.http.DialogTransformer;
import com.project.system.storemanagement.http.RxsRxSchedulers;
import com.project.system.storemanagement.utils.RegexUtils;
import com.project.system.storemanagement.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改信息
 */
public class ChangeInfoFragment extends BaseBackFragment {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_user_nickname)
    EditText etUserNickname;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.rb_man)
    RadioButton rbMan;
    @BindView(R.id.rb_woman)
    RadioButton rbWoman;

    public static ChangeInfoFragment newInstance() {
        ChangeInfoFragment fragment = new ChangeInfoFragment();
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_change_info;
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
        title.setText(getString(R.string.change_info));
    }

    @Override
    protected void initData() {
        super.initData();
        if (AppConfig.USERBAN != null) {
            etUserNickname.setText(AppConfig.USERBAN.getNickName());
            etPhone.setText(AppConfig.USERBAN.getTelNumber());
            etAddress.setText(AppConfig.USERBAN.getAddress());
            //用户性别（0：女， 1： 男）
            if (TextUtils.equals(AppConfig.USERBAN.getSex(), "1")) {
                rbMan.setChecked(true);
            } else if (TextUtils.equals(AppConfig.USERBAN.getSex(), "0")) {
                rbWoman.setChecked(true);
            }
        }
    }

    @OnClick(R.id.btn_commit)
    public void onViewClicked() {
        String nickName = etUserNickname.getText().toString().trim();
        if (TextUtils.isEmpty(nickName)) {
            ToastUtils.showShortToast(getResources().getString(R.string.input_nickname));
            return;
        }

        String telNumber = etPhone.getText().toString().trim();
        if (!TextUtils.isEmpty(telNumber) && !RegexUtils.isMobileSimple(telNumber)) {
            ToastUtils.showShortToast(getResources().getString(R.string.phone_error));
            return;
        }

        Map<String, String> map = new HashMap();
        map.put("nickName", nickName);

        if (!TextUtils.isEmpty(telNumber)) {
            map.put("telNumber", telNumber);
        }
        String address = etAddress.getText().toString().trim();
        if (!TextUtils.isEmpty(address)) {
            map.put("address", address);
        }

        //用户性别（0：女， 1： 男）
        if (rbMan.isChecked()) {
            map.put("sex", "1");
        } else if (rbWoman.isChecked()) {
            map.put("sex", "0");
        }

        dialogTransformer = new DialogTransformer(_mActivity);
        Client.getApiService().user(map)
                .compose(RxsRxSchedulers.<BaseBean>io_main())
                .compose(dialogTransformer.transformer())
                .subscribe(new ApiServiceResult(getComposite()) {

                    @Override
                    public void onFinish(BaseBean bean) {
                        ToastUtils.showShortToast(R.string.changeinfo_success);
                        _mActivity.onBackPressed();
                    }
                });
    }
}
