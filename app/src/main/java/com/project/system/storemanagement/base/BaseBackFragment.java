package com.project.system.storemanagement.base;


import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.gyf.barlibrary.ImmersionBar;
import com.project.system.storemanagement.R;
import com.project.system.storemanagement.http.DialogTransformer;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;
import me.yokeyword.fragmentation_swipeback.SwipeBackFragment;

/**
 * Created by YoKeyword on 16/2/7.
 */
public abstract class BaseBackFragment extends SwipeBackFragment {

    protected View mRootView;
    //对subscription进行管理 防止内存泄漏
//    public CompositeSubscription mCompositeSubscription;
    protected CompositeDisposable composite;
    protected ImmersionBar mImmersionBar;
    protected Unbinder unbinder;

    private FrameLayout flContent;
    private View contentView;
    protected int page = 1;

    protected DialogTransformer dialogTransformer;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setParallaxOffset(0.5f);
        View toolBarView = setToolBarView();
        if (toolBarView != null) {
            ImmersionBar.setTitleBar(_mActivity, toolBarView);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.activity_main, container, false);

        addView();

        initView();

        setListener();

        return attachToSwipeBack(mRootView);
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        initData();
    }

    //进行统一管理
    protected void addSubscription(Disposable disposable) {
        if (composite == null) {
            composite = new CompositeDisposable();
        }
//        composite.add(disposable);
    }

    public CompositeDisposable getComposite() {
        if (composite == null) {
            composite = new CompositeDisposable();
        }
        return composite;
    }

    //取消注册，以避免内存泄露
    public void onUnSubscribe() {
        if (composite != null) {
            composite.clear();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
        onUnSubscribe();
        if (dialogTransformer != null) {
            dialogTransformer.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null) {
            mImmersionBar.destroy();
        }
    }

    public void addView() {
        flContent = mRootView.findViewById(R.id.fl_container);
        contentView = View.inflate(_mActivity, setLayoutId(), null);
        flContent.addView(contentView);
        unbinder = ButterKnife.bind(this, mRootView);
    }

    public View getContentView() {
        return contentView;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        setStatusBar();
    }

    protected void setStatusBar() {
        if (statusBarDarkFont()) {
            initImmersionBar();
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

    protected void initImmersionBar() {
        if (mImmersionBar == null) {
            mImmersionBar = ImmersionBar.with(this).statusBarColorTransformEnable(false)
                    .keyboardEnable(true);  //解决软键盘与底部输入框冲突问题
        }
    }

    /**
     * 设置状态栏颜色
     *
     * @return
     */
    protected int statusBarColor() {
        return R.color.colorPrimary;
    }

    /**
     * 单独在标题栏的位置增加view，高度为状态栏的高度
     * Sets status bar view.
     */
    protected View setStatusBarView() {
        return null;
    }

    protected View setToolBarView() {
        return null;
    }


    /**
     * 如果当前设备支持状态栏字体变色，会设置成黑色，不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
     *
     * @return
     */
    protected boolean statusBarDarkFont() {
        return true;
    }

    /**
     * Sets layout id.
     *
     * @return the layout id
     */
    protected abstract int setLayoutId();


    /**
     * 初始化数据
     */
    protected void initData() {
    }

    /**
     * view与数据绑定
     */
    protected void initView() {
    }

    /**
     * 设置监听
     */
    protected void setListener() {
    }

    protected void initToolbarNav(Toolbar toolbar) {
        Resources resources = getResources();
        Drawable drawable = resources.getDrawable(R.mipmap.back);
        toolbar.setNavigationIcon(drawable);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    protected void hindBack(Toolbar toolbar) {
        toolbar.setNavigationIcon(null);
    }

    protected void finish() {
        _mActivity.onBackPressed();
    }
}