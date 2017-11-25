package com.xmx.nichcn.module.article

import android.content.Context
import android.content.Intent

/**
 * Created by The_onE on 2017/11/17.
 * 处理打开文章页请求
 */
object ArticleUtil {
    val URL_EXTRA = "url"

    fun openArticle(context: Context, url: String) {
        val intent = Intent(context, ArticleActivity::class.java)
        intent.putExtra(URL_EXTRA, url)
        context.startActivity(intent)
    }
}