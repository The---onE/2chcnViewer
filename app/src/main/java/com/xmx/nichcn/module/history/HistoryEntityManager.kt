package com.xmx.nichcn.module.history

import com.xmx.nichcn.common.data.sql.BaseSQLEntityManager
import org.greenrobot.eventbus.EventBus

import java.util.Date

/**
 * Created by The_onE on 2016/9/4.
 * 操作历史记录实体管理器，单例对象
 */
object HistoryEntityManager : BaseSQLEntityManager<History>() {

    init {
        tableName = "history" // 表名
        entityTemplate = History() // 实体模版
        // 打开数据库
        openDatabase()
    }

    /**
     * 添加历史记录
     * @param url 地址
     * @param title 标题
     */
    fun addHistory(url: String, title: String): Long {
        // 创建操作日志实体
        val entity = History()
        entity.mUrl = url
        entity.mTitle = title
        entity.mTime = Date()
        // 将历史添加到数据库
        return insertData(entity)
    }
}
