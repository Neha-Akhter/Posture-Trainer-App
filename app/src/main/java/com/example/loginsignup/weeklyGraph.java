package com.example.loginsignup;

import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.mikephil.charting.charts.BarChart;
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

import org.bson.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

import static com.mongodb.client.model.Sorts.descending;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class weeklyGraph extends AppCompatActivity {
    BarChart bchart;
    String Appid = "application-0-dslwi";
    App app = new App(new AppConfiguration.Builder(Appid).build());
    Realm backgroundThreadRealm;
    String partitionValue;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    final long ONE_DAY_MILLI_SECONDS = 24 * 60 * 60 * 1000;

    User user;
    RealmQuery data;
    int maxCapacity = 25;
    //int size;
    //List<BarEntry> barEntries;
    List<BarEntry> barEntries2;
    ArrayList plottingData = new ArrayList();
    Object[] plotlabel= new Object[0];
    ArrayList peakDay = new ArrayList();
    ArrayList dateLabel = new ArrayList();
    ArrayList dayLabel = new ArrayList();
    ArrayList peakWeek = new ArrayList();
    ArrayList peaksWholeDay = new ArrayList();

    int colorArrayForGraph[]= {Color.parseColor("#2B304C"),Color.parseColor("#2B304C")};
    String[] legendname = {"X-Axis - Days"," Y-Axis - Angles"};

    String[] dateList = {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null};
    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    MongoCollection<Document> mongoCollection;




    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_weekly_graph);

        ActionBar bar = getSupportActionBar();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
    @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<String> getXAxisValues()  {
        ArrayList<String> xAxis = new ArrayList<>();

        // day label for display at x axis
        for (int i=0;i<dayLabel.size();i++) {
            xAxis.add((String) dayLabel.get(i));

            //Date date1=new SimpleDateFormat("EEEE").parse((String) dateLabel.get(i));
            /*DateFormat format = new SimpleDateFormat("EEEE");
            Date d1= null;
            try {
                d1 = format.parse((String) dateLabel.get(i));
            } catch (ParseException e) {
                e.printStackTrace();
            }*/
        }
        System.out.println(xAxis);
        return xAxis;
        /*xAxis.add("Mon");
        xAxis.add("Tue");
        xAxis.add("Wed");
        xAxis.add("Thur");
        xAxis.add("Fri");
        xAxis.add("Sat");
        xAxis.add("Sun");
        return xAxis;*/
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
                dayLabel=(ArrayList)plotlabel[1];
                System.out.println(dateLabel);
                System.out.println(dayLabel);
                //bismillah
                //in loop body
                ///MONDAY TAKEN AS STANDARD WEEK START
                for (int i=0;i<7;i++ ) {
                    System.out.println("query start");
                    RealmResults<PeakPOJO> weeklyAngleData = backgroundThreadRealm.where(PeakPOJO.class).
                            equalTo("name",user.getProfile().getEmail()).and()
                            .equalTo("date", (String) dateLabel.get(i)).findAll();
                    System.out.println("query end");
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
                        System.out.println("no query doc");
                        peakWeek.add(0);
                    }
                }

                //in loop body
                System.out.println(peakWeek);
                System.out.println(dateLabel);
                System.out.println("let's see if I got query result");

                //data = backgroundThreadRealm.where(PeakPOJO.class).equalTo("name",user.getProfile().getEmail()).equalTo("date",getDate());
                //RealmResults<PeakPOJO> weeklyAngleData = backgroundThreadRealm.where(PeakPOJO.class).equalTo("name", user.getProfile().getEmail())
                //        .limit(7).findAll().sort("_id", Sort.ASCENDING);
                //Log.v("here", "below loop");
                /*if(weeklyAngleData.size()<7){
                    for (int i = 0; i < weeklyAngleData.size(); i++) {
                        Log.v("here", "inside loop");
                        PeakPOJO values =weeklyAngleData.get(i);
                        peakDay.add(Math.round(Float.valueOf(values.getForwardAngle())));
                        peakDay.add(Math.round(Float.valueOf(values.getBackwardAngle())));
                        peakDay.add(Math.round(Float.valueOf(values.getRightAngle())));
                        peakDay.add(Math.round(Float.valueOf(values.getLeftAngle())));
                        peakWeek.add(Collections.max(peakDay));
                        dateLabel.add(values.getDate());
                        peakDay.clear();
                        //System.out.println(values.getForwardAngle());

                    }
                }
                else{
                    for (int i = 0; i <7; i++) {
                        Log.v("here", "inside loop");
                        PeakPOJO values = weeklyAngleData.get(i);
                        peakDay.add(Math.round(Float.valueOf(values.getForwardAngle())));
                        peakDay.add(Math.round(Float.valueOf(values.getBackwardAngle())));
                        peakDay.add(Math.round(Float.valueOf(values.getRightAngle())));
                        peakDay.add(Math.round(Float.valueOf(values.getLeftAngle())));
                        peakWeek.add(Collections.max(peakDay));
                        dateLabel.add(values.getDate());
                        peakDay.clear();
                        System.out.println(values.getForwardAngle());

                    }
                }*/

                Log.v("Peak Values List week", "xxxx" + peakWeek);
                ArrayList<BarEntry> barEntries = new ArrayList<>();
                for (int i = 0; i < peakWeek.size(); i++) {
                    float x=i;
                    float y = (int) peakWeek.get(i);
                    barEntries.add(new BarEntry(0f,0f));
                    barEntries.add(new BarEntry(x,y));
                }
                System.out.println(barEntries);
                BarDataSet barDataSet = new BarDataSet(barEntries, "\n\nPeak Angles - ongoing week");

                bchart.getLegend().setTextSize(13f);


                BarData theData = new BarData(barDataSet);

                barDataSet.setColor(Color.parseColor("#2B304C"));

                barDataSet.setDrawValues(true);
                barDataSet.setBarShadowColor(Color.GREEN);
                bchart.setData(theData);
                bchart.setDrawMarkers(true);

                bchart.setTouchEnabled(true);
                bchart.setDragEnabled(true);
                bchart.setScaleEnabled(true);
                bchart.setPinchZoom(false);
                YAxis y = bchart.getAxisLeft();
                y.setAxisMaxValue(180);
                y.setAxisMinValue(0);

                bchart.setHighlightFullBarEnabled(true);
                bchart.getDescription().setEnabled(true);
                bchart.getDescription().setText("\\nPeak Angles - ongoing week ");
                bchart.setExtraOffsets(5f,5f,5f,15f);
                bchart.getDescription().setPosition(10f,10f);
                //bchart.setContentDescription("Peak Angles history of this week");
                bchart.animateXY(2000, 2000);
                bchart.getAxisRight().setDrawLabels(false);
                bchart.setFitBars(true); // make the x-axis fit exactly all bars
                bchart.setNoDataText("Loading...");
                bchart.refreshDrawableState();
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



                bchart.getXAxis().setDrawGridLines(false);
                bchart.getAxisLeft().setDrawGridLines(false);
                bchart.getAxisRight().setDrawGridLines(false);
                bchart.setHorizontalScrollBarEnabled(true);
                bchart.setScaleXEnabled(false);
                bchart.setScaleYEnabled(true);

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
                XAxis xAxis=bchart.getXAxis();
                //bchart.setVisibleXRangeMaximum(4);
                //bchart.getXAxis().setAxisMinimum(0);
                xAxis.setPosition (XAxis.XAxisPosition.BOTTOM);
                xAxis.setLabelRotationAngle(0);
                bchart.moveViewToAnimated(10, 10, YAxis.AxisDependency.LEFT, 2000);
                xAxis.setValueFormatter(new IndexAxisValueFormatter(getXAxisValues()));
                bchart.invalidate();

            }
        });

    }



    //    public void retrievePeakWeekly() {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                User user = app.currentUser();
//                mongoClient = user.getMongoClient("mongodb-atlas");
//                mongoDatabase = mongoClient.getDatabase("Android");
//                CodecRegistry pojoCodecRegistry = fromRegistries(AppConfiguration.DEFAULT_BSON_CODEC_REGISTRY,
//                        fromProviders(PojoCodecProvider.builder().automatic(true).build()));
//                MongoCollection<AnglePOJO> mongoCollection =
//                        mongoDatabase.getCollection(
//                                "Graphs",
//                                AnglePOJO.class).withCodecRegistry(pojoCodecRegistry);
//
//                Document query1 = new Document().
//                        append("name", user.getProfile().getEmail());
//                RealmResultTask<MongoCursor<AnglePOJO>> findTask = mongoCollection.find(query1).sort(descending
//                        ("_id")).limit(7).iterator();
//                findTask.getAsync(task -> {
//
//                    if (task.isSuccess()) {
//                        MongoCursor<AnglePOJO> results = task.get();
//                        Log.v("EXAMPLE", "successfully found all documents:");
//                        while (results.hasNext()) {
//                            Log.v("EXAMPLE", "FDGNZF");
//                            AnglePOJO document = results.next();
//                            peakDay.add(Math.round(Float.valueOf(document.getForwardAngle())));
//                            peakDay.add(Math.round(Float.valueOf(document.getBackwardAngle())));
//                            peakDay.add(Math.round(Float.valueOf(document.getRightAngle())));
//                            peakDay.add(Math.round(Float.valueOf(document.getLeftAngle())));
//                            peakWeek.add(Collections.max(peakDay));
//                            dateLabel.add(document.getDate());
//                            peakDay.clear();
//
//
//                        }
//                        Log.v("Peak Values List week", "xxxx" + peakWeek);
//                        ArrayList<BarEntry> barEntries = new ArrayList<>();
//                        for (int i = 0; i < peakWeek.size(); i++) {
//                            float x=i;
//                            float y = (int) peakWeek.get(i);
//                            barEntries.add(new BarEntry(x,y));
//                        }
//                        System.out.println(barEntries);
//                        BarDataSet barDataSet = new BarDataSet(barEntries, "Peak Angles");
//                        BarData theData = new BarData(barDataSet);//----Line of error
//                        barDataSet.setColor(Color.parseColor("#2B304C"));
//                       barDataSet.setDrawValues(true);
//                       barDataSet.setBarShadowColor(Color.RED);
//                        bchart.setData(theData);
//                        bchart.setTouchEnabled(true);
//                        bchart.setDragEnabled(true);
//                        bchart.setScaleEnabled(true);
//                        bchart.getDescription().setEnabled(false);
//                        bchart.animateXY(2000, 2000);
//                        bchart.getAxisRight().setDrawLabels(false);;
//                        bchart.setFitBars(true); // make the x-axis fit exactly all bars
//                        bchart.setNoDataText("Loading...");
//                        bchart.refreshDrawableState();
//                        bchart.setHighlightFullBarEnabled(true);
//                        bchart.setVisibleXRangeMinimum(barEntries.size());
//                        //bchart.getXAxis().setAxisMinimum(-theData.getBarWidth() / 2);
//
//                        //bchart.getXAxis().setAxisMaximum(barEntries.size()-theData.getBarWidth() / 2);
//                        theData.setBarWidth(0.7f);
//                        XAxis xAxis=bchart.getXAxis();
//                        xAxis.setValueFormatter(new IndexAxisValueFormatter(getXAxisValues()));
//                        xAxis.setLabelRotationAngle(25f);
//                        bchart.invalidate();
//                        //Log.v("Peak Values List for Y", "xxxx" + PeakY);
//                    } else {
//                        Log.e("EXAMPLE", "failed to find documents with: ", task.getError());
//                    }
//                });
//            }
//        });
//
//    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onDestroy() {
        super.onDestroy();

        backgroundThreadRealm.close();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getDate(int dayNum) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if (dayNum == 0) {
            System.out.println("inside if of day method");
            String currentDate = dateFormat.format(new Date());
            return currentDate;
        }
        else if (dayNum > 0 && dayNum < 7) {
            System.out.println("inside elif of date method");
            System.out.println(dayNum);
            Date TodayDate = new Date();
            long previousDayMilliSeconds = TodayDate.getTime() - dayNum * ONE_DAY_MILLI_SECONDS;
            Date previousDate = new Date(previousDayMilliSeconds);
            String previousDateStr = dateFormat.format(previousDate);
            System.out.println("Previous Date : " + previousDateStr);
            return previousDateStr;
        } else {
            System.out.println("got a bug");
            return null;
        }




        /*long previousDayMilliSeconds = DateS.getTime() - ONE_DAY_MILLI_SECONDS;
        Date previousDate = new Date(previousDayMilliSeconds);
        String previousDateStr = dateFormat.format(previousDate);

        long pTpMilliSeconds=DateS.getTime()-2*ONE_DAY_MILLI_SECONDS;
        Date pTpDate=new Date(pTpMilliSeconds);
        String pTpDateStr=dateFormat.format(pTpDate);

        long pTpTpMilliSeconds=DateS.getTime()-3*ONE_DAY_MILLI_SECONDS;
        Date pTpTpDate=new Date(pTpTpMilliSeconds);
        String pTpTpDateStr=dateFormat.format(pTpTpDate);

        System.out.println(currentDate);
        System.out.println("Previous Date : " + previousDateStr);
        System.out.println("Previous To previous Date : " + pTpDateStr);
        System.out.println("Previous To previous To previous Date : " + pTpTpDateStr);
        return currentDate;*/
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getDay(int dayNum) {
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("EEE");
        if (dayNum == 0) {
            System.out.println("inside if");
            String currentDay = dateFormat1.format(new Date());
            return currentDay;
        } else if (dayNum > 0 && dayNum < 7) {
            System.out.println("inside elif");
            System.out.println(dayNum);
            Date TodayDate = new Date();
            long previousDayMilliSeconds = TodayDate.getTime() - dayNum * ONE_DAY_MILLI_SECONDS;
            Date previousDate = new Date(previousDayMilliSeconds);
            String previousDay = dateFormat1.format(previousDate);
            return previousDay;
        } else {
            System.out.println("got a bug");
            return null;
        }
    /*public class NextPreviousFromUtilDate {

        // Millseconds in a day
        private static final long ONE_DAY_MILLI_SECONDS = 24 * 60 * 60 * 1000;

        // date format
        private static final String DATE_FORMAT = "dd/MM/YYYY";

        @RequiresApi(api = Build.VERSION_CODES.N)
        public static void main(String[] args) throws ParseException {

            // current date time
            Date currentDate = new Date();

            // Simple date formatter to show in the input and output dates to redable form.
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

            // Getting the next day and formatting into 'YYYY-MM-DD'
            long nextDayMilliSeconds = currentDate.getTime() + ONE_DAY_MILLI_SECONDS;
            Date nextDate = new Date(nextDayMilliSeconds);
            String nextDateStr = dateFormat.format(nextDate);

            // Getting the previous day and formatting into 'YYYY-MM-DD'
            long previousDayMilliSeconds = currentDate.getTime() - ONE_DAY_MILLI_SECONDS;
            Date previousDate = new Date(previousDayMilliSeconds);
            String previousDateStr = dateFormat.format(previousDate);

            // printing the input, tomorrow and yesterday's date as strings.
            System.out.println("Current Date : " + dateFormat.format(currentDate));
            System.out.println("Next Date : " + nextDateStr);
            System.out.println("Previous Date : " + previousDateStr);
        }
    }*/

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public Object[] getDateList(){
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("EEE");
        String currentDate = dateFormat1.format(new Date());
        String currentDay = dateFormat2.format(new Date());
        System.out.println(currentDay);

        Boolean bool3 = currentDay.equals("Mon");
        if (!bool3) {
            for (int i = 1; i < 7; i++) {
                long PreDayMilliSeconds = new Date().getTime() - i *ONE_DAY_MILLI_SECONDS;
                Date PreDate = new Date(PreDayMilliSeconds);
                String Predate =dateFormat1.format(PreDate);
                String PreDay = dateFormat2.format(PreDate);
                Boolean bool1 = PreDay.equals("Mon");
                if (bool1) {
                    dateLabel.add(Predate);
                    dayLabel.add(PreDay);
                    for (int j=1;j<7;j++){
                        long NextDayMilliSeconds = PreDate.getTime() + j * ONE_DAY_MILLI_SECONDS;
                        Date NextDate = new Date(NextDayMilliSeconds);
                        String NxtDate = dateFormat1.format(NextDate);
                        String NextDay = dateFormat2.format(NextDate);
                        dateLabel.add(NxtDate);
                        dayLabel.add(NextDay);
                    }
                    System.out.println(dateLabel);
                    System.out.println(dayLabel);
                    System.out.println("returning from nested if ");
                    return new Object[]{dateLabel, dayLabel};

                }
            }
        }
        else if (bool3){
            System.out.println("inside if get DateList, means today is saturday");
            dateLabel.add(currentDate);
            dayLabel.add(currentDay);
            for (int i=1;i<7;i++){
                long NextDayMilliSeconds = new Date().getTime() + i * ONE_DAY_MILLI_SECONDS;
                Date NextDate = new Date(NextDayMilliSeconds);
                String NxtDate = dateFormat1.format(NextDate);
                String NextDay = dateFormat2.format(NextDate);
                dateLabel.add(NxtDate);
                dayLabel.add(NextDay);
            }
            System.out.println(dateLabel);
            System.out.println(dayLabel);
            return new Object[]{dateLabel, dayLabel};

        }


        return null;
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