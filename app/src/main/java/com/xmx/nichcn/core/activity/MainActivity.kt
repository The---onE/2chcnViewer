package com.xmx.nichcn.core.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import com.xmx.nichcn.core.CoreConstants
import com.xmx.nichcn.R
import com.xmx.nichcn.base.activity.BaseActivity
import com.xmx.nichcn.common.data.DataManager
import com.xmx.nichcn.common.web.BaseWebChromeClient
import com.xmx.nichcn.common.web.BaseWebViewClient
import com.xmx.nichcn.core.MyApplication
import com.xmx.nichcn.module.user.LoginActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.tool_bar.*
import java.util.regex.Pattern
import android.support.v7.widget.SearchView
import com.xmx.nichcn.module.article.ArticleUtil
import com.xmx.nichcn.module.history.HistoryActivity
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.net.Uri
import com.xmx.nichcn.utils.StringUtil


/**
 * Created by The_onE on 2017/2/15.
 * 主Activity，显示主页
 */
class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var mSearchView: SearchView? = null // 顶部搜索框

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
                        Pattern.matches(CoreConstants.HOME_PATTERN, url) ||
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
                            ArticleUtil.openArticle(this@MainActivity, url)
                        else ->
                            StringUtil.copyToClipboard(this@MainActivity, url)
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
        // 初始化浏览器
        initBrowser()
        // 打开网页
        webBrowser.loadUrl(CoreConstants.HOME_URL)
    }

    // 侧滑菜单项点击事件
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // 显示选择的选项卡
        when (item.itemId) {
            R.id.nav_history ->
                startActivity(HistoryActivity::class.java)
            R.id.nav_user ->
                startActivity(LoginActivity::class.java)
            R.id.nav_exit ->
                MyApplication.getInstance().exit()
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

    override fun onResume() {
        super.onResume()
        val url = DataManager.getJumpUrl()
        if (url.isNotBlank()) {
            webBrowser.loadUrl(url)
            DataManager.setJumpUrl("")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        mSearchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        mSearchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?) = false

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && query.isNotBlank()) {
                    webBrowser.loadUrl("${CoreConstants.SEARCH_URL}$query")
                }
                return false
            }

        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_refresh ->
                webBrowser.reload()
        }
        return true
    }
}
