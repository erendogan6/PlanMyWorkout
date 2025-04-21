package com.erendogan6.planmyworkout.feature.auth.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.erendogan6.planmyworkout.feature.auth.R;
import com.erendogan6.planmyworkout.feature.auth.databinding.FragmentRegisterBinding;
import com.erendogan6.planmyworkout.feature.auth.viewmodel.RegisterViewModel;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Fragment for user registration.
 */
@AndroidEntryPoint
public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private RegisterViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        setupListeners();
        observeViewModel();
    }

    private void setupListeners() {
        binding.btnRegister.setOnClickListener(v -> attemptRegistration());

        binding.tvLogin.setOnClickListener(v ->
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_registerFragment_to_loginFragment));
    }

    private void observeViewModel() {
        // Observe registration result
        viewModel.getRegisterResult().observe(getViewLifecycleOwner(), result -> {
            if (result.isLoading()) {
                // Show loading state
                binding.btnRegister.setEnabled(false);
                binding.progressBar.setVisibility(View.VISIBLE);
            } else {
                binding.btnRegister.setEnabled(true);
                binding.progressBar.setVisibility(View.GONE);

                if (result.isSuccess()) {
                    // Navigate to onboarding screen
                    Navigation.findNavController(requireView())
                            .navigate(R.id.action_registerFragment_to_onboarding_navigation);
                } else if (result.isError()) {
                    // Show error message
                    Toast.makeText(requireContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Observe validation errors
        viewModel.getValidationError().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void attemptRegistration() {
        String fullName = binding.etFullName.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        viewModel.register(fullName, email, password);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
