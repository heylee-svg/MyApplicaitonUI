package com.cylan.smart.plugin.ui.home.fragment.accesscontrol;

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.cylan.smart.plugin.R
import com.cylan.smart.plugin.data.DataManger
import com.cylan.smart.plugin.data.bean.CaptureListItem
import com.cylan.smart.plugin.help.toHiden
import com.cylan.smart.plugin.help.toVisible
import com.cylan.smart.plugin.ui.home.fragment.base.BaseRecyclerViewFragment
import com.cylan.smart.plugin.ui.home.globalBrandId
import com.cylan.smart.plugin.ui.home.globalShopId
import kotlinx.android.synthetic.main.home_capture_list_item.view.*
import java.text.SimpleDateFormat

/**
 * @author Lupy
 * @desc $ 抓拍记录
 * @since 19-6-24
 */
class CaptureRecordFragment : BaseRecyclerViewFragment<CaptureListItem, CaptureRecordFragment.CaptureHolder>() {

    var dateFormatStrng = SimpleDateFormat("yyyy.MM.dd HH:mm:ss")

    companion object {
        var TAG = "CaptureRecordFragment"
    }


    override fun actualLoad(offset: Int) {
        if (globalShopId == null) {
            requestAbort()
            return
        }
        DataManger.getInstance().getChefList(globalShopId!!, globalBrandId ?: "all", offset) { list, i ->
            injectData(list,i)
        }

    }

    override fun getAdapterViewHolder(p0: ViewGroup, itemType: Int): CaptureHolder {
        return CaptureHolder(LayoutInflater.from(context).inflate(R.layout.home_capture_list_item, p0, false))
    }

    override fun initViewHolder(holder: CaptureHolder, itemData: CaptureListItem) {
        Glide.with(context!!).load(itemData.face_url).apply(
            RequestOptions().placeholder(R.mipmap.icon_head)
                .error(R.mipmap.icon_head)
        ).into(holder.image)
        holder.capture_time.text = "${dateFormatStrng.format(itemData.time?.times(1000))}"
        if (itemData.has_hat == 2) {
            holder.hat_tv.toHiden()
        } else {
            holder.hat_tv.toVisible()
            holder.hat_tv.setText("厨师帽：${getString(if (itemData.has_hat == 1) R.string.HOME_TEST_OK else R.string.HOME_TEST_NO)}")
            holder.hat_tv.setTextColor(resources.getColor(if (itemData.has_hat == 1) R.color.text_color else R.color.warn_text_color))
        }

        if (itemData.has_mask == 2) {
            holder.mask_tv.toHiden()
        } else {
            holder.mask_tv.toVisible()
            holder.mask_tv.setText("口罩：${getString(if (itemData.has_mask == 1) R.string.HOME_TEST_OK else R.string.HOME_TEST_NO)}")
            holder.mask_tv.setTextColor(resources.getColor(if (itemData.has_mask == 1) R.color.text_color else R.color.warn_text_color))
        }

        if (itemData.person_name.isNullOrEmpty()) {
            holder.staff_name.toHiden()
        } else {
            holder.staff_name.toVisible()
            holder.staff_name.text = "员工姓名: ${itemData.person_name}"
        }
    }

    override fun compareTo(data: CaptureListItem, target: CaptureListItem): Boolean {
        return data.time == target.time
    }

    /**
     * 切换店铺时调用
     */

    override fun OnShopIdOrBrandIdChange() {
        reLoadData()
    }

    inner class CaptureHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var image: ImageView
        var capture_time: TextView
        var mask_tv: TextView
        var hat_tv: TextView
        var staff_name: TextView

        init {
            image = itemView.image
            capture_time = itemView.capture_time
            mask_tv = itemView.mask_tv
            hat_tv = itemView.hat_tv
            staff_name = itemView.staff_name
        }
    }
}
