package com.example.quizwhiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText editTextName, editTextEmail;
    Button buttonLogin;
    SharedPreferences sharedPreferences;
    boolean isLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonLogin = findViewById(R.id.buttonLogin);

        sharedPreferences = getSharedPreferences("login_pref", MODE_PRIVATE);
        isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false);

        // Check if the user is already logged in
        if (isLoggedIn) {
            // Redirect to the main activity if the user is already logged in
            Intent intent = new Intent(LoginActivity.this, StartingScreenActivity.class);
            startActivity(intent);
            finish();
        }

        // Set up login button click listener
        buttonLogin.setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            String email = editTextEmail.getText().toString();

            if (!name.isEmpty() && !email.isEmpty()) {
                // Save login state in SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("is_logged_in", true);
                editor.putString("user_name", name);
                editor.putString("user_email", email);
                editor.apply();

                // Redirect to the starting screen
                Intent intent = new Intent(LoginActivity.this, StartingScreenActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Please enter both name and email", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
