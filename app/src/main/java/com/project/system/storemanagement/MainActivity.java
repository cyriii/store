package com.project.system.storemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gyf.barlibrary.ImmersionBar;
import com.project.system.storemanagement.base.BaseActivity;
import com.project.system.storemanagement.fragment.LoginFragment;
import com.project.system.storemanagement.fragment.MainFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!this.isTaskRoot()) {
            Intent intent = getIntent();
            if (intent != null) {
                String action = intent.getAction();
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                    finish();
                }
            }
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    /**
     * 初始化状态栏 导航栏 jar
     */
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this)
                .transparentStatusBar()
                .navigationBarColor(R.color.transparent)
                .keyboardEnable(true); //解决软键盘与底部输入框冲突问题
        mImmersionBar.init();
    }

    @Override
    protected void initView() {
        super.initView();
        loadRootFragment(R.id.fl_container, LoginFragment.newInstance());
    }
}
