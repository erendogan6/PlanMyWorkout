package com.erendogan6.planmyworkout.feature.profile.ui;

import android.content.SharedPreferences;
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
import com.erendogan6.planmyworkout.feature.profile.databinding.FragmentSettingsBinding;
import com.erendogan6.planmyworkout.feature.profile.viewmodel.SettingsViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Fragment for app settings and user preferences.
 */
@AndroidEntryPoint
public class SettingsFragment extends BaseFragment {

    private FragmentSettingsBinding binding;
    private SettingsViewModel viewModel;

    @Inject
    SharedPreferences sharedPreferences;

    private static final String PREF_WEIGHT_UNIT = "weight_unit";
    private static final String PREF_NOTIFICATIONS_ENABLED = "notifications_enabled";
    private static final String PREF_REMINDER_TIME = "reminder_time";
    private static final String UNIT_KG = "kg";
    private static final String UNIT_LB = "lb";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        setupToolbar();
        setupListeners();
        observeViewModel();
        updateSettingsDisplay();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> {
            Navigation.findNavController(requireView()).popBackStack();
        });
    }

    private void setupListeners() {
        // Units toggle
        binding.layoutUnits.setOnClickListener(v -> toggleWeightUnit());

        // Notifications toggle
        binding.switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) { // Only respond to user interactions
                toggleNotifications(isChecked);
            }
        });

        // Reminder time
        binding.layoutReminderTime.setOnClickListener(v -> showReminderTimePicker());

        // Privacy Policy
        binding.layoutPrivacyPolicy.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Privacy Policy coming soon", Toast.LENGTH_SHORT).show();
        });

        // Terms of Service
        binding.layoutTermsOfService.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Terms of Service coming soon", Toast.LENGTH_SHORT).show();
        });

        // App Version
        binding.layoutAppVersion.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Version 1.0.0", Toast.LENGTH_SHORT).show();
        });

        // Delete Account
        binding.layoutDeleteAccount.setOnClickListener(v -> showDeleteAccountConfirmation());
    }

    private void observeViewModel() {
        // Observe loading state
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                showLoading();
            } else {
                hideLoading();
            }
        });

        // Observe delete account success
        viewModel.getDeleteAccountSuccess().observe(getViewLifecycleOwner(), success -> {
            if (Boolean.TRUE.equals(success)) {
                Toast.makeText(requireContext(), "Account deleted successfully", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_settingsFragment_to_auth_navigation);
            }
        });

        // Observe error messages
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateSettingsDisplay() {
        // Update weight unit display
        String currentUnit = sharedPreferences.getString(PREF_WEIGHT_UNIT, UNIT_KG);
        String displayText = currentUnit.equals(UNIT_KG) ? "Metric (kg)" : "Imperial (lb)";
        binding.tvCurrentUnit.setText(displayText);

        // Update notifications switch
        boolean notificationsEnabled = sharedPreferences.getBoolean(PREF_NOTIFICATIONS_ENABLED, true);
        binding.switchNotifications.setChecked(notificationsEnabled);

        // Update reminder time display
        String reminderTime = sharedPreferences.getString(PREF_REMINDER_TIME, "09:00");
        binding.tvReminderTime.setText(reminderTime);

        // Enable/disable reminder time based on notifications
        binding.layoutReminderTime.setEnabled(notificationsEnabled);
        binding.layoutReminderTime.setAlpha(notificationsEnabled ? 1.0f : 0.5f);
    }

    private void toggleWeightUnit() {
        String currentUnit = sharedPreferences.getString(PREF_WEIGHT_UNIT, UNIT_KG);
        String newUnit = currentUnit.equals(UNIT_KG) ? UNIT_LB : UNIT_KG;

        sharedPreferences.edit()
                .putString(PREF_WEIGHT_UNIT, newUnit)
                .apply();

        updateSettingsDisplay();
        Toast.makeText(requireContext(), "Weight unit changed to " + newUnit.toUpperCase(), Toast.LENGTH_SHORT).show();
    }

    private void toggleNotifications(boolean enabled) {
        sharedPreferences.edit()
                .putBoolean(PREF_NOTIFICATIONS_ENABLED, enabled)
                .apply();

        updateSettingsDisplay();

        String message = enabled ? "Workout notifications enabled" : "Workout notifications disabled";
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void showReminderTimePicker() {
        boolean notificationsEnabled = sharedPreferences.getBoolean(PREF_NOTIFICATIONS_ENABLED, true);
        if (!notificationsEnabled) {
            Toast.makeText(requireContext(), "Enable notifications first", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get current time
        String currentTime = sharedPreferences.getString(PREF_REMINDER_TIME, "09:00");
        String[] timeParts = currentTime.split(":");
        int currentHour = Integer.parseInt(timeParts[0]);
        int currentMinute = Integer.parseInt(timeParts[1]);

        // Show time picker
        android.app.TimePickerDialog timePickerDialog = new android.app.TimePickerDialog(
                requireContext(),
                (view, hourOfDay, minute) -> {
                    String newTime = String.format("%02d:%02d", hourOfDay, minute);
                    sharedPreferences.edit()
                            .putString(PREF_REMINDER_TIME, newTime)
                            .apply();
                    updateSettingsDisplay();
                    Toast.makeText(requireContext(), "Reminder time set to " + newTime, Toast.LENGTH_SHORT).show();
                },
                currentHour,
                currentMinute,
                true // 24 hour format
        );

        timePickerDialog.setTitle("Set Workout Reminder Time");
        timePickerDialog.show();
    }

    private void showDeleteAccountConfirmation() {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete your account? This action cannot be undone and all your data will be permanently lost.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    showFinalDeleteConfirmation();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showFinalDeleteConfirmation() {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Final Confirmation")
                .setMessage("This will permanently delete your account and all associated data. Are you absolutely sure?")
                .setPositiveButton("Yes, Delete Forever", (dialog, which) -> viewModel.deleteAccount())
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}