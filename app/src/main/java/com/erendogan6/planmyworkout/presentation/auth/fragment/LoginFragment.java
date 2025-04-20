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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.erendogan6.planmyworkout.R;
import com.erendogan6.planmyworkout.databinding.FragmentLoginBinding;
import com.erendogan6.planmyworkout.presentation.auth.viewmodel.LoginViewModel;
import com.erendogan6.planmyworkout.presentation.home.HomeActivity;
import com.erendogan6.planmyworkout.presentation.onboarding.OnboardingActivity;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Fragment for user login.
 */
@AndroidEntryPoint
public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private LoginViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Set up click listeners
        setupClickListeners();

        // Observe login result
        observeLoginResult();
    }

    private void setupClickListeners() {
        // Login button click
        binding.btnLogin.setOnClickListener(v -> attemptLogin());

        // Forgot password click
        binding.tvForgotPassword.setOnClickListener(v -> navigateToForgotPassword());

        // Sign up click
        binding.tvSignUp.setOnClickListener(v -> navigateToSignUp());
    }

    private void observeLoginResult() {
        viewModel.getLoginResult().observe(getViewLifecycleOwner(), result -> {
            if (result != null) {
                if (result) {
                    // Login successful, navigate to onboarding
                    navigateToOnboarding();
                } else {
                    // Login failed, show error
                    Toast.makeText(requireContext(), R.string.error_login_failed, Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.btnLogin.setEnabled(!isLoading);
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });
    }

    private void attemptLogin() {
        // Get values
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString();

        // Validate input
        boolean cancel = false;
        View focusView = null;

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
            // Attempt login
            viewModel.login(email, password);
        }
    }

    private void navigateToForgotPassword() {
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.action_loginFragment_to_forgotPasswordFragment);
    }

    private void navigateToSignUp() {
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.action_loginFragment_to_registerFragment);
    }

    private void navigateToOnboarding() {
        // For new users, navigate to onboarding
        Intent intent = new Intent(requireContext(), OnboardingActivity.class);
        startActivity(intent);
        requireActivity().finish();

        // For testing purposes, we can also navigate directly to the home screen
        // navigateToHome();
    }

    private void navigateToHome() {
        // For returning users, navigate directly to home
        Intent intent = new Intent(requireContext(), HomeActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
