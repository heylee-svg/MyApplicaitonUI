package com.cylan.smart.plugin.ui.home.fragment.accesscontrol

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import com.cylan.smart.base.widget.calendarview.DateUtils
import com.cylan.smart.plugin.R
import com.cylan.smart.plugin.help.LoadingAndFailLayoutHelper
import com.cylan.smart.plugin.ui.home.fragment.base.BaseFragment
import com.cylan.smart.plugin.ui.home.iview.DoorRecordView
import com.cylan.smart.plugin.ui.home.present.DoorRecordPresent
import com.cylan.smart.plugin.widget.RecyclerItemDocor
import kotlinx.android.synthetic.main.home_doorguard_record_fragment_layout.*
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.cylan.smart.plugin.ui.home.MainActivity


/**
 *@author Lupy Create on 2019-08-12.
 *@description 门禁记录
 */

class DoorRecordFragment : BaseFragment(), DoorRecordView {

    private var isSearchState: Boolean = false  //是否在搜索状态
    private var shouldSaveData: Boolean = true  //是否需要存数据

    lateinit var doorRecordPresent: DoorRecordPresent
    var loadingAndFailLayoutHelper: LoadingAndFailLayoutHelper<FrameLayout>? = null
    lateinit var mActivity: MainActivity

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mActivity = activity as MainActivity
        doorRecordPresent = DoorRecordPresent(context, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_doorguard_record_fragment_layout, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        doorRecordPresent.start_time = 0
        doorRecordPresent.end_time = DateUtils.getCurrentTime(23, 59, 59) //view刚创建的时候load最新的数据
        doorRecordPresent.clearSearchData()
        doorRecordPresent.loadData()


    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initView() {
        loadingAndFailLayoutHelper = LoadingAndFailLayoutHelper(contentLayout)
        guardList.layoutManager = LinearLayoutManager(context)
        guardList.adapter = doorRecordPresent.getAdapter()
        guardList.addItemDecoration(RecyclerItemDocor(context!!))
        doorRecordPresent.getAdapter().autoRefresh=true
        swipe_refresh.setOnRefreshListener {
            System.out.println("AAAAAA onrefresh:" )
            doorRecordPresent.start_time = 0
            doorRecordPresent.end_time = DateUtils.getCurrentTime(23, 59, 59) //初始化当前时间
            doorRecordPresent.clearSearchData()
            doorRecordPresent.loadData()

        }


        search_icon.background = ContextCompat.getDrawable(context!!, R.mipmap.icon_search)

        searchKeyInput!!.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                //触摸输入框
                if (shouldSaveData) {
                    doorRecordPresent.saveData()
                    shouldSaveData = false
                }
                search_icon.background =
                    ContextCompat.getDrawable(context!!, R.mipmap.cancel_search)
                isSearchState = true
            }
            false
        }
        search_icon.setOnClickListener {
            if (isSearchState) {
                search_icon.background =
                    ContextCompat.getDrawable(context!!, R.mipmap.icon_search)
                searchKeyInput.text.clear()
                doorRecordPresent.restoreData()
                showKeyboard(false)
                shouldSaveData = true
            } else {
                if (shouldSaveData) {
                    doorRecordPresent.saveData()
                    shouldSaveData = false
                }
                search_icon.background =
                    ContextCompat.getDrawable(context!!, R.mipmap.cancel_search)
                showKeyboard(true)
            }
            isSearchState = !isSearchState
        }

        searchKeyInput.setOnEditorActionListener { v, actionId, event ->

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (searchKeyInput.text.toString().isEmpty()) {
                    //隐藏小键盘
                    true
                } else {
                    doorRecordPresent.name = searchKeyInput.text.toString()
                    doorRecordPresent.loadData()
                    true
                }
                showKeyboard(false)
            }

            false
        }

    }


    override fun emptyData() {
        loadingAndFailLayoutHelper?.loadNoData("暂无记录")
    }

    override fun OnShopIdOrBrandIdChange() {
        doorRecordPresent.reLoad()
    }


    override fun loadBegin() {
        loadingAndFailLayoutHelper?.loadBegin()
    }

    override fun loadSuccess() {
        swipe_refresh.isRefreshing = false
        loadingAndFailLayoutHelper?.loadSuccess()
    }

    override fun loadFails() {
        swipe_refresh.isRefreshing = false
        loadingAndFailLayoutHelper?.loadFails(object : View.OnClickListener {
            override fun onClick(v: View?) {
                doorRecordPresent.loadData()
            }
        })
    }

    override fun netError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1101 && resultCode == Activity.RESULT_OK) {
            var defineBegin = data!!.getStringExtra("begin")
            var defineEnd = data!!.getStringExtra("end")
            Log.e("lupy", "开始日期 ${defineBegin} 结束日期 ${defineEnd}")
            var beginDate = DateUtils.getTimeByDate(
                defineBegin.split("-")[0].toInt(),
                defineBegin.split("-")[1].toInt(),
                defineBegin.split("-")[2].toInt()
            )

            var endDate = DateUtils.getTimeByDate(
                defineEnd.split("-")[0].toInt(),
                defineEnd.split("-")[1].toInt(),
                defineEnd.split("-")[2].toInt(),
                23, 59, 59
            )

            doorRecordPresent.start_time = beginDate
            doorRecordPresent.end_time = endDate
            doorRecordPresent.clearSearchData()
            doorRecordPresent.loadData()


        }
    }

    protected fun showKeyboard(isShow: Boolean) {
        val imm = mActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (isShow) {
            if (mActivity.currentFocus == null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            } else {
                imm.showSoftInput(mActivity.currentFocus, 0)
            }
        } else {
            if (mActivity.currentFocus != null) {
                imm.hideSoftInputFromWindow(
                    mActivity.currentFocus!!.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        }
    }
}
