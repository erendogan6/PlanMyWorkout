package com.erendogan6.planmyworkout.presentation.common;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.erendogan6.planmyworkout.R;
import com.erendogan6.planmyworkout.databinding.ActivityBaseBinding;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Base activity that hosts fragments.
 * This activity provides a container for fragments and handles fragment transactions.
 */
@AndroidEntryPoint
public class BaseActivity extends AppCompatActivity {

    private ActivityBaseBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize ViewBinding
        binding = ActivityBaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    /**
     * Add a fragment to the container.
     * @param fragment The fragment to add
     * @param addToBackStack Whether to add the transaction to the back stack
     */
    public void addFragment(Fragment fragment, boolean addToBackStack) {
        if (fragment != null) {
            String fragmentTag = fragment.getClass().getSimpleName();
            
            if (addToBackStack) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, fragment, fragmentTag)
                        .addToBackStack(fragmentTag)
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, fragment, fragmentTag)
                        .commit();
            }
        }
    }

    /**
     * Replace the current fragment with a new one.
     * @param fragment The fragment to replace with
     * @param addToBackStack Whether to add the transaction to the back stack
     */
    public void replaceFragment(Fragment fragment, boolean addToBackStack) {
        addFragment(fragment, addToBackStack);
    }
}
