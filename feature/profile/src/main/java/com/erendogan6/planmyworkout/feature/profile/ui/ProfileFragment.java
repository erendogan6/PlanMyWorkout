package com.erendogan6.planmyworkout.feature.profile.ui;

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
import com.erendogan6.planmyworkout.feature.profile.R;
import com.erendogan6.planmyworkout.feature.profile.databinding.FragmentProfileBinding;
import com.erendogan6.planmyworkout.feature.profile.viewmodel.ProfileViewModel;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Fragment for user profile management and settings.
 */
@AndroidEntryPoint
public class ProfileFragment extends BaseFragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        setupListeners();
        observeViewModel();

        // Load user profile data
        viewModel.loadUserProfile();
        viewModel.loadWorkoutStats();
    }

    private void setupListeners() {
        // Settings - navigate to settings fragment
        binding.layoutSettings.setOnClickListener(v -> {
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_profileFragment_to_settingsFragment);
        });

        // Change plan
        binding.layoutChangePlan.setOnClickListener(v -> {
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_profileFragment_to_onboarding_navigation);
        });

        // Logout
        binding.layoutLogout.setOnClickListener(v -> showLogoutConfirmation());
    }

    private void observeViewModel() {
        // Observe user profile
        viewModel.getUserProfile().observe(getViewLifecycleOwner(), userProfile -> {
            if (userProfile != null) {
                binding.tvUserName.setText(userProfile.getFullName());
                binding.tvUserEmail.setText(userProfile.getEmail());
            }
        });

        // Observe current workout plan
        viewModel.getCurrentPlan().observe(getViewLifecycleOwner(), workoutPlan -> {
            if (workoutPlan != null) {
                binding.tvCurrentPlan.setText(workoutPlan.getName());
                binding.tvPlanDuration.setText(workoutPlan.getDurationWeeks() + " weeks");
                binding.tvPlanDifficulty.setText(workoutPlan.getDifficulty());
            } else {
                binding.tvCurrentPlan.setText("No active plan");
                binding.tvPlanDuration.setText("");
                binding.tvPlanDifficulty.setText("");
            }
        });

        // Observe loading state
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                showLoading();
            } else {
                hideLoading();
            }
        });

        // Observe logout success
        viewModel.getLogoutSuccess().observe(getViewLifecycleOwner(), success -> {
            if (Boolean.TRUE.equals(success)) {
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_profileFragment_to_auth_navigation);
            }
        });

        // Observe error messages
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showLogoutConfirmation() {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Logout", (dialog, which) -> viewModel.logout())
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}