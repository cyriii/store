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
import com.project.system.storemanagement.fragment.home.AddCommodityFragment;
import com.project.system.storemanagement.fragment.home.AddCustomerFragment;
import com.project.system.storemanagement.fragment.home.AddSupplierFragment;
import com.project.system.storemanagement.fragment.home.CustomerFragment;
import com.project.system.storemanagement.fragment.home.StoreFragment;
import com.project.system.storemanagement.fragment.home.SupplierFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 基本信息
 */
public class HomeFragment extends BaseMainFragment implements Toolbar.OnMenuItemClickListener {
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
    private StoreFragment storeFragment;
    private CustomerFragment customerFragment;
    private SupplierFragment supplierFragment;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
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
        title.setText(getString(R.string.home_des));

        toolbar.inflateMenu(R.menu.add);

        toolbar.setOnMenuItemClickListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        String[] titles = getResources().getStringArray(R.array.tab_home);
        List<Fragment> fragments = new ArrayList<Fragment>();
        storeFragment = StoreFragment.newInstance();
        customerFragment = CustomerFragment.newInstance();
        supplierFragment = SupplierFragment.newInstance();
        fragments.add(storeFragment);
        fragments.add(customerFragment);
        fragments.add(supplierFragment);
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
                    etSearch.setHint(R.string.input_search_goods);
                } else if (currentPosition == 1) {
                    etSearch.setHint(R.string.input_search_client);
                } else if (currentPosition == 2) {
                    etSearch.setHint(R.string.input_search_supplier);
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
            storeFragment.search();
        } else if (currentPosition == 1) {
            customerFragment.search();
        } else if (currentPosition == 2) {
            supplierFragment.search();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        int i = menuItem.getItemId();
        if (i == R.id.action_add) {
            if (currentPosition == 0) {
                addCommodity();
            } else if (currentPosition == 1) {
                addClient();
            } else if (currentPosition == 2) {
                addSupplier();
            }
        }
        return false;
    }

    /**
     * 添加供应商
     */
    private void addSupplier() {
        ((MainFragment) getParentFragment()).startBrotherFragment(AddSupplierFragment.newInstance());
    }

    /**
     * 添加客户
     */
    private void addClient() {
        ((MainFragment) getParentFragment()).startBrotherFragment(AddCustomerFragment.newInstance());
    }

    /**
     * 添加商品
     */
    private void addCommodity() {
        ((MainFragment) getParentFragment()).startBrotherFragment(AddCommodityFragment.newInstance());
    }

}
