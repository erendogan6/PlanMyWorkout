package com.erendogan6.planmyworkout.presentation.onboarding.fragment;


import android.os.Bundle;
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
import com.erendogan6.planmyworkout.databinding.FragmentOnboardingBinding;
import com.erendogan6.planmyworkout.domain.model.OnboardingChoice;
import com.erendogan6.planmyworkout.presentation.onboarding.viewmodel.OnboardingViewModel;

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
        // Save the choice to Firestore (in a real app)
        // For now, just navigate based on the choice

        NavController navController = Navigation.findNavController(requireView());

        switch (choice) {
            case READY_MADE:
                navController.navigate(R.id.action_onboardingFragment_to_readyMadePlansFragment);
                break;
            case MANUAL:
                navController.navigate(R.id.action_onboardingFragment_to_createPlanFragment);
                break;
            case AI:
                navController.navigate(R.id.action_onboardingFragment_to_aiPlanGenerationFragment);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
