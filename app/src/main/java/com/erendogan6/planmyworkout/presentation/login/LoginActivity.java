package com.erendogan6.planmyworkout.presentation.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.erendogan6.planmyworkout.R;
import com.erendogan6.planmyworkout.databinding.ActivityLoginBinding;
import com.erendogan6.planmyworkout.databinding.DialogForgotPasswordBinding;
import com.erendogan6.planmyworkout.domain.model.Result;
import com.erendogan6.planmyworkout.presentation.onboarding.OnboardingActivity;
import com.erendogan6.planmyworkout.presentation.register.RegisterActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Activity for user login.
 * This class is part of the presentation layer and handles the UI for login operations.
 */
@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {

    private LoginViewModel viewModel;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize ViewBinding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

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

    private void setupClickListeners() {
        binding.btnLogin.setOnClickListener(v -> attemptLogin());
        binding.tvForgotPassword.setOnClickListener(v -> showForgotPasswordDialog());
        binding.tvSignUp.setOnClickListener(v -> navigateToSignUp());
    }

    private void attemptLogin() {
        // Reset errors
        binding.tilEmail.setError(null);
        binding.tilPassword.setError(null);

        // Get values
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString();

        // Validate input
        boolean cancel = false;
        View focusView = null;

        // Check for a valid password
        if (TextUtils.isEmpty(password)) {
            binding.tilPassword.setError(getString(R.string.error_invalid_password));
            focusView = binding.etPassword;
            cancel = true;
        } else if (!viewModel.isValidPassword(password)) {
            binding.tilPassword.setError(getString(R.string.error_invalid_password));
            focusView = binding.etPassword;
            cancel = true;
        }

        // Check for a valid email address
        if (TextUtils.isEmpty(email)) {
            binding.tilEmail.setError(getString(R.string.error_invalid_email));
            focusView = binding.etEmail;
            cancel = true;
        } else if (!viewModel.isValidEmail(email)) {
            binding.tilEmail.setError(getString(R.string.error_invalid_email));
            focusView = binding.etEmail;
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

            // The result will be handled in the observeViewModel() method
        }
    }

    private void showForgotPasswordDialog() {
        DialogForgotPasswordBinding dialogBinding = DialogForgotPasswordBinding.inflate(getLayoutInflater());

        new MaterialAlertDialogBuilder(this)
            .setTitle(R.string.forgot_password)
            .setView(dialogBinding.getRoot())
            .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                String email = dialogBinding.etDialogEmail.getText().toString().trim();
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

        // The result will be handled in the observeViewModel() method
    }

    private void showProgress(boolean show) {
        binding.progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.btnLogin.setEnabled(!show);
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
        startActivity(new android.content.Intent(this, RegisterActivity.class));
    }
}
