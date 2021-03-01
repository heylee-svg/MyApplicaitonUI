package com.cylan.smart.plugin.ui.home.fragment.base;

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cylan.smart.plugin.R
import com.cylan.smart.plugin.help.LoadingAndFailLayoutHelper
import com.cylan.smart.plugin.widget.RecyclerItemDocor
import kotlinx.android.synthetic.main.home_base_recyclerview_layout.view.*
import kotlin.collections.ArrayList

/**
 * @author Lupy
 * @desc $ 自带刷新 加载更多的 列表 Fragment 用于单一的列表数据
 * @since 19-6-27
 */
abstract class BaseRecyclerViewFragment<T, VIEWHOLDER : RecyclerView.ViewHolder> : BaseFragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var loadingAndFailLayoutHelper: LoadingAndFailLayoutHelper<FrameLayout>
    private var datas: ArrayList<T> = ArrayList<T>()
    private lateinit var adapter: BaseInnerAdapter
    private lateinit var refreshLayout: SwipeRefreshLayout
    private var isRefreshing: Boolean = false
    private var isReload: Boolean = false
    /**
     * 分页加载数据的大小 默认100
     */
    protected var loadDataLimit: Int = 100


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = layoutInflater.inflate(R.layout.home_base_recyclerview_layout, container, false)
        recyclerView = view.findViewById(R.id.list_rv)
        loadingAndFailLayoutHelper = LoadingAndFailLayoutHelper(view.rootView as FrameLayout)
        refreshLayout = view.refreshLayout as SwipeRefreshLayout
        refreshLayout.setOnRefreshListener {
            if (!isRefreshing) {
                isRefreshing = true
                loadData()
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = BaseInnerAdapter().apply {
            loadMore = {
                loadData()
            }
        }
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(RecyclerItemDocor(context!!))
        loadData()
    }

    /**
     * 重新加载数据
     */
    protected fun reLoadData() {
        refreshLayout.isRefreshing = false
        isRefreshing = false
        isReload = true
        recyclerView.stopScroll()
        loadData()
    }

    /**
     * 开始加载数据
     */
    private fun loadData() {
        if (datas.size == 0 && !isRefreshing && !isReload) {//第一次显示加载框
            loadingAndFailLayoutHelper.loadBegin()
        }
        adapter.loadMoreEnable = false//开始加载数据的时候禁用加载更多
        actualLoad(if (isRefreshing || isReload) 0 else datas.size)
    }

    /**
     * 实际加载网络数据的地方,需要子类实现
     * @param offset 加载数据的偏移位置
     */
    abstract fun actualLoad(offset: Int)

    /**
     * 数据比较需要实现此接口，确定两条数据是一样的
     */
    abstract fun compareTo(data: T, target: T): Boolean

    /**
     * 请求被打断了
     */
    protected fun requestAbort() {
        adapter.loadMoreEnable = true
        if (datas.size == 0) {
            loadingAndFailLayoutHelper.loadNoData()
        } else {
            loadingAndFailLayoutHelper.loadSuccess()
        }
    }

    /**
     * 加载数据之后将数据注入列表
     * @param result 加载得到的数据
     * @param status 加载的状态 200代表加载成功，其他的失败
     */
    protected fun injectData(result: List<T>?, status: Int) {
        refreshLayout.isRefreshing = false
        if (status == 200) {
            loadingAndFailLayoutHelper.loadSuccess()
            result?.apply {
                if (isReload) {
                    datas.clear()
                }
                if (isRefreshing && datas.size != 0) {
                    recyclerView.stopScroll()
                    var target = datas.get(0)
                    var index = -1
                    for (i in 0..size) {
                        var d = get(i)
                        if (compareTo(d, target)) {
                            index = i
                            break
                        }
                    }
                    if (index == 0) {
                        //没有新数据，不用更新
                    }else if (index == size - 1) {
                        //说明全是新数据，害怕时间间隔太长，把老数据清掉
                        datas.clear()
                        datas.addAll(this)
                        recyclerView.adapter?.notifyDataSetChanged()
                    } else {
                        //部分是新数据，只把新数据加进来，老的不用清除
                        var tempList = ArrayList<T>();
                        if (index == -1) {
                            tempList.addAll(this)
                        } else {
                            tempList.addAll(subList(0, index))
                        }
                        var range = tempList.size
                        tempList.addAll(datas)
                        datas.clear()
                        datas.addAll(tempList)
                        recyclerView.adapter?.notifyItemRangeInserted(0, range)
                    }
                    recyclerView.scrollToPosition(0)
                } else {
                    datas.addAll(this)
                    recyclerView.adapter?.notifyDataSetChanged()
                    if (isReload) {
                        recyclerView.scrollToPosition(0)
                    }
                }

                if (datas.size == 0) {
                    loadingAndFailLayoutHelper.loadNoData()
                }
                //如果请求回来的结果不为空，则可以继续请求 否则就不能使用加载更多功能
                if (result.size != 0) {
                    adapter.loadMoreEnable = true
                }
            }
        } else {
            if (datas.size == 0) {
                loadingAndFailLayoutHelper.loadFails(View.OnClickListener {
                    loadData()
                })
            }
        }
        isRefreshing = false
        isReload = false
    }

    /**
     * 获取list条目的holder
     */
    abstract fun getAdapterViewHolder(p0: ViewGroup, itemType: Int): VIEWHOLDER

    /**
     * 条目holder的初始化
     */
    abstract fun initViewHolder(p0: VIEWHOLDER, itemData: T)

    /**
     * 重写此方法实现多条目
     */
    open fun getItemType(position: Int): Int {
        return 0
    }


    inner class BaseInnerAdapter() : RecyclerView.Adapter<VIEWHOLDER>() {

        var loadMoreEnable: Boolean = true
        var loadmoreStamp: Long = System.currentTimeMillis()
        var loadMore: () -> Unit = {}

        override fun getItemViewType(position: Int): Int {
            return getItemType(position)
        }

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): VIEWHOLDER {
            return getAdapterViewHolder(p0, p1)
        }

        override fun getItemCount(): Int {
            return datas.size
        }

        override fun onBindViewHolder(p0: VIEWHOLDER, p1: Int) {
            initViewHolder(p0, datas.get(p1))
        }

        override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
            super.onAttachedToRecyclerView(recyclerView)
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!loadMoreEnable || System.currentTimeMillis() - loadmoreStamp < 1000) {
                        return
                    }
                    var childCount = recyclerView.adapter?.itemCount ?: 0
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPostion = layoutManager.findLastCompletelyVisibleItemPosition()
                    if (childCount - lastPostion < loadDataLimit) {
                        loadmoreStamp = System.currentTimeMillis()
                        loadMore.invoke()
                    }
                }
            })
        }

    }

}
