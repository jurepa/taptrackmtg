package com.tcg.taptrackmtg;

import static android.content.ContentValues.TAG;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private TextView loginRegisterSwitch;

    private TextInputLayout confirmPasswordLayout;

    private TextInputEditText confirmPasswordInput;

    private TextInputEditText passwordInput;

    private TextInputLayout emailLayout;

    private TextInputEditText emailInput;

    private Button submitButton;

    private TextView textErrorMessage;

    private ProgressBar progressBar;

    private TextInputLayout passwordLayout;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.loginlayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        loginRegisterSwitch = findViewById(R.id.textSwitchForm);
        submitButton = findViewById(R.id.buttonSubmit);
        confirmPasswordLayout = findViewById(R.id.layoutConfirmPassword);
        emailLayout = findViewById(R.id.emailLayout);
        emailInput = findViewById(R.id.editTextEmail);
        confirmPasswordInput = findViewById(R.id.editTextConfirmPassword);
        passwordInput = findViewById(R.id.editTextPassword);
        textErrorMessage = findViewById(R.id.textErrorMessage);
        progressBar = findViewById(R.id.loadingProgressBar);
        passwordLayout = findViewById(R.id.passwordLayout);

        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    emailLayout.setError("Incorrect format");
                    submitButton.setEnabled(false);
                } else {
                    submitButton.setEnabled(true);
                    emailLayout.setError(null);
                }
            }
            @Override public void afterTextChanged(Editable s) {}
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        });

        confirmPasswordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = passwordInput.getText().toString();
                if (!s.toString().equals(password)) {
                    confirmPasswordLayout.setError("Passwords aren't equal");
                    if(submitButton.getText().equals("Register")){
                        submitButton.setEnabled(false);
                    }
                } else {
                    submitButton.setEnabled(true);
                    confirmPasswordLayout.setError(null);
                }
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });

        loginRegisterSwitch.setOnClickListener(v -> {
            if(submitButton.getText().equals("Login")){
                loginRegisterSwitch.setText(R.string.already_have_an_account_log_in);
                confirmPasswordLayout.setVisibility(VISIBLE);
                submitButton.setText("Register");
            }else{
                loginRegisterSwitch.setText(R.string.don_t_have_an_account_register);
                confirmPasswordLayout.setVisibility(GONE);
                submitButton.setText("Login");
            }
        });

        submitButton.setOnClickListener(v -> {
            String password = passwordInput.getText().toString();
            if(password.isBlank()){
                passwordLayout.setError("Password is empty");
            }else{
                passwordLayout.setError(null);
            }
            String email = emailInput.getText().toString();
            if(email.isBlank()){
                emailLayout.setError("Email is empty");
            }else {
                emailLayout.setError(null);
            }
            if(passwordLayout.getError() == null && emailLayout.getError() == null) {
                progressBar.setVisibility(VISIBLE);
                submitButton.setEnabled(false);
                textErrorMessage.setVisibility(GONE);
                if (submitButton.getText().equals("Login")) {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.putExtra("user", mAuth.getCurrentUser());
                                        LoginActivity.this.startActivity(intent);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        textErrorMessage.setText(task.getException().getMessage());
                                        textErrorMessage.setVisibility(VISIBLE);
                                    }
                                    progressBar.setVisibility(View.GONE);
                                    submitButton.setEnabled(true);
                                }
                            });
                } else {

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "createUserWithEmail:success");

                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.putExtra("user", mAuth.getCurrentUser());
                                        LoginActivity.this.startActivity(intent);
                                    } else {
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        textErrorMessage.setText(task.getException().getMessage());
                                        textErrorMessage.setVisibility(VISIBLE);
                                    }
                                    progressBar.setVisibility(View.GONE);
                                    submitButton.setEnabled(true);
                                }
                            });
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("user", mAuth.getCurrentUser());
            LoginActivity.this.startActivity(intent);        }
    }
}