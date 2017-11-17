package com.xmx.nichcn.module.article

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.webkit.WebView
import com.xmx.nichcn.R
import com.xmx.nichcn.base.activity.BaseTempActivity
import com.xmx.nichcn.common.data.DataManager
import com.xmx.nichcn.common.web.BaseWebChromeClient
import com.xmx.nichcn.common.web.BaseWebViewClient
import com.xmx.nichcn.core.CoreConstants
import com.xmx.nichcn.core.activity.MainActivity
import com.xmx.nichcn.module.history.HistoryManager
import kotlinx.android.synthetic.main.activity_article.*
import kotlinx.android.synthetic.main.tool_bar.*
import java.util.regex.Pattern

/**
 * Created by The_onE on 2017/11/16.
 * 文章页Activity
 */
class ArticleActivity : BaseTempActivity() {
    private var historyFlag = false // 是否已记入历史记录
    // 文章地址
    private val mUrl: String by lazy {
        intent.getStringExtra(ArticleUtil.URL_EXTRA)
    }

    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_article)
        if (mUrl.isBlank()) {
            finish()
        }
        toolbar.setTitle(R.string.loading)
    }

    override fun setListener() {
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun processLogic(savedInstanceState: Bundle?) {
        if (mUrl.isBlank()) {
            return
        }

        // 允许JS执行
        webBrowser.settings.javaScriptEnabled = true

        // 设置自定义浏览器属性(对不同协议的URL分别处理)
        webBrowser.webViewClient = object : BaseWebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null && url.isNotBlank()) {
                    if (url.startsWith(mUrl)) {
                        view?.loadUrl(url)
                    } else {
                        when {
                        // 首页
                            Pattern.matches(CoreConstants.HOME_PATTERN, url) -> {
                                DataManager.setJumpUrl(url)
                                startActivity(MainActivity::class.java)
                                finish()
                            }
                        // 分类页
                            Pattern.matches(CoreConstants.CATEGORY_PATTERN, url) ||
                                    // 标签页
                                    Pattern.matches(CoreConstants.TAG_PATTERN, url) ||
                                    // 搜索页
                                    Pattern.matches(CoreConstants.SEARCH_PATTERN, url) ->
                                view?.loadUrl(url)
                        // 图片页
                            Pattern.matches(CoreConstants.IMAGE_PATTERN, url) -> {
                                val intent = Intent(Intent.ACTION_VIEW)
                                intent.data = Uri.parse(url)
                                startActivity(intent)
                            }
                        // 文章页
                            Pattern.matches(CoreConstants.ARTICLE_PATTERN, url) ->
                                ArticleUtil.openArticle(this@ArticleActivity, url)
                        }
                    }
                }
                return true
            }
        }

        // 设置自定义页面事件处理(alert,prompt等页面事件)
        webBrowser.webChromeClient = object : BaseWebChromeClient() {
            override fun onAlert(message: String) {
                val builder = AlertDialog.Builder(this@ArticleActivity)
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

            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                title?.apply {
                    var temp = title
                    // 处理标题后缀
                    if (temp.endsWith(CoreConstants.NICH_ENDS)) {
                        temp = temp.substring(0, temp.length
                                - CoreConstants.NICH_ENDS.length)
                    }
                    // 添加历史记录
                    if (!historyFlag) {
                        HistoryManager.addHistory(mUrl, temp)
                        historyFlag = true
                    }
                    // 处理标题前缀
                    if (temp.startsWith(CoreConstants.VIP_TAG)) {
                        temp = temp.substring(CoreConstants.VIP_TAG.length)
                    }
                    if (temp.startsWith(CoreConstants.NICH_TAG)) {
                        temp = temp.substring(CoreConstants.NICH_TAG.length)
                    }
                    if (temp.startsWith(CoreConstants.NICH2_TAG)) {
                        temp = temp.substring(CoreConstants.NICH2_TAG.length)
                    }
                    if (temp.startsWith(CoreConstants.TWITTER_TAG)) {
                        temp = temp.substring(CoreConstants.TWITTER_TAG.length)
                    }
                    // 工具栏显示标题
                    toolbar.title = temp
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
        webBrowser.loadUrl(mUrl)
    }
}
