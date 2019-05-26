package com.project.system.storemanagement.fragment.user;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.project.system.storemanagement.R;
import com.project.system.storemanagement.base.BaseBackFragment;

import butterknife.BindView;

/**
 * 关于
 */
public class AboutFragment extends BaseBackFragment {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    public static AboutFragment newInstance() {
        AboutFragment fragment = new AboutFragment();
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_about;
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
        title.setText(getString(R.string.about));

    }
}
