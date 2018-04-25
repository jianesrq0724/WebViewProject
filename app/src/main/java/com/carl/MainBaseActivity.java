package com.carl;

import com.carl.pub.browse.x5webview.BrowserActivity;

public class MainBaseActivity extends BrowserActivity {

    @Override
    protected void initView() {
        super.initView();
        hideToolBar();
    }


}
