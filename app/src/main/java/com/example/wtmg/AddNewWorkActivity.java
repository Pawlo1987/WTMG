package com.example.wtmg;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

//Activity for add A new work
public class AddNewWorkActivity extends AppCompatActivity implements FieldListANWAcRecyclerAdapter.BackDataFormRAtoActInterface {

    Context context;
    DBUtilities dbUtilities;  //create local link for class DBUtilities
    ActionBar actionBar; //arrowGoBack

    public List<Integer> limitsOfTimeListRV; // limits of times List for RecyclerView

    private EditText etProjectNameANWAc; //string for filling with the project name
    private EditText etCompanyNameANWAc; //string for filling with the company name
    private EditText etContactPersonANWAc; //string for filling with the contact person name

    private RecyclerView rvWorksANWAc; //link to recyclerView
    Cursor cursor;
    private FieldListANWAcRecyclerAdapter fieldListANWAcRecyclerAdapter; // adaptor for Recyclerview

    private String mainQuery = "SELECT prj.id, prj.prj_name, prj.prj_time_limit, " +
            "prj.prj_waste_time, prj.company, prj.contact_person " +
            "FROM prj ORDER BY prj.prj_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_work);

        //add ActionBar arrow on Top-Left side on screen
        actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        context = getBaseContext();
        dbUtilities = new DBUtilities(this);
        dbUtilities.open();

        rvWorksANWAc = (RecyclerView) findViewById(R.id.rvWorksANWAc);
        etProjectNameANWAc = (EditText) findViewById(R.id.etProjectNameANWAc);
        etCompanyNameANWAc = (EditText) findViewById(R.id.etCompanyNameANWAc);
        etContactPersonANWAc = (EditText) findViewById(R.id.etContactPersonANWAc);
        limitsOfTimeListRV = new ArrayList<>();

        buildRecyclerView(); //function for building recyclerAdaptor
    }//OnCreate

    //function for building recyclerAdaptor
    private void buildRecyclerView() {
        // Get data from database to Cursor
        cursor = dbUtilities.getDb().rawQuery(mainQuery, null);
        // create adaptor and handing over a link
        //constructor for RecyclerAdaptor
        fieldListANWAcRecyclerAdapter
                = new FieldListANWAcRecyclerAdapter(context, this);

        rvWorksANWAc.setAdapter(fieldListANWAcRecyclerAdapter);
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

    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnNewKindOfWorkANWAc:
                // call alertDialog for creating new field
                alertDialogNewKindOfWork();
                break;

            case R.id.btnCreateANWAc:

                Log.e("myMSG",String.valueOf(limitsOfTimeListRV.size()));
                //addNewWorkToDB();
                break;
        }//switch
    }//onClick

    //AlertDialog for creating a new kind of works
    private void alertDialogNewKindOfWork() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Add a new kind of work?");
//        alert.setMessage("Create new data!");
        alert.setIcon(R.drawable.icon_information);
        final EditText input = new EditText(this);
//        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText("");
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                    String field = input.getText().toString().trim();
                    //checking for matching
                    String query = "SELECT fields.field_name FROM fields " +
                            "WHERE fields.field_name = \"" + field + "\"";
                    Cursor cursor = dbUtilities.getDb().rawQuery(query, null);
                    if (!field.equals("") && (cursor.getCount() == 0)) {
                        //add new value to the table
                        dbUtilities.insertIntoField(field);
                        buildRecyclerView();
                        Toast.makeText(context, "A new kind of work added!", Toast.LENGTH_SHORT).show();
                        // Do something with value!
                }//if(buttonFunction.equals("Add a new kind of work?"))
            }//onClick
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();
    }//alertDialogEditText

    //add a new work to DB
    public void addNewWorkToDB() {
        //flag for checking empty strings
        boolean flEmptyString = false; //default state FALSE, it means NO empty string

        String projectName = etProjectNameANWAc.getText().toString().trim();
        String companyName = etCompanyNameANWAc.getText().toString().trim();
        String contactPerson = etContactPersonANWAc.getText().toString().trim();
        Integer limitOfTime = 80;//??????????????????????????????????????????
        Integer field = 1;///////////////////////////////?????????????????????



        //TODO: Limits of time???????
        //Todo: Fields ???????

        //checking for empty strings
        //if we find one empty space
        //it's satisfies our conditions
        if ((projectName.equals("")) || (companyName.equals("")) || (contactPerson.equals("")) || (limitOfTime.equals(""))) {
            //found empty lines
            Toast.makeText(context, "Empty lines!", Toast.LENGTH_SHORT).show();
            flEmptyString = true;
        }else{
            //checking for matching project name
            //get Cursor from DB
            String query = "SELECT prj.prj_name FROM prj WHERE prj.prj_name = \"" + projectName + "\"";
            Cursor cursor = dbUtilities.getDb().rawQuery(query, null);
            //if matching was found
            if(cursor.getCount() > 0){
                Toast.makeText(context, "Found a match! Correct hebrew word or transcription!", Toast.LENGTH_SHORT).show();
                flEmptyString = true; //set the status in TRUE state if we found empty string
            }//for
        }//if-else

        //if empty lines or matching was not found
        if(!flEmptyString) {
            String query;
            Cursor cursor;

            ///////////////////////working with table prj////////////
            //write new line to table prj
            //Log.e("myMSG",String.valueOf(limitsOfTimeListRV.contains(80)));
            alertDialogNewWork(projectName, limitOfTime, companyName, contactPerson, field);
            finish();
        }//if(!flEmptyString)
    }//addNewWorkToDB

    //AlertDialog for creating a new work
    private void alertDialogNewWork(String projectName, Integer limitOfTime, String companyName, String contactPerson, Integer field) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Add a new work?");
//        alert.setMessage("Create new data!");
        alert.setIcon(R.drawable.icon_information);
        final EditText input = new EditText(this);
//        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText("");
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //save new line in Database
                dbUtilities.insertIntoPrj( projectName, limitOfTime, companyName, contactPerson, field);
                Toast.makeText(context, "New work added!", Toast.LENGTH_SHORT).show();

            }//onClick
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();
    }//alertDialogEditText


    //procedure from interface from RecyclerAdaptor "FieldListANWAcRecyclerAdapter"
    @Override
    public void passLimitsOfTimeListRA(List<Integer> limitsOfTimeListRV) {
        this.limitsOfTimeListRV = limitsOfTimeListRV;
    }
}//AddNewWork