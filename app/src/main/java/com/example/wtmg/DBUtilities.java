package com.example.wtmg;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

//TODO: adapting database for current program
//class with utilities for working with database
public class DBUtilities {

    private DBHelper dbHelper;
    private Context context;
    private SQLiteDatabase db;

    public String AllPrjQuery = "SELECT prj.id, prj.prj_name, prj.prj_time_limit, " +
            "prj.prj_waste_time, prj.company, prj.support_man " +
            "FROM prj ORDER BY prj.prj_name";


    /*public String mainQuery = "SELECT hebrew.id, hebrew.word_he, transcriptions.word_tr, " +
            "semantic.name, meanings.option, gender.option, quantity.option FROM hebrew " +
            "INNER JOIN transcriptions ON transcriptions.id = hebrew.transcription_id " +
            "INNER JOIN semantic ON semantic.id = hebrew.semantic_id " +
            "INNER JOIN meanings ON meanings.id = hebrew.meaning_id " +
            "INNER JOIN gender ON gender.id = hebrew.gender_id " +
            "INNER JOIN quantity ON quantity.id = hebrew.quantity_id ORDER BY hebrew.word_he";

     */

    //constructor
    public DBUtilities(Context context) {
        this.context = context;

        // creating supporting class
        dbHelper = new DBHelper( this.context);
        dbHelper.create_db();
    } // DBUtilities

    //добавить строку в таблицу
    // соответственно параметры(cv - данные, table - таблица)
    public void insertInto(ContentValues cv, String table){
        db.insert(table, null,  cv);
    }//insertInto

    //добавить строку в таблицу russian
    public void insertIntoRussians(String ruWord){
        ContentValues cv = new ContentValues();
        cv.put("word_ru", ruWord);
        //добваить данные через объект ContentValues(cv), в таблицу
        insertInto(cv, "russian");
    }//insertIntoRussians

    //добавить строку в таблицу translations
    public void insertIntoTranslations(int hebrewId, int russianId){
        ContentValues cv = new ContentValues();
        cv.put("hebrew_id", hebrewId);
        cv.put("russian_id", russianId);
        //добваить данные через объект ContentValues(cv), в таблицу
        insertInto(cv, "translations");
    }//insertIntoTranslations

    //добавить строку в таблицу semantic
    public void insertIntoSemantic(String name){
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        //добваить данные через объект ContentValues(cv), в таблицу
        insertInto(cv, "semantic");
    }//insertIntoSemantic

    //добавить строку в таблицу profile
    public void insertIntoProfile(String login, String password, int idWordLastLearn,
                                  int wordCountInLoop, int countLoopsReserved){
        ContentValues cv = new ContentValues();
        cv.put("login", login);
        cv.put("password", password);
        cv.put("id_word_last_learn", idWordLastLearn);
        cv.put("word_count_in_loop", wordCountInLoop);
        cv.put("count_loops_reserved", countLoopsReserved);
        //добваить данные через объект ContentValues(cv), в таблицу
        insertInto(cv, "profile");
    }//insertIntoProfile

    //добавить строку в таблицу hebrew
    public void insertIntoHebrew(String heWord, int transcId,
                                 int meaningId, int genderId,
                                 int quantityId, int semanticId){
        ContentValues cv = new ContentValues();
        cv.put("word_he", heWord);
        cv.put("transcription_id", transcId);
        cv.put("meaning_id", meaningId);
        cv.put("gender_id", genderId);
        cv.put("quantity_id", quantityId);
        cv.put("semantic_id", semanticId);
        //добваить данные через объект ContentValues(cv), в таблицу
        insertInto(cv, "hebrew");
    }//insertIntoHebrew

    //добавить строку в таблицу transcriptions
    public void insertIntoTranscriptions(String transc){
        ContentValues cv = new ContentValues();
        cv.put("word_tr", transc);
        //добваить данные через объект ContentValues(cv), в таблицу
        insertInto(cv, "transcriptions");
    }//insertIntoTranscriptions

    //получаем количество записей в таблице
    public int getCountTable(String tableName){
        String query = "SELECT " + tableName +".id FROM " + tableName;
        Cursor cursor = getDb().rawQuery(query, null);
        return cursor.getCount();
    }
    //поиск user._id по user.login
    public int findIdbyLogin(String login){
        String query = "SELECT user._id FROM user WHERE user.login = \"" + login + "\"";
        Cursor cursor = db.rawQuery(query, null);
        // переходим в курсоре в нулевую позицию
        cursor.moveToPosition(0);
        return cursor.getInt(0);
    }//insertInto

    //заполнить коллекцию(List) данные для отображения в Spinner
    public List<String> fillList(String query) {
        List<String> list = new ArrayList<>();

        // получаем данные из БД в виде курсора
        Cursor cursor = db.rawQuery(query, null);
        //количество строк в курсоре
        int n = cursor.getCount();
        for (int i = 0; i < n; i++) {
            // переходим в курсоре на текущую позицию
            cursor.moveToPosition(i);
            list.add(cursor.getString(0));
        }//for
        return list;
    }//fillList

    //обновить запись в таблице по id записи
    public int updTable(String tableName, ContentValues cv, String id){
        int updCount = db.update(tableName, cv, "id = ?",
                new String[] { id });
        return updCount;
    }

    //обновить запись в таблице semantic по id записи
    public int updTableSemantic(String id, String name){
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        return db.update("semantic", cv, "id = ?",
                new String[] { id });
    }//updTableTranscriptions

    //обновить запись в таблице transcriptions по id записи
    public int updTableTranscriptions(String id, String transcWord){
        ContentValues cv = new ContentValues();
        cv.put("word_tr", transcWord);
        return db.update("transcriptions", cv, "id = ?",
                new String[] { id });
    }//updTableTranscriptions

    //обновить запись в таблице russian по id записи
    public int updTableRussians(String id, String ruWord){
        ContentValues cv = new ContentValues();
        cv.put("word_ru", ruWord);
        return db.update("russian", cv, "id = ?",
                new String[] { id });
    }//updTableRussians

    //обновить запись в таблице hebrew по id записи
    public int updTableHebrew(String id, String heWord, int transcId,
                              int meaningId, int genderId,
                              int quantityId, int semanticId){
        ContentValues cv = new ContentValues();
        cv.put("word_he", heWord);
        cv.put("transcription_id", transcId);
        cv.put("meaning_id", meaningId);
        cv.put("gender_id", genderId);
        cv.put("quantity_id", quantityId);
        cv.put("semantic_id", semanticId);
        return db.update("hebrew", cv, "id = ?",
                new String[] { id });
    }//updTableHebrew

    //обновить запись в таблице profile по id записи
    public int updTableProfile(String id, int idWordLastLearn,
                               int wordCountInLoop, int countLoopsReserved){
        ContentValues cv = new ContentValues();
        cv.put("id_word_last_learn", idWordLastLearn);
        cv.put("word_count_in_loop", wordCountInLoop);
        cv.put("count_loops_reserved", countLoopsReserved);
        return db.update("profile", cv, "id = ?",
                new String[] { id });
    }//updTableProfile

    public SQLiteDatabase getDb() {
        return db;
    }

    // open connection to database
    public void open(){
        db = dbHelper.open();
    } // open

    // удаляем столбец по id из таблицы по названию
    public void removeColumnById(String id, String tableName){

        //удаляем элемент
        db.delete(tableName,"id = " + id, null);
    } // remove

    // close connection to database
    public void close(){
        if(dbHelper != null) dbHelper.close();
    } // close

}//DBUtilities
