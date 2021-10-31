package com.example.wtmg;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//TODO:adapting database for current program -- compete
//Supporting class for creating and opening DataBase
public class DBHelper extends SQLiteOpenHelper {
    private static String DB_PATH;                  // Full path to database
    private static String DB_NAME = "WTM.db";
    private static final int DB_VERSION = 1;        // version of the database

    private Context myContext;

    DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.myContext = context;

        // get path to database
        DB_PATH = context.getFilesDir().getPath() + "/" +DB_NAME;
    } // DBLocalHelper

    @Override
    public void onCreate(SQLiteDatabase db) { }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) { }

    // creating database - copying to the databases
    void create_db(){
        InputStream myInput = null;
        OutputStream myOutput = null;

        try {
            File file = new File(DB_PATH);
            if (!file.exists()) {
                this.getReadableDatabase();

                //get local database like stream
                myInput = myContext.getAssets().open(DB_NAME);
                // path to database
                String outFileName = DB_PATH;

                // open empty database
                myOutput = new FileOutputStream(outFileName);

                // copy the data byte by byte
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }

                myOutput.flush();  // finishing writing, flushing buffers to device
                myOutput.close();
                myInput.close();
            } // if
        } catch(IOException ex){
            Log.d("DBHelper", ex.getMessage());
        } // try-catch
    } // create_db

    public SQLiteDatabase open()throws SQLException {
        return SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
    }//open
}//DBHelper

