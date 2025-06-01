package com.erendogan6.planmyworkout.feature.auth.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.erendogan6.planmyworkout.coreui.base.BaseFragment;
import com.erendogan6.planmyworkout.feature.auth.R;
import com.erendogan6.planmyworkout.feature.auth.databinding.FragmentForgotPasswordBinding;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Fragment for password reset functionality.
 */
@AndroidEntryPoint
public class ForgotPasswordFragment extends BaseFragment {

    private FragmentForgotPasswordBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupListeners();
    }

    private void setupListeners() {
        binding.btnResetPassword.setOnClickListener(v -> attemptPasswordReset());

        binding.tvBackToLogin.setOnClickListener(v ->
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_forgotPasswordFragment_to_loginFragment));
    }

    private void attemptPasswordReset() {
        String email = binding.etEmail.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter your email address", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading state
        showLoading();
        binding.btnResetPassword.setEnabled(false);

        // TODO: Implement actual password reset logic with Firebase Auth or your backend
        // For now, just simulate success
        simulatePasswordReset(email);
    }

    private void simulatePasswordReset(String email) {
        // Simulate network delay
        binding.getRoot().postDelayed(() -> {
            hideLoading();
            binding.btnResetPassword.setEnabled(true);

            Toast.makeText(requireContext(),
                    "Password reset email sent to " + email,
                    Toast.LENGTH_LONG).show();

            // Navigate back to login
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_forgotPasswordFragment_to_loginFragment);
        }, 2000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}