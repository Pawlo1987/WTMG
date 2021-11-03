package com.example.wtmg;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//TODO:Here will be list with status of work of current day
public class StatusOfActiveWorksActivity extends AppCompatActivity {
    ActionBar actionBar; //arrowGoBack

    Context context;

    Cursor cursor;

    Calendar calendar = Calendar.getInstance(); // object for working with TIME and DATE
    String showEventDate;                   // date for show
    String eventDateForDB;                  // date for DB
    String eventStartTime;                  // Time of start event
    String time; //additional value

    TextView tvCurTimeAndDateSOAWAc; //current Date and Time

    String idAuthUser; //got id User number from authorization Activity

    DBUtilities dbUtilities;  //create local link for class DBUtilities

    RecyclerView rvActiveWorksSOAWAc; //link to recyclerView

    PrjListSOAWAcRecyclerAdapter prjListSOAWAcRecyclerAdapter; // adaptor for Recyclerview

    TextView tvQuantityOfActiveWorksSOAWAc; //count of works for this current account

    String mainQuery = "SELECT prj.id, prj.prj_name, prj.prj_time_limit, prj.prj_waste_time, prj.company, prj.contact_person FROM prj ORDER BY prj.prj_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_of_active_works);
        context = getBaseContext();

        dbUtilities = new DBUtilities(this);
        dbUtilities.open();

        //add ActionBar arrow on Top-Left side on screen
        actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        rvActiveWorksSOAWAc = (RecyclerView) findViewById(R.id.rvActiveWorksSOAWAc);
        tvQuantityOfActiveWorksSOAWAc = (TextView) findViewById(R.id.tvQuantityOfActiveWorksSOAWAc);
        tvCurTimeAndDateSOAWAc = (TextView) findViewById(R.id.tvCurTimeAndDateSOAWAc);

        setInitialDate();               //initialization of the data
        setInitialTime();               //initialization of the time

        buildRecyclerView(); //function for building recyclerAdaptor

    }//OnCreate

    //function for building recyclerAdaptor
    private void buildRecyclerView() {
        // Get data from database to Cursor
        cursor = dbUtilities.getDb().rawQuery(mainQuery, null);
        //Log.e("myMSG",String.valueOf(cursor.getCount()));
        //count of works
        tvQuantityOfActiveWorksSOAWAc.setText(String.valueOf(cursor.getCount()));

        // create adaptor and handing over a link
        prjListSOAWAcRecyclerAdapter
                = new PrjListSOAWAcRecyclerAdapter(context, idAuthUser);

        rvActiveWorksSOAWAc.setAdapter(prjListSOAWAcRecyclerAdapter);
    }//buildUserRecyclerView

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            //handler for actionBar arrow on Top-Left side on screen
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                onBackPressed();
                return true;
        }//switch
        return super.onOptionsItemSelected(item);
    }//onOptionsItemSelected

    // setting handler change / timing
    TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // prepare and display a new time in the display line
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            setInitialTime();
        }
    };

    // setting a date / picker handler
    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // prepare and output the new date in the display line
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDate();
        }
    };

    // setting start date
    private void setInitialDate() {
        //date formation
        showEventDate = DateUtils.formatDateTime(
                this,
                calendar.getTimeInMillis(),  // current time in milliseconds
                // we display this time in the usual representation - date and time
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);

        //setting text to TextView
        //set the date in the desired format for the database
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        eventDateForDB = simpleDateFormat.format(calendar.getTimeInMillis());

        //setting text to btnDateCrEv
        time = showEventDate;

    } // setInitialDateTime

    // display the date picker dialog box - DatePickerDialog
    public void setDate() {
        new DatePickerDialog(
                this,                  // window creation context
                dateSetListener,                    // event listener - date changed
                calendar.get(Calendar.YEAR),     // set year, month, and day from a calendar object
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
                .show();  // show dialogue
    } // setDate

    // setting start times
    private void setInitialTime() {
        //formation time
        eventStartTime = DateUtils.formatDateTime(
                this,
                calendar.getTimeInMillis(),  // current time in milliseconds
                // we display this time in the usual representation - date and time
                DateUtils.FORMAT_SHOW_TIME);

        //setting text to btnStartTimeCrEv
        tvCurTimeAndDateSOAWAc.setText(time + "   " +eventStartTime);
    } // setInitialDateTime

}//TimeCurrentStatusActivity