package com.project.system.storemanagement.base;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.project.system.storemanagement.R;
import com.project.system.storemanagement.http.DialogTransformer;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import me.yokeyword.fragmentation.SupportFragment;


/**
 * 懒加载
 * Created by YoKeyword on 16/6/5.
 */
public abstract class BaseMainFragment extends SupportFragment {
    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;

    protected View mRootView;
    //对subscription进行管理 防止内存泄漏
//    public CompositeSubscription mCompositeSubscription;
    protected CompositeDisposable composite;
    protected ImmersionBar mImmersionBar;
    protected Unbinder unbinder;

    private FrameLayout flContent;
    protected DialogTransformer dialogTransformer;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = setToolBarView();
        if (view != null) {
            ImmersionBar.setTitleBar(_mActivity, view);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.activity_main, container, false);

        addView();

        initView();

        initData();

        setListener();

        return mRootView;
    }

//    //进行统一管理
//    protected void addSubscription(Subscription subscription) {
//        if (mCompositeSubscription == null) {
//            mCompositeSubscription = new CompositeSubscription();
//        }
//        mCompositeSubscription.add(subscription);
//    }

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

    private void addView() {
        flContent = mRootView.findViewById(R.id.fl_container);
        View contentView = View.inflate(_mActivity, setLayoutId(), null);
        flContent.addView(contentView);
        unbinder = ButterKnife.bind(this, mRootView);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        setStatusBar();
    }


    protected View setToolBarView() {
        return null;
    }


    private void setStatusBar() {
        if (statusBarDarkFont()) {
            initImmersionBar();
//            mImmersionBar.statusBarDarkFont(true, 0.2f);
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
            mImmersionBar = ImmersionBar.with(this);
            mImmersionBar.keyboardEnable(true);  //解决软键盘与底部输入框冲突问题
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

    /**
     * 处理回退事件
     *
     * @return
     */
    @Override
    public boolean onBackPressedSupport() {
        if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
            _mActivity.finish();
            NotificationManager manager = (NotificationManager) _mActivity.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancelAll();
        } else {
            TOUCH_TIME = System.currentTimeMillis();
            Toast.makeText(_mActivity, R.string.press_again_exit, Toast.LENGTH_SHORT).show();
        }
        return true;
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

}
