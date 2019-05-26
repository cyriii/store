package com.project.system.storemanagement.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
 * 注册
 */
public class RegistFragment extends BaseBackFragment {
    @BindView(R.id.et_use_name)
    EditText etUseName;
    @BindView(R.id.et_use_psw)
    EditText etUsePsw;
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
    @BindView(R.id.rg)
    RadioGroup rg;

    public static RegistFragment newInstance() {
        RegistFragment fragment = new RegistFragment();
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_regist;
    }

    protected void setStatusBar() {
        if (statusBarDarkFont()) {
            initImmersionBar();
            mImmersionBar.statusBarDarkFont(true, 0.2f);
        }

        int statusBarColor = statusBarColor();
        if (statusBarColor != 0) {
            initImmersionBar();
            mImmersionBar.statusBarColor(statusBarColor);
        }

        View statusBarView = setStatusBarView();
        if (statusBarView != null) {
            initImmersionBar();
            mImmersionBar.statusBarView(statusBarView);
        }

        if (mImmersionBar != null) {
            mImmersionBar.init();
        }
    }

    @Override
    protected int statusBarColor() {
        return R.color.white;
    }


    @Override
    protected void setListener() {
        super.setListener();
    }

    @OnClick({R.id.iv_back, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                _mActivity.onBackPressed();
                break;
            case R.id.btn_login:
                login();
                break;
        }
    }

    private void login() {
        String username = etUseName.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            ToastUtils.showShortToast(getResources().getString(R.string.input_usename));
            return;
        }
        String nickName = etUserNickname.getText().toString().trim();
        if (TextUtils.isEmpty(nickName)) {
            ToastUtils.showShortToast(getResources().getString(R.string.input_nickname));
            return;
        }
        String pwd = etUsePsw.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.showShortToast(getResources().getString(R.string.input_use_psw));
            return;
        }
        if (pwd.length() < 6) {
            ToastUtils.showShortToast(getResources().getString(R.string.psw_error));
            return;
        }
        String telNumber = etPhone.getText().toString().trim();
        if (!TextUtils.isEmpty(telNumber) && !RegexUtils.isMobileSimple(telNumber)) {
            ToastUtils.showShortToast(getResources().getString(R.string.phone_error));
            return;
        }

        Map<String, String> map = new HashMap();
        map.put("userName", username);
        map.put("nickName", nickName);
        map.put("pwd", pwd);

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
        Client.getApiService().register(map)
                .compose(RxsRxSchedulers.<BaseBean>io_main())
                .compose(dialogTransformer.transformer())
                .subscribe(new ApiServiceResult(getComposite()) {

                    @Override
                    public void onFinish(BaseBean bean) {
                        ToastUtils.showShortToast(R.string.regist_success);
                        _mActivity.onBackPressed();
                    }
                });
    }

}
