package com.project.system.storemanagement.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.project.system.storemanagement.AppConfig;
import com.project.system.storemanagement.R;
import com.project.system.storemanagement.base.BaseBean;
import com.project.system.storemanagement.base.BaseMainFragment;
import com.project.system.storemanagement.bean.UserBean;
import com.project.system.storemanagement.http.ApiServiceResult;
import com.project.system.storemanagement.http.Client;
import com.project.system.storemanagement.http.DialogTransformer;
import com.project.system.storemanagement.http.RxsRxSchedulers;
import com.project.system.storemanagement.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录
 */
public class LoginFragment extends BaseMainFragment {
    @BindView(R.id.et_use_name)
    EditText etUseName;
    @BindView(R.id.et_use_psw)
    EditText etUsePsw;

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_login;
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

    @OnClick({R.id.iv_back, R.id.btn_login, R.id.tv_regist})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                _mActivity.onBackPressed();
                break;
            case R.id.btn_login:
                login();
                break;
            case R.id.tv_regist:
                start(RegistFragment.newInstance());
                break;
        }
    }

    private void login() {
        String username = etUseName.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            ToastUtils.showShortToast(getResources().getString(R.string.input_usename));
            return;
        }
        String password = etUsePsw.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            ToastUtils.showShortToast(getResources().getString(R.string.input_use_psw));
            return;
        }

        dialogTransformer = new DialogTransformer(_mActivity);
        Client.getApiService().login(username, password)
                .compose(RxsRxSchedulers.<BaseBean>io_main())
                .compose(dialogTransformer.transformer())
                .subscribe(new ApiServiceResult<UserBean>(getComposite()) {

                    @Override
                    public void onFinish(BaseBean<UserBean> bean) {
                        if (bean != null && bean.getData() != null) {
                            AppConfig.USERBAN = bean.getData();
                            AppConfig.TOKEN = bean.getData().getToken();
                            ToastUtils.showShortToast(R.string.login_success);
                            startWithPop(MainFragment.newInstance());
                        }
                    }
                });
    }
}
