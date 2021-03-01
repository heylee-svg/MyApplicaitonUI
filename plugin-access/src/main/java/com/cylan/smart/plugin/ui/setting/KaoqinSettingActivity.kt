package com.cylan.smart.plugin.ui.setting

import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.alibaba.android.arouter.facade.annotation.Route
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnDialogDismissListener
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.bigkoo.pickerview.view.TimePickerView
import com.cylan.smart.base.constant.ConstantField
import com.cylan.smart.base.dialog.TipsDialog
import com.cylan.smart.base.storage
import com.cylan.smart.base.utils.FormatUtils
import com.cylan.smart.plugin.R
import com.cylan.smart.plugin.R.*
import com.cylan.smart.plugin.data.DataManger
import com.cylan.smart.plugin.data.bean.KaoqinRule
import com.cylan.smart.plugin.ui.BaseActivity
import com.cylan.smart.plugin.ui.home.model.RecognitionViewModel
import kotlinx.android.synthetic.main.home_kaoqin_setting.*
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

@Route(path = "/home/kaoqin_setting")
class KaoqinSettingActivity : BaseActivity(), View.OnClickListener {
    var TAG: String = "KaoqinSettingActivity"

    var kaoqinBean: KaoqinRule? = null
    var isMonChecked = false
    var isTuesChecked = false
    var isWesChecked = false
    var isThurChecked = false
    var isFriChecked = false
    var isSatChecked = false
    var isSunChecked = false

    var isKaoqinChecked = false

    var editState = false //初始的可编辑状态
    var isFirstEdit = true


    override fun useCommonTitle(): Boolean = true

    override fun layoutRes(): Int = R.layout.home_kaoqin_setting


    override fun initView() {
        company_name.text = storage?.getString(ConstantField.SHOP_NAME, "")
        titleTv?.text = "修改考勤规则"
        rightText?.text = "编辑"
        rightText?.visibility = View.VISIBLE
        rightText?.setOnClickListener(this)
        am_time_from.setOnClickListener(this)
        am_time_to.setOnClickListener(this)
        pm_time_from.setOnClickListener(this)
        pm_time_to.setOnClickListener(this)
        monday.setOnClickListener(this)
        tuesday.setOnClickListener(this)
        wesday.setOnClickListener(this)
        thursday.setOnClickListener(this)
        friday.setOnClickListener(this)
        saturday.setOnClickListener(this)
        sunday.setOnClickListener(this)
        kaoqin_check.setOnClickListener(this)
        getKaoqinRule()

    }


    override fun onClick(v: View?) {
        when (v) {
            rightText -> {
                when (rightText?.text) {
                    resources.getString(string.finish) -> {
                        rightText?.text = resources.getString(string.edit) //在浏览状态
                        editState = false
                        initView(editState)
                        if (isFirstEdit) {
                            setKaoqinRule()
                        } else {
                            editKaoqinRule()
                        }

                    }
                    resources.getString(string.edit) -> {
                        rightText?.text = resources.getString(string.finish) //在编辑状态
                        editState = true
                        initView(editState)


                    }
                }
            }
            am_time_from -> {
                getDateDialog().show(am_time_from)
                rightText?.isEnabled = false

            }
            am_time_to -> {
                getDateDialog().show(am_time_to)
                rightText?.isEnabled = false

            }
            pm_time_from -> {
                getDateDialog().show(pm_time_from)
                rightText?.isEnabled = false
            }
            pm_time_to -> {
                getDateDialog().show(pm_time_to)
                rightText?.isEnabled = false
            }
            monday -> {
                isMonChecked = !isMonChecked
                updateView(v as TextView, isMonChecked, editState)

            }
            tuesday -> {
                isTuesChecked = !isTuesChecked
                updateView(v as TextView, isTuesChecked, editState)

            }
            wesday -> {
                isWesChecked = !isWesChecked
                updateView(v as TextView, isWesChecked, editState)

            }
            thursday -> {
                isThurChecked = !isThurChecked
                updateView(v as TextView, isThurChecked, editState)

            }
            friday -> {
                isFriChecked = !isFriChecked
                updateView(v as TextView, isFriChecked, editState)

            }
            saturday -> {
                isSatChecked = !isSatChecked
                updateView(v as TextView, isSatChecked, editState)

            }
            sunday -> {
                isSunChecked = !isSunChecked
                updateView(v as TextView, isSunChecked, editState)

            }
            kaoqin_check -> {
                if (editState) {
                    isKaoqinChecked = !isKaoqinChecked
                    if (isKaoqinChecked) kaoqin_check.text =
                        resources.getString(R.string.enable) else kaoqin_check.text =
                        resources.getString(R.string.disable)
                    kaoqinBean?.enable = isKaoqinChecked
                }

            }


        }
    }

    var pvTime: TimePickerView? = null
    var recognitionMode = RecognitionViewModel()
    val timeFormat by lazy { SimpleDateFormat("HH:mm") }

    fun initData(bean: KaoqinRule) {

        var weekstr = FormatUtils.intToByte(bean.work_week)
        Log.i(TAG, "weekstr is:" + weekstr)
        if ('0'.equals(weekstr.get(weekstr.length - 1))) isMonChecked = false else isMonChecked =
            true
        if ('0'.equals(weekstr.get(weekstr.length - 2))) isTuesChecked = false else isTuesChecked =
            true
        if ('0'.equals(weekstr.get(weekstr.length - 3))) isWesChecked = false else isWesChecked =
            true
        if ('0'.equals(weekstr.get(weekstr.length - 4))) isThurChecked = false else isThurChecked =
            true
        if ('0'.equals(weekstr.get(weekstr.length - 5))) isFriChecked = false else isFriChecked =
            true
        if ('0'.equals(weekstr.get(weekstr.length - 6))) isSatChecked = false else isSatChecked =
            true
        if ('0'.equals(weekstr.get(weekstr.length - 7))) isSunChecked = false else isSunChecked =
            true
        isKaoqinChecked = bean.enable


        am_time_from.text = bean.work_time_am.split(Pattern.compile("-"), 0)[0]
        am_time_to.text = bean.work_time_am.split(Pattern.compile("-"), 0)[1]
        pm_time_from.text = bean.work_time_pm.split(Pattern.compile("-"), 0)[0]
        pm_time_to.text = bean.work_time_pm.split(Pattern.compile("-"), 0)[1]
        kaoqin_check.text =
            if (bean.enable) resources.getString(R.string.enable) else resources.getString(R.string.disable)


    }

    fun initView(editable: Boolean) {
        if (!isSatChecked && !isSunChecked) {
            weekend_layout.visibility = View.GONE
        }
        updateView(am_time_from, editable)
        updateView(am_time_to, editable)
        updateView(pm_time_from, editable)
        updateView(pm_time_to, editable)
        //如果在编辑状态
        if (editable) {
            weekend_layout.visibility = View.VISIBLE
            monday.visibility = View.VISIBLE
            tuesday.visibility = View.VISIBLE
            wesday.visibility = View.VISIBLE
            thursday.visibility = View.VISIBLE
            friday.visibility = View.VISIBLE
            saturday.visibility = View.VISIBLE
            sunday.visibility = View.VISIBLE
        }
        updateView(monday, isMonChecked, editable)
        updateView(tuesday, isTuesChecked, editable)
        updateView(wesday, isWesChecked, editable)
        updateView(thursday, isThurChecked, editable)
        updateView(friday, isFriChecked, editable)
        updateView(saturday, isSatChecked, editable)
        updateView(sunday, isSunChecked, editable)
        if (editable) kaoqin_check.setTextColor(resources.getColor(color.text_333333)) else kaoqin_check.setTextColor(
            resources.getColor(color.text_999999)
        )
    }

    /**
     * 更新日期UI
     */
    fun updateView(view: TextView, checked: Boolean, editable: Boolean) {
        view.isEnabled = editable
        if (editable) {

            if (checked) {
                view.setTextColor(resources.getColor(color.text_333333))
            } else {
                view.setTextColor(resources.getColor(color.text_AAAAAA))
            }
            view.setBackgroundColor(resources.getColor(color.text_bg_F8F8F8))
        } else {
            if (checked) {
                view.visibility = View.VISIBLE
                view.setTextColor(resources.getColor(color.text_999999))
                view.setBackgroundColor(resources.getColor(color.transparent))
            } else {
                view.visibility = View.GONE
            }
        }

    }


    fun updateView(view: TextView, editable: Boolean) {
        view.isEnabled = editable
        if (editable) {
            view.setTextColor(resources.getColor(color.text_333333))
            view.setBackgroundColor(resources.getColor(color.text_bg_F8F8F8))

        } else {
            view.setTextColor(resources.getColor(color.text_999999))
            view.setBackgroundColor(resources.getColor(color.transparent))
        }
    }

    fun getDateDialog(): TimePickerView {
        if (pvTime == null) {
            pvTime = TimePickerBuilder(this, object : OnTimeSelectListener {
                override fun onTimeSelect(date: Date, v: View?) {//选中事件回调
                    recognitionMode.dateDes = timeFormat.format(date)
                    if (v != null && !"".equals(recognitionMode.dateDes)) {
                        (v as TextView).text = recognitionMode.dateDes

                    }
                }
            }, object : OnDialogDismissListener {
                override fun onDimiss() {
                    rightText?.isEnabled = true
                }
            })
                .setType(booleanArrayOf(false, false, false, true, true, false))// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setDecorView(this?.findViewById(id.kaoqinContainer) as ViewGroup)
                .setTitleText("")//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.parseColor("#21CBCB"))//确定按钮文字颜色
                .setCancelColor(Color.parseColor("#21CBCB"))//取消按钮文字颜色
                .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式
                .build()
        }
        return pvTime!!
    }

    fun getKaoqinRule() {
        DataManger.getInstance().getKaoqinInfo { response, status ->
            if (status == ConstantField.SUCCESS) {

                kaoqinBean = response
                if (kaoqinBean != null) {
                    initData(kaoqinBean!!)
                    isFirstEdit=false
                } else {
                    kaoqinBean = KaoqinRule(0, "", "", "", "", 0, true)
                }
                initView(editState)

            } else {
                if(kaoqinBean==null){
                    kaoqinBean = KaoqinRule(0, "", "", "", "", 0, true)
                }
                // TipsDialog.showCommonTipsDialog(response?.msg ?: "没有数据", this)
            }

        }
    }

    fun editKaoqinRule() {
        kaoqinBean?.work_time_am = am_time_from.text.toString() + "-" + am_time_to.text.toString()
        kaoqinBean?.work_time_pm = pm_time_from.text.toString() + "-" + pm_time_to.text.toString()

        var builder = StringBuilder()
        if (isSunChecked) builder.append("1") else builder.append("0")
        if (isSatChecked) builder.append("1") else builder.append("0")
        if (isFriChecked) builder.append("1") else builder.append("0")
        if (isThurChecked) builder.append("1") else builder.append("0")
        if (isWesChecked) builder.append("1") else builder.append("0")
        if (isTuesChecked) builder.append("1") else builder.append("0")
        if (isMonChecked) builder.append("1") else builder.append("0")
        kaoqinBean?.work_week = FormatUtils.byteToInt(builder.toString())
        Log.i(TAG, "editKaoqinRule work_week is" + kaoqinBean?.work_week)

        DataManger.getInstance().editKaoqinInfo(
            kaoqinBean?.work_time_am,
            kaoqinBean?.work_time_pm,
            kaoqinBean?.work_week,
            kaoqinBean?.enable

        ) { response, status ->
            if (status == ConstantField.SUCCESS) {
//                TipsDialog.showConfrimPrivacyDialog(resources.getString(string.sucuess), this)
            } else {
                TipsDialog.showCommonTipsDialog(response?.msg ?: "编辑失败", this)
            }
        }

    }

    /**
     * 第一次编辑的时候需要设置考勤规则
     */
    fun setKaoqinRule() {

        kaoqinBean?.work_time_am = am_time_from.text.toString() + "-" + am_time_to.text.toString()
        kaoqinBean?.work_time_pm = pm_time_from.text.toString() + "-" + pm_time_to.text.toString()

        var builder = StringBuilder()
        if (isSunChecked) builder.append("1") else builder.append("0")
        if (isSatChecked) builder.append("1") else builder.append("0")
        if (isFriChecked) builder.append("1") else builder.append("0")
        if (isThurChecked) builder.append("1") else builder.append("0")
        if (isWesChecked) builder.append("1") else builder.append("0")
        if (isTuesChecked) builder.append("1") else builder.append("0")
        if (isMonChecked) builder.append("1") else builder.append("0")
        kaoqinBean?.work_week = FormatUtils.byteToInt(builder.toString())
        DataManger.getInstance().setKaoqinInfo(
            kaoqinBean?.work_time_am,
            kaoqinBean?.work_time_pm,
            kaoqinBean?.work_week,
            kaoqinBean?.enable
        ) { response, status ->
            if (status == ConstantField.SUCCESS) {
                isFirstEdit=false
            } else {
                TipsDialog.showCommonTipsDialog(response.msg ?: "设置失败", this)
            }
        }
    }
}