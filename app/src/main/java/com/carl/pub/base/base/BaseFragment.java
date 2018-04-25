package com.carl.pub.base.base;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ruiqin.shen.
 * 类说明：所有的Fragment的基类，创建的Fragment都继承BaseFragment
 * 在onCreate调用attachView，P获取V的索引
 * 在onCreate中createPresenter，V获取P的索引
 */
public abstract class BaseFragment<V, T extends BasePresenter<V>> extends Fragment {

    /**
     * Fragment当前状态是否可见
     */
    protected boolean mIsVisible;
    /**
     * 表示预加载
     */
    protected boolean mIsPrepared;

    protected BaseActivity mActivity;
    private Unbinder mBind;

    public T mPresenter;

    public Context mContext;

    /**
     * ViewPager adapter中的每个fragment切换的时候都会被调用，如果是切换到当前页，那么isVisibleToUser==true，否则为false
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mIsVisible = true;
            onVisible();
        } else {
            mIsVisible = false;
            onInvisible();
        }
    }

    /**
     * 不可见
     */
    protected void onInvisible() {
        stopLoad();//停止耗时的操作
    }

    /**
     * 可见
     */
    protected void onVisible() {
        whetherLazyLoad();
    }

    /**
     * 延迟加载
     * 子类必须重写此方法
     */
    protected void whetherLazyLoad() {
        if (!mIsVisible || !mIsPrepared) {
            return;
        }
        lazyLoad();
    }

    /**
     * 懒加载，联网等操作
     */
    protected abstract void lazyLoad();

    /**
     * 停止耗时的操作，计时器，动画等
     */
    protected abstract void stopLoad();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();

        if (mPresenter != null) {
            mPresenter.attachView((V) mContext);
            Log.e("TAG", "当前页面：" + getClass().getSimpleName());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mActivity = (BaseActivity) context;
    }

    /**
     * 获取宿主Activity
     *
     * @return
     */
    protected BaseActivity getHoldingActivity() {
        return mActivity;
    }

    /**
     * 添加Fragment
     *
     * @param fragment
     */
    protected void addFragment(BaseFragment fragment) {
        if (null != fragment) {
            getHoldingActivity().addFragment(fragment);
        }
    }

    /**
     * 移除Fragment
     */
    protected void removeFragment() {
        getHoldingActivity().removeFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        mBind = ButterKnife.bind(this, view);
        mIsPrepared = true;
        whetherLazyLoad();
        initView(view, savedInstanceState);
        return view;
    }


    protected abstract void initView(View view, Bundle savedInstanceState);


    /**
     * 得到视图的ID
     *
     * @return
     */
    protected abstract int getLayoutId();

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 创建Presenter
     * @return
     */
    protected abstract T createPresenter();

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        mBind.unbind();
    }
}
