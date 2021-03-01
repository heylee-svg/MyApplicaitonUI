package com.cylan.smart.base.dialog


import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.cylan.smart.base.R

/**
 * @author Lupy create on 19-1-15
 * @Description 提示信息弹窗
 */

class InfoDialog() : DialogFragment() {

    var positiveClick: (view: View) -> Unit = {}
    var negativeClick: (view: View) -> Unit = {}
    var message: String? = null
    var posText: String = ""
    var negText: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.smart_dialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val view = inflater.inflate(R.layout.logout_dialog, null) as LinearLayout
        view.findViewById<TextView>(R.id.dialog_message).setText(message)

        view.findViewById<TextView>(R.id.negitive_btn).apply {

            if (TextUtils.isEmpty(negText)) {
                visibility = View.GONE
            } else {
                setText(negText)
                setOnClickListener {
                    negativeClick.invoke(it)
                    dismiss()
                }
            }
        }

        view.findViewById<TextView>(R.id.positive_btn).apply {
            setOnClickListener {
                positiveClick.invoke(it)
                dismiss()
            }

            setText(posText)
        }

        view.minimumHeight = (0.2 * resources.displayMetrics.heightPixels).toInt()

        return view
    }


    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(false)
            val window = dialog.window

            window!!.setLayout(
                (resources.displayMetrics.widthPixels * 0.8).toInt(),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            val attributes = window.attributes
            attributes.dimAmount = 0.2f
            attributes.flags = attributes.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
            window.attributes = attributes
        }
    }


    companion object {
        fun showDialog(
                activity: FragmentActivity,
                message: String,
                posText: String = "确定",
                posClick: (view: View) -> Unit = {},
                negText: String = "取消",
                negClick: (view: View) -> Unit = {}
        ): InfoDialog {
            var infoDialog = InfoDialog();
            infoDialog.message = message
            infoDialog.posText = posText
            infoDialog.negText = negText
            infoDialog.positiveClick = posClick
            infoDialog.negativeClick = negClick
            infoDialog.show(activity.supportFragmentManager, "infoDialog")
            return infoDialog
        }
    }


}