package com.xmx.nichcn.common.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Typeface

// TODO
@SuppressLint("StaticFieldLeak")
/**
 * Created by The_onE on 2016/2/21.
 * 手机默认存储数据管理器
 */
object DataManager {

    private lateinit var mContext: Context
    private lateinit var mData: SharedPreferences

    /**
     * 初始化管理器
     */
    fun setContext(context: Context) {
        mContext = context
        mData = context.getSharedPreferences("DATA", Context.MODE_PRIVATE)
    }

    public fun setJumpUrl(url: String) {
        setString("JumpUrl", url)
    }

    public fun getJumpUrl() = getString("JumpUrl")

    private fun getInt(key: String, def: Int): Int = mData.getInt(key, def)

    private fun setInt(key: String, value: Int) {
        val editor = mData.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    private fun getFloat(key: String, def: Float): Float = mData.getFloat(key, def)

    private fun setFloat(key: String, value: Float) {
        val editor = mData.edit()
        editor.putFloat(key, value)
        editor.apply()
    }

    private fun getLong(key: String, def: Long): Long = mData.getLong(key, def)

    private fun setLong(key: String, value: Long) {
        val editor = mData.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    private fun getBoolean(key: String, def: Boolean): Boolean = mData.getBoolean(key, def)

    private fun setBoolean(key: String, value: Boolean) {
        val editor = mData.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    private fun getString(key: String): String = mData.getString(key, "")

    private fun getString(key: String, def: String): String = mData.getString(key, def)

    private fun setString(key: String, value: String) {
        val editor = mData.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getSearchValue(key: String): String = getString("s_" + key)

    fun setSearchValue(key: String, value: String) {
        setString("s_" + key, value)
    }
}
