package com.xmx.nichcn.module.browser

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import com.xmx.nichcn.R
import com.xmx.nichcn.base.activity.BaseTempActivity
import com.xmx.nichcn.common.web.BaseWebChromeClient
import com.xmx.nichcn.common.web.BaseWebViewClient
import kotlinx.android.synthetic.main.activity_browser.*

/**
 * Created by The_onE on 2017/11/25.
 * 浏览器模版
 */
abstract class BaseBrowserActivity : BaseTempActivity() {
    // 当前地址
    private var currentUrl: String? = null

    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_browser)
        initView()
    }

    /**
     * 初始化页面
     */
    protected abstract fun initView()

    /**
     * 设置初始打开URL
     * @return 初始URL
     */
    protected abstract fun index(): String

    /**
     * 加载URL回调
     * @param view 当前WebView
     * @param url 要处理的URL
     */
    protected abstract fun onLoadUrl(view: WebView, url: String): Boolean

    /**
     * 处理进度条
     * @param newProgress 页面加载进度
     */
    protected abstract fun onProgressChanged(newProgress: Int)

    /**
     * 处理网页标题
     * @param title 标题
     */
    protected abstract fun onReceivedTitle(title: String)

    override fun setListener() {
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun processLogic(savedInstanceState: Bundle?) {
        // 允许JS执行
        webBrowser.settings.javaScriptEnabled = true

        // 设置自定义浏览器属性(对不同协议的URL分别处理)
        webBrowser.webViewClient = object : BaseWebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (view != null && url != null && url.isNotBlank()) {
                    if (onLoadUrl(view, url)) {
                        currentUrl = url
                    }
                }
                return true
            }
        }

        // 设置自定义页面事件处理(alert,prompt等页面事件)
        webBrowser.webChromeClient = object : BaseWebChromeClient() {
            override fun onAlert(message: String) {
                val builder = AlertDialog.Builder(this@BaseBrowserActivity)
                builder.setMessage(message)
                        .setTitle("提示")
                        .setPositiveButton("确定", { dialogInterface, _ ->
                            dialogInterface.dismiss()
                        })
                        .show()
            }

            override fun onProgressChanged(view: WebView, newProgress: Int) {
                onProgressChanged(newProgress)
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                title?.apply {
                    onReceivedTitle(this)
                }
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
        webBrowser.loadUrl(index())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.article, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_refresh ->
                webBrowser.reload()
            R.id.action_browser -> {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(currentUrl)
                startActivity(intent)
            }
        }
        return true
    }
}