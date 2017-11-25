package com.xmx.nichcn.module.user

import android.content.Context
import android.content.Intent

/**
 * Created by The_onE on 2017/11/17.
 * 处理打开用户页请求
 */
object UserUtil {
    val URL_EXTRA = "url"

    fun openSelf(context: Context) {
        val intent = Intent(context, UserActivity::class.java)
        context.startActivity(intent)
    }

    fun openUser(context: Context, url: String) {
        val intent = Intent(context, UserActivity::class.java)
        intent.putExtra(URL_EXTRA, url)
        context.startActivity(intent)
    }
}