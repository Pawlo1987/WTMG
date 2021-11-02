package com.example.wtmg;


import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PrjListCSOWAcRecyclerAdapter extends RecyclerView.Adapter<PrjListCSOWAcRecyclerAdapter.ViewHolder>{

    Context context;

    //fields of class PrjListRecyclerAdapter
    private LayoutInflater inflater;
    private Cursor cursor;
    private String idPrj; // value for transfer to next level

    //List<Works> worksList = new ArrayList<>(); //collection of Works objects
    DBUtilities dbUtilities;//link to DBUtilities class

    String idAuthUser;         //userId

    //constructor
    public PrjListCSOWAcRecyclerAdapter(Context context, String idAuthUser) {
        this.inflater = LayoutInflater.from(context);
        //handler tap on RecyclerAdapter
        this.context = context;
        this.idAuthUser = idAuthUser;
        dbUtilities = new DBUtilities(context);
        dbUtilities.open();

        String mainQuery = "SELECT prj.id, prj.prj_name, prj.prj_time_limit, prj.prj_waste_time, " +
                "prj.company, prj.contact_person FROM prj ORDER BY prj.prj_name ";
        cursor = dbUtilities.getDb().rawQuery(mainQuery, null);

    } // PrjListRecyclerAdapter

    //create a new markup (View) by specifying the markup
    @Override
    public PrjListCSOWAcRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.prj_list_recycler_adapter, parent, false);
        return new ViewHolder(view);
    }

    //bind markup elements to object variables (in this case, to the cursor)
    @Override
    public void onBindViewHolder(PrjListCSOWAcRecyclerAdapter.ViewHolder holder, int position) {
        //move cursor on current position
        cursor.moveToPosition(position);

        //get data
        idPrj = cursor.getString(0); //caught current prjId
        holder.tvPrjNameCSOWAcRA.setText(cursor.getString(1));    //Project Name
        holder.tvCompNameCSOWAcRA.setText(cursor.getString(4));   //Company name
        holder.tvPrjTimeLimitCSOWAcRA.setText(cursor.getString(2));  //timeLimit
        holder.tvPrjWasteTimeCSOWAcRA.setText(cursor.getString(3));    //wasteTime
    } // onBindViewHolder

    //get the number of elements of an object (cursor)
    @Override
    public int getItemCount() { return cursor.getCount(); }

    // Create a ViewHolder class with which we get a link to each element
    // a separate list item and connect the listener for the menu click event
    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvPrjNameCSOWAcRA, tvCompNameCSOWAcRA, tvPrjTimeLimitCSOWAcRA, tvPrjWasteTimeCSOWAcRA;
        final CardView cvPrjListCSOWAcRA;
        final LinearLayout llMainCSOWAcRA;
        final Button btnEditPrjCSOWAcRA, btnStartPrjCSOWAcRA, btnStopPrjCSOWAcRA;

        ViewHolder(View view) {
            super(view);

            llMainCSOWAcRA = (LinearLayout) view.findViewById(R.id.llMainCSOWAcRA);
            tvPrjNameCSOWAcRA = (TextView) view.findViewById(R.id.tvPrjNameCSOWAcRA);
            tvCompNameCSOWAcRA = (TextView) view.findViewById(R.id.tvCompNameCSOWAcRA);
            tvPrjTimeLimitCSOWAcRA = (TextView) view.findViewById(R.id.tvPrjTimeLimitCSOWAcRA);
            tvPrjWasteTimeCSOWAcRA = (TextView) view.findViewById(R.id.tvPrjWasteTimeCSOWAcRA);
            cvPrjListCSOWAcRA = (CardView) view.findViewById(R.id.cvPrjListCSOWAcRA);
            btnEditPrjCSOWAcRA = (Button) view.findViewById(R.id.btnEditPrjCSOWAcRA);
            btnStartPrjCSOWAcRA = (Button) view.findViewById(R.id.btnStartPrjCSOWAcRA);
            btnStopPrjCSOWAcRA = (Button) view.findViewById(R.id.btnStopPrjCSOWAcRA);

            //handler of Edit BUTTON
            btnEditPrjCSOWAcRA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //caught position of a cursor
                    cursor.moveToPosition(getAdapterPosition());
                    //approval dialog
                    alertDialogTwoButton(view.getContext(), "EDIT", idPrj);
                }//onClick
            });//btnEditVDRA.setOnClickListener

            //handler of Start BUTTON
            btnStartPrjCSOWAcRA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //caught position of a cursor
                    cursor.moveToPosition(getAdapterPosition());
                    //approval dialog
                    alertDialogTwoButton(view.getContext(), "START", idPrj);
                }//onClick
            });//btnStartPrjCSOWAcRA.setOnClickListener

            //handler of Stop BUTTON
            btnStopPrjCSOWAcRA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //caught position of a cursor
                    cursor.moveToPosition(getAdapterPosition());
                    //approval dialog
                    alertDialogTwoButton(view.getContext(), "STOP", idPrj);
                }//onClick
            });//btnStopPrjCSOWAcRA.setOnClickListener
        } //ViewHolder(View view)

        //AlertDialog with a couple of button
        private void alertDialogTwoButton(final Context context, final String message, final String idPrj) {
            AlertDialog.Builder ad;
            String button1String = "OK";
            String button2String = "Cancel";

            ad = new AlertDialog.Builder(context);
            ad.setTitle("Warning");  // Title
            ad.setMessage(message + ". Are you sure?"); // message
            ad.setIcon(R.drawable.icon_question);
            ad.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
                    //Point of checking which function called the AlertDialog
                    if (message.equals("EDIT")) {
                        //Intent intent = new Intent(context, EditWordActivity.class);
                        //intent.putExtra("idPrj", idPrj);
                        //context.startActivity(intent);
                    } else if (message.equals("START")){
                        //Intent intent = new Intent(context, EditWordActivity.class);
                        //intent.putExtra("idPrj", idPrj);
                        //context.startActivity(intent);
                    } else if (message.equals("STOP")){
                        //Intent intent = new Intent(context, EditWordActivity.class);
                        //intent.putExtra("idPrj", idPrj);
                        //context.startActivity(intent);
                    }//if-else
                }//onClick
            });
            ad.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
                }
            });
            ad.setCancelable(true);
            ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                }
            });
            ad.show();

        }//alertDialogTwoButton

    }//public class ViewHolder extends RecyclerView.ViewHolder

}//public class PrjListCSOWAcRARecyclerAdapter