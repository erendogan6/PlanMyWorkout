package com.erendogan6.planmyworkout.feature.home.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.erendogan6.planmyworkout.coreui.base.BaseFragment;
import com.erendogan6.planmyworkout.feature.home.R;
import com.erendogan6.planmyworkout.feature.home.databinding.FragmentHomeBinding;
import com.erendogan6.planmyworkout.feature.home.viewmodel.HomeViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Home screen fragment.
 */
@AndroidEntryPoint
public class HomeFragment extends BaseFragment {

    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        setupListeners();
        observeViewModel();
    }

    private void setupListeners() {
        // Set up current plan card click listener
        binding.cardCurrentPlan.setOnClickListener(v -> {
            // Navigate to workout screen with the current plan ID
            if (viewModel.getCurrentPlanId() != null) {
                // Create a bundle with the plan ID
                Bundle args = new Bundle();
                args.putString("planId", viewModel.getCurrentPlanId());

                // Navigate to the workout navigation graph
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_homeFragment_to_workout_navigation, args);
            }
        });

        binding.btnPlanMenu.setOnClickListener(this::showPlanMenu);
    }

    private void showPlanMenu(View anchor) {
        PopupMenu popup = new PopupMenu(requireContext(), anchor);
        popup.getMenuInflater().inflate(R.menu.plan_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();

            if (id == R.id.action_rename_plan) {
                showRenamePlanDialog();
                return true;
            } else if (id == R.id.action_delete_plan) {
                showDeletePlanDialog();
                return true;
            }

            return false;
        });

        popup.show();
    }

    private void observeViewModel() {
        // Observe loading state
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (Boolean.TRUE.equals(isLoading)) {
                showLoading();
            } else {
                hideLoading();
            }
        });

        viewModel.getUserName().observe(getViewLifecycleOwner(), name -> {
            if (name != null && !name.isEmpty()) {
                binding.tvMotivationalMessage.setText("Keep pushing, " + name + "! You're doing great!");
            }
        });

        viewModel.getCurrentPlan().observe(getViewLifecycleOwner(), plan -> {
            if (plan != null) {
                // Set plan name
                binding.tvCurrentPlanName.setText(plan.getName());

                // Set schedule
                if (plan.getWeeklySchedule() != null && !plan.getWeeklySchedule().isEmpty()) {
                    binding.tvPlanSchedule.setText(String.join(", ", plan.getWeeklySchedule()));
                } else {
                    binding.tvPlanSchedule.setText(plan.getDaysPerWeek() + " days per week");
                }

                // Set duration
                binding.tvPlanDuration.setText(plan.getDurationWeeks() + " weeks program");

                // Set exercises preview
                if (plan.getExerciseNames() != null && !plan.getExerciseNames().isEmpty()) {
                    // Show first 3 exercises with ellipsis if there are more
                    List<String> previewExercises = plan.getExerciseNames().subList(
                            0, Math.min(3, plan.getExerciseNames().size()));
                    String exercisesText = String.join(", ", previewExercises);
                    if (plan.getExerciseNames().size() > 3) {
                        exercisesText += "...";
                    }
                    binding.tvPlanExercises.setText(exercisesText);
                } else {
                    binding.tvPlanExercises.setVisibility(View.GONE);
                }

                // Show the plan layout and hide the no plan message
                binding.tvNoWorkoutPlan.setVisibility(View.GONE);
                binding.layoutCurrentPlan.setVisibility(View.VISIBLE);

                // Show menu button when plan is available
                binding.btnPlanMenu.setVisibility(View.VISIBLE);
            } else {
                // No plan available, show the no plan message
                binding.tvNoWorkoutPlan.setVisibility(View.VISIBLE);
                binding.layoutCurrentPlan.setVisibility(View.GONE);

                // Hide menu button when no plan
                binding.btnPlanMenu.setVisibility(View.GONE);
            }
        });

        // Observe plan update success
        viewModel.getPlanUpdateSuccess().observe(getViewLifecycleOwner(), success -> {
            if (Boolean.TRUE.equals(success)) {
                Toast.makeText(requireContext(), "Plan updated successfully", Toast.LENGTH_SHORT).show();
            }
        });

        // Observe error messages
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showRenamePlanDialog() {
        if (viewModel.getCurrentPlan().getValue() == null) return;

        // Create custom dialog layout
        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_rename_plan, null);

        TextInputLayout inputLayout = dialogView.findViewById(R.id.tilPlanName);
        TextInputEditText etPlanName = dialogView.findViewById(R.id.etPlanName);

        // Set current plan name
        String currentName = viewModel.getCurrentPlan().getValue().getName();
        etPlanName.setText(currentName);
        etPlanName.setSelection(currentName.length()); // Cursor at end

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle("Rename Plan")
                .setView(dialogView)
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel", (d, which) -> d.dismiss())
                .create();

        dialog.setOnShowListener(dialogInterface -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                String newName = etPlanName.getText().toString().trim();

                if (newName.isEmpty()) {
                    inputLayout.setError("Plan name cannot be empty");
                    return;
                }

                if (newName.equals(currentName)) {
                    dialog.dismiss();
                    return;
                }

                // Update plan name
                viewModel.updatePlanName(newName);
                dialog.dismiss();
            });
        });

        dialog.show();
    }

    private void showDeletePlanDialog() {
        if (viewModel.getCurrentPlan().getValue() == null) return;

        String planName = viewModel.getCurrentPlan().getValue().getName();

        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Plan")
                .setMessage("Are you sure you want to delete \"" + planName + "\"?\n\nThis action cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    viewModel.deletePlan();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}