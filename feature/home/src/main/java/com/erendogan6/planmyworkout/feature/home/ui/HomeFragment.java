package com.erendogan6.planmyworkout.feature.home.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.erendogan6.planmyworkout.feature.home.R;
import com.erendogan6.planmyworkout.feature.home.databinding.FragmentHomeBinding;
import com.erendogan6.planmyworkout.feature.home.viewmodel.HomeViewModel;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Home screen fragment.
 */
@AndroidEntryPoint
public class HomeFragment extends Fragment {

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

        // Set up create workout plan section
        binding.layoutCreateHeader.setOnClickListener(v -> {
            // Toggle visibility of create options
            boolean isVisible = binding.layoutCreateOptions.getVisibility() == View.VISIBLE;
            binding.layoutCreateOptions.setVisibility(isVisible ? View.GONE : View.VISIBLE);
            // Rotate arrow based on expanded state
            binding.ivExpandArrow.setRotation(isVisible ? 0 : 180);
        });

        // Set up create options buttons
        binding.btnReadyMadePlan.setOnClickListener(v ->
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_homeFragment_to_readyMadePlansFragment));

        binding.btnBuildOwnPlan.setOnClickListener(v ->
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_homeFragment_to_createPlanFragment));

        binding.btnGenerateAI.setOnClickListener(v ->
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_homeFragment_to_aiGeneratePlanFragment));
    }

    private void observeViewModel() {
        viewModel.getUserName().observe(getViewLifecycleOwner(), name -> {
            if (name != null && !name.isEmpty()) {
                binding.tvMotivationalMessage.setText("Keep pushing, " + name + "! You're doing great!");
            }
        });

        viewModel.getCurrentPlan().observe(getViewLifecycleOwner(), plan -> {
            if (plan != null) {
                binding.tvCurrentPlanName.setText(plan.getName());
                binding.tvNoWorkoutPlan.setVisibility(View.GONE);
                binding.layoutCurrentPlan.setVisibility(View.VISIBLE);
            } else {
                binding.tvNoWorkoutPlan.setVisibility(View.VISIBLE);
                binding.layoutCurrentPlan.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
