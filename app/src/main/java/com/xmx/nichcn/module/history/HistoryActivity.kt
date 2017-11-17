package com.xmx.nichcn.module.history

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.xmx.nichcn.R
import com.xmx.nichcn.base.activity.BaseTempActivity
import com.xmx.nichcn.module.article.ArticleUtil
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

        listHistory.setOnItemClickListener { _, _, i, _ ->
            val item = historyAdapter?.getItem(i) as History
            ArticleUtil.openArticle(this, item.mUrl)
        }
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        HistoryManager.updateData()
        HistoryManager.data?.apply {
            historyAdapter?.updateList(this)
        }
    }
}
