package com.xmx.nichcn.module.history

import android.content.ContentValues
import android.database.Cursor
import com.xmx.nichcn.common.data.sql.ISQLEntity
import java.util.*

/**
 * Created by The_onE on 2017/11/17.
 * 历史记录实体
 */
class History : ISQLEntity {
    private var mId: Long = -1
    // 地址
    var mUrl = ""
    // 标题
    var mTitle = ""
    // 访问时间
    var mTime: Date? = null

    override fun tableFields() =
            "ID integer not null primary key autoincrement, " +
                    "url text, " +
                    "title text, " +
                    "time integer not null default(0)"

    override fun getContent(): ContentValues {
        val content = ContentValues()
        if (mId > 0) {
            content.put("ID", mId)
        }
        content.put("url", mUrl)
        content.put("title", mTitle)
        content.put("time", mTime?.time)
        return content
    }

    override fun convertToEntity(c: Cursor): ISQLEntity {
        val entity = History()
        entity.mId = c.getLong(0)
        entity.mUrl = c.getString(1)
        entity.mTitle = c.getString(2)
        entity.mTime = Date(c.getLong(3))

        return entity
    }
}