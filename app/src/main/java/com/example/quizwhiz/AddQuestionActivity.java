package com.example.quizwhiz;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class AddQuestionActivity extends AppCompatActivity {
    private EditText editTextQuestion, editTextOption1, editTextOption2, editTextOption3, editTextAnswer;
    private Spinner spinnerDifficulty, spinnerCategory;
    private Button buttonAddQuestion;
    private QuizDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        dbHelper = QuizDbHelper.getInstance(this);

        editTextQuestion = findViewById(R.id.edit_text_question);
        editTextOption1 = findViewById(R.id.edit_text_option1);
        editTextOption2 = findViewById(R.id.edit_text_option2);
        editTextOption3 = findViewById(R.id.edit_text_option3);
        editTextAnswer = findViewById(R.id.edit_text_answer);
        spinnerDifficulty = findViewById(R.id.spinner_difficulty);
        spinnerCategory = findViewById(R.id.spinner_category);
        buttonAddQuestion = findViewById(R.id.button_add_question);

        loadDifficultyLevels();
        loadCategories();

        buttonAddQuestion.setOnClickListener(v -> addQuestionToDatabase());
    }

    private void loadDifficultyLevels() {
        String[] difficultyLevels = Question.getAllDifficultyLevels();
        ArrayAdapter<String> adapterDifficulty = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, difficultyLevels);
        adapterDifficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(adapterDifficulty);
    }

    private void loadCategories() {
        List<Category> categories = dbHelper.getAllCategories();
        ArrayAdapter<Category> adapterCategories = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapterCategories);
    }

    private void addQuestionToDatabase() {
        String questionText = editTextQuestion.getText().toString();
        String option1 = editTextOption1.getText().toString();
        String option2 = editTextOption2.getText().toString();
        String option3 = editTextOption3.getText().toString();
        int answerNr = Integer.parseInt(editTextAnswer.getText().toString());
        String difficulty = spinnerDifficulty.getSelectedItem().toString();
        Category selectedCategory = (Category) spinnerCategory.getSelectedItem();
        int categoryID = selectedCategory.getId();

        Question newQuestion = new Question(questionText, option1, option2, option3, answerNr, difficulty, categoryID);
        dbHelper.addQuestion(newQuestion);

        Toast.makeText(this, "Question Added", Toast.LENGTH_SHORT).show();
        finish();
    }
}
