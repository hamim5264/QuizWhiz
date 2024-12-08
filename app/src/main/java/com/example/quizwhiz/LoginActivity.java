package com.example.quizwhiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail;
    private Button buttonSignupStudent, buttonSignupTeacher, buttonLoginStudent, buttonLoginTeacher;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonSignupStudent = findViewById(R.id.buttonSignupStudent);
        buttonSignupTeacher = findViewById(R.id.buttonSignupTeacher);
        buttonLoginStudent = findViewById(R.id.buttonLoginStudent);
        buttonLoginTeacher = findViewById(R.id.buttonLoginTeacher);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);

        // Check if a user is already logged in
        checkLoggedInUser();

        // Sign up as Student
        buttonSignupStudent.setOnClickListener(v -> saveUserData("student"));

        // Sign up as Teacher
        buttonSignupTeacher.setOnClickListener(v -> saveUserData("teacher"));

        // Log in as Student
        buttonLoginStudent.setOnClickListener(v -> loginUser("student"));

        // Log in as Teacher
        buttonLoginTeacher.setOnClickListener(v -> loginUser("teacher"));
    }

    private void saveUserData(String userType) {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill in both fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if user already exists
        String existingUserType = sharedPreferences.getString(name + ":" + email, "");
        if (!existingUserType.isEmpty()) {
            Toast.makeText(this, "This user already exists as " + existingUserType + ".", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save user data persistently
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(name + ":" + email, userType); // Store user in database
        editor.apply();

        Toast.makeText(this, "Sign-up successful!", Toast.LENGTH_SHORT).show();
        clearFields();
    }

    private void loginUser(String userType) {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill in both fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        String storedUserType = sharedPreferences.getString(name + ":" + email, "");
        if (storedUserType.equals(userType)) {
            // Save the current user details persistently
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("current_user_name", name);
            editor.putString("current_user_email", email);
            editor.putString("current_user_role", userType);
            editor.apply();

            if (userType.equals("student")) {
                startActivity(new Intent(this, StartingScreenActivity.class));
            } else if (userType.equals("teacher")) {
                startActivity(new Intent(this, TeacherDashboardActivity.class));
            }
            finish();
        } else {
            Toast.makeText(this, "Invalid credentials or user type.", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkLoggedInUser() {
        String name = sharedPreferences.getString("current_user_name", null);
        String email = sharedPreferences.getString("current_user_email", null);
        String role = sharedPreferences.getString("current_user_role", null);

        if (name != null && email != null && role != null) {
            if (role.equals("student")) {
                startActivity(new Intent(this, StartingScreenActivity.class));
            } else if (role.equals("teacher")) {
                startActivity(new Intent(this, TeacherDashboardActivity.class));
            }
            finish();
        }
    }

    private void clearFields() {
        editTextName.setText("");
        editTextEmail.setText("");
    }
}
//The Data Engineer's@hamim leon



