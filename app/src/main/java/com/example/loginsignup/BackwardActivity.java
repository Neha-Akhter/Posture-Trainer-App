package com.example.loginsignup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.core.content.ContextCompat;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

// FORWARD PLOT X
public class BackwardActivity extends AppCompatActivity {
    private static final String LIST_KEY1="list_key1";
    LineChart mChart;
    LineDataSet set1;
    LineDataSet set2;
    int size;
    int labels;
    int sectimeCount=1;
    int mintimeCount=1;
    int hourtimeCount=1;
    int colorArrayForGraph[]= {R.color.design_default_color_secondary_variant};
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xvalue);
        ActionBar bar = getSupportActionBar();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mChart = findViewById(R.id.chart); // object initialization by finding view by id

        text= findViewById(R.id.textView15);

        SharedPreferences sh = getSharedPreferences("MySharedPrefForList1", MODE_PRIVATE);
        String jsonString = sh.getString(LIST_KEY1, "");
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {
        }.getType();
        List<String> Forwardplotx = gson.fromJson(jsonString, type);
        size = Forwardplotx.size();
        System.out.println(Forwardplotx);

        int[] yaxisAngleData = new int[size];
        String[] axisData = new String[size];
        System.out.println(size);
        labels=(size/60);
        System.out.println(yaxisAngleData);
        System.out.println(labels);
        ArrayList arraytocheckdata=new ArrayList();
        arraytocheckdata.add("");

        text.setText("Total Duration is  " + labels + " minute");

        for (int i = 0; i < Forwardplotx.size(); i++) {
            yaxisAngleData[i] = Integer.valueOf(Forwardplotx.get(i));
        }


        ArrayList<Entry> yVals = new ArrayList<>();
        ArrayList<Entry> redline=new ArrayList<>();
        for (int i=0;i<=yaxisAngleData.length-1;i++){
            redline.add(new Entry(i, 25f));
        }


        for (int i = 0; i <= yaxisAngleData.length - 1; i++) {
            if (sectimeCount <= 59) {
                axisData[i] = (sectimeCount+" sec");
                sectimeCount += 1;
            } else if(sectimeCount==60 && mintimeCount<=59) {
                axisData[i] = (mintimeCount+" min");
                mintimeCount+=1;
                sectimeCount = 1;
            }
            else{
                axisData[i] = (hourtimeCount+" hour");
                hourtimeCount+=1;
                mintimeCount=1;
                sectimeCount=1;
            }
            yVals.add(new Entry(i, yaxisAngleData[i]));

        }
        // to check what values are in my axisData

        System.out.println(yVals);
        if (size > 0){
            plotGraphX(yVals,redline,arraytocheckdata);
        }
        else{
            mChart.setNoDataText(" No statistics");
        }
    }
    public void plotGraphX(ArrayList<Entry> yVals, ArrayList<Entry> redline, ArrayList arraytocheckdata){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                set1 = new LineDataSet(yVals, "Angles");
                set1.setLineWidth(2f);
                for (int i=0;i<yVals.size();i++){
                    if (yVals.get(i).getX()<25){
                        set1.setColor(Color.GREEN);
                    }
                    else{
                        set1.setCircleColor(Color.parseColor("#2B304C"));
                    }
                }

                //set1.setColor(Color.GREEN);
                set1.setDrawCircles(false);
                set1.setDrawCircleHole(false);
                set1.setCircleColor(Color.parseColor("#2B304C"));
                set1.setCircleRadius(1f);
                set1.setValueTextSize(5f);
                set1.setDrawIcons(false);
                //set1.enableDashedLine(10f, 5f, 0f);
                //set1.enableDashedHighlightLine(10f, 5f, 0f);
                set1.setDrawValues(false);
                set1.setColors(colorArrayForGraph,getApplicationContext());

                set2=new LineDataSet(redline,"Correct Posture Range");
                set2.setDrawCircles(false);
                set2.setDrawCircleHole(false);
                set2.setCircleColor(Color.RED);
                set2.setCircleRadius(1f);
                set2.setValueTextSize(5f);
                set2.setDrawIcons(false);
                set2.setDrawValues(false);
                set2.setColors(Color.RED);
                //set2.setColors(colorArrayForGraph,getApplicationContext());
                set2.setLineWidth(1f);


                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(set1);
                dataSets.add(set2);
                LineData data = new LineData(dataSets);
                mChart.setTouchEnabled(true);
                mChart.setPinchZoom(true);
                mChart.setBackgroundColor(Color.WHITE);
                mChart.setData(data);
                mChart.setNoDataText("No data to show on graph");
                mChart.setDrawGridBackground(true);
                mChart.setDrawBorders(true);
                mChart.setBorderColor(android.R.color.background_dark);
                mChart.setBorderWidth(3);
                mChart.invalidate();
                ////LEGEND
                Legend legend=mChart.getLegend();
                legend.setEnabled(true);
                legend.setTextSize(12);
                legend.setTextColor(Color.parseColor("#2B304C"));
                legend.setForm(Legend.LegendForm.CIRCLE);
                legend.setFormSize(12);
                legend.setXEntrySpace(10);
                legend.setFormToTextSpace(5);
                // legend entry
                /*LegendEntry[] legendEntries=new LegendEntry[2];
                for (int i=0;i<legendEntries.length;i++){
                    LegendEntry entry =new LegendEntry();
                    entry.formColor=colorclassArray[i];
                    entry.label=String.valueOf(legendName[i]);
                    legendEntries[i]=entry;
                }
                legend.setCustom(legendEntries);*/
                Description description=new Description();
                description.setText("Backward X Angle");
                description.setTextSize(10);
                mChart.setDescription(description);



                mChart.invalidate();


                XAxis xAxis=mChart.getXAxis();
                xAxis.setGranularity(1f);


            //X-axis label redrawing is prohibited after zooming in
                //List<String> list = new ArrayList<>();
                /*for (int i = 0; i < 12; i++) {
                    list.add(String.valueOf(i+1).concat("month"));
                }*/

                xAxis.setValueFormatter(new IndexAxisValueFormatter(arraytocheckdata));
                xAxis.setLabelCount(0,false);
                xAxis.setCenterAxisLabels(true);


                mChart.invalidate();







                /*xAxis.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getAxisLabel(float value, AxisBase axis) {
                        if (Math.round(value) == 1) {
                return "JAN"; //make it a string and return
        }       else  if (Math.round(value) == 2) {
                return "FEB"; //make it a string and return
            }
                else {
                    return ""; // return empty for other values where you don't want to print anything on the X Axis
                }
                    }
                });*/



            }
        });
    }

}