package com.example.quizwhiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class StartingScreenActivity extends AppCompatActivity {

    public static final String EXTRA_CATEGORY_ID = "categoryID";
    public static final String EXTRA_CATEGORY_NAME = "categoryName";
    public static final String EXTRA_DIFFICULTY = "difficulty";

    private Spinner spinnerCategory, spinnerDifficulty;
    private Button buttonStartQuiz, buttonLogout;
    private TextView textViewHighScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        spinnerCategory = findViewById(R.id.spinner_category);
        spinnerDifficulty = findViewById(R.id.spinner_difficulty);
        buttonStartQuiz = findViewById(R.id.button_start_quiz);
        Button buttonLogout = findViewById(R.id.button_log_out);
        textViewHighScore = findViewById(R.id.text_view_highScore);

        loadCategories();
        loadDifficultyLevels();
        loadScores(); // Load scores on start

        buttonStartQuiz.setOnClickListener(v -> startQuiz());
        buttonLogout.setOnClickListener(v -> logout());
    }

    private void loadCategories() {
        List<Category> categories = QuizDbHelper.getInstance(this).getAllCategories();
        if (categories.isEmpty()) {
            Toast.makeText(this, "No categories available. Please contact your teacher.", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayAdapter<Category> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }

    private void loadDifficultyLevels() {
        String[] difficulties = Question.getAllDifficultyLevels();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, difficulties);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(adapter);
    }

    private void loadScores() {
        SharedPreferences sharedPreferences = getSharedPreferences("quiz_data", MODE_PRIVATE);

        // Retrieve scores
        int highScore = sharedPreferences.getInt("high_score", 0);
        int latestScore = sharedPreferences.getInt("latest_score", 0);

        // Update the TextView
        textViewHighScore.setText("Latest Score: " + latestScore + " | HighScore: " + highScore);
    }

    private void startQuiz() {
        Category category = (Category) spinnerCategory.getSelectedItem();
        String difficulty = spinnerDifficulty.getSelectedItem().toString();

        if (category == null || difficulty.isEmpty()) {
            Toast.makeText(this, "Please select a category and difficulty level!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, QuizActivity.class);
        intent.putExtra(EXTRA_CATEGORY_ID, category.getId());
        intent.putExtra(EXTRA_CATEGORY_NAME, category.getName());
        intent.putExtra(EXTRA_DIFFICULTY, difficulty);
        startActivityForResult(intent, 1); // Use startActivityForResult
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadScores(); // Reload scores after finishing the quiz
        }
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
