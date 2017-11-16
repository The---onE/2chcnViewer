package com.xmx.nichcn.module.user

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.webkit.WebView
import com.xmx.nichcn.R
import com.xmx.nichcn.base.activity.BaseTempActivity
import com.xmx.nichcn.common.web.BaseWebChromeClient
import com.xmx.nichcn.common.web.BaseWebViewClient
import com.xmx.nichcn.core.CoreConstants
import kotlinx.android.synthetic.main.activity_article.*
import kotlinx.android.synthetic.main.tool_bar.*
import java.util.regex.Pattern


/**
 * Created by The_onE on 2017/11/16.
 * 文章页Activity
 */
class LoginActivity : BaseTempActivity() {

    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_article)
    }

    override fun setListener() {
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun processLogic(savedInstanceState: Bundle?) {
        // 允许JS执行
        webBrowser.settings.javaScriptEnabled = true

        // 设置自定义浏览器属性(对不同协议的URL分别处理)
        webBrowser.webViewClient = object : BaseWebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null && url.isNotBlank()) {
                    view?.loadUrl(url)
                }
                return true
            }
        }

        // 设置自定义页面事件处理(alert,prompt等页面事件)
        webBrowser.webChromeClient = object : BaseWebChromeClient() {
            override fun onAlert(message: String) {
                val builder = AlertDialog.Builder(this@LoginActivity)
                builder.setMessage(message)
                        .setTitle("提示")
                        .setPositiveButton("确定", { dialogInterface, _ ->
                            dialogInterface.dismiss()
                        })
                        .show()
            }

            override fun onProgressChanged(view: WebView, newProgress: Int) =
                    if (newProgress == 100) {
                        progressBar.visibility = View.GONE
                    } else {
                        progressBar.visibility = View.VISIBLE
                        progressBar.setProgress(newProgress)
                    }
        }

        // 设置可以支持缩放
        webBrowser.settings.setSupportZoom(false)
        // 设置出现缩放工具
        webBrowser.settings.builtInZoomControls = false
        //设置可在大视野范围内上下左右拖动，并且可以任意比例缩放
        webBrowser.settings.useWideViewPort = false
        //设置默认加载的可视范围是大视野范围
        webBrowser.settings.loadWithOverviewMode = true

        // 打开网络网页
        webBrowser.loadUrl(CoreConstants.LOGIN_URL)
    }
}
