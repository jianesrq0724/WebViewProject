package com.carl;

/**
 * @author Carl
 * @version 1.0
 * @since 2018/4/25
 */

public class MainActivity extends MainBaseActivity {

    @Override
    protected void initData() {
        mHomeUrl = "http://www.baidu.com";
    }

    @Override
    public void hideToolBar() {
        super.hideToolBar();
    }
}
