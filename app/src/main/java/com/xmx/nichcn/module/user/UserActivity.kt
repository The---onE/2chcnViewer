package com.xmx.nichcn.module.user

import android.view.View
import android.webkit.WebView
import com.xmx.nichcn.module.browser.BaseBrowserActivity
import com.xmx.nichcn.module.browser.UrlConstants
import kotlinx.android.synthetic.main.activity_browser.*
import kotlinx.android.synthetic.main.tool_bar.*

/**
 * Created by The_onE on 2017/11/16.
 * 登录页Activity
 */
class UserActivity : BaseBrowserActivity() {
    override fun initView() {}

    override fun index() = intent.getStringExtra(UserUtil.URL_EXTRA) ?: UrlConstants.LOGIN_URL

    override fun onLoadUrl(view: WebView, url: String): Boolean {
        view.loadUrl(url)
        return true
    }

    override fun onProgressChanged(newProgress: Int) =
            if (newProgress == 100) {
                progressBar.visibility = View.GONE
            } else {
                progressBar.visibility = View.VISIBLE
                progressBar.setProgress(newProgress)
            }

    override fun onReceivedTitle(title: String) {
        toolbar.title = title
    }
}
