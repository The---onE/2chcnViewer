package com.xmx.nichcn.core.activity

import android.Manifest
import android.app.AppOpsManager
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import com.xmx.nichcn.R
import com.xmx.nichcn.base.activity.BaseSplashActivity
import com.xmx.nichcn.core.CoreConstants
import com.xmx.nichcn.utils.Timer
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * Created by The_onE on 2017/2/15.
 * 应用启动页，一定时间后自动或点击按钮跳转至主Activity
 */
class SplashActivity : BaseSplashActivity() {
    // 跳转至主Activity定时器
    private val timer = Timer {
        timeFlag = true
        skip()
    }
    private var readyFlag = false // 数据库是否已更新完毕
    private var timeFlag = false // 是否已过自动跳转时间
    private var skipFlag = false // 是否已跳转

    private val writeSdRequest = 1 // 申请读写SD数据权限

    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_splash)
    }

    override fun setListener() {
        // 点击跳过按钮直接跳到主页
        btnSkip.setOnClickListener { skip() }
        btnPermission.setOnClickListener { checkPermission() }
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        checkPermission()
    }

    private fun checkPermission() {
        // 检验是否有读写SD数据权限
        if (checkLocalPhonePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, writeSdRequest)) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                if (checkOpsPermission(AppOpsManager.OPSTR_WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, writeSdRequest)) {
                    // 具有权限则进行初始化
                    init()
                }
            } else {
                // 具有权限则进行初始化
                init()
            }
        }
    }

    private fun init() {
        ready()
        // 开启跳转定时器
        timer.start(CoreConstants.SPLASH_TIME, true)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            writeSdRequest -> if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                // 用户拒绝了读写SD数据权限
                showToast("您拒绝了读写手机存储的权限，某些功能会导致程序出错，请手动允许该权限！")
                btnPermission.visibility = View.VISIBLE
            } else {
                // 用户同意了读写SD数据权限
                init()
                btnPermission.visibility = View.GONE
            }
        }
    }

    /**
     * 若已准备就绪则跳转至主页
     */
    private fun skip() {
        if (!skipFlag && readyFlag) {
            jumpToMainActivity()
            skipFlag = true
        }
    }

    /**
     * 设置状态已准备就绪，显示跳过按钮
     */
    private fun ready() {
        readyFlag = true
        btnSkip.visibility = View.VISIBLE
    }
}
