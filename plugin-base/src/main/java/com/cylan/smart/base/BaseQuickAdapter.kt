package com.cylan.smart.base

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * @author Lupy create on 19-1-21
 * @Description 基类适配器　　
 */

abstract class BaseQuickAdapter<T>(var context: Context, var source: List<T>) :
    RecyclerView.Adapter<BaseQuickAdapter<T>.ViewHolderHelper>() {

    var layoutInflater: LayoutInflater
    var onItemClickListener: OnItemClickListener? = null
    var loadMore: () -> Unit = {}
    var lastLoadMoreTime: Long
    var loadMoreEnable: Boolean = true;

    init {
        layoutInflater = LayoutInflater.from(context)
        lastLoadMoreTime = System.currentTimeMillis()
    }

    abstract fun itemLayout(): Int

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolderHelper {
        return ViewHolderHelper(layoutInflater.inflate(itemLayout(), p0, false))
    }

    override fun getItemCount(): Int {
        if (source == null || source.size == 0) {
            return 0
        } else {
            return source.size
        }
    }

    override fun onBindViewHolder(helper: ViewHolderHelper, p1: Int) {
        helper.bindPosi(p1)
        var t = source.get(p1)
        convert(helper, t)
    }


    abstract fun convert(helper: ViewHolderHelper, t: T)


    inner class ViewHolderHelper(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var pos: Int = 0;

        init {
            itemView.setOnClickListener {
                onItemClickListener?.onItemClick(it, pos)
            }
        }


        fun bindPosi(pos: Int) {
            this.pos = pos
        }

        fun setText(resId: Int, content: String) {
            itemView.findViewById<TextView>(resId)?.setText(content ?: " ")
        }

        fun setImage(resId: Int, drawableRes: Int) {
            itemView.findViewById<ImageView>(resId)?.setImageResource(drawableRes)
        }

        fun loadImage(resId: Int, url: String) {
            itemView.findViewById<ImageView>(resId)?.let {
                Glide.with(context).load(url).apply(
                    RequestOptions().dontAnimate().skipMemoryCache(false)
                ).into(it)
            }
        }

        fun loadImage(resId: Int, url: String,@DrawableRes error:  Int) {
            itemView.findViewById<ImageView>(resId)?.let {
                Glide.with(context).load(url).apply(
                    RequestOptions().dontAnimate().skipMemoryCache(false)
                        .error(error)
                ).into(it)
            }
        }
    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!loadMoreEnable) {
                    return
                }
                var childCount = recyclerView.adapter?.itemCount ?: 0
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                if (recyclerView.scrollState == RecyclerView.SCROLL_STATE_SETTLING && recyclerView.childCount > 0) {
                    val lastPasition = layoutManager.findLastCompletelyVisibleItemPosition()
                    if (childCount - lastPasition == 2 && System.currentTimeMillis() - lastLoadMoreTime > 3000) {
                        //加上时间限制，防止一秒请求多次
                        lastLoadMoreTime = System.currentTimeMillis()
                        loadMore.invoke()
                    }
                }
            }
        })
    }

    fun loadMore(loadMore: () -> Unit) {
        this.loadMore = loadMore
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, pos: Int)
    }

}