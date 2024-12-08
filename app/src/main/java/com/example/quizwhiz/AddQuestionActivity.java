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

        // Initialize views
        editTextQuestion = findViewById(R.id.edit_text_question);
        editTextOption1 = findViewById(R.id.edit_text_option1);
        editTextOption2 = findViewById(R.id.edit_text_option2);
        editTextOption3 = findViewById(R.id.edit_text_option3);
        editTextAnswer = findViewById(R.id.edit_text_answer);
        spinnerDifficulty = findViewById(R.id.spinner_difficulty);
        spinnerCategory = findViewById(R.id.spinner_category);
        buttonAddQuestion = findViewById(R.id.button_add_question);

        // Load data into spinners
        loadDifficultyLevels();
        loadCategories();

        // Handle add question button click
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
        String questionText = editTextQuestion.getText().toString().trim();
        String option1 = editTextOption1.getText().toString().trim();
        String option2 = editTextOption2.getText().toString().trim();
        String option3 = editTextOption3.getText().toString().trim();

        // Validate answer input
        int answerNr;
        try {
            answerNr = Integer.parseInt(editTextAnswer.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid answer number!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate inputs
        if (questionText.isEmpty() || option1.isEmpty() || option2.isEmpty() || option3.isEmpty() || answerNr < 1 || answerNr > 3) {
            Toast.makeText(this, "Please fill in all fields correctly!", Toast.LENGTH_SHORT).show();
            return;
        }

        String difficulty = spinnerDifficulty.getSelectedItem().toString();
        Category selectedCategory = (Category) spinnerCategory.getSelectedItem();
        int categoryID = selectedCategory.getId();

        // Create new question and save to the database
        Question newQuestion = new Question(questionText, option1, option2, option3, answerNr, difficulty, categoryID);
        dbHelper.addQuestion(newQuestion);

        // Show success message and clear fields
        Toast.makeText(this, "Question added successfully!", Toast.LENGTH_SHORT).show();
        clearFields();
    }

    private void clearFields() {
        // Clear input fields for the next question
        editTextQuestion.setText("");
        editTextOption1.setText("");
        editTextOption2.setText("");
        editTextOption3.setText("");
        editTextAnswer.setText("");
        spinnerDifficulty.setSelection(0);
        spinnerCategory.setSelection(0);
    }
}
