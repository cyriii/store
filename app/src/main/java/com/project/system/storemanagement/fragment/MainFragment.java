package com.project.system.storemanagement.fragment;

import com.project.system.storemanagement.AppConfig;
import com.project.system.storemanagement.R;
import com.project.system.storemanagement.base.BaseMainFragment;
import com.project.system.storemanagement.view.BottomBar;
import com.project.system.storemanagement.view.BottomBarTab;

import butterknife.BindView;
import me.yokeyword.fragmentation.SupportFragment;

public class MainFragment extends BaseMainFragment {
    @BindView(R.id.bottomBar)
    BottomBar bottomBar;

    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;
    public static final int FOURTH = 3;

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    private SupportFragment[] mFragments = new SupportFragment[4];

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView() {
        SupportFragment firstFragment = findFragment(HomeFragment.class);
        if (firstFragment == null) {
            mFragments[FIRST] = HomeFragment.newInstance();
            mFragments[SECOND] = ManageFragment.newInstance();
            mFragments[THIRD] = InfoFragment.newInstance();
            mFragments[FOURTH] = UserFragment.newInstance();

            loadMultipleRootFragment(R.id.fl_main_container, FIRST,
                    mFragments[FIRST],
                    mFragments[SECOND],
                    mFragments[THIRD],
                    mFragments[FOURTH]);

        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题
            // 这里我们需要拿到mFragments的引用
            mFragments[FIRST] = firstFragment;
            mFragments[SECOND] = findFragment(ManageFragment.class);
            mFragments[THIRD] = findFragment(InfoFragment.class);
            mFragments[FOURTH] = findFragment(UserFragment.class);
        }

        bottomBar.addItem(new BottomBarTab(_mActivity, R.mipmap.ic_home, R.mipmap.ic_home_s, getString(R.string.home)))
                .addItem(new BottomBarTab(_mActivity, R.mipmap.ic_manage, R.mipmap.ic_manage_s, getString(R.string.manage)))
                .addItem(new BottomBarTab(_mActivity, R.mipmap.ic_info, R.mipmap.ic_info_s, getString(R.string.info)))
                .addItem(new BottomBarTab(_mActivity, R.mipmap.ic_user, R.mipmap.ic_user_s, getString(R.string.user)));

        bottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                if (position == FOURTH && !AppConfig.isLogin()) {
                    bottomBar.setCurrentItem(prePosition);
                    startBrotherFragment(LoginFragment.newInstance());
                    return;
                }
                showHideFragment(mFragments[position], mFragments[prePosition]);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    public void startBrotherFragment(SupportFragment targetFragment) {
        start(targetFragment);
    }
}
