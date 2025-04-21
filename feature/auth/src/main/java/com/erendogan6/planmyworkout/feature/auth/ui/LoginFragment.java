package com.erendogan6.planmyworkout.feature.auth.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.erendogan6.planmyworkout.coreui.base.BaseFragment;

import com.erendogan6.planmyworkout.feature.auth.R;
import com.erendogan6.planmyworkout.feature.auth.databinding.FragmentLoginBinding;
import com.erendogan6.planmyworkout.feature.auth.viewmodel.LoginViewModel;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Login screen fragment.
 */
@AndroidEntryPoint
public class LoginFragment extends BaseFragment {

    private FragmentLoginBinding binding;
    private LoginViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        setupListeners();
        observeViewModel();
    }

    private void setupListeners() {
        binding.btnLogin.setOnClickListener(v -> attemptLogin());

        binding.tvForgotPassword.setOnClickListener(v ->
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_loginFragment_to_forgotPasswordFragment));

        binding.tvSignUp.setOnClickListener(v ->
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_loginFragment_to_registerFragment));
    }

    private void observeViewModel() {
        viewModel.getLoginResult().observe(getViewLifecycleOwner(), result -> {
            if (result.isLoading()) {
                // Show loading state
                binding.btnLogin.setEnabled(false);
                showLoading();
            } else {
                binding.btnLogin.setEnabled(true);
                hideLoading();

                if (result.isSuccess()) {
                    // Navigate to onboarding or home screen
                    Navigation.findNavController(requireView())
                            .navigate(R.id.action_loginFragment_to_onboarding_navigation);
                } else if (result.isError()) {
                    // Show error message
                    Toast.makeText(requireContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void attemptLogin() {
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading state
        showLoading();
        binding.btnLogin.setEnabled(false);

        viewModel.login(email, password);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
