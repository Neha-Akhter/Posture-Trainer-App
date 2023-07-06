package com.example.loginsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;



public class ForwardActivity extends AppCompatActivity {
    private static final String LIST_KEY="list_key";
    LineChart mChart1;
    LineDataSet set2;
    LineDataSet set1;
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
        setContentView(R.layout.activity_graph);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar bar = getSupportActionBar();
        mChart1 = findViewById(R.id.chart1); // object initialization by finding view by id
        text= findViewById(R.id.textView15);


        SharedPreferences sh1 = getSharedPreferences("MySharedPrefForList", MODE_PRIVATE);
        String jsonString1 = sh1.getString(LIST_KEY, "");
        Gson gson1 = new Gson();
        Type type = new TypeToken<List<String>>() {
        }.getType();
        List<String> ForwardPlot = gson1.fromJson(jsonString1, type);
        size = ForwardPlot.size();
        System.out.println(ForwardPlot);
        System.out.println(size);
        int[] YaxisData = new int[size];
        String[] XaxisData = new String[size];

        labels=(size/60);
        System.out.println(labels);
        text.setText("Total Duration is  " + labels + "minute");
        ArrayList arraytocheckdata=new ArrayList();
        arraytocheckdata.add("");
        System.out.println(YaxisData);


        for (int i = 0; i < ForwardPlot.size(); i++) {
            YaxisData[i] = Integer.valueOf(ForwardPlot.get(i));
        }


        ArrayList<Entry> yVals = new ArrayList<>();
        ArrayList<Entry> redline=new ArrayList<>();
        for (int i=0;i<=YaxisData.length-1;i++){
            redline.add(new Entry(i, 25f));
        }


        //XaxisData[0]=" ";
        for (int i = 0; i <= YaxisData.length - 1; i++) {
            if (sectimeCount <= 59) {
                XaxisData[i] = (sectimeCount+" sec");
                sectimeCount += 1;
            } else if(sectimeCount==60 && mintimeCount<=59) {
                XaxisData[i] = (mintimeCount+" min");
                mintimeCount+=1;
                sectimeCount = 1;
            }
            else{
                XaxisData[i] = (hourtimeCount+" hour");
                hourtimeCount+=1;
                mintimeCount=1;
                sectimeCount=1;
            }
            yVals.add(new Entry(i, YaxisData[i]));

        }
        // to check what values are in my axisData

        //for (int i=0;i<labels;i++){




        System.out.println(yVals);
        if (size > 0){
            plotGraphX(yVals,redline,arraytocheckdata);
        }
        else{
            mChart1.setNoDataText("No statistics");
        }
    }
    public void plotGraphX(ArrayList<Entry> yVals, ArrayList<Entry> redline, ArrayList arraytocheckdata){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                set2 = new LineDataSet(yVals, "Angles");
                set2.setLineWidth(2f);
                for (int i=0;i<yVals.size();i++){
                    if (yVals.get(i).getX()<25){
                        set2.setColor(Color.GREEN);
                    }
                    else{
                        set2.setColors(Color.parseColor("#2B304C"));
                    }
                }

                //set1.setColor(Color.GREEN);
                set2.setDrawCircles(false);
                set2.setDrawCircleHole(false);
                set2.setCircleColor(Color.parseColor("#2B304C"));
                set2.setCircleRadius(1f);
                set2.setValueTextSize(5f);
                set2.setDrawIcons(false);
                //set1.enableDashedLine(10f, 5f, 0f);
                //set1.enableDashedHighlightLine(10f, 5f, 0f);
                set2.setDrawValues(false);
                set2.setColors(colorArrayForGraph,getApplicationContext());
                set1=new LineDataSet(redline,"Correct Posture Range");
                set1.setDrawCircles(false);
                set1.setDrawCircleHole(false);
                set1.setCircleColor(Color.RED);
                set1.setCircleRadius(1f);
                set1.setValueTextSize(5f);
                set1.setDrawIcons(false);
                set1.setDrawValues(false);
                set1.setColors(colorArrayForGraph,getApplicationContext());
                set1.setLineWidth(1f);
                set1.setColors(Color.RED);


                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(set2);
                dataSets.add(set1);
                LineData data = new LineData(dataSets);
                mChart1.setTouchEnabled(true);
                mChart1.setPinchZoom(true);
                mChart1.setBackgroundColor(Color.WHITE);
                mChart1.setData(data);
                mChart1.setNoDataText("No data to show on graph");
                mChart1.setDrawGridBackground(true);
                mChart1.setDrawBorders(true);
                mChart1.setBorderColor(android.R.color.background_dark);
                mChart1.setBorderWidth(3);
                mChart1.invalidate();
                ////LEGEND
                Legend legend=mChart1.getLegend();
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
                }*/
                //legend.setCustom(legendEntries);
                Description description=new Description();
                description.setText("Forward X Angles");
                description.setTextSize(10);
                mChart1.setDescription(description);
                mChart1.invalidate();



                //YAxis rightAxis = mChart1.getAxisRight();  //may be it will display angles on left side only
                //rightAxis.setEnabled(true);

                XAxis xAxis=mChart1.getXAxis();

                xAxis.setGranularity(1f);



                xAxis.setValueFormatter(new IndexAxisValueFormatter(arraytocheckdata));
                xAxis.setLabelCount(0,false);
                xAxis.setCenterAxisLabels(true);


                mChart1.invalidate();




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