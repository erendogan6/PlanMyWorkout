package com.erendogan6.planmyworkout.presentation.plan.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.erendogan6.planmyworkout.databinding.FragmentAiPlanGenerationBinding;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Fragment for generating a workout plan using AI.
 * This is a placeholder fragment that will be implemented in the next phase.
 */
@AndroidEntryPoint
public class AiPlanGenerationFragment extends Fragment {

    private FragmentAiPlanGenerationBinding binding;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAiPlanGenerationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // This is a placeholder fragment
        Toast.makeText(requireContext(), "AI Plan Generation Screen - Coming Soon", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
