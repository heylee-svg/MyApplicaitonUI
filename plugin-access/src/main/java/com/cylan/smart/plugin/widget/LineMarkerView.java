package com.cylan.smart.plugin.widget;

import android.content.Context;
import android.widget.TextView;
import com.cylan.smart.plugin.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.SimpleDateFormat;

/**
 * @Author Lupy create on 18-11-13
 * @Description
 */
public class LineMarkerView extends MarkerView {

    private TextView tvMarkerText;
    private SimpleDateFormat simpleDateFormat;

    public LineMarkerView(Context context) {
        super(context, R.layout.home_linechart_marker);
        tvMarkerText = findViewById(R.id.tv_marker_text);
    }

    public void setSimpleDateFormat(SimpleDateFormat simpleDateFormat) {
        this.simpleDateFormat = simpleDateFormat;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        if (e.getData() == null) {
            tvMarkerText.setVisibility(GONE);
        } else {
            if (simpleDateFormat == null) {
                if (e.getData() instanceof String) {
                    tvMarkerText.setText(e.getData() + " " + String.valueOf(Math.round(e.getY())));
                } else {
                    tvMarkerText.setText(String.valueOf(Math.round(e.getY())));
                }
            } else {
                long timeSenc = Long.valueOf(e.getData().toString());
                tvMarkerText.setText(simpleDateFormat.format(timeSenc * 1000) + " " + Math.round(e.getY()));
            }
        }
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }

}
