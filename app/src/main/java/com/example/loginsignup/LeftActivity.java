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

public class LeftActivity extends AppCompatActivity {
    private static final String LIST_KEY3="list_key3";
    LineChart mChart3;
    LineDataSet set4;
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
        setContentView(R.layout.activity_left_y);
        mChart3 = findViewById(R.id
                .chart1);
        ActionBar bar = getSupportActionBar();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        text= findViewById(R.id.textView15);


        SharedPreferences sh3 = getSharedPreferences("MySharedPrefForList3", MODE_PRIVATE);
        String jsonString3 = sh3.getString(LIST_KEY3, "");
        Gson gson3 = new Gson();
        Type type = new TypeToken<List<String>>() {
        }.getType();
        List<String> Leftploty = gson3.fromJson(jsonString3, type);
        size =  Leftploty.size();
        System.out.println(Leftploty);

        int[] yaxisAngleData3 = new int[size];
        String[] axisData = new String[size];
        System.out.println(size);
        labels=(size/60);
        ArrayList arraytocheckdata=new ArrayList();
        arraytocheckdata.add("");

        text.setText("Total Duration is  " + labels + "minute");
        System.out.println(yaxisAngleData3);


        for (int i = 0; i <  Leftploty.size(); i++) {
            yaxisAngleData3[i] = Integer.valueOf( Leftploty.get(i));
        }


        ArrayList<Entry> yVals = new ArrayList<>();
        ArrayList<Entry> redline=new ArrayList<>();
        for (int i=0;i<=yaxisAngleData3.length-1;i++){
            redline.add(new Entry(i, 25f));
        }



        for (int i = 0; i <= yaxisAngleData3.length - 1; i++) {
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
            yVals.add(new Entry(i, yaxisAngleData3[i]));

        }

        if (size > 0){
            plotGraphX(yVals,redline, arraytocheckdata);
        }
        else{
            mChart3.setNoDataText("No statistics");
        }
    }
    public void plotGraphX(ArrayList<Entry> yVals,ArrayList redline, ArrayList arraytocheckdata){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                set4 = new LineDataSet(yVals, "Angles");
                set4.setLineWidth(2f);
                for (int i=0;i<yVals.size();i++){
                    if (yVals.get(i).getX()<25){
                        set4.setColor(Color.GREEN);
                    }
                    else{
                        set4.setColors(Color.parseColor("#2B304C"));
                    }
                }

                //set1.setColor(Color.GREEN);
                set4.setDrawCircles(false);
                set4.setDrawCircleHole(false);
                set4.setCircleColor(Color.parseColor("#2B304C"));
                set4.setCircleRadius(1f);
                set4.setValueTextSize(5f);
                set4.setDrawIcons(false);
                //set1.enableDashedLine(10f, 5f, 0f);
                //set1.enableDashedHighlightLine(10f, 5f, 0f);
                set4.setDrawValues(false);
                set4.setColors(colorArrayForGraph,getApplicationContext());
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
                dataSets.add(set4);
                dataSets.add(set1);
                LineData data = new LineData(dataSets);
                mChart3.setTouchEnabled(true);
                mChart3.setPinchZoom(true);
                mChart3.setBackgroundColor(Color.WHITE);
                mChart3.setData(data);
                mChart3.setNoDataText("No data to show on graph");
                mChart3.setDrawGridBackground(true);
                mChart3.setDrawBorders(true);
                mChart3.setBorderColor(android.R.color.background_dark);
                mChart3.setBorderWidth(3);
                mChart3.invalidate();
                ////LEGEND
                Legend legend=mChart3.getLegend();
                legend.setEnabled(true);
                legend.setTextSize(12);
                legend.setTextColor(Color.parseColor("#2B304C"));
                legend.setForm(Legend.LegendForm.CIRCLE);
                legend.setFormSize(12);
                legend.setXEntrySpace(10);
                legend.setFormToTextSpace(5);
                // legend entry
              /*  LegendEntry[] legendEntries=new LegendEntry[2];
                for (int i=0;i<legendEntries.length;i++){
                    LegendEntry entry =new LegendEntry();
                    entry.formColor=colorclassArray[i];
                    entry.label=String.valueOf(legendName[i]);
                    legendEntries[i]=entry;
                }
                legend.setCustom(legendEntries);*/
                Description description=new Description();
                description.setText("Left Y Angle");
                description.setTextSize(10);
                mChart3.setDescription(description);




                XAxis xAxis=mChart3.getXAxis();
                xAxis.setGranularity(1f);


                xAxis.setValueFormatter(new IndexAxisValueFormatter(arraytocheckdata));
                xAxis.setLabelCount(0,false);
                xAxis.setCenterAxisLabels(true);


                mChart3.invalidate();



                //X-axis label redrawing is prohibited after zooming in
                //List<String> list = new ArrayList<>();
                /*for (int i = 0; i < 12; i++) {
                    list.add(String.valueOf(i+1).concat("month"));
                }*/



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
