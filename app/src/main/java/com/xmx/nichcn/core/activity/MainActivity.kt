package com.xmx.nichcn.core.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import com.xmx.nichcn.core.CoreConstants
import com.xmx.nichcn.R
import com.xmx.nichcn.base.activity.BaseActivity
import com.xmx.nichcn.common.web.BaseWebChromeClient
import com.xmx.nichcn.common.web.BaseWebViewClient
import com.xmx.nichcn.module.title.ArticleActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.tool_bar.*
import java.util.regex.Pattern
import android.view.View.GONE


/**
 * Created by The_onE on 2017/2/15.
 * 主Activity，利用Fragment展示所有程序内容
 */
class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    // 侧滑菜单登录菜单项
    private var loginItem: MenuItem? = null

    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main)

        // 初始化工具栏
        setSupportActionBar(toolbar)

        // 初始化侧滑菜单
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun setListener() {
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initBrowser() {
        // 允许JS执行
        webBrowser.settings.javaScriptEnabled = true

        // 设置自定义浏览器属性(对不同协议的URL分别处理)
        webBrowser.webViewClient = object : BaseWebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null && url.isNotBlank()) {
                    when {
                    // 首页
                        Pattern.matches(CoreConstants.HOME_PATTERN, url) ->
                            view?.loadUrl(url)
                    // 分类页
                        Pattern.matches(CoreConstants.CATEGORY_PATTERN, url) ->
                            view?.loadUrl(url)
                    // 标签页
                        Pattern.matches(CoreConstants.TAG_PATTERN, url) ->
                            view?.loadUrl(url)
                    // 搜索页
                        Pattern.matches(CoreConstants.SEARCH_PATTERN, url) ->
                            view?.loadUrl(url)
                    // 文章页
                        Pattern.matches(CoreConstants.ARTICLE_PATTERN, url) ->
                            startActivity(ArticleActivity::class.java, "url", url)
                    }
                }
                return true
            }
        }

        // 设置自定义页面事件处理(alert,prompt等页面事件)
        webBrowser.webChromeClient = object : BaseWebChromeClient() {
            override fun onAlert(message: String) {
                val builder = AlertDialog.Builder(this@MainActivity)
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
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        // 设置侧滑菜单
        val menu = nav_view.menu
        loginItem = menu.findItem(R.id.nav_user)

        // 初始化浏览器
        initBrowser()
        // 打开网页
        webBrowser.loadUrl(CoreConstants.HOME_URL)
    }

    // 侧滑菜单项点击事件
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // 显示选择的选项卡
        when (item.itemId) {
        }
        // 关闭侧边栏
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    // 点击返回键时添加二次确认
    private var mExitTime: Long = 0 // 上次按键的时间

    override fun onBackPressed() {
        // 如果侧边栏已打开，则关闭
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
            return
        }
        // 若可以返回上一页则返回
        if (webBrowser.canGoBack()) {
            webBrowser.goBack()
            return
        }
        // 如果是第一次按键或距离上次按键时间过长，则重新计时
        if (System.currentTimeMillis() - mExitTime > CoreConstants.LONGEST_EXIT_TIME) {
            showToast(R.string.confirm_exit)
            mExitTime = System.currentTimeMillis()
        } else {
            super.onBackPressed()
        }
    }
}
