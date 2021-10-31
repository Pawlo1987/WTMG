package com.example.wtmg;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

//TODO: Here must be work button for add new kind of work or delete kind of work
// additional option or new work (spinner > option Engineering, layout, else) + count of ordering hours.
public class KindsOfWorkActivity extends AppCompatActivity {

    Context context;
    DBUtilities dbUtilities;  //create local link for class DBUtilities
    ActionBar actionBar; //arrowGoBack
    public String mainQuery = "SELECT prj.id, prj.prj_name, prj.prj_time_limit, " +
            "prj.prj_waste_time, prj.company, prj.support_man " +
            "FROM prj ORDER BY prj.prj_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kinds_of_work);

        //add ActionBar arrow on Top-Left side on screen
        actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        dbUtilities = new DBUtilities(this);

    }//OnCreate

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

}//KindsOfWorkActivity