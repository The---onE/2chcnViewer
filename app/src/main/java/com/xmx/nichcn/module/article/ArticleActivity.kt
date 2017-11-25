package com.xmx.nichcn.module.article

import android.content.Intent
import android.net.Uri
import android.view.View
import android.webkit.WebView
import com.xmx.nichcn.R
import com.xmx.nichcn.common.data.DataManager
import com.xmx.nichcn.core.activity.MainActivity
import com.xmx.nichcn.module.browser.BaseBrowserActivity
import com.xmx.nichcn.module.browser.UrlConstants
import com.xmx.nichcn.module.browser.UrlFilter
import com.xmx.nichcn.module.history.HistoryManager
import com.xmx.nichcn.module.user.UserUtil
import com.xmx.nichcn.utils.StringUtil
import kotlinx.android.synthetic.main.activity_browser.*
import kotlinx.android.synthetic.main.tool_bar.*

/**
 * Created by The_onE on 2017/11/16.
 * 文章页Activity
 */
class ArticleActivity : BaseBrowserActivity() {
    private var historyFlag = false // 是否已记入历史记录

    // 文章地址
    private val mUrl: String by lazy {
        intent.getStringExtra(ArticleUtil.URL_EXTRA)
    }

    override fun initView() {
        if (mUrl.isBlank()) {
            finish()
        }
        toolbar.setTitle(R.string.loading)
    }

    override fun index(): String = mUrl

    override fun onLoadUrl(view: WebView, url: String): Boolean {
        if (url.startsWith(mUrl)) {
            view.loadUrl(url)
            return true
        } else {
            when (UrlFilter.filterUrl(url)) {
            // 首页
                UrlFilter.HOME_PAGE -> {
                    DataManager.setJumpUrl(url)
                    startActivity(MainActivity::class.java)
                    finish()
                    return false
                }
            // 分类页 标签页 搜索页
                UrlFilter.CATEGORY_PAGE, UrlFilter.TAG_PAGE, UrlFilter.SEARCH_PAGE -> {
                    view.loadUrl(url)
                    return true
                }
            // 图片页
                UrlFilter.IMAGE_PAGE -> {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                    return false
                }
            // 文章页
                UrlFilter.ARTICLE_PAGE -> {
                    ArticleUtil.openArticle(this@ArticleActivity, url)
                    return false
                }
            // 用户页
                UrlFilter.AUTHOR_PAGE -> {
                    UserUtil.openUser(this@ArticleActivity, url)
                    return false
                }
            // 其他页
                UrlFilter.UNKNOWN_PAGE -> {
                    StringUtil.copyToClipboard(this@ArticleActivity, url)
                    return false
                }
                else -> {
                    StringUtil.copyToClipboard(this@ArticleActivity, url)
                    return false
                }
            }
        }
    }

    override fun onProgressChanged(newProgress: Int) =
            if (newProgress == 100) {
                progressBar.visibility = View.GONE
            } else {
                progressBar.visibility = View.VISIBLE
                progressBar.setProgress(newProgress)
            }

    override fun onReceivedTitle(title: String) {
        var temp = title
        // 处理标题后缀
        if (temp.endsWith(UrlConstants.NICH_ENDS)) {
            temp = temp.substring(0, temp.length
                    - UrlConstants.NICH_ENDS.length)
        }
        // 添加历史记录
        if (!historyFlag) {
            HistoryManager.addHistory(mUrl, temp)
            historyFlag = true
        }
        // 处理标题前缀
        if (temp.startsWith(UrlConstants.VIP_TAG)) {
            temp = temp.substring(UrlConstants.VIP_TAG.length)
        }
        if (temp.startsWith(UrlConstants.NICH_TAG)) {
            temp = temp.substring(UrlConstants.NICH_TAG.length)
        }
        if (temp.startsWith(UrlConstants.NICH2_TAG)) {
            temp = temp.substring(UrlConstants.NICH2_TAG.length)
        }
        if (temp.startsWith(UrlConstants.TWITTER_TAG)) {
            temp = temp.substring(UrlConstants.TWITTER_TAG.length)
        }
        // 工具栏显示标题
        toolbar.title = temp
    }
}
