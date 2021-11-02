package com.example.wtmg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

//TODO: Before this Activity, in future, will be add authorization activity
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }//OnCreate

    public void onClick(View view){
        switch(view.getId()){
            case R.id.btnChangeStateOfWorkMAc:
                changeStateOfWork();
                break;

            case R.id.btnAddNewWorkMAc:
                addNewWork();
                break;

            case R.id.btnStatusOfActiveWorksMAc:
                statusOfActiveWorks();
                break;

            case R.id.btnReportBuilderMAc:
                reportBuilder();
                break;

        }//switch

    }//OnClick

    //Activity with features: start work, stop work, change current work to another, short details of works.
    private void changeStateOfWork(){
        Intent intent = new Intent(this, ChangeStateOfWorkActivity.class);
        startActivity(intent);

    }//changeStateOfWork

    //Activity with features: Creating a new work or detail view of existed works or delete existed works.
    private void addNewWork() {
        Intent intent = new Intent(this, AddNewWork.class);
        startActivity(intent);

    }//addNewWork

    //Activity with features: Detail view of works.
    private void statusOfActiveWorks() {
        Intent intent = new Intent(this, StatusOfActiveWorksActivity.class);
        startActivity(intent);

    }//statusOfActiveWork

    //Activity with features: create reports and send or print it.
    private void reportBuilder(){
        Intent intent = new Intent(this, ReportBuilderActivity.class);
        startActivity(intent);

    }//reportBuilder

}//class MainActivity