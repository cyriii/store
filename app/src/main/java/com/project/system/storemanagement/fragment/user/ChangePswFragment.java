package com.project.system.storemanagement.fragment.user;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.project.system.storemanagement.R;
import com.project.system.storemanagement.base.BaseBackFragment;
import com.project.system.storemanagement.base.BaseBean;
import com.project.system.storemanagement.http.ApiServiceResult;
import com.project.system.storemanagement.http.Client;
import com.project.system.storemanagement.http.DialogTransformer;
import com.project.system.storemanagement.http.RxsRxSchedulers;
import com.project.system.storemanagement.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.project.system.storemanagement.R.string.input_old_psw;

/**
 * 修改密码
 */
public class ChangePswFragment extends BaseBackFragment {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_old_psw)
    EditText etOldPsw;
    @BindView(R.id.et_new_psw)
    EditText etNewPsw;

    public static ChangePswFragment newInstance() {
        ChangePswFragment fragment = new ChangePswFragment();
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_change_paw;
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
        title.setText(getString(R.string.change_psw));

    }

    @OnClick(R.id.btn_commit)
    public void onViewClicked() {
        String oldPwd = etOldPsw.getText().toString().trim();
        if (TextUtils.isEmpty(oldPwd)) {
            ToastUtils.showShortToast(getResources().getString(input_old_psw));
            return;
        }
        String newPwd = etNewPsw.getText().toString().trim();
        if (TextUtils.isEmpty(newPwd)) {
            ToastUtils.showShortToast(getResources().getString(R.string.input_new_psw));
            return;
        }
        if (newPwd.length() < 6) {
            ToastUtils.showShortToast(getResources().getString(R.string.psw_error));
            return;
        }
        Map<String, String> map = new HashMap();
        map.put("oldPwd", oldPwd);
        map.put("newPwd", newPwd);
        dialogTransformer = new DialogTransformer(_mActivity);
        Client.getApiService().changepwd(map)
                .compose(RxsRxSchedulers.<BaseBean>io_main())
                .compose(dialogTransformer.transformer())
                .subscribe(new ApiServiceResult(getComposite()) {

                    @Override
                    public void onFinish(BaseBean bean) {
                        ToastUtils.showShortToast(R.string.changepwd_success);
                        _mActivity.onBackPressed();
                    }
                });
    }
}
