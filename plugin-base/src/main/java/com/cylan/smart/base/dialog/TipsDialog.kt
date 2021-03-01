package com.cylan.smart.base.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.fragment.app.DialogFragment
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import com.cylan.smart.base.R

class TipsDialog :DialogFragment(){
    companion object {

        fun showCommonTipsDialog(str: String, context: Context) {
            var dialog: AlertDialog
            val view = View.inflate(context, R.layout.common_info_dialog, null)

            val title = view.findViewById<TextView>(R.id.title)
            val confirmTv = view.findViewById<TextView>(R.id.ok)
            title.text = str

            var builder = AlertDialog.Builder(context)
            builder.setView(view)
            dialog = builder.create()
            val window = dialog.window
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
            window!!.setLayout(
                (context.resources.displayMetrics.widthPixels * 0.8).toInt(),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            val attributes = window.attributes
            attributes.dimAmount = 0.2f
            attributes.flags = attributes.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
            window.attributes = attributes

            confirmTv.setOnClickListener {
                dialog.dismiss()
            }
        }


    }
}
