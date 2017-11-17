package com.xmx.nichcn.module.history

import android.os.Bundle
import android.support.v7.app.AlertDialog
import com.xmx.nichcn.R
import com.xmx.nichcn.base.activity.BaseTempActivity
import com.xmx.nichcn.module.article.ArticleUtil
import com.xmx.nichcn.utils.StringUtil
import kotlinx.android.synthetic.main.activity_history.*

import java.util.ArrayList

/**
 * Created by The_onE on 2016/10/3.
 * 查看日志页，显示所有本应用的操作日志
 */
class HistoryActivity : BaseTempActivity() {

    // 操作日志列表适配器
    private var historyAdapter: HistoryAdapter? = null

    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_history)
        historyAdapter = HistoryAdapter(this, ArrayList())
        listHistory.adapter = historyAdapter
    }

    override fun setListener() {
        // 清空日志
        btnClearHistory.setOnClickListener {
            HistoryManager.clearHistory()
            HistoryManager.data?.apply {
                historyAdapter?.updateList(this)
            }
        }
        // 点击进入对应文章页
        listHistory.setOnItemClickListener { _, _, i, _ ->
            val item = historyAdapter?.getItem(i) as History
            ArticleUtil.openArticle(this, item.mUrl)
        }
        // 长按提示操作
        listHistory.setOnItemLongClickListener { _, _, i, _ ->
            val item = historyAdapter?.getItem(i) as History
            AlertDialog.Builder(this)
                    .setMessage("要操作该记录吗？")
                    .setTitle("提示")
                    // 删除
                    .setPositiveButton("删除") { _, _ ->
                        HistoryManager.deleteHistory(item.mId)
                        HistoryManager.data?.apply {
                            historyAdapter?.updateList(this)
                        }
                    }
                    .setNegativeButton("复制链接") { _, _ ->
                        StringUtil.copyToClipboard(this, item.mUrl)
                    }
                    .setNeutralButton("取消") { dialogInterface, _ -> dialogInterface.dismiss() }
                    .show()
            true
        }
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        HistoryManager.updateData()
        HistoryManager.data?.apply {
            historyAdapter?.updateList(this)
        }
    }
}
