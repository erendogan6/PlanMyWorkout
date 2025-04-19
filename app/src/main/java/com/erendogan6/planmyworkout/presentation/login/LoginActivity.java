package com.erendogan6.planmyworkout.presentation.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.erendogan6.planmyworkout.R;
import com.erendogan6.planmyworkout.domain.model.Result;
import com.erendogan6.planmyworkout.presentation.onboarding.OnboardingActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Activity for user login.
 * This class is part of the presentation layer and handles the UI for login operations.
 */
@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {

    private LoginViewModel viewModel;

    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;
    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private MaterialButton btnLogin;
    private View progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Initialize views
        initViews();

        // Set up click listeners
        setupClickListeners();

        // Observe LiveData
        observeViewModel();
    }

    private void observeViewModel() {
        viewModel.getLoginResult().observe(this, result -> {
            if (result != null) {
                if (result.isLoading()) {
                    showProgress(true);
                } else if (result.isSuccess()) {
                    showProgress(false);
                    navigateToOnboarding();
                } else if (result.isError()) {
                    showProgress(false);
                    showLoginError(result.getMessage());
                }
            }
        });

        viewModel.getResetPasswordResult().observe(this, result -> {
            if (result != null) {
                showProgress(false);
                if (result.isSuccess()) {
                    Toast.makeText(this, R.string.success_reset_password, Toast.LENGTH_LONG).show();
                } else if (result.isError()) {
                    Toast.makeText(this, R.string.error_reset_password, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initViews() {
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar);

        // Set up click listeners for other views
        findViewById(R.id.tvForgotPassword).setOnClickListener(v -> showForgotPasswordDialog());
        findViewById(R.id.tvSignUp).setOnClickListener(v -> navigateToSignUp());
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(v -> attemptLogin());
    }

    private void attemptLogin() {
        // Reset errors
        tilEmail.setError(null);
        tilPassword.setError(null);

        // Get values
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();

        // Validate input
        boolean cancel = false;
        View focusView = null;

        // Check for a valid password
        if (TextUtils.isEmpty(password)) {
            tilPassword.setError(getString(R.string.error_invalid_password));
            focusView = etPassword;
            cancel = true;
        } else if (!viewModel.isValidPassword(password)) {
            tilPassword.setError(getString(R.string.error_invalid_password));
            focusView = etPassword;
            cancel = true;
        }

        // Check for a valid email address
        if (TextUtils.isEmpty(email)) {
            tilEmail.setError(getString(R.string.error_invalid_email));
            focusView = etEmail;
            cancel = true;
        } else if (!viewModel.isValidEmail(email)) {
            tilEmail.setError(getString(R.string.error_invalid_email));
            focusView = etEmail;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first form field with an error
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to perform the user login attempt
            showProgress(true);

            // Attempt login
            viewModel.login(email, password);
        }
    }

    private void showForgotPasswordDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot_password, null);
        TextInputEditText etDialogEmail = dialogView.findViewById(R.id.etDialogEmail);

        new MaterialAlertDialogBuilder(this)
            .setTitle(R.string.forgot_password)
            .setView(dialogView)
            .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                String email = etDialogEmail.getText().toString().trim();
                if (viewModel.isValidEmail(email)) {
                    resetPassword(email);
                } else {
                    Toast.makeText(this, R.string.error_invalid_email, Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton(android.R.string.cancel, null)
            .show();
    }

    private void resetPassword(String email) {
        showProgress(true);
        viewModel.resetPassword(email);
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!show);
    }

    private void showLoginError(String message) {
        Toast.makeText(this, message != null ? message : getString(R.string.error_login_failed), Toast.LENGTH_LONG).show();
    }

    private void navigateToOnboarding() {
        // Navigate to OnboardingActivity
        startActivity(new android.content.Intent(this, OnboardingActivity.class));
        finish();
    }

    private void navigateToSignUp() {
        // Navigate to RegisterActivity
        // This will be implemented in the next phase
        Toast.makeText(this, "Sign Up functionality will be implemented in the next phase", Toast.LENGTH_SHORT).show();
    }
}
