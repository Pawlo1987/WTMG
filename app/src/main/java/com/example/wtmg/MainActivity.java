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
            case R.id.btnStartOrChangeMAc:
                startWorkTimeOrChangeKindOfWork();
                break;

            case R.id.btnStopWorkTimeMAc:
                stopWorkTime();
                break;

            case R.id.btnKindsOfWorkMAc:
                kindsOfWork();
                break;

            case R.id.btnCurrentStatusMAc:
                timeCurrentStatus();
                break;

            case R.id.btnReportsMAc:
                reports();
                break;

        }//switch

    }//OnClick

    //Activity with features: start work, change current work to another, short details of works.
    private void startWorkTimeOrChangeKindOfWork(){
        Intent intent = new Intent(this, StartWorkTimeOrChangeKindOfWorkActivity.class);
        startActivity(intent);

    }//startWorkTime

    //Activity with feature: stop works
    private void stopWorkTime(){
        Intent intent = new Intent(this, StopWorkTimeActivity.class);
        startActivity(intent);

    }//stopWorkTime

    //Activity with features: Creating a new kind of work or detail view of existed works or delete existed works.
    private void kindsOfWork() {
        Intent intent = new Intent(this, KindsOfWorkActivity.class);
        startActivity(intent);

    }//timeCurrentStatus

    //Activity with features: Detail view of works.
    private void timeCurrentStatus() {
        Intent intent = new Intent(this, TimeCurrentStatusActivity.class);
        startActivity(intent);

    }//timeCurrentStatus

    //Activity with features: create reports and send or print it.
    private void reports(){
        Intent intent = new Intent(this, ReportsActivity.class);
        startActivity(intent);

    }//reports

}//class MainActivity