package com.cylan.smart.base.widget.calendarview;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import com.cylan.smart.base.R;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DayPickerView extends RecyclerView {
    protected Context mContext;
    protected SimpleMonthAdapter mAdapter;
    private TypedArray typedArray;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public DayPickerView(Context context) {
        this(context, null);
    }

    public DayPickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DayPickerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.DayPickerView);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        init(context);
    }

    public void init(Context paramContext) {
        setLayoutManager(new LinearLayoutManager(paramContext));
        mContext = paramContext;
        setUpListView();
        setUpAdapter();
    }

    protected void setUpAdapter() {
        if (mAdapter == null) {
            mAdapter = new SimpleMonthAdapter(getContext(), typedArray);
            setAdapter(mAdapter);
        }
        mAdapter.notifyDataSetChanged();
    }

    protected void setUpListView() {
        setVerticalScrollBarEnabled(false);
        setFadingEdgeLength(0);
    }


    /**
     * 开始时间
     *
     * @return
     */
    public String getSelectStartDay() {
        SimpleMonthAdapter.CalendarDay seleectStartDay = mAdapter.getSeleectStartDay();
        if (seleectStartDay != null) {
            return sdf.format(seleectStartDay.getDate());
        }
        return "";

    }

    /**
     * 获取终止时间
     *
     * @return
     */
    public String getSelectEndDay() {
        SimpleMonthAdapter.CalendarDay endDay = mAdapter.getSeleectEndDay();
        if (endDay != null) {
            return sdf.format(endDay.getDate());
        }
        return null;
    }
}