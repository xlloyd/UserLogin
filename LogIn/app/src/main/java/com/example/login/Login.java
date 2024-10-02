package com.example.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPassword;
    Button ButtonLogin;
    TextView forgotPassword;

    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textView;

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        ButtonLogin = findViewById(R.id.btn_login);
        forgotPassword = findViewById(R.id.forgotPassword); // New TextView for forgot password
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.RegisterNow);
        VideoView videoView = findViewById(R.id.backgroundLoginVideo);
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.background_loginvideo);
        videoView.setVideoURI(videoUri);

        videoView.setOnPreparedListener(mp -> {
            mp.setLooping(true);
            mp.setVolume(50, 50);
        });

        videoView.start();

        textView.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Registration.class);
            startActivity(intent);
            finish();
        });

        forgotPassword.setOnClickListener(view -> {
            String email = String.valueOf(editTextEmail.getText());
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(Login.this, "Enter your registered email", Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Login.this, "Error in sending password reset email", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        ButtonLogin.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            String email, password;
            email = String.valueOf(editTextEmail.getText());
            password = String.valueOf(editTextPassword.getText());

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(Login.this, "Enter email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(Login.this, "Enter password", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
