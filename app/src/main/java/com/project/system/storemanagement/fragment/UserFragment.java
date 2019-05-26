package com.project.system.storemanagement.fragment;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.system.storemanagement.AppConfig;
import com.project.system.storemanagement.R;
import com.project.system.storemanagement.base.BaseBean;
import com.project.system.storemanagement.base.BaseMainFragment;
import com.project.system.storemanagement.bean.UserBean;
import com.project.system.storemanagement.fragment.user.AboutFragment;
import com.project.system.storemanagement.fragment.user.ChangeInfoFragment;
import com.project.system.storemanagement.fragment.user.ChangePswFragment;
import com.project.system.storemanagement.http.ApiServiceResult;
import com.project.system.storemanagement.http.Client;
import com.project.system.storemanagement.http.RxsRxSchedulers;
import com.project.system.storemanagement.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 用户管理
 */
public class UserFragment extends BaseMainFragment {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_photo)
    ImageView ivPhoto;
    @BindView(R.id.tv_name)
    TextView tvName;

    public static UserFragment newInstance() {
        UserFragment fragment = new UserFragment();
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_user;
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
        title.setText(getString(R.string.user_des));
    }


    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (AppConfig.USERBAN != null) {
            tvName.setText(getString(R.string.nickname) + AppConfig.USERBAN.getNickName());
        }

        getUserData();
    }

    private void getUserData() {
        Client.getApiService().getUser( )
                .compose(RxsRxSchedulers.<BaseBean<UserBean>>io_main())
                .subscribe(new ApiServiceResult<UserBean>(getComposite()) {

                    @Override
                    public void onFinish(BaseBean<UserBean> bean) {
                        if (bean != null && bean.getData() != null) {
                            AppConfig.USERBAN = bean.getData();
                            tvName.setText(getString(R.string.nickname) + AppConfig.USERBAN.getNickName());
                        }
                    }
                });
    }

    @OnClick({R.id.tv_change_psw, R.id.tv_change_info, R.id.tv_abuout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_change_psw:
                ((MainFragment) getParentFragment()).startBrotherFragment(ChangePswFragment.newInstance());
                break;
            case R.id.tv_change_info:
                ((MainFragment) getParentFragment()).startBrotherFragment(ChangeInfoFragment.newInstance());
                break;
            case R.id.tv_abuout:
                ((MainFragment) getParentFragment()).startBrotherFragment(AboutFragment.newInstance());
                break;
        }
    }
}
