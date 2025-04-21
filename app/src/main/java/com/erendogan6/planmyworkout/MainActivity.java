package com.erendogan6.planmyworkout;

import android.os.Bundle;

import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.erendogan6.planmyworkout.coreui.base.BaseActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Main activity for the PlanMyWorkout app.
 * This activity hosts the navigation components and bottom navigation.
 */
@AndroidEntryPoint
public class MainActivity extends BaseActivity {
    private NavController navController;
    private BottomNavigationView bottomNavigationView;
    private int currentTabId = R.id.home_navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the NavController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        }

        // Set up bottom navigation
        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set up the bottom navigation with the NavController
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // Handle bottom navigation item clicks
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            // If the same tab is selected, pop to root
            if (itemId == currentTabId) {
                navController.popBackStack(navController.getGraph().getStartDestinationId(), false);
                return true;
            }

            // Switch to the selected tab
            currentTabId = itemId;
            return NavigationUI.onNavDestinationSelected(item, navController);
        });

        // Handle destination changes to update the bottom navigation
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            // Show/hide bottom navigation based on destination
            updateBottomNavigationVisibility(destination);
        });
    }

    private void updateBottomNavigationVisibility(NavDestination destination) {
        // By default, don't show the bottom navigation
        boolean showBottomNav = false;

        // Check if the destination is within the bottom_nav_graph
        NavDestination currentDest = destination;
        while (currentDest != null) {
            // If we're in the bottom_nav_graph, we might show the bottom nav
            if (currentDest.getId() == R.id.bottom_nav_graph) {
                showBottomNav = true;
                break;
            }

            // Move up to the parent
            NavGraph parent = currentDest.getParent();
            if (parent == currentDest) {
                break; // Avoid infinite loop
            }
            currentDest = parent;
        }

        // Even if we're in the bottom_nav_graph, we need to check specific fragments
        // where we want to hide the bottom navigation
        if (showBottomNav) {
            int destinationId = destination.getId();

            // Hide bottom navigation in the ExerciseListFragment flow
            // Check the label of the destination to identify the fragments
            String label = destination.getLabel() != null ? destination.getLabel().toString() : "";
            if (label.equals("Exercise List") ||
                label.equals("Exercise History") ||
                label.equals("Exercise Detail")) {
                showBottomNav = false;
            }
        }

        bottomNavigationView.setVisibility(showBottomNav ? android.view.View.VISIBLE : android.view.View.GONE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        // If we're on a main tab and can't go back further in that tab's stack
        if (!navController.popBackStack()) {
            // If we're not on the home tab, switch to home
            if (currentTabId != R.id.home_navigation) {
                bottomNavigationView.setSelectedItemId(R.id.home_navigation);
            } else {
                // Otherwise, let the system handle the back press (exit app)
                super.onBackPressed();
            }
        }
    }
}
