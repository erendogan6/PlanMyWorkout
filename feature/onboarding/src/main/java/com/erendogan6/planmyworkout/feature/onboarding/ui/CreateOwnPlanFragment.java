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
import com.erendogan6.planmyworkout.feature.onboarding.databinding.FragmentCreateOwnPlanBinding;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Fragment for creating a custom workout plan.
 */
@AndroidEntryPoint
public class CreateOwnPlanFragment extends Fragment {
    
    private FragmentCreateOwnPlanBinding binding;
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCreateOwnPlanBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        setupListeners();
    }
    
    private void setupListeners() {
        binding.btnCreatePlan.setOnClickListener(v -> {
            // Validate inputs
            if (binding.etPlanName.getText().toString().trim().isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a plan name", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // In a real implementation, this would save the plan to a repository
            Toast.makeText(requireContext(), "Plan created successfully!", Toast.LENGTH_SHORT).show();
            
            // Navigate to home
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_createOwnPlanFragment_to_home_navigation);
        });
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
