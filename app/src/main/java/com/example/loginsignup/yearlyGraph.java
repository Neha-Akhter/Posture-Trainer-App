package com.example.loginsignup;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;

public class yearlyGraph extends AppCompatActivity {
    BarChart bchart;
    String Appid = "application-0-dslwi";
    App app = new App(new AppConfiguration.Builder(Appid).build());
    Realm backgroundThreadRealm;
    String partitionValue;

    ArrayList peakDay = new ArrayList();
    ArrayList peakMonth = new ArrayList();
    ArrayList peakYearly=new ArrayList();
    ArrayList monthYearString=new ArrayList();

    int colorArrayForGraph[]= {Color.parseColor("#2B304C"),Color.parseColor("#2B304C")};
    String[] legendname = {"X-Axis - Months "," Y-Axis - Angles"};

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yearly_graph);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Toolbar appBar = findViewById(R.id.appBar);
        setSupportActionBar(appBar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bchart = findViewById(R.id.Barchart); // object initialization by finding view by id
        Realm.init(this);
        app = new App(new AppConfiguration.Builder(Appid).build());
        partitionValue = "My project";

        backgroundThreadRealm = Realm.getDefaultInstance();
        retrievePeakyearly();
    }
    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("Jan");
        xAxis.add("Feb");
        xAxis.add("March");
        xAxis.add("April");
        xAxis.add("May");
        xAxis.add("June");
        xAxis.add("July");
        xAxis.add("Aug");
        xAxis.add("Sep");
        xAxis.add("Oct");
        xAxis.add("Nov");
        xAxis.add("Dec");
        System.out.println(xAxis);
        return xAxis;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void retrievePeakyearly() {
        User user = app.currentUser();
        getDateForQuery();
        for (int i=0;i<12;i++ ) {
            RealmResults<PeakPOJO> weeklyAngleData = backgroundThreadRealm.where(PeakPOJO.class).
                    equalTo("name",user.getProfile().getEmail()).
                    and().equalTo("MonthYear", (String) monthYearString.get(i)).findAll();
            System.out.println(weeklyAngleData.size()+ " size of queried doc");
            if (weeklyAngleData.size() != 0) {
                for (int j=0;j<weeklyAngleData.size();j++){
                    PeakPOJO values=weeklyAngleData.get(j);
                    peakDay.add(Math.round(Float.valueOf(values.getForwardAngle())));
                    peakDay.add(Math.round(Float.valueOf(values.getBackwardAngle())));
                    peakDay.add(Math.round(Float.valueOf(values.getRightAngle())));
                    peakDay.add(Math.round(Float.valueOf(values.getLeftAngle())));
                    peakMonth.add(Collections.max(peakDay));
                    peakDay.clear();
                    System.out.println("fetching angles per month");
                    System.out.println(peakMonth);
                }
                System.out.println(peakMonth);
                peakYearly.add(Collections.max(peakMonth));
                peakMonth.clear();
                System.out.println(peakYearly);

            } else {
                System.out.println("inside else");
                peakYearly.add(0);
                System.out.println(peakYearly);
            }
        }
        Log.v("Peak Values List week", "xxxx" + peakYearly);
        if(peakYearly.size()==12){
            ArrayList<BarEntry> barEntries = new ArrayList<>();
            for (int i = 0; i < peakYearly.size(); i++) {
                float x=i;
                float y = (int) peakYearly.get(i);
                barEntries.add(new BarEntry(x,y));
            }
            BarDataSet barDataSet = new BarDataSet(barEntries, "Peak Angles - ongoing year");
            BarData theData = new BarData(barDataSet);
            barDataSet.setColor(Color.parseColor("#2B304C"));
            barDataSet.setDrawValues(true);
            barDataSet.setBarShadowColor(Color.GREEN);
            bchart.setData(theData);


            bchart.getAxisRight().setDrawLabels(false);
            bchart.setTouchEnabled(true);
            bchart.setDragEnabled(true);
            bchart.setScaleEnabled(true);
            bchart.setPinchZoom(false);
            // bchart.setScaleXEnabled(false);
            bchart.setHorizontalScrollBarEnabled(true);
            // bchart.setScaleYEnabled(true);

            bchart.setHighlightFullBarEnabled(true);
            bchart.getDescription().setEnabled(false);
            bchart.animateXY(2000, 2000);
            bchart.getAxisRight().setDrawLabels(false);
            bchart.setFitBars(true); // make the x-axis fit exactly all bars
            bchart.setNoDataText("Loading...");
            bchart.getLegend().setTextSize(13f);
            bchart.getDescription().setText("\\nPeak Angles - ongoing year ");
            bchart.getDescription().setPosition(4f,7f);
            bchart.setFitBars(true); // make the x-axis fit exactly all bars
            bchart.setNoDataText("Loading...");
            bchart.refreshDrawableState();
            bchart.getXAxis().setDrawGridLines(false);
            bchart.getAxisLeft().setDrawGridLines(false);
            bchart.getAxisRight().setDrawGridLines(false);
            bchart.setExtraOffsets(5f,5f,5f,15f);
            YAxis y = bchart.getAxisLeft();
            y.setAxisMaxValue(180);
            y.setAxisMinValue(0);

            Legend legend= bchart.getLegend();
            legend.setTextColor(Color.parseColor("#2B304C"));
            legend.setTextSize(13f);
            legend.setForm(Legend.LegendForm.SQUARE);


            legend.setFormSize(10);
            legend.setYEntrySpace(15);
            legend.setXEntrySpace(29);
            legend.setFormToTextSpace(7);
            legend.setXOffset(10f);

            LegendEntry[] legendEntries =new LegendEntry[2];
            int i=0;

            for (i=0; i <legendEntries.length; i++){
                LegendEntry entry= new LegendEntry();
                entry.formColor= colorArrayForGraph[i];
                entry.label =  String.valueOf(legendname[i]);
                legendEntries[i]= entry;

            }


            legend.setCustom(legendEntries);


            bchart.setDrawGridBackground(false);

            theData.setBarWidth(0.7f);
            theData.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {

                    if (value > 0){
                        return super.getFormattedValue(value);
                    }else{
                        return "";
                    }
                }

            });


          //  bchart.getXAxis().setAxisMinimum(0);

            bchart.setHorizontalScrollBarEnabled(true);
            bchart.setScaleXEnabled(false);
            bchart.setScaleYEnabled(true);
            XAxis xAxis=bchart.getXAxis();
            xAxis.setPosition (XAxis.XAxisPosition.BOTTOM);
            xAxis.setLabelRotationAngle(0);
            bchart.setVisibleXRangeMaximum(6);
          //  bchart.getXAxis().setAxisMinimum(0);

            xAxis.setValueFormatter(new IndexAxisValueFormatter(getXAxisValues()));
            bchart.invalidate();
        }
        else{
            System.out.println("issue with data retrieval");
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        backgroundThreadRealm.close();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getDateForQuery() {
        LocalDate date = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            date = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM yyyy");
            for (int currentMonth = 1; currentMonth <= 12; currentMonth++) {
                date = date.withMonth(currentMonth);
                LocalDate firstDay = date.withDayOfMonth(1);
                monthYearString.add(formatter.format(firstDay));

            }
            System.out.println(monthYearString);

        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent1 = new Intent(getApplicationContext(),anglesValue.class);
                startActivity(intent1);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}