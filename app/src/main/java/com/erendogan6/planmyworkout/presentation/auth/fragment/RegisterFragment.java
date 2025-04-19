package com.erendogan6.planmyworkout.presentation.auth.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.erendogan6.planmyworkout.R;
import com.erendogan6.planmyworkout.databinding.FragmentRegisterBinding;
import com.erendogan6.planmyworkout.presentation.auth.viewmodel.RegisterViewModel;
import com.erendogan6.planmyworkout.presentation.onboarding.OnboardingActivity;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Fragment for user registration.
 */
@AndroidEntryPoint
public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private RegisterViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        
        // Set up click listeners
        setupClickListeners();
        
        // Observe registration result
        observeRegistrationResult();
    }
    
    private void setupClickListeners() {
        // Register button click
        binding.btnRegister.setOnClickListener(v -> attemptRegistration());
        
        // Login click
        binding.tvLogin.setOnClickListener(v -> requireActivity().onBackPressed());
        
        // Back button click
        binding.ivBack.setOnClickListener(v -> requireActivity().onBackPressed());
    }
    
    private void observeRegistrationResult() {
        viewModel.getRegistrationResult().observe(getViewLifecycleOwner(), result -> {
            if (result != null) {
                if (result) {
                    // Registration successful, navigate to onboarding
                    navigateToOnboarding();
                } else {
                    // Registration failed, show error
                    Toast.makeText(requireContext(), R.string.error_registration_failed, Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.btnRegister.setEnabled(!isLoading);
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });
    }
    
    private void attemptRegistration() {
        // Get values
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString();
        String confirmPassword = binding.etConfirmPassword.getText().toString();

        // Validate input
        boolean cancel = false;
        View focusView = null;

        // Check for a valid confirm password
        if (TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(requireContext(), R.string.error_invalid_password, Toast.LENGTH_SHORT).show();
            focusView = binding.etConfirmPassword;
            cancel = true;
        } else if (!viewModel.doPasswordsMatch(password, confirmPassword)) {
            Toast.makeText(requireContext(), R.string.error_passwords_dont_match, Toast.LENGTH_SHORT).show();
            focusView = binding.etConfirmPassword;
            cancel = true;
        }

        // Check for a valid password
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(requireContext(), R.string.error_invalid_password, Toast.LENGTH_SHORT).show();
            focusView = binding.etPassword;
            cancel = true;
        } else if (!viewModel.isValidPassword(password)) {
            Toast.makeText(requireContext(), R.string.error_invalid_password, Toast.LENGTH_SHORT).show();
            focusView = binding.etPassword;
            cancel = true;
        }

        // Check for a valid email address
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(requireContext(), R.string.error_invalid_email, Toast.LENGTH_SHORT).show();
            focusView = binding.etEmail;
            cancel = true;
        } else if (!viewModel.isValidEmail(email)) {
            Toast.makeText(requireContext(), R.string.error_invalid_email, Toast.LENGTH_SHORT).show();
            focusView = binding.etEmail;
            cancel = true;
        }

        if (cancel) {
            // There was an error; focus the first form field with an error
            if (focusView != null) {
                focusView.requestFocus();
            }
        } else {
            // Attempt registration
            viewModel.register(email, password);
        }
    }
    
    private void navigateToOnboarding() {
        Intent intent = new Intent(requireContext(), OnboardingActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
