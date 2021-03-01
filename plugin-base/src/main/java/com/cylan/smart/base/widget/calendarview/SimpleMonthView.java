package com.cylan.smart.base.widget.calendarview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.view.MotionEvent;
import android.view.View;
import com.cylan.smart.base.R;

import java.security.InvalidParameterException;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * 每个月作为一个ItemView
 */
class SimpleMonthView extends View {

    public static final String VIEW_PARAMS_SELECTED_BEGIN_DATE = "selected_begin_date";
    public static final String VIEW_PARAMS_SELECTED_LAST_DATE = "selected_last_date";

    public static final String VIEW_PARAMS_MONTH = "month";
    public static final String VIEW_PARAMS_YEAR = "year";

    private static final int SELECTED_CIRCLE_ALPHA = 128;
    protected static int DEFAULT_HEIGHT = 32;                           // 默认一行的高度
    protected static int DAY_SELECTED_RECT_SIZE;                        // 选中圆角矩形半径
    protected static int ROW_SEPARATOR = 12;                            // 每行中间的间距
    protected static int MINI_DAY_NUMBER_TEXT_SIZE;                     // 日期字体的最小尺寸
    protected static int EDGE_TEXT_SIZE;
    protected static int MIN_HEIGHT = 10;                               // 最小高度
    protected static int MONTH_HEADER_SIZE;                             // 头部的高度（包括年份月份，星期几）
    protected static int YEAR_MONTH_TEXT_SIZE;                         // 头部年份月份的字体大小

    protected int mPadding = 0;

    protected Paint mDayTextPaint;
    protected Paint mYearMonthPaint;// 头部的画笔

    protected Paint mSelectedDayBgPaint;
    protected Paint mSelectedDayHihtBgPaint;

    protected Paint mDayTextWhitePaint;
    protected Paint mTextPaint;
    protected Paint invalidatePaint;//不可用日期的字体


    private final StringBuilder mStringBuilder;

    protected boolean mHasToday = false;
    protected int mToday = -1;
    protected int mWeekStart = 1;               // 一周的第一天（不同国家的一星期的第一天不同）
    protected int mNumDays = 7;                 // 一行几列
    protected int mNumCells;                    // 一个月有多少天
    private int mDayOfWeekStart = 0;            // 日期对应星期几
    protected int mRowHeight = DEFAULT_HEIGHT;  // 行高
    protected int mWidth;                       // simpleMonthView的宽度

    protected int mYear;
    protected int mMonth;
    final Time today;

    private final Calendar mCalendar;
    private final Calendar mDayLabelCalendar;           // 用于显示星期几

    private int mNumRows;

    private DateFormatSymbols mDateFormatSymbols = new DateFormatSymbols();

    private OnDayClickListener mOnDayClickListener;

    SimpleMonthAdapter.CalendarDay mStartDate;          // 入住日期
    SimpleMonthAdapter.CalendarDay mEndDate;            // 退房日期

    SimpleMonthAdapter.CalendarDay cellCalendar;        // cell的对应的日期
    private boolean crossMouth = false;
    private int cellWidth = 0;

    /**
     * @param context
     * @param typedArray
     */
    public SimpleMonthView(Context context, TypedArray typedArray) {
        super(context);

        Resources resources = context.getResources();
        mDayLabelCalendar = Calendar.getInstance();
        mCalendar = Calendar.getInstance();
        today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        mStringBuilder = new StringBuilder(50);

        MINI_DAY_NUMBER_TEXT_SIZE = typedArray.getDimensionPixelSize(R.styleable.DayPickerView_textSizeDay, resources.getDimensionPixelSize(R.dimen.text_size_day));
        EDGE_TEXT_SIZE = typedArray.getDimensionPixelSize(R.styleable.DayPickerView_edgeTextSize, resources.getDimensionPixelSize(R.dimen.text_size_tag));
        YEAR_MONTH_TEXT_SIZE = typedArray.getDimensionPixelSize(R.styleable.DayPickerView_textSizeYearMonth, resources.getDimensionPixelSize(R.dimen.text_size_month));
        MONTH_HEADER_SIZE = typedArray.getDimensionPixelOffset(R.styleable.DayPickerView_headerMonthHeight, resources.getDimensionPixelOffset(R.dimen.header_month_height));
        DAY_SELECTED_RECT_SIZE = typedArray.getDimensionPixelSize(R.styleable.DayPickerView_selectedDayRadius, resources.getDimensionPixelOffset(R.dimen.selected_day_radius));

        mRowHeight = ((typedArray.getDimensionPixelSize(R.styleable.DayPickerView_calendarHeight,
                resources.getDimensionPixelOffset(R.dimen.calendar_height)) - MONTH_HEADER_SIZE - ROW_SEPARATOR) / 6);

        cellCalendar = new SimpleMonthAdapter.CalendarDay();

        initView();
    }

    /**
     * 计算每个月的日期占用的行数
     *
     * @return
     */
    private int calculateNumRows() {
        int offset = findDayOffset();
        int dividend = (offset + mNumCells) / mNumDays;
        int remainder = (offset + mNumCells) % mNumDays;
        return (dividend + (remainder > 0 ? 1 : 0));
    }

    /**
     * 绘制头部（年份月份，星期几）
     *
     * @param canvas
     */
    private void drawMonthTitle(Canvas canvas) {
        int x = (mWidth + 2 * mPadding) / 2;
        int y = (MONTH_HEADER_SIZE) / 2 + (YEAR_MONTH_TEXT_SIZE / 3);
        StringBuilder stringBuilder = new StringBuilder(getMonthAndYearString().toLowerCase());
        stringBuilder.setCharAt(0, Character.toUpperCase(stringBuilder.charAt(0)));
        canvas.drawText(stringBuilder.toString(), x, y, mYearMonthPaint);
    }

    /**
     * 每个月第一天是星期几
     *
     * @return
     */
    private int findDayOffset() {
        return (mDayOfWeekStart < mWeekStart ? (mDayOfWeekStart + mNumDays) : mDayOfWeekStart)
                - mWeekStart;
    }

    /**
     * 获取年份和月份
     *
     * @return
     */
    private String getMonthAndYearString() {
        int flags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_NO_MONTH_DAY;
        mStringBuilder.setLength(0);
        long millis = mCalendar.getTimeInMillis();
        return DateUtils.formatDateRange(getContext(), millis, millis, flags);
    }

    /**
     * 是否有起始位置
     *
     * @param calendarDay
     * @return
     */
    private boolean onDayClick(SimpleMonthAdapter.CalendarDay calendarDay) {
        if (mOnDayClickListener != null) {
            return mOnDayClickListener.onDayClick(this, calendarDay);
        }
        return false;
    }

    private boolean sameDay(int monthDay, Time time) {
        return (mYear == time.year) && (mMonth == time.month) && (monthDay == time.monthDay);
    }


    /**
     * 绘制所有的cell
     *
     * @param canvas
     */
    protected void drawMonthCell(Canvas canvas) {
        // ?
        int y = MONTH_HEADER_SIZE + ROW_SEPARATOR + mRowHeight / 2;
        int paddingDay = (mWidth - 2 * mPadding) / (2 * mNumDays);
        int dayOffset = findDayOffset();
        int day = 1;


        while (day <= mNumCells) {
            int x = paddingDay * (1 + dayOffset * 2) + mPadding;
            cellCalendar.setDay(mYear, mMonth, day);

            Paint datePaint = mDayTextPaint;

            Calendar instance = Calendar.getInstance();
            int mouth = instance.get(Calendar.MONTH);
            int nowDay = instance.get(Calendar.DAY_OF_MONTH);
            if (mMonth == mouth && day > nowDay) {
                datePaint = invalidatePaint;
            }

            if (shouldDrawBg(day)) {
                drawDayBg(canvas, x, y, mSelectedDayBgPaint);
            }

            if (mStartDate != null && mStartDate.day == day) {
                drawDayBg(canvas, x, y, mSelectedDayHihtBgPaint);
                canvas.drawText("起", x, getTextYCenter(mTextPaint, y + DAY_SELECTED_RECT_SIZE / 2), mTextPaint);
                datePaint = mDayTextWhitePaint;
            }

            if (mEndDate != null && mEndDate.day == day) {
                drawDayBg(canvas, x, y, mSelectedDayHihtBgPaint);
                canvas.drawText("止", x, getTextYCenter(mTextPaint, y + DAY_SELECTED_RECT_SIZE / 2), mTextPaint);
                datePaint = mDayTextWhitePaint;
            }

            canvas.drawText(String.format("%d", day), x, y, datePaint);

            dayOffset++;
            if (dayOffset == mNumDays) {
                dayOffset = 0;
                y += mRowHeight;
            }
            day++;
        }
    }

    private boolean shouldDrawBg(int day) {

        if (mStartDate != null && mEndDate != null && day > mStartDate.day && day < mEndDate.day) {
            return true;
        }

        if (crossMouth && mStartDate != null && day > mStartDate.day) {
            return true;
        }

        if (mStartDate == null && mEndDate != null && day < mEndDate.day) {
            return true;
        }


        return false;
    }


    /**
     * 根据坐标获取对应的日期
     *
     * @param x
     * @param y
     * @return
     */
    public SimpleMonthAdapter.CalendarDay getDayFromLocation(float x, float y) {
        int padding = mPadding;
        if ((x < padding) || (x > mWidth - mPadding)) {
            return null;
        }

        int yDay = (int) (y - MONTH_HEADER_SIZE) / mRowHeight;
        int day = 1 + ((int) ((x - padding) * mNumDays / (mWidth - padding - mPadding)) - findDayOffset()) + yDay * mNumDays;

        if (mMonth > 11 || mMonth < 0 || CalendarUtils.getDaysInMonth(mMonth, mYear) < day || day < 1)
            return null;

        SimpleMonthAdapter.CalendarDay calendar = new SimpleMonthAdapter.CalendarDay(mYear, mMonth, day);

        return calendar;
    }

    /**
     * 初始化一些paint
     */
    protected void initView() {
        // 头部年份和月份的字体paint
        mYearMonthPaint = new Paint();
        mYearMonthPaint.setFakeBoldText(true);
        mYearMonthPaint.setAntiAlias(true);
        mYearMonthPaint.setTextSize(YEAR_MONTH_TEXT_SIZE);
        mYearMonthPaint.setColor(Color.parseColor("#727272"));
        mYearMonthPaint.setTextAlign(Align.CENTER);
        mYearMonthPaint.setStyle(Style.FILL);


        // 被选中的日期背景paint
        mSelectedDayBgPaint = new Paint();
        mSelectedDayBgPaint.setFakeBoldText(true);
        mSelectedDayBgPaint.setAntiAlias(true);
        mSelectedDayBgPaint.setColor(Color.parseColor("#CDF3EF"));
        mSelectedDayBgPaint.setTextAlign(Align.CENTER);
        mSelectedDayBgPaint.setStyle(Style.FILL);
        mSelectedDayBgPaint.setAlpha(SELECTED_CIRCLE_ALPHA);

        //深色背景
        mSelectedDayHihtBgPaint = new Paint();
        mSelectedDayHihtBgPaint.setFakeBoldText(true);
        mSelectedDayHihtBgPaint.setAntiAlias(true);
        mSelectedDayHihtBgPaint.setColor(Color.parseColor("#06C1AE"));
        mSelectedDayHihtBgPaint.setTextAlign(Align.CENTER);
        mSelectedDayHihtBgPaint.setStyle(Style.FILL);
        mSelectedDayHihtBgPaint.setAlpha(SELECTED_CIRCLE_ALPHA);


        // 日期字体paint
        mDayTextPaint = new Paint();
        mDayTextPaint.setAntiAlias(true);
        mDayTextPaint.setColor(Color.parseColor("#727272"));
        mDayTextPaint.setTextSize(MINI_DAY_NUMBER_TEXT_SIZE);
        mDayTextPaint.setStyle(Style.FILL);
        mDayTextPaint.setTextAlign(Align.CENTER);
        mDayTextPaint.setFakeBoldText(false);

        invalidatePaint = new Paint();
        invalidatePaint.setAntiAlias(true);
        invalidatePaint.setColor(Color.parseColor("#33727272"));
        invalidatePaint.setTextSize(MINI_DAY_NUMBER_TEXT_SIZE);
        invalidatePaint.setStyle(Style.FILL);
        invalidatePaint.setTextAlign(Align.CENTER);
        invalidatePaint.setFakeBoldText(false);

        //白色日期
        mDayTextWhitePaint = new Paint();
        mDayTextWhitePaint.setAntiAlias(true);
        mDayTextWhitePaint.setColor(Color.WHITE);
        mDayTextWhitePaint.setTextSize(MINI_DAY_NUMBER_TEXT_SIZE);
        mDayTextWhitePaint.setStyle(Style.FILL);
        mDayTextWhitePaint.setTextAlign(Align.CENTER);
        mDayTextWhitePaint.setFakeBoldText(false);

        //白色文字
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(EDGE_TEXT_SIZE);
        mTextPaint.setStyle(Style.FILL);
        mTextPaint.setTextAlign(Align.CENTER);
        mTextPaint.setFakeBoldText(false);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawMonthTitle(canvas);
        drawMonthCell(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 设置simpleMonthView的宽度和高度
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), mRowHeight * mNumRows + MONTH_HEADER_SIZE + ROW_SEPARATOR);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        cellWidth = (mWidth - 2 * mPadding) / mNumDays;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            SimpleMonthAdapter.CalendarDay calendarDay = getDayFromLocation(event.getX(), event.getY());

            if (calendarDay == null) {
                return super.onTouchEvent(event);
            }

            Calendar instance = Calendar.getInstance();
            int mouth = instance.get(Calendar.MONTH);
            int nowDay = instance.get(Calendar.DAY_OF_MONTH);
            if (mMonth == mouth && calendarDay.day > nowDay) {
                return super.onTouchEvent(event);
            }

            boolean isHasStart = onDayClick(calendarDay);
            if (isHasStart) {
                mEndDate = calendarDay;
            } else {
                mStartDate = calendarDay;
            }
            invalidate();
        }
        return true;
    }

    public void clearState() {
        mStartDate = null;
        mEndDate = null;
        crossMouth = false;
        invalidate();
    }

    public void crossMouth(boolean crossMouth) {
        this.crossMouth = crossMouth;
        invalidate();
    }


    /**
     * 设置传递进来的参数
     *
     * @param params
     */
    @SuppressLint("WrongConstant")
    public void setMonthParams(HashMap<String, Object> params) {
        setTag(params);

        if (params.containsKey("crossMonth")) {
            crossMouth = (boolean) params.get("crossMonth");
        }

        if (params.containsKey(VIEW_PARAMS_SELECTED_BEGIN_DATE)) {
            mStartDate = (SimpleMonthAdapter.CalendarDay) params.get(VIEW_PARAMS_SELECTED_BEGIN_DATE);
        }
        if (params.containsKey(VIEW_PARAMS_SELECTED_LAST_DATE)) {
            mEndDate = (SimpleMonthAdapter.CalendarDay) params.get(VIEW_PARAMS_SELECTED_LAST_DATE);
        }

        mMonth = (int) params.get(VIEW_PARAMS_MONTH);
        mYear = (int) params.get(VIEW_PARAMS_YEAR);

        mCalendar.set(Calendar.MONTH, mMonth);
        mCalendar.set(Calendar.YEAR, mYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, 1);
        mDayOfWeekStart = mCalendar.get(Calendar.DAY_OF_WEEK);

        mWeekStart = mCalendar.getFirstDayOfWeek();

        mNumCells = CalendarUtils.getDaysInMonth(mMonth, mYear);
        for (int i = 0; i < mNumCells; i++) {
            final int day = i + 1;
            if (sameDay(day, today)) {
                mHasToday = true;
                mToday = day;
            }
        }

        mNumRows = calculateNumRows();
    }

    public void setOnDayClickListener(OnDayClickListener onDayClickListener) {
        mOnDayClickListener = onDayClickListener;
    }

    public interface OnDayClickListener {
        /**
         * @param simpleMonthView
         * @param calendarDay
         * @return　是否记录了起始位置
         */
        boolean onDayClick(SimpleMonthView simpleMonthView, SimpleMonthAdapter.CalendarDay calendarDay);
    }

    /**
     * 绘制cell
     *
     * @param canvas
     * @param x
     * @param y
     */
    private void drawDayBg(Canvas canvas, int x, int y, Paint paint) {
//        RectF rectF = new RectF(x - DAY_SELECTED_RECT_SIZE, y - DAY_SELECTED_RECT_SIZE,
//                x + DAY_SELECTED_RECT_SIZE, y + DAY_SELECTED_RECT_SIZE);
        RectF rectF = new RectF(x - cellWidth / 2, y - mRowHeight / 2,
                x + cellWidth / 2, y + mRowHeight / 2);
        canvas.drawRoundRect(rectF, 0.0f, 0.0f, paint);
    }

    /**
     * 在使用drawText方法时文字不能根据y坐标居中，所以重新计算y坐标
     *
     * @param paint
     * @param y
     * @return
     */
    private float getTextYCenter(Paint paint, int y) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float fontTotalHeight = fontMetrics.bottom - fontMetrics.top;
        float offY = fontTotalHeight / 2 - fontMetrics.bottom;
        return y + offY;
    }
}