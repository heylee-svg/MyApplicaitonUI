package com.cylan.smart.plugin.adapter

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import com.cylan.smart.base.BaseQuickAdapter
import com.cylan.smart.base.utils.hhmmFormat
import com.cylan.smart.plugin.R
import com.cylan.smart.plugin.data.bean.KaoqinInfo
import com.cylan.smart.plugin.widget.DotView

/**
 * @author Lupy
 * @since 2019/8/18
 */
class KaoqinTotalAdapter(context: Context, var kaoqinTotals: List<KaoqinInfo>) :
    BaseQuickAdapter<KaoqinInfo>(context, kaoqinTotals) {

    override fun itemLayout(): Int = R.layout.home_kaoqin_total_item_layout

    override fun convert(helper: ViewHolderHelper, t: KaoqinInfo) {

        var dotView = helper.itemView.findViewById<DotView>(R.id.lineBottom)
        var time = helper.itemView.findViewById<TextView>(R.id.time)
        var name = helper.itemView.findViewById<TextView>(R.id.name)
        time.text = hhmmFormat(t.time)
        name.text = t.person_name
        dotView.visibility = if (helper.pos == itemCount - 1) View.GONE else View.VISIBLE
    }


}