package com.example.wtmg;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

//TODO:  recycler adaptor must save all condition in time of work
//Recycler Adaptor for list in Add A New Work Activity
public class FieldListANWAcRecyclerAdapter extends RecyclerView.Adapter<FieldListANWAcRecyclerAdapter.ViewHolder>{

    Context context;

    //Link on interface for pass data from Recycler Adaptor
    BackDataFormRAtoActInterface backDataFormRAtoActInterface;

    //fields of class PrjListRecyclerAdapter
    private LayoutInflater inflater;
    private Cursor cursor;
    private String idField; // value for transfer to next level

    public Integer[] limitsOfTimeArrayRV; // limits of times array for RecyclerView and for ordering data

    DBUtilities dbUtilities;//link to DBUtilities class


    //constructor
    public FieldListANWAcRecyclerAdapter(Context context, BackDataFormRAtoActInterface backDataFormRAtoActInterface) {
        this.inflater = LayoutInflater.from(context);
        //handler tap on RecyclerAdapter
        this.context = context;
        dbUtilities = new DBUtilities(context);
        dbUtilities.open();
        String mainQuery = "SELECT fields.id, fields.field_name FROM fields";
        cursor = dbUtilities.getDb().rawQuery(mainQuery, null);

        this.backDataFormRAtoActInterface = backDataFormRAtoActInterface;

        //size of array limitsOfTimeArrayRV we will declare according to cursor size
        limitsOfTimeArrayRV = new Integer[cursor.getCount()];

    } // FieldListANWAcRecyclerAdapter

    //create a new markup (View) by specifying the markup
    @Override
    public FieldListANWAcRecyclerAdapter.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.field_list_anw_recycler_adapter, parent, false);
        BackDataFormRAtoActInterface backDataFormRAtoActInterface = this.backDataFormRAtoActInterface;
        return new FieldListANWAcRecyclerAdapter.ViewHolder(view, backDataFormRAtoActInterface);
    }

    //bind markup elements to object variables (in this case, to the cursor)
    @Override
    public void onBindViewHolder(FieldListANWAcRecyclerAdapter.ViewHolder holder, int position) {
        //move cursor on current position
        cursor.moveToPosition(position);

        //get data
        idField = cursor.getString(0); //caught current idField
        holder.tvFieldNameANWAcRA.setText(cursor.getString(1));    //Field Name
        holder.btnLimitOfTimeANWAcRA.setEnabled(false);
        holder.btnLimitOfTimeANWAcRA.setText("0");//limitsOfTimeListRV.get(position).toString());
        //save change array limitsOfTimeListRV every time when we create Recycler Adaptor
        limitsOfTimeArrayRV[position] = 0;
        //pass data Limits of Time every time after add new value to layout of Recycler Adaptor
        backDataFormRAtoActInterface.passLimitsOfTimeListRA(Arrays.asList(limitsOfTimeArrayRV.clone()));
    } // onBindViewHolder

    //get the number of elements of an object (cursor)
    @Override
    public int getItemCount() { return cursor.getCount(); }

    // Create a ViewHolder class with which we get a link to each element
    // a separate list item and connect the listener for the menu click event
    public class ViewHolder extends RecyclerView.ViewHolder implements NumberPicker.OnValueChangeListener {
        final TextView tvFieldNameANWAcRA;
        final CardView cvPrjListANWAcRA;
        final LinearLayout llMainANWAcRA;
        final CheckBox cbFieldANWAcRA;
        final Button btnLimitOfTimeANWAcRA;
        BackDataFormRAtoActInterface backDataFormRAtoActInterface;

        ViewHolder(View view, BackDataFormRAtoActInterface backDataFormRAtoActInterface) {
            super(view);

            cbFieldANWAcRA = (CheckBox) view.findViewById(R.id.cbFieldANWAcRA);
            llMainANWAcRA = (LinearLayout) view.findViewById(R.id.llMainANWAcRA);
            tvFieldNameANWAcRA = (TextView) view.findViewById(R.id.tvFieldNameANWAcRA);
            cvPrjListANWAcRA = (CardView) view.findViewById(R.id.cvPrjListANWAcRA);
            btnLimitOfTimeANWAcRA = (Button) view.findViewById(R.id.btnLimitOfTimeANWAcRA);
            this.backDataFormRAtoActInterface = backDataFormRAtoActInterface;

            //handler of checkbox
            cbFieldANWAcRA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //caught position of a cursor
                    Integer recAdPosition = getAdapterPosition();
                    cursor.moveToPosition(recAdPosition);
                    //Log.e("myMSG",String.valueOf(cbFieldANWAcRA.isChecked()));
                    if(cbFieldANWAcRA.isChecked()) {
                        //EditText activated
                        btnLimitOfTimeANWAcRA.setEnabled(true);
                    } else {
                        //EditText deactivated and button text reset to zero value
                        btnLimitOfTimeANWAcRA.setEnabled(false);
                        btnLimitOfTimeANWAcRA.setText("0");//limitsOfTimeListRV.get(position).toString());
                        //change array limitsOfTimeListRV every time when we change Recycler Adaptor
                        limitsOfTimeArrayRV[recAdPosition] = 0;
                        //pass data Limits of Time every time after changing value to layout of Recycler Adaptor
                        ViewHolder.this.backDataFormRAtoActInterface.passLimitsOfTimeListRA(Arrays.asList(limitsOfTimeArrayRV.clone()));
                    }//if(cbFieldANWAcRA.isSelected())
                }//onClick
            });//cbFieldANWAcRA.setOnClickListener

            //handler of button
            btnLimitOfTimeANWAcRA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //caught position of a cursor
                    cursor.moveToPosition(getAdapterPosition());
                    //setting dialog
                    alertDialogBtnLimitOfTimeANWAcRA(view.getContext(), "Limit of time", getAdapterPosition());
                }//onClick
            });//cbFieldANWAcRA.setOnClickListener
        } //ViewHolder(View view)

        //AlertDialog with a couple of button for btnLimitOfTimeANWAcRA
        private void alertDialogBtnLimitOfTimeANWAcRA(final Context context, final String message, final Integer recAdPosition) {
            AlertDialog.Builder ad;
            String buttonString = "Set";

            ad = new AlertDialog.Builder(context);
            final NumberPicker numberPicker = new NumberPicker(ad.getContext());
            numberPicker.setMaxValue(360);
            numberPicker.setMinValue(0);
            ad.setTitle("Warning");  // Title
            ad.setMessage(message + ". Are you sure?"); // message
            ad.setIcon(R.drawable.icon_question);
            ad.setView(numberPicker);
            ad.setPositiveButton(buttonString, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
                    //set data on text and transmit to collection
                    Integer limitsOfTime = numberPicker.getValue(); // temper variable for saving limitsOfTime
                    //change array limitsOfTimeListRV every time when we change Recycler Adaptor
                    limitsOfTimeArrayRV[recAdPosition] = limitsOfTime;
                    //pass data Limits of Time every time after changing value to layout of Recycler Adaptor
                    backDataFormRAtoActInterface.passLimitsOfTimeListRA(Arrays.asList(limitsOfTimeArrayRV.clone()));
                    btnLimitOfTimeANWAcRA.setText(String.valueOf(limitsOfTime));
                }//onClick
            });
            ad.setCancelable(true);
            ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                }
            });
            ad.show();

        }//alertDialogTwoButton

        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

        }
    }//public class ViewHolder extends RecyclerView.ViewHolder

   //interface for pass data from the Recycler Adaptor to the activity which use created A adaptor.
    //https://www.youtube.com/watch?v=_rPY0s7yJ1g
   interface BackDataFormRAtoActInterface {
        void passLimitsOfTimeListRA( List<Integer> limitsOfTimeListRV);
    }//public interface BackDataFormRAtoActInterface

}//public class FieldListANWAcRecyclerAdapter
