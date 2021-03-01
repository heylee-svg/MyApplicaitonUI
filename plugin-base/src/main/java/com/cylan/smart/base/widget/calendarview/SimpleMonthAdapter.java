package com.cylan.smart.base.widget.calendarview;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import com.cylan.smart.base.R;
import com.cylan.smart.base.utils.ToastUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SimpleMonthAdapter extends RecyclerView.Adapter<SimpleMonthAdapter.ViewHolder> implements SimpleMonthView.OnDayClickListener {
    protected static final int MONTHS_IN_YEAR = 12;
    private final TypedArray typedArray;
    private final Context mContext;

    private int monthStart;
    private int yearStart;
    private int mouthNumber;
    private CalendarDay seleectStartDay;
    private CalendarDay seleectEndDay;
    private SimpleMonthView selectStartMonthView;
    private SimpleMonthView selectEndMonthView;

    public SimpleMonthAdapter(Context context, TypedArray typedArray) {
        mContext = context;
        this.typedArray = typedArray;
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Calendar calendar = Calendar.getInstance();
        int nowYear = calendar.get(Calendar.YEAR);
        int nowMouth = calendar.get(Calendar.MONTH);
        int nowDay = calendar.get(Calendar.DAY_OF_MONTH);
        seleectEndDay = new CalendarDay(nowYear, nowMouth, nowDay);

        calendar.add(Calendar.MONTH, -12);
        yearStart = calendar.get(Calendar.YEAR);
        monthStart = calendar.get(Calendar.MONTH) + 1;
        mouthNumber = typedArray.getInteger(R.styleable.DayPickerView_monthNumber, 12);

        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DAY_OF_MONTH, -1);
        int yesterdayYear = ca.get(Calendar.YEAR);
        int yesterdayMouth = ca.get(Calendar.MONTH);
        int yesterdayDay = ca.get(Calendar.DAY_OF_MONTH);
        seleectStartDay = new CalendarDay(yesterdayYear, yesterdayMouth, yesterdayDay);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final SimpleMonthView simpleMonthView = new SimpleMonthView(mContext, typedArray);
        return new ViewHolder(simpleMonthView, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final SimpleMonthView v = viewHolder.simpleMonthView;
        final HashMap<String, Object> drawingParams = new HashMap<String, Object>();
        int month = (monthStart + (position % MONTHS_IN_YEAR)) % MONTHS_IN_YEAR;
        int year = position / MONTHS_IN_YEAR + yearStart + ((monthStart + (position % MONTHS_IN_YEAR)) / MONTHS_IN_YEAR);
        if (seleectStartDay != null) {
            if (seleectStartDay.month == month && seleectStartDay.year == year) {
                drawingParams.put(SimpleMonthView.VIEW_PARAMS_SELECTED_BEGIN_DATE, seleectStartDay);
                if (seleectEndDay != null && seleectEndDay.month != seleectStartDay.month) {
                    drawingParams.put("crossMonth", true);
                } else {
                    drawingParams.put("crossMonth", false);
                }
                selectStartMonthView = v;
            } else {
                drawingParams.put(SimpleMonthView.VIEW_PARAMS_SELECTED_BEGIN_DATE, null);
                drawingParams.put("crossMonth", false);
            }
        } else {
            drawingParams.put(SimpleMonthView.VIEW_PARAMS_SELECTED_BEGIN_DATE, null);
            drawingParams.put("crossMonth", false);
        }

        if (seleectEndDay != null) {
            if (seleectEndDay.month == month && seleectEndDay.year == year) {
                drawingParams.put(SimpleMonthView.VIEW_PARAMS_SELECTED_LAST_DATE, seleectEndDay);
                selectEndMonthView = v;
            } else {
                drawingParams.put(SimpleMonthView.VIEW_PARAMS_SELECTED_LAST_DATE, null);
            }
        } else {
            drawingParams.put(SimpleMonthView.VIEW_PARAMS_SELECTED_LAST_DATE, null);
        }

        drawingParams.put(SimpleMonthView.VIEW_PARAMS_YEAR, year);
        drawingParams.put(SimpleMonthView.VIEW_PARAMS_MONTH, month);
        v.setMonthParams(drawingParams);
        v.invalidate();
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mouthNumber;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final SimpleMonthView simpleMonthView;

        public ViewHolder(View itemView, SimpleMonthView.OnDayClickListener onDayClickListener) {
            super(itemView);
            simpleMonthView = (SimpleMonthView) itemView;
            simpleMonthView.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            simpleMonthView.setClickable(true);
            simpleMonthView.setOnDayClickListener(onDayClickListener);
        }
    }

    @Override
    public boolean onDayClick(SimpleMonthView simpleMonthView, CalendarDay calendarDay) {
        return onDayTapped(simpleMonthView, calendarDay);

    }


    /**
     * 点时间
     *
     * @param calendarDay
     */
    protected boolean onDayTapped(SimpleMonthView simpleMonthView, CalendarDay calendarDay) {
        if (seleectStartDay == null) {
            seleectStartDay = calendarDay;
            selectStartMonthView = simpleMonthView;
            selectStartMonthView.crossMouth(false);
            return false;
        } else {
            //已经有一个区间的范围,重新开始选择
            if (seleectEndDay != null) {
                if (selectStartMonthView != null) {
                    selectStartMonthView.clearState();
                }
                if (selectEndMonthView != null) {
                    selectEndMonthView.clearState();
                }
                seleectEndDay = null;
                seleectStartDay = calendarDay;
                selectStartMonthView = simpleMonthView;
                return false;
            }

            //结束日期在选择日期之前
            if (calendarDay.before(seleectStartDay)) {
                selectStartMonthView.clearState();
                seleectStartDay = calendarDay;
                selectStartMonthView = simpleMonthView;
                seleectEndDay = null;
                return false;
            }

            //相差大于３０天
            if (dateDiff(seleectStartDay, calendarDay) > 30) {
                selectStartMonthView.clearState();
                seleectStartDay = calendarDay;
                selectStartMonthView = simpleMonthView;
                seleectEndDay = null;
                ToastUtils.INSTANCE.showToast(mContext, "日期跨度不能超过30天");
                return false;
            }

            if (selectStartMonthView != simpleMonthView) {
                //选择的日期不在同一个月之内，通知起始月份更新视图
                selectStartMonthView.crossMouth(true);
            }

            //两次选择的日期相同
            if (seleectStartDay.equals(calendarDay)) {
                seleectStartDay = calendarDay;
                selectStartMonthView = simpleMonthView;
                seleectEndDay = null;
                return false;
            }


            seleectEndDay = calendarDay;
            selectEndMonthView = simpleMonthView;
            return true;
        }


    }


    /**
     * 两个日期中间隔多少天
     *
     * @param first
     * @param last
     * @return
     */
    protected int dateDiff(CalendarDay first, CalendarDay last) {
        long dayDiff = Math.abs(last.getDate().getTime() - first.getDate().getTime()) / (1000 * 3600 * 24);
        return Integer.valueOf(String.valueOf(dayDiff)) + 1;
    }


    public static class CalendarDay implements Serializable, Comparable<CalendarDay> {
        private static final long serialVersionUID = -5456695978688356202L;
        private Calendar calendar;

        public int day;
        public int month;
        public int year;
        public String tag;

        public CalendarDay(Calendar calendar, String tag) {
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            this.tag = tag;
        }

        public CalendarDay() {
            setTime(System.currentTimeMillis());
        }

        public CalendarDay(int year, int month, int day) {
            setDay(year, month, day);
        }

        public CalendarDay(long timeInMillis) {
            setTime(timeInMillis);
        }

        public CalendarDay(Calendar calendar) {
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }

        private void setTime(long timeInMillis) {
            if (calendar == null) {
                calendar = Calendar.getInstance();
            }
            calendar.setTimeInMillis(timeInMillis);
            month = this.calendar.get(Calendar.MONTH);
            year = this.calendar.get(Calendar.YEAR);
            day = this.calendar.get(Calendar.DAY_OF_MONTH);
        }

        public void set(CalendarDay calendarDay) {
            year = calendarDay.year;
            month = calendarDay.month;
            day = calendarDay.day;
        }

        public void setDay(int year, int month, int day) {
            this.year = year;
            this.month = month;
            this.day = day;
        }

        public Date getDate() {
            if (calendar == null) {
                calendar = Calendar.getInstance();
            }
            calendar.clear();
            calendar.set(year, month, day);
            return calendar.getTime();
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        @Override
        public String toString() {
            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("{ year: ");
            stringBuilder.append(year);
            stringBuilder.append(", month: ");
            stringBuilder.append(month);
            stringBuilder.append(", day: ");
            stringBuilder.append(day);
            stringBuilder.append(" }");

            return stringBuilder.toString();
        }

        /**
         * 只比较年月日
         *
         * @param calendarDay
         * @return
         */
        @Override
        public int compareTo(CalendarDay calendarDay) {
//            return getDate().compareTo(calendarDay.getDate());
            if (calendarDay == null) {
                throw new IllegalArgumentException("被比较的日期不能是null");
            }

            if (year == calendarDay.year && month == calendarDay.month && day == calendarDay.day) {
                return 0;
            }

            if (year < calendarDay.year ||
                    (year == calendarDay.year && month < calendarDay.month) ||
                    (year == calendarDay.year && month == calendarDay.month && day < calendarDay.day)) {
                return -1;
            }
            return 1;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof CalendarDay) {
                CalendarDay calendarDay = (CalendarDay) o;
                if (compareTo(calendarDay) == 0) {
                    return true;
                }
            }
            return false;
        }

        /**
         * 大于比较的日期（只比较年月日）
         *
         * @param o
         * @return
         */
        public boolean after(Object o) {
            if (o instanceof CalendarDay) {
                CalendarDay calendarDay = (CalendarDay) o;
                if (compareTo(calendarDay) == 1) {
                    return true;
                }
            }
            return false;
        }

        /**
         * 小于比较的日期（只比较年月日）
         *
         * @param o
         * @return
         */
        public boolean before(Object o) {
            if (o instanceof CalendarDay) {
                CalendarDay calendarDay = (CalendarDay) o;
                if (compareTo(calendarDay) == -1) {
                    return true;
                }
            }
            return false;
        }
    }

    public static class SelectedDays<K> implements Serializable {
        private static final long serialVersionUID = 3942549765282708376L;
        private K first;
        private K last;

        public SelectedDays() {
        }

        public SelectedDays(K first, K last) {
            this.first = first;
            this.last = last;
        }

        public K getFirst() {
            return first;
        }

        public void setFirst(K first) {
            this.first = first;
        }

        public K getLast() {
            return last;
        }

        public void setLast(K last) {
            this.last = last;
        }
    }

    public CalendarDay getSeleectStartDay() {
        return seleectStartDay;
    }

    public CalendarDay getSeleectEndDay() {
        return seleectEndDay;
    }
}