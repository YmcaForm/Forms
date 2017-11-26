package com.ymcaforms.ymcaforms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "contactsManager";

    // Contacts table name
    private static final String TABLE_QUESTIONS = "questions";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_QUESTION = "question";
    private static final String KEY_TYPE = "type";
    private static final String KEY_COUNT ="count";
    private static final String KEY_OPTIONS = "options";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_QUESTIONS_TABLE = "CREATE TABLE " + TABLE_QUESTIONS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_QUESTION + " TEXT,"
                + KEY_TYPE + " TEXT," + KEY_COUNT + " INTEGER," + KEY_OPTIONS + " TEXT" + ")";
        db.execSQL(CREATE_QUESTIONS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    void addQuestion(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_QUESTION, question.getQuestion()); // Contact Name
        values.put(KEY_TYPE, question.get_type());
        values.put(KEY_COUNT, question.getCount());
        values.put(KEY_OPTIONS, question.getOptions());// Contact Phone

        // Inserting Row
        db.insert(TABLE_QUESTIONS, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    // Getting single contact
    Question getQuestion(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_QUESTIONS, new String[] { KEY_ID,
                        KEY_QUESTION,KEY_TYPE,KEY_COUNT,KEY_OPTIONS }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Question question = new Question();
        question.setID(Integer.parseInt(cursor.getString(0)));
        question.setQuestion(cursor.getString(1));
        question.set_type(cursor.getString(2));
        question.setCount(Integer.parseInt(cursor.getString(3)));
        question.setOptions(cursor.getString(4));

        // return contact
        return question;
    }

    // Getting All Contacts
    public List<Question> getAllQuestion() {
        List<Question> questionList = new ArrayList<Question>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_QUESTIONS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                Question question = new Question();
                question.setID(Integer.parseInt(cursor.getString(0)));
                question.setQuestion(cursor.getString(1));
                question.set_type(cursor.getString(2));
                question.setCount(Integer.parseInt(cursor.getString(3)));
                question.setOptions(cursor.getString(4));

                // Adding contact to list
                questionList.add(question);
            } while (cursor.moveToNext());
        }

        // return contact list
        return questionList;
    }

    // Updating single contact
    public int updateQuestion(Question question,int mQuestionId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_QUESTION, question.getQuestion());
        values.put(KEY_TYPE, question.get_type());
        values.put(KEY_COUNT,question.getCount());
        values.put(KEY_OPTIONS,question.getOptions());

        // updating row
        return db.update(TABLE_QUESTIONS, values, KEY_ID + " = ?",
                new String[] {String.valueOf(mQuestionId)});
    }

    // Deleting single contact
    public void deleteQuestion(int mQuestionId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_QUESTIONS, KEY_ID + " = ?",
                new String[] { String.valueOf(mQuestionId) });
        db.close();
    }

    // Getting contacts Count
    public int getQuestionCount() {
        String countQuery = "SELECT  * FROM " + TABLE_QUESTIONS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public void delete(Context applicationContext) {

        applicationContext.deleteDatabase(DATABASE_NAME);
    }
}