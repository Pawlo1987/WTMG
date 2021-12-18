package com.example.wtmg;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
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
                //massive "limitsOfTimeListRV" with time limits of each lines of kinds of works from RecyclerAdaptor
                //Log.e("myMSG",String.valueOf(limitsOfTimeListRV.size()));

                addNewWorkToDB();
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
        Integer limitOfTime;  //supporting variable for limits of time
        Integer field;  //supporting variable for number of fields
        int n = limitsOfTimeListRV.size();
        //find max value
        Integer max = 0;
        for(int i=0; i < n; i++ ) {
            if(limitsOfTimeListRV.get(i) > max){
                max = limitsOfTimeListRV.get(i);
            }//if(limitsOfTimeListRV.get(i) > max)
        }//for(int i=0; i < n; i++ )

        //checking for empty strings
        //if we find one empty space
        //it's satisfies our conditions
        if ((projectName.equals("")) || (companyName.equals("")) || (contactPerson.equals("")) || (max == 0)) {
            //found empty lines
            Toast.makeText(context, "Empty lines or didn't choose any time limits", Toast.LENGTH_SHORT).show();
            flEmptyString = true;
        }else{
            //checking for matching of the project name
            //get Cursor from DB
            String query = "SELECT prj.prj_name FROM prj WHERE prj.prj_name = \"" + projectName + "\"";
            Cursor cursor = dbUtilities.getDb().rawQuery(query, null);
            //if matching was found
            if(cursor.getCount() > 0){
                Toast.makeText(context, "Found a match! Please correct name of project!", Toast.LENGTH_SHORT).show();
                flEmptyString = true; //set the status in TRUE state if we found empty string
            }//for
        }//if-else

        //if empty lines or matching was not found
        if(!flEmptyString) {
            String query;
            Cursor cursor;

            ///////////////////////working with table prj////////////
            //write new line to table prj
            alertDialogNewWork(projectName, companyName, contactPerson);
        }//if(!flEmptyString)
    }//addNewWorkToDB

    //AlertDialog for creating a new work
    private void alertDialogNewWork(String projectName, String companyName, String contactPerson) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Add a new work?");
        alert.setMessage("Confirm please.");
        alert.setIcon(R.drawable.icon_information);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //save new lines in Database
                int n = limitsOfTimeListRV.size();
                for(int i = 0; i < n; i++) {
                    if(limitsOfTimeListRV.get(i) > 0 ){
                        dbUtilities.insertIntoPrj( projectName, limitsOfTimeListRV.get(i), companyName, contactPerson, i);
                    }// if(limitsOfTimeListRV.get(i) != 0 )
                }//for(int i = 0; i <= sizeOfLimOfTimeMas; i++ )
                Toast.makeText(context, "New work added!", Toast.LENGTH_SHORT).show();
                finish();
            }//onClick
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
                finish();
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