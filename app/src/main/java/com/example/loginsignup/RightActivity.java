package com.example.loginsignup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

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
import java.util.ArrayList;
import java.util.List;

public class RightActivity extends AppCompatActivity {

    private static final String LIST_KEY2="list_key2";
    LineChart mChart2;
    LineDataSet set3;
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
        setContentView(R.layout.activity_right_y);
        ActionBar bar = getSupportActionBar();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mChart2 = findViewById(R.id.chart2); // object initialization by finding view by id
        text= findViewById(R.id.textView15);


        SharedPreferences sh2 = getSharedPreferences("MySharedPrefForList2", MODE_PRIVATE);
        String jsonString2 = sh2.getString(LIST_KEY2, "");
        Gson gson2 = new Gson();
        Type type = new TypeToken<List<String>>() {
        }.getType();
        List<String> Rightploty = gson2.fromJson(jsonString2, type);
        size = Rightploty .size();
        System.out.println(Rightploty);

        int[] yaxisAngleData = new int[size];
        String[] axisData = new String[size];
        System.out.println(size);
        labels=(size/60);
        System.out.println(yaxisAngleData);
        ArrayList arraytocheckdata=new ArrayList();
        arraytocheckdata.add("");
        text.setText("Total Duration is  " + labels + "minute");



        for (int i = 0; i < Rightploty.size(); i++) {
            yaxisAngleData[i] = Integer.valueOf(Rightploty.get(i));
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
        System.out.println(yVals);
        if (size > 0){
            plotGraphX(yVals,redline,arraytocheckdata);
        }
        else{
            mChart2.setNoDataText("No statistics");
        }
    }
    public void plotGraphX(ArrayList<Entry> yVals, ArrayList<Entry> redline, ArrayList arraytocheckdata){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                set3 = new LineDataSet(yVals, "Angles");
                set3.setLineWidth(2f);
                for (int i=0;i<yVals.size();i++){
                    if (yVals.get(i).getX()<25){
                        set3.setColor(Color.GREEN);
                    }
                    else{
                        set3.setColors(Color.parseColor("#2B304C"));
                    }
                }

                //set1.setColor(Color.GREEN);
                set3.setDrawCircles(false);
                set3.setDrawCircleHole(false);

                set3.setCircleColor(Color.parseColor("#2B304C"));
                set3.setCircleRadius(1f);
                set3.setValueTextSize(5f);
                set3.setDrawIcons(false);
                //set1.enableDashedLine(10f, 5f, 0f);
                //set1.enableDashedHighlightLine(10f, 5f, 0f);
                set3.setDrawValues(false);
                set3.setColors(colorArrayForGraph,getApplicationContext());
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
                dataSets.add(set3);
                dataSets.add(set1);
                LineData data = new LineData(dataSets);
                mChart2.setTouchEnabled(true);
                mChart2.setPinchZoom(true);
                mChart2.setBackgroundColor(Color.WHITE);
                mChart2.setData(data);
                mChart2.setNoDataText("No data to show on graph");
                //mChart2.setDrawGridBackground(false);
                mChart2.setDrawGridBackground(true);
                mChart2.setDrawBorders(true);
                mChart2.setBorderColor(android.R.color.background_dark);
                mChart2.setBorderWidth(3);
                mChart2.invalidate();
                ////LEGEND
                Legend legend=mChart2.getLegend();
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
                description.setText("Right Y Angle");
                description.setTextSize(10);
                mChart2.setDescription(description);



                XAxis xAxis=mChart2.getXAxis();
                xAxis.setGranularity(1f);


                //X-axis label redrawing is prohibited after zooming in
                //List<String> list = new ArrayList<>();
                /*for (int i = 0; i < 12; i++) {
                    list.add(String.valueOf(i+1).concat("month"));
                }*/

                xAxis.setValueFormatter(new IndexAxisValueFormatter(arraytocheckdata));
                //xAxis.setLabelCount(labels,true);



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