package com.cylan.smart.plugin.help

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.DrawableRes
import com.alibaba.android.arouter.launcher.ARouter
import com.cylan.smart.plugin.R

/**
 *
 * @author Lupy create on 19-1-12
 * @Description 加载失败　网络不可用视图管理 暂无数据
 *              添加到要显示的group中，父级视图只能 FrameLayout
 *
 */

class LoadingAndFailLayoutHelper<T : ViewGroup>(var parent: T) {

    private val layoutRes: Int = R.layout.home_load_fails_layout
    private var inflater: LayoutInflater

    private var rootLayout: View
    private var progress: View

    private var loadFailsLayout: View
    private var failsTv: TextView
    private var retryBtn: Button

    private var emptyLayout: View
    private var emptyTextView: TextView
    private var emptyImage: ImageView

    private var register :TextView

    init {
        if (parent !is RelativeLayout && parent !is FrameLayout) {
            throw IllegalArgumentException("父亲布局只能为FrameLayout")
        }
        inflater = LayoutInflater.from(parent.context)
        rootLayout = inflater?.inflate(layoutRes, parent, false)
        parent.addView(rootLayout)
        progress = rootLayout?.findViewById(R.id.progress)
        loadFailsLayout = rootLayout?.findViewById(R.id.loadFailLayout)
        failsTv = rootLayout?.findViewById(R.id.fail_des)
        retryBtn = rootLayout?.findViewById(R.id.retryBtn)
        emptyTextView = rootLayout?.findViewById(R.id.emptyTextView)
        emptyImage = rootLayout?.findViewById(R.id.emptyImage)
        emptyLayout = rootLayout?.findViewById(R.id.emptyViewLayout)
        register=rootLayout?.findViewById(R.id.registerForcompany)
    }

    /**
     * 开始加载
     */
    fun loadBegin() {
        rootLayout?.toVisible()
        progress?.toVisible()
        loadFailsLayout?.removeSpace()
        emptyLayout.removeSpace()
    }

    /**
     * 加载成功
     */
    fun loadSuccess() {
        rootLayout?.removeSpace()
    }

    /**
     * 加载失败
     */
    fun loadFails(onClickListener: View.OnClickListener?) {
        rootLayout?.toVisible()
        progress?.removeSpace()
        emptyLayout.removeSpace()
        loadFailsLayout.toVisible()
        failsTv.setText(R.string.load_fails)
        retryBtn.setOnClickListener(onClickListener)
    }

    /**
     * 网络链接失败
     */
    fun netFails(onClickListener: View.OnClickListener?) {
        rootLayout?.toVisible()
        progress?.removeSpace()
        loadFailsLayout.toVisible()
        failsTv.setText(R.string.net_fails)
        retryBtn.setOnClickListener(onClickListener)
        emptyLayout.removeSpace()
    }
    fun setRegisterListener(context : Context){
        register.setOnClickListener{
            ARouter.getInstance().build("/home/add_account").navigation(context)
        }
    }
    /**
     * 空数据视图的情况
     * 可以自定义图片和提示文字
     */
    fun loadNoData(showTips: String = "暂无数据", @DrawableRes imgRes: Int = -1, type:String ="") {
        if("Menu".equals(type)){
            register?.toVisible()
        }else{
            register?.removeSpace()
        }
        rootLayout?.toVisible()
        emptyTextView.text = showTips

        if (imgRes != -1) {
            emptyImage.toVisible()
            emptyImage.setImageResource(imgRes)
        }

        emptyLayout.toVisible()
        progress?.removeSpace()
        loadFailsLayout.removeSpace()
    }


    /**
     * 自定义失败文字
     */
    fun defineFails(resId: Int, onClickListener: View.OnClickListener?) {
        rootLayout?.toVisible()
        progress?.removeSpace()
        emptyLayout.removeSpace()

        loadFailsLayout.toVisible()
        failsTv.setText(resId)
        retryBtn.setOnClickListener(onClickListener)
    }

}
