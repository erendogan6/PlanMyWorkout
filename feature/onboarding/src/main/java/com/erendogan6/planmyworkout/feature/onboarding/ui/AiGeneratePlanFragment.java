package com.erendogan6.planmyworkout.feature.onboarding.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.erendogan6.planmyworkout.feature.onboarding.R;
import com.erendogan6.planmyworkout.feature.onboarding.databinding.FragmentAiGeneratePlanBinding;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Fragment for generating a workout plan using AI.
 */
@AndroidEntryPoint
public class AiGeneratePlanFragment extends Fragment {
    
    private FragmentAiGeneratePlanBinding binding;
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAiGeneratePlanBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        setupListeners();
    }
    
    private void setupListeners() {
        binding.btnGeneratePlan.setOnClickListener(v -> {
            // Validate inputs
            if (binding.etGoal.getText().toString().trim().isEmpty()) {
                Toast.makeText(requireContext(), "Please enter your fitness goal", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Show loading state
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.btnGeneratePlan.setEnabled(false);
            
            // Simulate AI plan generation (would be a real API call in production)
            binding.getRoot().postDelayed(() -> {
                binding.progressBar.setVisibility(View.GONE);
                binding.btnGeneratePlan.setEnabled(true);
                
                // Navigate to plan detail with a generated plan ID
                AiGeneratePlanFragmentDirections.ActionAiGeneratePlanFragmentToPlanDetailFragment action =
                        AiGeneratePlanFragmentDirections.actionAiGeneratePlanFragmentToPlanDetailFragment("ai_generated_plan");
                Navigation.findNavController(requireView()).navigate(action);
            }, 2000); // Simulate 2 second delay for AI processing
        });
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
