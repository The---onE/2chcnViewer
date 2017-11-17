package com.xmx.nichcn.module.history

/**
 * Created by The_onE on 2016/2/24.
 * 历史记录数据管理器，单例对象
 */
object HistoryManager {
    private val historyManager = HistoryEntityManager
    // 实体管理器版本，不一致时需更新
    private var sqlVersion: Long = 0
    // 自身版本，提示调用者是否有更新
    var version = System.currentTimeMillis()
        private set
    // 实体列表
    private var sqlList: List<History>? = null

    // 获取数据
    val data: List<History>?
        get() = sqlList

    /**
     * 添加历史记录
     * @param url 地址
     * @param title 标题
     */
    fun addHistory(url: String, title: String) {
        historyManager.addHistory(url, title)
        updateData()
    }

    fun clearHistory() {
        historyManager.clearDatabase()
        updateData()
    }

    // 更新数据，若实体有更新则同步更新
    fun updateData(): Long {
        // 判断实体是否有更新
        if (historyManager.version != sqlVersion) {
            sqlVersion = historyManager.version
            // 将所有日志按时间降序排列
            sqlList = historyManager.selectAll("Time", false)
            // 数据更新
            version++
        }
        return version
    }
}
