package com.erendogan6.planmyworkout.presentation.auth.fragment;

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
import com.erendogan6.planmyworkout.databinding.FragmentForgotPasswordBinding;
import com.erendogan6.planmyworkout.presentation.auth.viewmodel.ForgotPasswordViewModel;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Fragment for password reset.
 */
@AndroidEntryPoint
public class ForgotPasswordFragment extends Fragment {

    private FragmentForgotPasswordBinding binding;
    private ForgotPasswordViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(ForgotPasswordViewModel.class);
        
        // Set up click listeners
        setupClickListeners();
        
        // Observe reset password result
        observeResetPasswordResult();
    }
    
    private void setupClickListeners() {
        // Reset password button click
        binding.btnResetPassword.setOnClickListener(v -> attemptResetPassword());
        
        // Back button click
        binding.ivBack.setOnClickListener(v -> requireActivity().onBackPressed());
    }
    
    private void observeResetPasswordResult() {
        viewModel.getResetPasswordResult().observe(getViewLifecycleOwner(), result -> {
            if (result != null) {
                if (result) {
                    // Reset password email sent successfully
                    Toast.makeText(requireContext(), R.string.reset_password_email_sent, Toast.LENGTH_SHORT).show();
                    requireActivity().onBackPressed();
                } else {
                    // Reset password failed, show error
                    Toast.makeText(requireContext(), R.string.error_reset_password_failed, Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.btnResetPassword.setEnabled(!isLoading);
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });
    }
    
    private void attemptResetPassword() {
        // Get email
        String email = binding.etEmail.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(requireContext(), R.string.error_invalid_email, Toast.LENGTH_SHORT).show();
            binding.etEmail.requestFocus();
            return;
        } else if (!viewModel.isValidEmail(email)) {
            Toast.makeText(requireContext(), R.string.error_invalid_email, Toast.LENGTH_SHORT).show();
            binding.etEmail.requestFocus();
            return;
        }

        // Attempt to send reset password email
        viewModel.resetPassword(email);
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
