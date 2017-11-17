package com.xmx.nichcn.module.history

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.xmx.nichcn.R

import com.xmx.nichcn.common.data.BaseEntityAdapter
import com.xmx.nichcn.module.history.History

import java.text.SimpleDateFormat

/**
 * Created by The_onE on 2016/3/27.
 * 操作日志实体适配器
 */
class HistoryAdapter(context: Context, data: List<History>) : BaseEntityAdapter<History>(context, data) {
    // 复用列表中的项
    private class ViewHolder {
        internal var title: TextView? = null
        internal var time: TextView? = null
    }

    // 将数据填充到列表项中
    @SuppressLint("SimpleDateFormat")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val cv: View
        val holder: ViewHolder
        if (convertView == null) {
            // 生成新View
            cv = LayoutInflater.from(mContext).inflate(R.layout.item_history, parent, false)
            // 生成ViewHolder
            holder = ViewHolder()
            holder.title = cv.findViewById(R.id.itemTitle)
            holder.time = cv.findViewById(R.id.itemTime)
            cv.tag = holder
        } else {
            cv = convertView
            holder = cv.tag as ViewHolder
        }
        // 处理数据
        if (position < mData.size) {
            val history = mData[position]
            // 日志内容
            holder.title?.text = history.mTitle
            // 日志时间
            val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val timeString = df.format(history.mTime)
            holder.time?.text = timeString
        } else {
            holder.title?.text = "加载失败"
        }

        return cv
    }
}