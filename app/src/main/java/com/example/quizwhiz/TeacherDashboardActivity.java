package com.example.quizwhiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class TeacherDashboardActivity extends AppCompatActivity {

    private Button buttonAddQuestion, buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);

        // Initialize buttons
        buttonAddQuestion = findViewById(R.id.button_add_question);
        buttonLogout = findViewById(R.id.button_logout);

        // Add question button
        buttonAddQuestion.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddQuestionActivity.class);
            startActivity(intent);
        });

        // Logout button
        buttonLogout.setOnClickListener(v -> logout());
    }

    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Clear user data
        editor.apply();

        // Navigate to LoginActivity
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
//The Data Engineer's@hamim leon all copy rights reserved.