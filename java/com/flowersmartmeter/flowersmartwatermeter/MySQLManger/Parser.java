package com.flowersmartmeter.flowersmartwatermeter.MySQLManger;

import android.content.Context;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.model.GradientColor;
import com.github.mikephil.charting.utils.MPPointF;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Parser extends AsyncTask<Void,Void,Integer> {

    Context c;
    String data,col;
    ArrayList<String> names=new ArrayList<>();
    ArrayList<String> r_meter=new ArrayList<>();
    Spinner sp;
    LineChart lineChart;
    String[] res = new String[]{""};
    int i;

    public Parser(Context c, String data,Spinner sp,String col,int i) {
        this.c = c;
        this.data = data;
        this.sp=sp;
        this.col=col;
        this.i = i;

    }

    public Parser(Context c, String data, LineChart lineChart, String col, int i) {
        this.c = c;
        this.data=data;
        this.lineChart = lineChart;
        this.col = col;
        this.i = i;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        return this.parse();
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);

        if(integer==1)
        {
            if(i==0) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(c, android.R.layout.simple_list_item_1, names);
                sp.setAdapter(adapter);
            }else if(i==1)
            {
                ArrayList<Entry> barEntries = new ArrayList<Entry>() {
                };
                String[] jj = r_meter.toArray(new String[0]);

                int n =0;
                Float m =0f;
                float[] num =new float[jj.length];
                float[] num1 =new float[jj.length];

                for(int i=0 ;i!=jj.length;i++) {

                               num[i]= Float.valueOf(jj[i]);

                               if (num[i]!=num[0])
                               {
                                   num1[i]=num[i]-num[i-1];
                                   m = num1[i];
                               }else
                               {
                                   num1[i]=num[i];
                                   m=num1[i];


                               }


                            n++;
                            barEntries.add(new BarEntry(n, m));

                }
                ILineDataSet barDataSet = new ILineDataSet() {
                    @Override
                    public float getYMin() {
                        return 0;
                    }

                    @Override
                    public float getYMax() {
                        return 0;
                    }

                    @Override
                    public float getXMin() {
                        return 0;
                    }

                    @Override
                    public float getXMax() {
                        return 0;
                    }

                    @Override
                    public int getEntryCount() {
                        return 0;
                    }

                    @Override
                    public void calcMinMax() {

                    }

                    @Override
                    public void calcMinMaxY(float fromX, float toX) {

                    }

                    @Override
                    public Entry getEntryForXValue(float xValue, float closestToY, DataSet.Rounding rounding) {
                        return null;
                    }

                    @Override
                    public Entry getEntryForXValue(float xValue, float closestToY) {
                        return null;
                    }

                    @Override
                    public List<Entry> getEntriesForXValue(float xValue) {
                        return null;
                    }

                    @Override
                    public Entry getEntryForIndex(int index) {
                        return null;
                    }

                    @Override
                    public int getEntryIndex(float xValue, float closestToY, DataSet.Rounding rounding) {
                        return 0;
                    }

                    @Override
                    public int getEntryIndex(Entry e) {
                        return 0;
                    }

                    @Override
                    public int getIndexInEntries(int xIndex) {
                        return 0;
                    }

                    @Override
                    public boolean addEntry(Entry e) {
                        return false;
                    }

                    @Override
                    public void addEntryOrdered(Entry e) {

                    }

                    @Override
                    public boolean removeFirst() {
                        return false;
                    }

                    @Override
                    public boolean removeLast() {
                        return false;
                    }

                    @Override
                    public boolean removeEntry(Entry e) {
                        return false;
                    }

                    @Override
                    public boolean removeEntryByXValue(float xValue) {
                        return false;
                    }

                    @Override
                    public boolean removeEntry(int index) {
                        return false;
                    }

                    @Override
                    public boolean contains(Entry entry) {
                        return false;
                    }

                    @Override
                    public void clear() {

                    }

                    @Override
                    public String getLabel() {
                        return null;
                    }

                    @Override
                    public void setLabel(String label) {

                    }

                    @Override
                    public YAxis.AxisDependency getAxisDependency() {
                        return null;
                    }

                    @Override
                    public void setAxisDependency(YAxis.AxisDependency dependency) {

                    }

                    @Override
                    public List<Integer> getColors() {
                        return null;
                    }

                    @Override
                    public int getColor() {
                        return 0;
                    }

                    @Override
                    public GradientColor getGradientColor() {
                        return null;
                    }

                    @Override
                    public List<GradientColor> getGradientColors() {
                        return null;
                    }

                    @Override
                    public GradientColor getGradientColor(int index) {
                        return null;
                    }

                    @Override
                    public int getColor(int index) {
                        return 0;
                    }

                    @Override
                    public boolean isHighlightEnabled() {
                        return false;
                    }

                    @Override
                    public void setHighlightEnabled(boolean enabled) {

                    }

                    @Override
                    public void setValueFormatter(ValueFormatter f) {

                    }

                    @Override
                    public ValueFormatter getValueFormatter() {
                        return null;
                    }

                    @Override
                    public boolean needsFormatter() {
                        return false;
                    }

                    @Override
                    public void setValueTextColor(int color) {

                    }

                    @Override
                    public void setValueTextColors(List<Integer> colors) {

                    }

                    @Override
                    public void setValueTypeface(Typeface tf) {

                    }

                    @Override
                    public void setValueTextSize(float size) {

                    }

                    @Override
                    public int getValueTextColor() {
                        return 0;
                    }

                    @Override
                    public int getValueTextColor(int index) {
                        return 0;
                    }

                    @Override
                    public Typeface getValueTypeface() {
                        return null;
                    }

                    @Override
                    public float getValueTextSize() {
                        return 0;
                    }

                    @Override
                    public Legend.LegendForm getForm() {
                        return null;
                    }

                    @Override
                    public float getFormSize() {
                        return 0;
                    }

                    @Override
                    public float getFormLineWidth() {
                        return 0;
                    }

                    @Override
                    public DashPathEffect getFormLineDashEffect() {
                        return null;
                    }

                    @Override
                    public void setDrawValues(boolean enabled) {

                    }

                    @Override
                    public boolean isDrawValuesEnabled() {
                        return false;
                    }

                    @Override
                    public void setDrawIcons(boolean enabled) {

                    }

                    @Override
                    public boolean isDrawIconsEnabled() {
                        return false;
                    }

                    @Override
                    public void setIconsOffset(MPPointF offset) {

                    }

                    @Override
                    public MPPointF getIconsOffset() {
                        return null;
                    }

                    @Override
                    public void setVisible(boolean visible) {

                    }

                    @Override
                    public boolean isVisible() {
                        return false;
                    }

                    @Override
                    public int getHighLightColor() {
                        return 0;
                    }

                    @Override
                    public boolean isVerticalHighlightIndicatorEnabled() {
                        return false;
                    }

                    @Override
                    public boolean isHorizontalHighlightIndicatorEnabled() {
                        return false;
                    }

                    @Override
                    public float getHighlightLineWidth() {
                        return 0;
                    }

                    @Override
                    public DashPathEffect getDashPathEffectHighlight() {
                        return null;
                    }

                    @Override
                    public int getFillColor() {
                        return 0;
                    }

                    @Override
                    public Drawable getFillDrawable() {
                        return null;
                    }

                    @Override
                    public int getFillAlpha() {
                        return 0;
                    }

                    @Override
                    public float getLineWidth() {
                        return 0;
                    }

                    @Override
                    public boolean isDrawFilledEnabled() {
                        return false;
                    }

                    @Override
                    public void setDrawFilled(boolean enabled) {

                    }

                    @Override
                    public LineDataSet.Mode getMode() {
                        return null;
                    }

                    @Override
                    public float getCubicIntensity() {
                        return 0;
                    }

                    @Override
                    public boolean isDrawCubicEnabled() {
                        return false;
                    }

                    @Override
                    public boolean isDrawSteppedEnabled() {
                        return false;
                    }

                    @Override
                    public float getCircleRadius() {
                        return 0;
                    }

                    @Override
                    public float getCircleHoleRadius() {
                        return 0;
                    }

                    @Override
                    public int getCircleColor(int index) {
                        return 0;
                    }

                    @Override
                    public int getCircleColorCount() {
                        return 0;
                    }

                    @Override
                    public boolean isDrawCirclesEnabled() {
                        return false;
                    }

                    @Override
                    public int getCircleHoleColor() {
                        return 0;
                    }

                    @Override
                    public boolean isDrawCircleHoleEnabled() {
                        return false;
                    }

                    @Override
                    public DashPathEffect getDashPathEffect() {
                        return null;
                    }

                    @Override
                    public boolean isDashedLineEnabled() {
                        return false;
                    }

                    @Override
                    public IFillFormatter getFillFormatter() {
                        return null;
                    }
                };
                LineData theData = new LineData(barDataSet);
                lineChart.setData(theData);
                lineChart.setTouchEnabled(true);
                lineChart.setDragEnabled(true);
                lineChart.setScaleEnabled(true);
                Legend legend;
                legend = lineChart.getLegend();
                legend.setForm(Legend.LegendForm.CIRCLE);
              //  legend.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
                lineChart.getAxisRight().setDrawLabels(false);
              final String[] j = names.toArray(new String[0]);//Set date in x Axis by array j
                XAxis xAxis = lineChart.getXAxis();
                xAxis.setCenterAxisLabels(true);
                lineChart.getLegend().setWordWrapEnabled(true);
                lineChart.getDescription().setEnabled(false);
                lineChart.getDescription().setTextSize(10f);
                lineChart.setExtraTopOffset(10f);
                lineChart.getXAxis().setGranularity(1f);
                lineChart.setExtraBottomOffset(50f);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setLabelRotationAngle(272f);
                xAxis.setCenterAxisLabels(true);
                xAxis.setGranularity(1.1f);
                xAxis.setAxisMaximum(8f);
                xAxis.setAxisMaxValue(8f);
               // lineChart.setFitBars(true);
                lineChart.notifyDataSetChanged();
                lineChart.invalidate();
                xAxis.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        return j[(int)value % j.length];

                    }
                });
            }


            Toast.makeText(c,"Success",Toast.LENGTH_SHORT).show();

        }else {
            Toast.makeText(c,"No data available",Toast.LENGTH_SHORT).show();
        }
    }
    private int parse()
    {
        try
        {
            JSONArray ja=new JSONArray(data);
            JSONObject jo=null;

            for (int i=0;i<ja.length();i++)
            {
                jo=ja.getJSONObject(i);
                if (jo.has(col)) {
                    String name = jo.getString(col);
                    names.add(name);
                }
               if( jo.has("r_meter")){
                String jj = jo.getString("r_meter");
                r_meter.add(jj);
            }



            }

            return 1;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }
}