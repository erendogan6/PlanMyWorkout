package com.erendogan6.planmyworkout.presentation.plan.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.erendogan6.planmyworkout.databinding.FragmentCreatePlanBinding;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Fragment for creating a custom workout plan.
 * This is a placeholder fragment that will be implemented in the next phase.
 */
@AndroidEntryPoint
public class CreatePlanFragment extends Fragment {

    private FragmentCreatePlanBinding binding;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCreatePlanBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // This is a placeholder fragment
        Toast.makeText(requireContext(), "Create Plan Screen - Coming Soon", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
