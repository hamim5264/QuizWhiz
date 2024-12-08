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
    private Button buttonSignupStudent, buttonSignupTeacher, buttonLogin;
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
        buttonLogin = findViewById(R.id.buttonLogin);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);

        // Check if user is already logged in
        String storedName = sharedPreferences.getString("name", "");
        String storedEmail = sharedPreferences.getString("email", "");
        String userType = sharedPreferences.getString("userType", "");

        if (!storedName.isEmpty() && !storedEmail.isEmpty()) {
            autoLogin(userType);
        }

        // Sign up as Student
        buttonSignupStudent.setOnClickListener(v -> saveUserData("student"));

        // Sign up as Teacher
        buttonSignupTeacher.setOnClickListener(v -> saveUserData("teacher"));

        // Log in
        buttonLogin.setOnClickListener(v -> loginUser());
    }

    private void saveUserData(String userType) {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill in both fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.putString("email", email);
        editor.putString("userType", userType);
        editor.apply();

        autoLogin(userType);
    }

    private void autoLogin(String userType) {
        if (userType.equals("student")) {
            startActivity(new Intent(this, StartingScreenActivity.class));
        } else if (userType.equals("teacher")) {
            startActivity(new Intent(this, TeacherDashboardActivity.class));
        }
        finish();
    }

    private void loginUser() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill in both fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        String storedName = sharedPreferences.getString("name", "");
        String storedEmail = sharedPreferences.getString("email", "");
        String userType = sharedPreferences.getString("userType", "");

        if (name.equals(storedName) && email.equals(storedEmail)) {
            autoLogin(userType);
        } else {
            Toast.makeText(this, "Invalid credentials. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
//The Data Engineer's@hamim leon
