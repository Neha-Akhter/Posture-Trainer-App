package com.example.loginsignup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;
import io.realm.mongodb.sync.SyncConfiguration;

import static com.mongodb.client.model.Sorts.descending;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class monthlyGraph  extends AppCompatActivity {
    BarChart bchart;

    int maxCapacity = 25;
    ArrayList plottingData = new ArrayList();
    Object[] plotlabel= new Object[0];
    //private static final String LIST_KEY4 = "list_key4";
    ArrayList peakDay = new ArrayList();
    ArrayList peakWeek = new ArrayList();

    ArrayList dateLabel = new ArrayList();
    ArrayList dayLabel=new ArrayList();
    ArrayList peaksWholeDay=new ArrayList();

    int colorArrayForGraph[]= {Color.parseColor("#2B304C"),Color.parseColor("#2B304C")};
    String[] legendname = {"X-Axis - Date"," Y-Axis - Angles"};


    String[] dateList = {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null};
    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    MongoCollection<Document> mongoCollection;
    String Appid = "application-0-dslwi";
    App app = new App(new AppConfiguration.Builder(Appid).build());
    Realm backgroundThreadRealm;
    String partitionValue;

    User user;
    RealmQuery data;




    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_graph);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar bar = getSupportActionBar();
        Toolbar appBar = findViewById(R.id.appBar);
        setSupportActionBar(appBar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bchart = findViewById(R.id.Barchart); // object initialization by finding view by id
        Realm.init(this);
        app = new App(new AppConfiguration.Builder(Appid).build());
        partitionValue = "My project";
        user = app.currentUser();
//        SyncConfiguration config = new SyncConfiguration.Builder(
//                user,
//                partitionValue
//        ).allowQueriesOnUiThread(true)
//                .allowWritesOnUiThread(true)
//                .build();
        backgroundThreadRealm = Realm.getDefaultInstance();
        retrievePeakWeekly();




    }

    public void plotGraph(ArrayList p) {
        System.out.println(plottingData);


    }
    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        for (int i=1;i<dateLabel.size()+1;i++){
            xAxis.add(""+i);
        }
        System.out.println(xAxis);
        return xAxis;
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    public void retrievePeakWeekly() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                User user = app.currentUser();
                Log.v("here", "below user");
                plotlabel=getDateList();
                dateLabel= (ArrayList) plotlabel[0];



                //data = backgroundThreadRealm.where(PeakPOJO.class).equalTo("name",user.getProfile().getEmail()).equalTo("date",getDate());
                for (int i=0;i<dateLabel.size();i++ ) {
                    RealmResults<PeakPOJO> weeklyAngleData = backgroundThreadRealm.where(PeakPOJO.class).
                            equalTo("name",user.getProfile().getEmail()).and()
                            .equalTo("date", (String) dateLabel.get(i)).findAll();
                    System.out.println(weeklyAngleData.size()+ "size of queried doc");
                    if (weeklyAngleData.size() != 0) {
                        for (int j=0;j<weeklyAngleData.size();j++){
                            PeakPOJO values=weeklyAngleData.get(j);
                            peakDay.add(Math.round(Float.valueOf(values.getForwardAngle())));
                            peakDay.add(Math.round(Float.valueOf(values.getBackwardAngle())));
                            peakDay.add(Math.round(Float.valueOf(values.getRightAngle())));
                            peakDay.add(Math.round(Float.valueOf(values.getLeftAngle())));
                            peaksWholeDay.add(Collections.max(peakDay));
                            peakDay.clear();
                        }
                        peakWeek.add(Collections.max(peaksWholeDay));
                        peaksWholeDay.clear();
                    } else {
                        peakWeek.add(0);
                    }
                }


                //in loop
                System.out.println(peakWeek);
                System.out.println(dateLabel);
                System.out.println("let's see if I got query result");



                Log.v("Peak Values List week", "xxxx" + peakWeek);
                ArrayList<BarEntry> barEntries = new ArrayList<>();
                for (int i = 0; i < peakWeek.size(); i++) {
                    float x=i;
                    float y = (int) peakWeek.get(i);
                    barEntries.add(new BarEntry(x,y));
                }
                System.out.println(barEntries);

             /*   LimitLine ll = new LimitLine(maxCapacity, "");
                bchart.getAxisLeft().addLimitLine(ll);
                ll.setLineWidth(2f);*/

                BarDataSet barDataSet = new BarDataSet(barEntries, "Peak Angles - ongoing month");
                BarData theData = new BarData(barDataSet);//----Line of error
                barDataSet.setColor(Color.parseColor("#2B304C"));
                barDataSet.setDrawValues(true);
                barDataSet.setBarShadowColor(Color.RED);
                bchart.setData(theData);
                theData.setBarWidth(0.5f);

                bchart.getAxisRight().setDrawLabels(false);
                bchart.setTouchEnabled(true);
                bchart.setDragEnabled(true);
                bchart.setScaleEnabled(true);
                bchart.setPinchZoom(false);
                bchart.setHorizontalScrollBarEnabled(true);
                bchart.setScaleXEnabled(false);
                bchart.setScaleYEnabled(true);



                bchart.setHighlightFullBarEnabled(true);
                bchart.getDescription().setEnabled(false);
                bchart.animateXY(2000, 2000);
                bchart.getAxisRight().setDrawLabels(false);
                bchart.setFitBars(true); // make the x-axis fit exactly all bars
                bchart.setNoDataText("Loading...");
                bchart.getLegend().setTextSize(13f);
                bchart.getDescription().setText("\\nPeak Angles - ongoing month ");
                bchart.getDescription().setPosition(10f,10f);
                bchart.setExtraOffsets(5f,5f,5f,15f);
                bchart.refreshDrawableState();

                bchart.getXAxis().setDrawGridLines(false);
                bchart.getAxisLeft().setDrawGridLines(false);
                bchart.getAxisRight().setDrawGridLines(false);
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
                bchart.setVisibleXRangeMaximum(7);
                XAxis xAxis=bchart.getXAxis();

                xAxis.setPosition (XAxis.XAxisPosition.BOTTOM);
                xAxis.setLabelRotationAngle(0);

                xAxis.setValueFormatter(new IndexAxisValueFormatter(getXAxisValues()));
                bchart.invalidate();

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onDestroy() {
        super.onDestroy();

        backgroundThreadRealm.close();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public Object[] getDateList() {

        DateTimeFormatter formatter = null;

        //String currentDay = dateFormat2.format(new Date());
        //if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        //formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        //}
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate date = LocalDate.now();
            LocalDate dfoMonth=date.withDayOfMonth(1);
            LocalDate dloMonth=date.withDayOfMonth(date.lengthOfMonth());

            dateLabel.add(formatter.format(dfoMonth));
            System.out.println(dateLabel);
            for (int j=1;j<date.lengthOfMonth()-1;j++)
            {
                dateLabel.add(formatter.format(dfoMonth.plusDays(j)));

            }
            System.out.println(dateLabel);
            System.out.println("coming out of loop, adding last day to list hue hue");
            dateLabel.add(formatter.format(dloMonth));
        }
        System.out.println(dateLabel);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate date = LocalDate.now();
            LocalDate start = date.withDayOfMonth(1);
            LocalDate end = date.withDayOfMonth(date.lengthOfMonth());
            String FirstDay = dateFormat1.format(start);
            String LastDay = dateFormat1.format(end);
            System.out.println(FirstDay);
            System.out.println(LastDay);
        }*/



        return new Object[]{dateLabel};
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