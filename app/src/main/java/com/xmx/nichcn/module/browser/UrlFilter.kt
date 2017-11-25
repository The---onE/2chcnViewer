package com.xmx.nichcn.module.browser

import com.xmx.nichcn.core.CoreConstants
import java.util.regex.Pattern

/**
 * Created by The_onE on 2017/11/25.
 */
object UrlFilter {
    val UNKNOWN_PAGE = 0 // 首页
    val HOME_PAGE = 1 // 分类页
    val CATEGORY_PAGE = 2 // 标签页
    val TAG_PAGE = 3 // 搜索页
    val SEARCH_PAGE = 4 // 图片页
    val IMAGE_PAGE = 5 // 文章页
    val ARTICLE_PAGE = 6 // 用户页
    val AUTHOR_PAGE = 7 // 其他页

    public fun filterUrl(url: String): Int = when {
    // 首页
        Pattern.matches(UrlConstants.HOME_PATTERN, url) -> HOME_PAGE
    // 分类页
        Pattern.matches(UrlConstants.CATEGORY_PATTERN, url) -> CATEGORY_PAGE
    // 标签页
        Pattern.matches(UrlConstants.TAG_PATTERN, url) -> TAG_PAGE
    // 搜索页
        Pattern.matches(UrlConstants.SEARCH_PATTERN, url) -> SEARCH_PAGE
    // 图片页
        Pattern.matches(UrlConstants.IMAGE_PATTERN, url) -> IMAGE_PAGE
    // 文章页
        Pattern.matches(UrlConstants.ARTICLE_PATTERN, url) -> ARTICLE_PAGE
    // 用户页
        Pattern.matches(UrlConstants.AUTHOR_PATTERN, url) -> AUTHOR_PAGE
    // 其他页
        else -> UNKNOWN_PAGE
    }
}
