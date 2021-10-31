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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//TODO: HERE Must be line with current system time, next line with spinner with relevant works
// and button start or change(function for change text in side of button)
public class StartWorkTimeOrChangeKindOfWorkActivity extends AppCompatActivity {
    ActionBar actionBar; //arrowGoBack

    Context context;

    Cursor cursor;

    Calendar calendar = Calendar.getInstance(); // object for working with TIME and DATE
    String showEventDate;                   // дата события для показа
    String eventDateForDB;                  // дата события для БД
    String eventStartTime;                  // Время начала события
    String time; //additional value
    TextView tvCurTimeAndDateSWTOCKOWAc; //current Date and Time

    Spinner spKindOfWorkSWTOCKOWAc; //spinner for filter projects view by kind of works

    String idAuthUser; //got id User number from authorization Activity

    DBUtilities dbUtilities;  //create local link for class DBUtilities

    RecyclerView rvExistWorkSWTOCKOWAc; //link to recyclerView

    PrjListSWTOCKOWAcRecyclerAdapter prjListSWTOCKOWAcRecyclerAdapter; // adaptor for Recyclerview

    TextView tvWorksCountSWTOCKOWAc; //count of works for this current account

    String mainQuery = "SELECT prj.id, prj.prj_name, prj.prj_time_limit, prj.prj_waste_time, prj.company, prj.support_man FROM prj ORDER BY prj.prj_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_work_time_or_change_kind_of_work);

        context = getBaseContext();

        dbUtilities = new DBUtilities(this);
        dbUtilities.open();

        //add ActionBar arrow on Top-Left side on screen
        actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        rvExistWorkSWTOCKOWAc = (RecyclerView) findViewById(R.id.rvExistWorkSWTOCKOWAc);
        tvWorksCountSWTOCKOWAc = (TextView) findViewById(R.id.tvWorksCountSWTOCKOWAc);
        spKindOfWorkSWTOCKOWAc = (Spinner) findViewById(R.id.spKindOfWorkSWTOCKOWAc);
        tvCurTimeAndDateSWTOCKOWAc = (TextView) findViewById(R.id.tvCurTimeAndDateSWTOCKOWAc);

        setInitialDate();               //начальная установка даты
        setInitialTime();               //начальная установка время

        buildSpinner();//create spinner

        buildRecyclerView(); //function for building recyclerAdaptor

    }//OnCreate

    //function for building recyclerAdaptor
    private void buildRecyclerView() {
        // Get data from database to Cursor
        cursor = dbUtilities.getDb().rawQuery(mainQuery, null);
        //Log.e("myMSG",String.valueOf(cursor.getCount()));
        //count of works
        tvWorksCountSWTOCKOWAc.setText(String.valueOf(cursor.getCount()));

        // create adaptor and handing over a link
        prjListSWTOCKOWAcRecyclerAdapter
                = new PrjListSWTOCKOWAcRecyclerAdapter(context, idAuthUser);

        rvExistWorkSWTOCKOWAc.setAdapter(prjListSWTOCKOWAcRecyclerAdapter);
    }//buildUserRecyclerView

    //arrow for back to previous menu
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

    //build "kind of work" Spinner
    private void buildSpinner() {
        //fill spKindOfWorkSWTOCKOWAc by data of kinds of work for Spinner;
        List<String> spListKindOfWorks = new ArrayList<>(); //collection (data) for spinner
        spListKindOfWorks.add("All");
        //query for getting data for spinner
        String query = "SELECT fields.field_name FROM fields";
        spListKindOfWorks.addAll(dbUtilities.fillList(query));
        //Log.e("myMSG",String.valueOf(spListKindOfWorks.size()));

        //create spinner Adapter
        ArrayAdapter<String> spAdapterKindOfWorks = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                spListKindOfWorks
        );
        // adapter assignment for the list
        spAdapterKindOfWorks.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spKindOfWorkSWTOCKOWAc.setAdapter(spAdapterKindOfWorks);
    }//buildCitySpinner

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
        //Log.e("myMSG",showEventDate.toString());

        //setting text to btnDateCrEv
        time = showEventDate;

        Log.e("myMSG",time);
    } // setInitialDateTime

    // setting start times
    private void setInitialTime() {
        //formation time
        eventStartTime = DateUtils.formatDateTime(
                this,
                calendar.getTimeInMillis(),  // current time in milliseconds
                // we display this time in the usual representation - date and time
                DateUtils.FORMAT_SHOW_TIME);

        //setting text to btnStartTimeCrEv
        tvCurTimeAndDateSWTOCKOWAc.setText(time + "   " +eventStartTime);
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

}//StartWorkTimeOrChangeKindOfWorkActivity