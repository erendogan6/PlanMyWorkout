package com.erendogan6.planmyworkout.presentation.register;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.erendogan6.planmyworkout.R;
import com.erendogan6.planmyworkout.databinding.ActivityRegisterBinding;
import com.erendogan6.planmyworkout.presentation.login.LoginActivity;
import com.erendogan6.planmyworkout.presentation.onboarding.OnboardingActivity;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Activity for user registration.
 * This class is part of the presentation layer and handles the UI for registration operations.
 */
@AndroidEntryPoint
public class RegisterActivity extends AppCompatActivity {

    private RegisterViewModel viewModel;
    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize ViewBinding
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        // Set up click listeners
        setupClickListeners();

        // Observe LiveData
        observeViewModel();
    }

    private void observeViewModel() {
        viewModel.getRegisterResult().observe(this, result -> {
            if (result != null) {
                if (result.isLoading()) {
                    showProgress(true);
                } else if (result.isSuccess()) {
                    showProgress(false);
                    Toast.makeText(this, R.string.success_registration, Toast.LENGTH_SHORT).show();
                    navigateToOnboarding();
                } else if (result.isError()) {
                    showProgress(false);
                    showRegistrationError(result.getMessage());
                }
            }
        });
    }

    private void setupClickListeners() {
        binding.btnRegister.setOnClickListener(v -> attemptRegistration());
        binding.tvLogin.setOnClickListener(v -> navigateToLogin());
    }

    private void attemptRegistration() {
        // Reset errors
        binding.tilEmail.setError(null);
        binding.tilPassword.setError(null);
        binding.tilConfirmPassword.setError(null);

        // Get values
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString();
        String confirmPassword = binding.etConfirmPassword.getText().toString();

        // Validate input
        boolean cancel = false;
        View focusView = null;

        // Check for a valid confirm password
        if (TextUtils.isEmpty(confirmPassword)) {
            binding.tilConfirmPassword.setError(getString(R.string.error_invalid_password));
            focusView = binding.etConfirmPassword;
            cancel = true;
        } else if (!viewModel.doPasswordsMatch(password, confirmPassword)) {
            binding.tilConfirmPassword.setError(getString(R.string.error_passwords_dont_match));
            focusView = binding.etConfirmPassword;
            cancel = true;
        }

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
            // There was an error; don't attempt registration and focus the first form field with an error
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to perform the user registration attempt
            showProgress(true);

            // Attempt registration
            viewModel.register(email, password);

            // The result will be handled in the observeViewModel() method
        }
    }

    private void showProgress(boolean show) {
        binding.progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.btnRegister.setEnabled(!show);
    }

    private void showRegistrationError(String message) {
        Toast.makeText(this, message != null ? message : getString(R.string.error_registration_failed), Toast.LENGTH_LONG).show();
    }

    private void navigateToOnboarding() {
        // Navigate to OnboardingActivity
        Intent intent = new Intent(this, OnboardingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void navigateToLogin() {
        // Navigate to LoginActivity
        finish();
    }
}
