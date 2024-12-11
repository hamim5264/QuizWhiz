package com.example.quizwhiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TeacherDashboardActivity extends AppCompatActivity {

    private Button buttonAddQuestion, buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);

        // Initialize buttons
        buttonAddQuestion = findViewById(R.id.button_add_question);
        Button buttonLogout = findViewById(R.id.button_logout);
        Button buttonClearQuestions = findViewById(R.id.button_clear_questions); // New button

        // Add question button
        buttonAddQuestion.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddQuestionActivity.class);
            startActivity(intent);
        });

        // Clear questions button
        buttonClearQuestions.setOnClickListener(v -> clearAllQuestions());

        // Logout button
        buttonLogout.setOnClickListener(v -> logout());
    }

    // Method to clear all questions
    private void clearAllQuestions() {
        QuizDbHelper dbHelper = QuizDbHelper.getInstance(this);
        dbHelper.clearQuestions(); // Call the method to clear questions from the database
        Toast.makeText(this, "All questions have been removed!", Toast.LENGTH_SHORT).show();
    }

    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("current_user_name");
        editor.remove("current_user_email");
        editor.remove("current_user_role");
        editor.apply();

        // Navigate back to LoginActivity
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
//The Data Engineer's@hamim leon