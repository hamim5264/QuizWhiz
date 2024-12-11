package com.example.quizwhiz;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.quizwhiz.QuizContract.*;

import java.util.ArrayList;
import java.util.List;

public class QuizDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "QuizWhiz.db";
    private static final int DATABASE_VERSION = 1;
    private static QuizDbHelper instance;
    private SQLiteDatabase db;

    // Private constructor to enforce singleton pattern
    private QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Get the singleton instance of the database helper
    public static synchronized QuizDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new QuizDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_CATEGORIES_TABLE = "CREATE TABLE " +
                CategoriesTable.TABLE_NAME + " ( " +
                CategoriesTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CategoriesTable.COLUMN_NAME + " TEXT NOT NULL )";

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionsTable.TABLE_NAME + " ( " +
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsTable.COLUMN_QUESTION + " TEXT NOT NULL, " +
                QuestionsTable.COLUMN_OPTION1 + " TEXT NOT NULL, " +
                QuestionsTable.COLUMN_OPTION2 + " TEXT NOT NULL, " +
                QuestionsTable.COLUMN_OPTION3 + " TEXT NOT NULL, " +
                QuestionsTable.COLUMN_ANSWER_NR + " INTEGER NOT NULL, " +
                QuestionsTable.COLUMN_DIFFICULTY + " TEXT NOT NULL, " +
                QuestionsTable.COLUMN_CATEGORY_ID + " INTEGER NOT NULL, " +
                "FOREIGN KEY(" + QuestionsTable.COLUMN_CATEGORY_ID + ") REFERENCES " +
                CategoriesTable.TABLE_NAME + "(" + CategoriesTable._ID + ") ON DELETE CASCADE )";

        db.execSQL(SQL_CREATE_CATEGORIES_TABLE);
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);

        // Fill tables with initial data
        fillCategoriesTable(db);
        fillQuestionsTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CategoriesTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Fill categories table with initial data
    private void fillCategoriesTable(SQLiteDatabase db) {
        insertCategory(db, new Category("Programming"));
        insertCategory(db, new Category("Geography"));
        insertCategory(db, new Category("Math"));
    }

    private void insertCategory(SQLiteDatabase db, Category category) {
        ContentValues cv = new ContentValues();
        cv.put(CategoriesTable.COLUMN_NAME, category.getName());
        db.insert(CategoriesTable.TABLE_NAME, null, cv);
    }

    // Fill questions table with initial data
    private void fillQuestionsTable(SQLiteDatabase db) {
        insertQuestion(db, new Question("Programming, Easy: A is correct",
                "A", "B", "C", 1, Question.DIFFICULTY_EASY, 1));
        insertQuestion(db, new Question("Geography, Medium: B is correct",
                "A", "B", "C", 2, Question.DIFFICULTY_MEDIUM, 2));
        insertQuestion(db, new Question("Math, Hard: C is correct",
                "A", "B", "C", 3, Question.DIFFICULTY_HARD, 3));
    }

    private void insertQuestion(SQLiteDatabase db, Question question) {
        ContentValues cv = new ContentValues();
        cv.put(QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionsTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuestionsTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuestionsTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuestionsTable.COLUMN_ANSWER_NR, question.getAnswerNr());
        cv.put(QuestionsTable.COLUMN_DIFFICULTY, question.getDifficulty());
        cv.put(QuestionsTable.COLUMN_CATEGORY_ID, question.getCategoryID());
        db.insert(QuestionsTable.TABLE_NAME, null, cv);
    }

    // Public methods to interact with the database
    public void addCategory(Category category) {
        db = getWritableDatabase();
        insertCategory(db, category);
    }

    public void addCategories(List<Category> categories) {
        db = getWritableDatabase();
        for (Category category : categories) {
            insertCategory(db, category);
        }
    }

    public void addQuestion(Question question) {
        db = getWritableDatabase();
        insertQuestion(db, question);
    }

    public void addQuestions(List<Question> questions) {
        db = getWritableDatabase();
        for (Question question : questions) {
            insertQuestion(db, question);
        }
    }

    @SuppressLint("Range")
    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + CategoriesTable.TABLE_NAME, null);

        if (c.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(c.getInt(c.getColumnIndex(CategoriesTable._ID)));
                category.setName(c.getString(c.getColumnIndex(CategoriesTable.COLUMN_NAME)));
                categoryList.add(category);
            } while (c.moveToNext());
        }
        c.close();
        return categoryList;
    }

    @SuppressLint("Range")
    public ArrayList<Question> getQuestions(int categoryID, String difficulty) {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();

        String selection = QuestionsTable.COLUMN_CATEGORY_ID + " = ? AND " +
                QuestionsTable.COLUMN_DIFFICULTY + " = ?";
        String[] selectionArgs = {String.valueOf(categoryID), difficulty};

        Cursor c = db.query(QuestionsTable.TABLE_NAME, null, selection, selectionArgs,
                null, null, null);

        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(QuestionsTable._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                question.setDifficulty(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_DIFFICULTY)));
                question.setCategoryID(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_CATEGORY_ID)));

                questionList.add(question);
            } while (c.moveToNext());
        }
        c.close();
        return questionList;
    }

    // Method to clear all questions
    public void clearQuestions() {
        db = getWritableDatabase();
        db.delete(QuestionsTable.TABLE_NAME, null, null); // Delete all rows in the questions table
    }
}
//The Data Engineer's@hamim leon
