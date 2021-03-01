package com.cylan.smart.base.dialog

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.fragment.app.DialogFragment
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.TextView
import com.cylan.smart.base.R


class ProgressDialog : DialogFragment() {


    companion object {
        var dialog: AlertDialog? = null
        lateinit var title: TextView
        lateinit var progressBar: ProgressBar
        var context: Context? = null
        fun showProgressDialog(context: Context, str: String) {
            this.context = context
            val view = View.inflate(context, R.layout.common_info_progress_dialog, null)

            title = view.findViewById(R.id.title)
            progressBar = view.findViewById(R.id.progressBar)
            title.text = str
            progressBar.visibility = View.VISIBLE
            title.visibility = View.GONE

            var builder = AlertDialog.Builder(context)
            builder.setView(view)
            dialog = builder.create()
            val window = dialog!!.window
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.show()
            window!!.setLayout(
                (Companion.context!!.resources.displayMetrics.widthPixels * 0.8).toInt(),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            val attributes = window.attributes
            attributes.dimAmount = 0.2f
            attributes.flags = attributes.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
            window.attributes = attributes

        }


        /**
         * 成功加载出数据
         */
        fun loadSuccess() {
            dialog?.dismiss()
        }

        /**
         * 加载失败或者数据为空时 显示提示语句
         *
         * @param textRes
         */
        fun loadFailWithPrompt(textRes: String) {
            progressBar.setVisibility(View.GONE)
            title.visibility = View.VISIBLE
            title.text = textRes

        }
    }
}