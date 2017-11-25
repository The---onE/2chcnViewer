package com.xmx.nichcn.module.browser

/**
 * Created by The_onE on 2017/11/25.
 * URL处理相关常量
 */
object UrlConstants {
    val HOME_URL = "http://2chcn.com/" // 首页地址
    val LOGIN_URL = "http://2chcn.com/wp-admin/" // 登录地址
    val SEARCH_URL = "http://2chcn.com/?s=" // 搜索匹配
    val HOME_PATTERN = ".*2chcn.com(/page/\\d{1,4})?/" // 首页匹配
    val ARTICLE_PATTERN = ".*2chcn.com/\\d{1,8}/.*" // 文章匹配
    val IMAGE_PATTERN = ".*img.2chcn.com/.*\\..*" // 图片匹配
    val CATEGORY_PATTERN = ".*2chcn.com/category/.*" // 分类匹配
    val TAG_PATTERN = ".*2chcn.com/tag/.*" // 标签匹配
    val SEARCH_PATTERN = ".*2chcn.com/(.*)?\\?s=.*" // 搜索匹配
    val AUTHOR_PATTERN = ".*2chcn.com/author/\\d{1,8}/.*" // 用户匹配

    val VIP_TAG = "【会员】" // 会员标签
    val NICH_TAG = "2ch：" // 会员标签
    val NICH2_TAG = "【2ch】" // 会员标签
    val TWITTER_TAG = "twitter：" // 会员标签

    val NICH_ENDS = " | 2ch中文网" // 标题后缀
}