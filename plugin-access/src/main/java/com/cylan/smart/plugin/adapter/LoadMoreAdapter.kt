package com.cylan.smart.plugin.adapter

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater

/**
 *@author Lupy Create on 2019-08-14.
 *@description
 */
abstract class LoadMoreAdapter<T : RecyclerView.ViewHolder> : RecyclerView.Adapter<T> {

    var loadMoreEnable: Boolean = true
    var autoRefresh : Boolean = false  //给区分拉刷新 下拉加载
    var loadMore: () -> Unit = {}
    var context: Context
    var layoutInflater: LayoutInflater
    var loadmoreStamp: Long = System.currentTimeMillis()

    /**
     * 分页加载数据的大小 默认100
     */
    protected var loadDataLimit: Int = 100

    constructor(context: Context) : super() {
        this.context = context
        layoutInflater = LayoutInflater.from(context)
    }


    fun loadMore(loadMoreListener: () -> Unit) {
        this.loadMore = loadMoreListener
    }

    //
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        var offsetY = -1
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                offsetY = dy;
                System.out.println("AAAAAA onScrolled:" + recyclerView.scrollState + "dy" + dy)

            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!loadMoreEnable || System.currentTimeMillis() - loadmoreStamp < 1000) {
                    return
                }
                if (recyclerView.scrollState == RecyclerView.SCROLL_STATE_IDLE) {
                    System.out.println("AAAAAA autoRefresh:" + autoRefresh + "OffsetY" + offsetY)
                    if((autoRefresh&&offsetY>0)||!autoRefresh){
                        System.out.println("AAAAAA onScrollStateChanged:" + recyclerView.scrollState + "scrollStateChange" + offsetY)
                        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                        val lastPostion = layoutManager.findLastCompletelyVisibleItemPosition()

                        var childCount = recyclerView.adapter?.itemCount ?: 0
                        if (childCount - lastPostion < loadDataLimit) {
                            loadmoreStamp = System.currentTimeMillis()
                            loadMore.invoke()
                        }
                    }
                }
            }
        })
    }
}