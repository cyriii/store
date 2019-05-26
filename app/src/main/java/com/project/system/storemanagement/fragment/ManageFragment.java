package com.project.system.storemanagement.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.project.system.storemanagement.R;
import com.project.system.storemanagement.adapter.HomeViewPageAdapter;
import com.project.system.storemanagement.base.BaseMainFragment;
import com.project.system.storemanagement.fragment.manage.AddInFragment;
import com.project.system.storemanagement.fragment.manage.AddOutFragment;
import com.project.system.storemanagement.fragment.manage.PutInFragment;
import com.project.system.storemanagement.fragment.manage.PutOutFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 库存管理
 */
public class ManageFragment extends BaseMainFragment implements Toolbar.OnMenuItemClickListener {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.stl)
    SlidingTabLayout stl;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.et_search)
    EditText etSearch;

    private int currentPosition;

    private PutInFragment putInFragment;
    private PutOutFragment putOutFragment;

    public static ManageFragment newInstance() {
        ManageFragment fragment = new ManageFragment();
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_home;
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
        title.setText(getString(R.string.manage_des));

        toolbar.inflateMenu(R.menu.add);

        toolbar.setOnMenuItemClickListener(this);

        etSearch.setHint(R.string.input_search_goods_supplier);
    }

    @Override
    protected void initData() {
        super.initData();
        String[] titles = getResources().getStringArray(R.array.tab_manage);
        List<Fragment> fragments = new ArrayList<Fragment>();
        putInFragment = PutInFragment.newInstance();
        putOutFragment = PutOutFragment.newInstance();
        fragments.add(putInFragment);
        fragments.add(putOutFragment);
        HomeViewPageAdapter pageAdapter = new HomeViewPageAdapter(getChildFragmentManager(), fragments, titles);
        viewPager.setAdapter(pageAdapter);
        stl.setViewPager(viewPager);
        stl.onPageSelected(0);
    }

    @Override
    protected void setListener() {
        super.setListener();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                currentPosition = i;
                etSearch.setText(null);
                if (currentPosition == 0) {
                    etSearch.setHint(R.string.input_search_goods_supplier);
                } else if (currentPosition == 1) {
                    etSearch.setHint(R.string.input_search_goods_client);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    public String getSearchStr() {
        if (etSearch != null) {
            return etSearch.getText().toString().trim();
        }
        return null;
    }

    @OnClick(R.id.tv_search)
    public void onViewClicked() {
        if (currentPosition == 0) {
            putInFragment.search();
        } else if (currentPosition == 1) {
            putOutFragment.search();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        int i = menuItem.getItemId();
        if (i == R.id.action_add) {
            if (currentPosition == 0) {
                ((MainFragment) getParentFragment()).startBrotherFragment(AddInFragment.newInstance());
            } else if (currentPosition == 1) {
                ((MainFragment) getParentFragment()).startBrotherFragment(AddOutFragment.newInstance());
            }
        }
        return false;
    }
}
