package com.cylan.smart.plugin.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;

import java.text.SimpleDateFormat;

/**
 * @Author Lupy create on 18-11-13
 * @Description
 */
public class DogLineChart extends LineChart {
    private LineMarkerView lineMarkerView;

    public DogLineChart(Context context) {
        this(context, null);
    }

    public DogLineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setLineMarkerView(LineMarkerView lineMarkerView) {
        this.lineMarkerView = lineMarkerView;
    }


    @Override
    protected void init() {
        super.init();
        mAxisRendererLeft = new DogYAxisRenderer(mViewPortHandler, mAxisLeft, mLeftAxisTransformer);
    }

    /**
     * 设置格式化公式
     *
     * @param simpleDateFormat
     */
    public void setSimpleDateFormat(SimpleDateFormat simpleDateFormat) {
        if (lineMarkerView != null) {
            lineMarkerView.setSimpleDateFormat(simpleDateFormat);
        }
    }

    @Override
    protected void drawMarkers(Canvas canvas) {
        if (lineMarkerView == null || !isDrawMarkersEnabled() || !valuesToHighlight()) {
            return;
        }


        for (int i = 0; i < mIndicesToHighlight.length; i++) {
            Highlight highlight = mIndicesToHighlight[i];
            IDataSet set = mData.getDataSetByIndex(highlight.getDataSetIndex());
            Entry e = mData.getEntryForHighlight(mIndicesToHighlight[i]);
            int entryIndex = set.getEntryIndex(e);
            if (e == null || entryIndex > set.getEntryCount() * mAnimator.getPhaseX()) {
                continue;
            }
            float[] pos = getMarkerPosition(highlight);
            if (!mViewPortHandler.isInBounds(pos[0], pos[1])) {
                continue;
            }
            lineMarkerView.refreshContent(e, highlight);
            float markerx = pos[0];
            if (markerx - lineMarkerView.getWidth()/2 < 0) {
                markerx =  lineMarkerView.getWidth()/2;
            } else if (markerx > (mViewPortHandler.contentRight() - lineMarkerView.getWidth()/2)) {
                //marker绘制的时候会自动向左平移半个宽度
                markerx = mViewPortHandler.contentRight() - lineMarkerView.getWidth()/2;
            }
            lineMarkerView.draw(canvas, markerx,
                    mViewPortHandler.contentTop() - lineMarkerView.getHeight());

        }
    }

}
