package com.example.wtmg;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

//Recycler Adaptor for list in Add New Work Activity
public class FieldListANWAcRecyclerAdapter extends RecyclerView.Adapter<FieldListANWAcRecyclerAdapter.ViewHolder>{


    Context context;

    //fields of class PrjListRecyclerAdapter
    private LayoutInflater inflater;
    private Cursor cursor;
    private String idField; // value for transfer to next level

    //List<Works> worksList = new ArrayList<>(); //collection of Works objects
    DBUtilities dbUtilities;//link to DBUtilities class


    //constructor
    public FieldListANWAcRecyclerAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        //handler tap on RecyclerAdapter
        this.context = context;
        dbUtilities = new DBUtilities(context);
        dbUtilities.open();

        String mainQuery = "SELECT fields.id, fields.field_name FROM fields";;
        cursor = dbUtilities.getDb().rawQuery(mainQuery, null);

    } // FieldListANWAcRecyclerAdapter

    //create a new markup (View) by specifying the markup
    @Override
    public FieldListANWAcRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.field_list_anw_recycler_adapter, parent, false);
        return new FieldListANWAcRecyclerAdapter.ViewHolder(view);
    }

    //bind markup elements to object variables (in this case, to the cursor)
    @Override
    public void onBindViewHolder(FieldListANWAcRecyclerAdapter.ViewHolder holder, int position) {
        //move cursor on current position
        cursor.moveToPosition(position);

        //get data
        idField = cursor.getString(0); //caught current idField
        holder.tvFieldNameANWAcRA.setText(cursor.getString(1));    //Field Name
        holder.etLimitOfTimeANWAcRA.setEnabled(false);
    } // onBindViewHolder

    //get the number of elements of an object (cursor)
    @Override
    public int getItemCount() { return cursor.getCount(); }

    // Create a ViewHolder class with which we get a link to each element
    // a separate list item and connect the listener for the menu click event
    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvFieldNameANWAcRA;
        final CardView cvPrjListANWAcRA;
        final LinearLayout llMainANWAcRA;
        final CheckBox cbFieldANWAcRA;
        final EditText etLimitOfTimeANWAcRA;

        ViewHolder(View view) {
            super(view);

            cbFieldANWAcRA = (CheckBox) view.findViewById(R.id.cbFieldANWAcRA);
            llMainANWAcRA = (LinearLayout) view.findViewById(R.id.llMainANWAcRA);
            tvFieldNameANWAcRA = (TextView) view.findViewById(R.id.tvFieldNameANWAcRA);
            cvPrjListANWAcRA = (CardView) view.findViewById(R.id.cvPrjListANWAcRA);
            etLimitOfTimeANWAcRA = (EditText) view.findViewById(R.id.etLimitOfTimeANWAcRA);

            //handler of checkbox
            cbFieldANWAcRA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //caught position of a cursor
                    cursor.moveToPosition(getAdapterPosition());
                    //EditText activated
                    etLimitOfTimeANWAcRA.setEnabled(true);
                }//onClick
            });//cbFieldANWAcRA.setOnClickListener
        } //ViewHolder(View view)

    }//public class ViewHolder extends RecyclerView.ViewHolder

}//public class FieldListANWAcRecyclerAdapter
