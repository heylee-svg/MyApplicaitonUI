package com.cylan.smart.plugin.ui.home.fragment.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cylan.smart.plugin.R
import com.cylan.smart.plugin.widget.SmartTabLayout
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.home_common_tablayout_fragment.*

/**
 * @author Lupy
 * @since 19-6-24
 * @desc $ 负责子fragmeng的切换 fragment中有多个子fragment切换的场景
 */

abstract class TablayoutBaseFragment : BaseFragment() {


    var current: Fragment? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_common_tablayout_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        for (title in onTitles()) {
            val newTab = smartTabLayout.newTab()
            newTab.text = title
            smartTabLayout.addTab(newTab)
        }

        var indicator = smartTabLayout.getChildAt(0) as ViewGroup
        var childNum = indicator.childCount
        for (i in 0 until childNum) {
            var child = indicator.getChildAt(i);
            val layoutParams = child?.layoutParams
            layoutParams?.width = ViewGroup.LayoutParams.WRAP_CONTENT
            child?.layoutParams = layoutParams
        }
        initView()
        initCheckgroup()
    }

    private fun initCheckgroup() {

        smartTabLayout.setOnTabSelectedListener(object : SimpleTabSelector() {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                switchFragment(tab?.position ?: 0)
            }
        })

        switchFragment(0)
    }

    /**
     * 切换fragment
     */
    fun switchFragment(index: Int) {
        var tag = loadChildFragmentTagByIndex(index)
        var target = childFragmentManager.findFragmentByTag(tag)
        var transaction = childFragmentManager.beginTransaction()
        if (target == null) {
            if (target == null) {
                target = loadChildFragmentByIndex(index)
            }
            if (target == null) {
                return throw IllegalArgumentException("切换的目标fragment不能为空")
            }
            transaction.add(R.id.fragmentContainer, target, tag)
        }
        current?.apply {
            transaction.hide(this)
        }
        target?.apply {
            transaction.show(this)
        }
        transaction.commitAllowingStateLoss()
        current = target

    }

    /**
     * 根据tag来获取fragment
     */
    protected fun getChildByTag(tag: String): Fragment? {
        return childFragmentManager.findFragmentByTag(tag)
    }

    /**
     * 获取所有的fragment
     */
    protected fun getChilds(): List<Fragment> {
        return childFragmentManager.fragments
    }

    protected fun getSmartTablayout(): SmartTabLayout = smartTabLayout

    /**
     * 初始化
     */
    protected open fun initView(){

    }

    /**
     * 初始化标题
     */
    abstract fun onTitles(): Array<String>

    /**
     * 获取子fragment
     */
    abstract fun loadChildFragmentByIndex(index: Int): Fragment

    /**
     * 获取孩子的标签 可以不重写，默认指定一个
     */
    protected open fun loadChildFragmentTagByIndex(index: Int): String {
        return "${index}-fragment"
    }


    inner abstract class SimpleTabSelector : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(p0: TabLayout.Tab?) {
        }

        override fun onTabUnselected(p0: TabLayout.Tab?) {
        }
    }

}