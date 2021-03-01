package com.cylan.smart.plugin.ui

import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.TextView
import com.alibaba.android.arouter.launcher.ARouter
import com.cylan.smart.base.R
import com.cylan.smart.base.constant.ConstantField
import com.cylan.smart.base.dialog.InfoDialog
import com.cylan.smart.base.storage
import kotlinx.android.synthetic.main.common_activity_with_title_template.*
import android.graphics.Color
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager

/**
 * @author Lupy create on 19-1-14
 * @Description
 */

abstract class BaseActivity : AppCompatActivity() {


    protected var rightText: TextView? = null
    protected var rightIcon: ImageView? = null
    protected var titleTv: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (useCommonTitle()) {
            setContentView(R.layout.common_activity_with_title_template)
            var layoutParams = statusBar.layoutParams
            layoutParams.height = getStatusBarHeight()
            statusBar.layoutParams = layoutParams
            backIcon.setOnClickListener {
                finish()
            }
            View.inflate(this, layoutRes(), contentDefine)
            rightText = findViewById(R.id.rightText)
            rightIcon = findViewById(R.id.rightIcon)
            titleTv = findViewById(R.id.titleTv)
        } else {
            setContentView(layoutRes())
        }
        setTranslucentStatus()
        initView()
    }


    /**
     * 布局文件
     */
    abstract fun layoutRes(): Int

    /**
     * 初始化view
     */
    abstract fun initView()

    /**
     * 是否使用模板布局
     */
    abstract fun useCommonTitle(): Boolean


    @TargetApi(19)
    fun setTranslucentStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            val option =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            window.decorView.systemUiVisibility = option
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            )
        }
    }


    /**
     * @return 状态栏的高度
     */
    protected fun getStatusBarHeight(): Int {
        var result = 0
        //获取状态栏高度的资源id
        var resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    override fun onResume() {
        super.onResume()
        var intentFilter = IntentFilter()
        intentFilter.addAction("com.cylan.smart.token")
        LocalBroadcastManager.getInstance(this).registerReceiver(smartReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(smartReceiver)
    }

    var smartReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.action?.let {
                when (it) {
                    "com.cylan.smart.token" -> {
                        LocalBroadcastManager.getInstance(this@BaseActivity).unregisterReceiver(this)
                        var type = intent?.getIntExtra("type", 0)
                        var message = messageTips(type)
                        InfoDialog.showDialog(this@BaseActivity, message, negText = "", posClick = {
                            storage?.putString(ConstantField.TOKEN, "")
                            storage?.putString(ConstantField.PWD, "")
                            ARouter.getInstance()
                                .build("/home/main")
                                .withString("finish", "finish")
                                .navigation()
                        })
                    }
                    else -> {
                    }
                }
            }
        }
    }

    fun messageTips(type: Int): String = if (type == 0) {
        "登录过期,请您重新登录"
    } else if (type == 1) {
        "账号密码已修改,请您重新登入"
    } else if (type == 2) {
        "账号已停用"
    } else {
        ""
    }


    /**
     * 初始化title
     */
    protected fun initTilte(title: String) {
        titleTv?.text = title
    }

    /**
     * 初始化右侧文字
     */
    protected fun initRightText(text: String, function: () -> Unit) {
        rightText?.apply {
            visibility = View.VISIBLE
            this.text = text
            setOnClickListener {
                function.invoke()
            }
        }
    }

    /**
     * 初始化右侧图标
     */
    protected fun initRightIcon(@DrawableRes iconRes: Int, function: () -> Unit) {
        rightIcon?.apply {
            visibility = View.VISIBLE
            setImageResource(iconRes)
            setOnClickListener {
                function.invoke()
            }
        }
    }

    /**
     * 点击空白区域影藏小键盘
     */

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val manager =
            this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (event?.getAction() === MotionEvent.ACTION_DOWN) {
            if (this.getCurrentFocus() != null && this.getCurrentFocus()!!.getWindowToken() != null) {
                manager.hideSoftInputFromWindow(
                    this.getCurrentFocus()!!.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        }
        return super.onTouchEvent(event)
    }

}