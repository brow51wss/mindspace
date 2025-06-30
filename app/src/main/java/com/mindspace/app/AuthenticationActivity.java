package com.mindspace.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * AuthenticationActivity - Handles user registration and login
 * Secure Firebase Authentication for MindSpace mental health data
 */
public class AuthenticationActivity extends AppCompatActivity {
    private static final String TAG = "Authentication";
    
    private EditText emailEditText, passwordEditText;
    private Button signInButton, registerButton;
    private TextView switchModeText, titleText;
    private ProgressBar progressBar;
    
    private FirebaseAuth firebaseAuth;
    private boolean isLoginMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        
        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
        
        // Check if user is already signed in
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            // User is already signed in, go to main activity
            navigateToMainActivity();
            return;
        }
        
        initializeViews();
        setupClickListeners();
        
        Log.d(TAG, "Authentication Activity initialized");
    }
    
    private void initializeViews() {
        titleText = findViewById(R.id.titleText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signInButton = findViewById(R.id.signInButton);
        registerButton = findViewById(R.id.registerButton);
        switchModeText = findViewById(R.id.switchModeText);
        progressBar = findViewById(R.id.progressBar);
        
        updateUI();
    }
    
    private void setupClickListeners() {
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoginMode) {
                    signInUser();
                } else {
                    registerUser();
                }
            }
        });
        
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoginMode) {
                    registerUser();
                } else {
                    signInUser();
                }
            }
        });
        
        switchModeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLoginMode = !isLoginMode;
                updateUI();
            }
        });
    }
    
    private void updateUI() {
        if (isLoginMode) {
            titleText.setText("Welcome Back to MindSpace");
            signInButton.setText("Sign In");
            registerButton.setText("Create Account");
            switchModeText.setText("New to MindSpace? Create an account");
        } else {
            titleText.setText("Join MindSpace");
            signInButton.setText("Create Account");
            registerButton.setText("Sign In");
            switchModeText.setText("Already have an account? Sign in");
        }
    }
    
    private void signInUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        
        if (!validateInput(email, password)) {
            return;
        }
        
        showProgress(true);
        
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        showProgress(false);
                        
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Toast.makeText(AuthenticationActivity.this, 
                                "Welcome back to MindSpace!", Toast.LENGTH_SHORT).show();
                            navigateToMainActivity();
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(AuthenticationActivity.this, 
                                "Authentication failed: " + task.getException().getMessage(), 
                                Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    
    private void registerUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        
        if (!validateInput(email, password)) {
            return;
        }
        
        showProgress(true);
        
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        showProgress(false);
                        
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Toast.makeText(AuthenticationActivity.this, 
                                "Welcome to MindSpace! Your mental health journey begins now.", 
                                Toast.LENGTH_LONG).show();
                            navigateToMainActivity();
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(AuthenticationActivity.this, 
                                "Registration failed: " + task.getException().getMessage(), 
                                Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    
    private boolean validateInput(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return false;
        }
        
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please enter a valid email");
            emailEditText.requestFocus();
            return false;
        }
        
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return false;
        }
        
        if (password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            passwordEditText.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        signInButton.setEnabled(!show);
        registerButton.setEnabled(!show);
    }
    
    private void navigateToMainActivity() {
        Intent intent = new Intent(AuthenticationActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
} 