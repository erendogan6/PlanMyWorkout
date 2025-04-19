package com.erendogan6.planmyworkout.presentation.onboarding.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.erendogan6.planmyworkout.databinding.FragmentOnboardingBinding;
import com.erendogan6.planmyworkout.domain.model.OnboardingChoice;
import com.erendogan6.planmyworkout.presentation.home.HomeActivity;
import com.erendogan6.planmyworkout.presentation.onboarding.viewmodel.OnboardingViewModel;
import com.erendogan6.planmyworkout.presentation.common.BaseActivity;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Fragment for user onboarding.
 * This fragment allows the user to choose how they want to start using the app.
 */
@AndroidEntryPoint
public class OnboardingFragment extends Fragment {

    private FragmentOnboardingBinding binding;
    private OnboardingViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOnboardingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(OnboardingViewModel.class);

        // Set up click listeners
        setupClickListeners();

        // Observe onboarding choice result
        observeOnboardingChoiceResult();
    }

    private void setupClickListeners() {
        // Ready-made plans card click
        binding.cardReadyMade.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Ready-made card clicked", Toast.LENGTH_SHORT).show();
            viewModel.saveOnboardingChoice(OnboardingChoice.READY_MADE);
        });

        // Create own plan card click
        binding.cardManual.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Manual card clicked", Toast.LENGTH_SHORT).show();
            viewModel.saveOnboardingChoice(OnboardingChoice.MANUAL);
        });

        // AI generate plan card click
        binding.cardAi.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "AI card clicked", Toast.LENGTH_SHORT).show();
            viewModel.saveOnboardingChoice(OnboardingChoice.AI);
        });
    }

    private void observeOnboardingChoiceResult() {
        viewModel.getOnboardingChoiceResult().observe(getViewLifecycleOwner(), result -> {
            if (result != null) {
                navigateBasedOnChoice(result);
            }
        });
    }

    private void navigateBasedOnChoice(OnboardingChoice choice) {
        // Save the choice and navigate to the home screen
        // In a real app, we would save the choice to Firestore here

        // Navigate to the home screen
        Intent intent = new Intent(requireActivity(), HomeActivity.class);
        startActivity(intent);
        requireActivity().finish(); // Close the onboarding activity
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
