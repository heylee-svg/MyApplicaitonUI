package com.cylan.smart.plugin.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import com.cylan.smart.plugin.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.utils.MPPointF;

/**
 * @Author Lupy create on 18-11-22
 * @Description
 */
public class DogBarChart extends BarChart {
    String unitPerpeo = "";
    String unitYears = "";

    public DogBarChart(Context context) {
        super(context);
        unitPerpeo = "(" + context.getString(R.string.HOME_UNIT_PEOPLE) + ")";
        unitYears = "(" + context.getString(R.string.HOME_UNIT_YEARS) + ")";
    }

    public DogBarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        unitPerpeo = "(" + context.getString(R.string.HOME_UNIT_PEOPLE) + ")";
        unitYears = "(" + context.getString(R.string.HOME_UNIT_YEARS) + ")";
    }

    @Override
    protected void init() {
        super.init();
        mAxisRendererLeft = new DogYAxisRenderer(mViewPortHandler, mAxisLeft, mLeftAxisTransformer);
    }

    @Override
    protected void drawDescription(Canvas c) {
        // check if description should be drawn
        if (mDescription != null && mDescription.isEnabled()) {

            MPPointF position = mDescription.getPosition();

            mDescPaint.setTypeface(mDescription.getTypeface());
            mDescPaint.setTextSize(mDescription.getTextSize());
            mDescPaint.setColor(mDescription.getTextColor());
            mDescPaint.setTextAlign(mDescription.getTextAlign());

            float x, y;

            // if no position specified, draw on default position
            if (position == null) {
                x = getWidth() - mDescription.getXOffset();
                y = getHeight() - mViewPortHandler.offsetBottom() - mDescription.getYOffset();
            } else {
                x = position.x;
                y = position.y;
            }
            c.drawText(unitYears, x, y, mDescPaint);
            float text = mDescPaint.measureText(unitPerpeo);
            c.drawText(unitPerpeo, x, mViewPortHandler.contentTop(), mDescPaint);
        }


    }
}
